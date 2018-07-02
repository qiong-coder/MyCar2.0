package org.buaa.ly.MyCar.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.exception.*;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.http.response.*;
import org.buaa.ly.MyCar.internal.*;
import org.buaa.ly.MyCar.logic.*;
import org.buaa.ly.MyCar.logic.impl.AlgorithmLogic;
import org.buaa.ly.MyCar.service.OrderService;
import org.buaa.ly.MyCar.service.PayService;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.buaa.ly.MyCar.utils.OrderUtils;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.buaa.ly.MyCar.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("orderService")
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {


    private OrderLogic orderLogic;

    private StoreLogic storeLogic;

    private VehicleLogic vehicleLogic;

    private VehicleInfoLogic vehicleInfoLogic;

    private PayService payService;

    @Autowired
    public void setOrderLogic(OrderLogic orderLogic) {
        this.orderLogic = orderLogic;
    }

    @Autowired
    public void setStoreLogic(StoreLogic storeLogic) {
        this.storeLogic = storeLogic;
    }

    @Autowired
    public void setVehicleLogic(VehicleLogic vehicleLogic) {
        this.vehicleLogic = vehicleLogic;
    }

    @Autowired
    public void setVehicleInfoLogic(VehicleInfoLogic vehicleInfoLogic) {
        this.vehicleInfoLogic = vehicleInfoLogic;
    }

    @Autowired
    public void setPayService(PayService payService) {
        this.payService = payService;
    }

    @Override
    public Order find(int id) {
        Order order = orderLogic.find(id);
        if ( order == null ) throw new NotFoundError("failure to find the order");
        return order;
    }

    protected OrdersAndVehiclesAndVehicleInfos build(List<Order> orders) {

        Map<Integer, Vehicle> vehicleMap = Maps.newHashMap();
        Map<Integer, VehicleInfo> vehicleInfoMap = Maps.newHashMap();

        for ( Order order : orders ) {
            Vehicle vehicle = order.getVehicle();
            if ( vehicle != null ) vehicleMap.put(vehicle.getId(), vehicle);

            VehicleInfo vehicleInfo = order.getVehicleInfo();
            if ( vehicleInfo != null ) vehicleInfoMap.put(vehicleInfo.getId(), vehicleInfo);
        }

        return OrdersAndVehiclesAndVehicleInfos.builder()
                .orders(OrderDTO.build(orders))
                .vehicles(VehicleDTO.build(vehicleMap))
                .vehicleInfos(VehicleInfoDTO.build(vehicleInfoMap))
                .build();

    }

    @Override
    public OrdersAndVehiclesAndVehicleInfos find(String identity, String phone, List<Integer> status, boolean exclude) {
        List<Order> orders = orderLogic.find(identity, phone, status, exclude);
        return build(orders);
    }

    @Override
    public OrdersAndVehiclesAndVehicleInfos findByStatusAndVehiclesAndVehicleInfos(Integer status) {
        List<Order> orders = orderLogic.find(null, null, status);
        return build(orders);
    }

    @Override
    public List<OrderCountByStatus> findByOrdersCountByStatus() {

        Map<Integer, Integer> statusCount = Maps.newHashMap();

        orderLogic.countByStatus(null, statusCount);

        List<OrderCountByStatus> orderCountByStatuses = Lists.newArrayList();

        for ( Map.Entry<Integer, Integer> entry : statusCount.entrySet() ) {
            orderCountByStatuses.add(OrderCountByStatus.builder()
                    .status(entry.getKey())
                    .count(entry.getValue())
                    .build());
        }

        return orderCountByStatuses;
    }

    @Override
    public OrderAndVehicleAndVehicleInfo findByIdAndVehicleAndVehicleInfo(int id) {
        Order order = find(id);
        return OrderAndVehicleAndVehicleInfo.builder()
                .order(OrderDTO.build(order))
                .vehicle(order.getVehicle()!=null?VehicleDTO.build(order.getVehicle()):null)
                .vehicleInfo(order.getVehicleInfo()!=null?VehicleInfoDTO.build(order.getVehicleInfo()):null)
                .build();
    }

    @Override
    public OrderDTO insert(int viid, OrderDTO orderDTO) {

        List<Order> orders = orderLogic.find(orderDTO.getIdentity(), Lists.newArrayList(StatusEnum.PENDING.getStatus(),StatusEnum.RENTING.getStatus()));

        if ( !orders.isEmpty() ) throw new OrderIdentityDuplicate("identity is duplicate");

        VehicleInfo vehicleInfo = vehicleInfoLogic.find(viid);
        if  ( vehicleInfo == null ) throw new NotFoundError(String.format("failure to find the vehicle info - %s",viid));

        VehicleInfoDTO vehicleInfoDTO = VehicleInfoDTO.build(vehicleInfo);

        orderDTO.setViid(viid);
        orderDTO.setPreCost(PreCost.build(vehicleInfoDTO.getCost(), orderDTO.getBeginTime(), orderDTO.getEndTime(), orderDTO.getInsurance()));
        orderDTO.setOid(OrderUtils.oid(orderDTO.getRentSid(),viid,orderDTO.getIdentity()));
        return OrderDTO.build(orderLogic.insert(orderDTO.build()));

    }

    @Override
    public OrderDTO update(int id, OrderDTO orderDTO) {
        orderDTO.setId(id);
        Order update = orderLogic.update(orderDTO.build());
        if ( update == null ) throw new NotFoundError("failure to find the order");
        else return OrderDTO.build(update);
    }

    @Override
    public OrderDTO delete(int id, int force) {
        Order order;

        if ( force == 0 ) order = orderLogic.delete(id);
        else order = orderLogic.update(id, StatusEnum.DELETE.getStatus());

        if ( order == null ) throw new NotFoundError("failure to find the order");
        else return OrderDTO.build(order);
    }

    @Override
    public OrderDTO check(int id) {
        Order order = find(id);

        if ( order.getStatus().compareTo(StatusEnum.PENDING.getStatus()) == 0 ) return OrderDTO.build(order);

        if ( order.getStatus().compareTo(StatusEnum.UNPIAD.getStatus()) != 0 ) {
            throw new StatusError("order's status is unpaid");
        } else {
            if ( payService.check(id) ) {
                order = orderLogic.update(id, StatusEnum.PENDING.getStatus());
                if ( order == null ) throw new BaseError(-1,"update error");
                return OrderDTO.build(order);
            } else throw new CheckError("pay check error");
        }
    }

    private static void merge(OrderDTO orderDTO, Order order) {
        if ( orderDTO.getPayInfo() != null ) {
            if (order.getPayInfo() != null) {
                List<CostItem> payInfo = JSON.parseArray(order.getPayInfo(), CostItem.class);
                payInfo.addAll(orderDTO.getPayInfo());
                orderDTO.setPayInfo(payInfo);
            }
        }

        if ( orderDTO.getCostInfo() != null ) {
            if ( order.getCostInfo() != null ) {
                List<CostItem> costInfo = JSON.parseArray(order.getCostInfo(), CostItem.class);
                costInfo.addAll(orderDTO.getCostInfo());
                orderDTO.setCostInfo(costInfo);
            }
        }

        BeanCopyUtils.copyPropertiesIgnoreNull(orderDTO.build(), order);
    }

    @Modifying
    @Override
    public OrderDTO rent(int id, String number, OrderDTO orderDTO) {

        Vehicle vehicle = vehicleLogic.find(number);
        if ( vehicle == null ) throw new NotFoundError("failure to find the vehicle");
        if ( vehicle.getStatus().compareTo(StatusEnum.OK.getStatus()) != 0 &&
                vehicle.getStatus().compareTo(StatusEnum.SPARE.getStatus()) != 0 ) throw new StatusError("vehicle's status is not ok or spare");

        Order order = find(id);
        if ( order.getStatus().compareTo(StatusEnum.PENDING.getStatus()) != 0 ) throw new StatusError("order's status is not pending");

        orderDTO.setVid(vehicle.getId());
        orderDTO.setStatus(StatusEnum.RENTING.getStatus());
        merge(orderDTO, order);

        vehicle.setBeginTime(order.getRealBeginTime());
        vehicle.setEndTime(order.getRealEndTime());
        vehicle.setStatus(StatusEnum.RENTING.getStatus());

        return OrderDTO.build(order);
    }

    @Modifying
    @Override
    public OrderDTO drawback(int id, OrderDTO orderDTO) {
        Order order = find(id);

        if ( order.getStatus().compareTo(StatusEnum.RENTING.getStatus()) != 0 ) throw new StatusError("order's status error");

        orderDTO.setStatus(StatusEnum.DRAWBACK.getStatus());
        merge(orderDTO, order);

        Vehicle vehicle = order.getVehicle();
        if ( vehicle == null ) throw new NotFoundError("failure to find the vehicle");
        if ( vehicle.getStatus() == StatusEnum.RENTING.getStatus() ) vehicle.setStatus(StatusEnum.OK.getStatus());
        vehicle.setStore(order.getRealReturnStore());

        return OrderDTO.build(order);
    }

    @Modifying
    @Override
    public OrderDTO finished(int id, OrderDTO orderDTO) {
        Order order = find(id);

        if ( order.getStatus().compareTo(StatusEnum.DRAWBACK.getStatus()) != 0 ) throw new StatusError("order's status error");

        orderDTO.setStatus(StatusEnum.FINISHED.getStatus());
        merge(orderDTO, order);

        return OrderDTO.build(order);
    }

    @Modifying
    @Override
    public OrderDTO cancel(int id, OrderDTO orderDTO) {
        Order order = find(id);

        orderDTO.setStatus(StatusEnum.CANCELED.getStatus());
        merge(orderDTO, order);

        if ( order.getVid() != null ) vehicleLogic.updateStatus(order.getVid(), StatusEnum.OK.getStatus());

        return OrderDTO.build(order);
    }

    @Override
    public OrderHistory history(Integer viid, String number, Timestamp begin, Timestamp end) {

        List<OrderHistoryItem> items = Lists.newArrayList();

        Integer vid = null;
        if ( number != null ) {
            Vehicle vehicle = vehicleLogic.find(number);
            if ( vehicle == null ) throw new NotFoundError("failure to find vehicle");
            else vid = vehicle.getId();
        }

        List<Order> orders = orderLogic.findHistoryOrders(viid, vid, begin, end);

        int ret_day_total = 0;

        Set<Integer> vids = Sets.newHashSet();

        for ( Order order : orders ) {

            Vehicle vehicle = order.getVehicle();

            if ( vehicle != null ) vids.add(vehicle.getId());
            else vids.add(0);

            int days = TimeUtils.daysByTime(order.getRealBeginTime().compareTo(begin) > 0 ? order.getRealBeginTime() : begin,
                    order.getRealEndTime().compareTo(end) > 0 ? end : order.getRealEndTime() );

            ret_day_total += days;

            VehicleInfo vehicleInfo = order.getVehicleInfo();
            OrderHistoryItem item = OrderHistoryItem.builder()
                    .name(vehicleInfo==null?"未知":vehicleInfo.getName())
                    .number(vehicle==null?"未知":vehicle.getNumber())
                    .oid(order.getOid())
                    .ret_day(days)
                    .build();

            items.add(item);
        }

        int total_days = vids.size() * Long.valueOf(TimeUtils.daysByTime(begin, end)).intValue();

        return OrderHistory.builder()
                .history(items)
                .ret_day_total(ret_day_total)
                .idle_day(total_days-ret_day_total)
                .build();
    }

    @Override
    public OrderSchedule schedule(Integer sid, Integer viid, Timestamp begin, Timestamp end) {

        AlgorithmLogic algorithmLogic = new AlgorithmLogic(null, sid, viid, begin, end, storeLogic, vehicleLogic, orderLogic);

        Map<Integer, Map<Integer, List<Vehicle>>> stockVehicleMap = algorithmLogic.getStockMap();
        Map<Integer, Map<Integer, List<Order>>> needOrderMap = algorithmLogic.getNeedOrderMap();

        int days = TimeUtils.days(begin, end);

        Map<Integer, Map<String, List<ScheduleItem>>> orderSchedules = Maps.newHashMap();

        for ( Map.Entry<Integer, Map<Integer, List<Vehicle>>> storeEntry : stockVehicleMap.entrySet() ) {

            int store = storeEntry.getKey();
            Map<Integer, List<Vehicle>> stockVehicleInfoMap = storeEntry.getValue();

            Map<Integer, List<Order>> needVehicleInfo = needOrderMap.get(store);

            for ( Map.Entry<Integer, List<Vehicle>> vEntry : stockVehicleInfoMap.entrySet() ) {

                int v = vEntry.getKey();
                String vehicleInfoName = vEntry.getValue().get(0).getVehicleInfo().getName();

                int[] use = new int[days];
                //for( int i = 0; i < days; ++ i ) use[i] = 0;

                if ( needVehicleInfo != null && needVehicleInfo.containsKey(v) ) {
                    for( Order order : needVehicleInfo.get(v) ) {
                        int bIndex = TimeUtils.days(begin, order.getBeginTime());
                        int eIndex = TimeUtils.days(begin, order.getEndTime());
                        for ( int i = Math.max(bIndex,0); i < Math.min(eIndex,days); ++ i) {
                            ++use[i];
                        }
                    }
                }


                if ( !orderSchedules.containsKey(store) ) {
                    orderSchedules.put(store, Maps.<String, List<ScheduleItem>>newHashMap());
                }

                if ( !orderSchedules.get(store).containsKey(vehicleInfoName) ) {
                    orderSchedules.get(store).put(vehicleInfoName, Lists.<ScheduleItem>newArrayList());
                }

                List<ScheduleItem> scheduleItems = orderSchedules.get(store).get(vehicleInfoName);

                VehicleInfo vehicleInfo = vEntry.getValue().get(0).getVehicleInfo();
                int count = vEntry.getValue().size() + vehicleInfo.getSpare();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(begin);
                ScheduleItem item = ScheduleItem.builder().begin(new Timestamp(calendar.getTimeInMillis()))
                        .count(count)
                        .build();
                for ( int i = 0; i < use.length; ++ i ) {

                    if ( i == 0 ) {
                        item.setStock(count - use[i]);
                    }
                    else if ( item.getStock() != count - use[i] ) {
                        item.setEnd(new Timestamp(calendar.getTimeInMillis()));
                        scheduleItems.add(item);

                        item = ScheduleItem.builder()
                                .count(count)
                                .begin(new Timestamp(calendar.getTimeInMillis()))
                                .stock(count - use[i])
                                .build();
                    }
                    calendar.add(Calendar.DATE, 1);
                }

                item.setEnd(new Timestamp(calendar.getTimeInMillis()));
                scheduleItems.add(item);
            }

        }


        return OrderSchedule.builder()
                .schedule(orderSchedules)
                .build();
    }

    @Override
    public OrderConflict conflict(Integer sid, Integer viid, Timestamp begin, Timestamp end) {

        List<Order> rentingOrders = orderLogic.findRentingOrders(sid, viid, begin, end);

        List<Order> pendingOrders = orderLogic.findPendingOrders(sid, viid, begin, end);

        List<Vehicle> vehicles = vehicleLogic.find(sid, viid, Lists.newArrayList(StatusEnum.OK.getStatus(), StatusEnum.SPARE.getStatus()), false);

        int spare = 0;
        Map<Integer, Integer> spareMap = Maps.newHashMap();


        for ( Vehicle vehicle : vehicles ) {
            if ( !spareMap.containsKey(vehicle.getViid()) ) spareMap.put(vehicle.getViid(), vehicle.getVehicleInfo().getSpare());
            if (vehicle.getStatus().compareTo(StatusEnum.SPARE.getStatus())  == 0 )
                ++ spare;
        }
        int total = vehicles.size();
        for (  Map.Entry<Integer, Integer> entry : spareMap.entrySet() ) {
            spare += entry.getValue();
            total -= entry.getValue();
        }

        return OrderConflict.builder()
                .orders(OrderDTO.build(pendingOrders))
                .used(rentingOrders.size())
                .to_used(pendingOrders.size())
                .sparse(spare)
                .total(total+rentingOrders.size())
                .build();
    }
}

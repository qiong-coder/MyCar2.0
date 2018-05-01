package org.buaa.ly.MyCar.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.exception.CheckError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.exception.StatusError;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.http.response.*;
import org.buaa.ly.MyCar.internal.*;
import org.buaa.ly.MyCar.logic.OrderLogic;
import org.buaa.ly.MyCar.logic.PayLogic;
import org.buaa.ly.MyCar.logic.VehicleInfoLogic;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.service.OrderService;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
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

    private VehicleLogic vehicleLogic;

    private VehicleInfoLogic vehicleInfoLogic;

    private PayLogic payLogic;

    @Autowired
    public void setOrderLogic(OrderLogic orderLogic) {
        this.orderLogic = orderLogic;
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
    public void setPayLogic(PayLogic payLogic) {
        this.payLogic = payLogic;
    }

    @Override
    public Order find(int id) {
        Order order = orderLogic.find(id);
        if ( order == null ) throw new NotFoundError("failure to find the order");
        return order;
    }

    @Override
    public OrdersAndVehiclesAndVehicleInfos findByStatusAndVehiclesAndVehicleInfos(Integer status) {

        List<OrderDTO> orders = Lists.newArrayList();
        Map<Integer, VehicleDTO> vehicleDTOMap = Maps.newHashMap();
        Map<Integer, VehicleInfoDTO> vehicleInfoDTOMap = Maps.newConcurrentMap();

        orderLogic.findByViidAndStatus(null, status, orders, vehicleDTOMap, vehicleInfoDTOMap);

        return OrdersAndVehiclesAndVehicleInfos.builder()
                .orders(orders)
                .vehicles(vehicleDTOMap)
                .vehicleInfos(vehicleInfoDTOMap)
                .build();
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
        VehicleInfo vehicleInfo = vehicleInfoLogic.find(viid);
        if  ( vehicleInfo == null ) throw new NotFoundError(String.format("failure to find the vehicle info - %s",viid));

        VehicleInfoDTO vehicleInfoDTO = VehicleInfoDTO.build(vehicleInfo);

        orderDTO.setViid(viid);
        orderDTO.setPreCost(PreCost.build(vehicleInfoDTO.getCost(), orderDTO.getBeginTime(), orderDTO.getEndTime(), orderDTO.getInsurance()));

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
            if ( payLogic.check(id) ) {
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
        vehicle.setStatus(StatusEnum.OK.getStatus());
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

    @Override
    public OrderDTO cancel(int id, OrderDTO orderDTO) {
        Order order = find(id);

        orderDTO.setStatus(StatusEnum.CANCELED.getStatus());
        merge(orderDTO, order);

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

        List<Order> orders = orderLogic.findHistoryOrders(viid, vid, begin, end,
                Lists.newArrayList(StatusEnum.RENTING.getStatus(), StatusEnum.DRAWBACK.getStatus(), StatusEnum.FINISHED.getStatus()));

        int ret_day_total = 0;

        Set<Integer> vids = Sets.newHashSet();

        for ( Order order : orders ) {

            Vehicle vehicle = order.getVehicle();

            if ( vehicle != null ) vids.add(vehicle.getId());
            else vids.add(0);

            int days = Long.valueOf(TimeUtils.days(order.getRealBeginTime().compareTo(begin) > 0 ? order.getRealBeginTime() : begin,
                    order.getRealEndTime().compareTo(end) > 0 ? end : order.getRealEndTime() )).intValue();

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

        int total_days = vids.size() * Long.valueOf(TimeUtils.days(begin, end)).intValue();

        return OrderHistory.builder()
                .history(items)
                .ret_day_total(ret_day_total)
                .idle_day(total_days-ret_day_total)
                .build();
    }

    @Override
    public OrderSchedule schedule(Integer sid, Integer viid, Timestamp begin, Timestamp end) {

        List<Order> orders = orderLogic.findScheduleOrders(viid, sid, begin, end);

        Map<Integer, VehicleInfo> vehicleInfoMap = vehicleInfoLogic.findVehicleInfoMap(viid);
        Map<Integer, Map<Integer, Integer>> storeVehicleInfoCountMap = vehicleLogic.countByStoreAndVehicleInfoAndStatusNot(viid, sid, StatusEnum.DELETE.getStatus());
        Map<Integer, Map<Integer, int[]>> storeVehicleInfoUseMap = Maps.newHashMap();
        int days = TimeUtils.days(begin, end) + 1;

        for ( Order order : orders ) {
            VehicleInfo vehicleInfo = order.getVehicleInfo();
            int v = vehicleInfo.getId();

            if ( !vehicleInfoMap.containsKey(v) ) continue;

            int s;
            Timestamp vBegin, vEnd;

            if ( order.getStatus() == StatusEnum.PENDING.getStatus() ) {
                s = order.getRentStore().getId();
                vBegin = order.getBeginTime();
                vEnd = order.getEndTime();
            } else if ( order.getStatus() == StatusEnum.RENTING.getStatus() ) {
                s = order.getRealRentStore().getId();
                vBegin = order.getRealBeginTime();
                vEnd = order.getRealEndTime();
            } else continue;

            if ( !storeVehicleInfoCountMap.containsKey(s) ) continue;

            vBegin = vBegin.compareTo(begin) > 0 ? vBegin : begin;
            vEnd = vEnd.compareTo(end) > 0 ? vEnd : end;

            if ( !storeVehicleInfoUseMap.containsKey(s) ) {
                storeVehicleInfoUseMap.put(s, Maps.<Integer, int[]>newHashMap());
            }

            Map<Integer, int[]> vehicleInfoUseMap = storeVehicleInfoUseMap.get(s);

            if ( !vehicleInfoUseMap.containsKey(v) ) vehicleInfoUseMap.put(v, new int[days]);

            int[] use = vehicleInfoUseMap.get(v);

            int begin_index = TimeUtils.days(vBegin, begin);
            int end_index = TimeUtils.days(vEnd, begin);

            for ( int i = begin_index; i < end_index; ++ i ) {
                use[i] += 1;
            }
        }

        Map<Integer, Map<String, List<ScheduleItem>>> orderSchedules = Maps.newHashMap();

        for ( Map.Entry<Integer, Map<Integer, int[]>> storeEntry : storeVehicleInfoUseMap.entrySet() ) {

            int store = storeEntry.getKey();
            Map<Integer, int[]> vehicleInfoUseMap = storeEntry.getValue();

            Map<Integer, Integer> vehicleInfoCountMap = storeVehicleInfoCountMap.get(store);

            for ( Map.Entry<Integer, int[]> vEntry : vehicleInfoUseMap.entrySet() ) {

                int v = vEntry.getKey();
                String vehicleInfoName = vehicleInfoMap.get(v).getName();
                int[] use = vEntry.getValue();

                if ( !orderSchedules.containsKey(store) ) {
                    orderSchedules.put(store, Maps.<String, List<ScheduleItem>>newHashMap());
                }

                if ( !orderSchedules.get(store).containsKey(vehicleInfoMap.get(v).getName()) ) {
                    orderSchedules.get(store).put(vehicleInfoMap.get(v).getName(), Lists.<ScheduleItem>newArrayList());
                }

                List<ScheduleItem> scheduleItems = orderSchedules.get(store).get(vehicleInfoName);

                int count = vehicleInfoCountMap.get(v);
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
        Map<Integer, Map<Integer, Integer>> storeVehicleInfoCountMap = vehicleLogic.countByStoreAndVehicleInfoAndStatusNot(viid, sid, StatusEnum.DELETE.getStatus());



        return null;
    }
}

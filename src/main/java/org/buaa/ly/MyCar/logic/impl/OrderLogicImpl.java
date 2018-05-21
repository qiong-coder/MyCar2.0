package org.buaa.ly.MyCar.logic.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.QOrder;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.logic.OrderLogic;
import org.buaa.ly.MyCar.repository.OrderRepository;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component("orderLogic")
@Slf4j
@Transactional
public class OrderLogicImpl implements OrderLogic {


    private OrderRepository orderRepository;
    
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order find(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> find(Integer sid, Integer viid, Integer status) {
        QOrder qOrder = QOrder.order;

        BooleanExpression expression = null;

        if ( sid != null ) expression = qOrder.rentSid.eq(sid);

        if ( viid != null ) expression = expression == null ? qOrder.viid.eq(viid) : expression.and(qOrder.viid.eq(viid));

        if ( status != null ) expression = expression != null? expression.and(qOrder.status.eq(status)) : qOrder.status.eq(status);

        if ( expression != null ) return Lists.newArrayList(orderRepository.findAll(expression));
        else return Lists.newArrayList(orderRepository.findAll());
    }

//    @Override
//    public void find(String identity, String phone, List<Integer> status, boolean exclude, List<Order> orders, Map<Integer, Vehicle> vehicleMap, Map<Integer, VehicleInfo> vehicleInfoMap) {
//        orders.addAll(find(identity, phone, status, exclude));
//
//        for ( Order order : orders ) {
//            Vehicle vehicle = order.getVehicle();
//            if ( vehicle != null ) vehicleMap.put(vehicle.getId(), vehicle);
//
//            VehicleInfo vehicleInfo = order.getVehicleInfo();
//            if ( vehicleInfo != null ) vehicleInfoMap.put(vehicleInfo.getId(), vehicleInfo);
//        }
//
//    }

    @Override
    public List<Order> find(@Nonnull String identity, @Nonnull String phone, List<Integer> status, boolean exclude) {
        QOrder qOrder = QOrder.order;

        BooleanExpression expression = qOrder.identity.eq(identity).and(qOrder.phone.eq(phone));

        if ( status != null ) {
            if ( !exclude ) expression = expression.and(qOrder.status.in(status));
            else expression = expression.and(qOrder.status.in(status).not());
        }

        return Lists.newArrayList(orderRepository.findAll(expression));
    }

//    @Override
//    public void find(Integer sid, Integer viid, Integer status,
//                     List<Order> orders,
//                     Map<Integer, Vehicle> vehicleMap,
//                     Map<Integer, VehicleInfo> vehicleInfoMap) {
//        orders.addAll(find(sid, viid, status));
//
//        for ( Order order : orders ) {
//            Vehicle vehicle = order.getVehicle();
//            if ( vehicle != null ) vehicleMap.put(vehicle.getId(), vehicle);
//
//            VehicleInfo vehicleInfo = order.getVehicleInfo();
//            if ( vehicleInfo != null ) vehicleInfoMap.put(vehicleInfo.getId(), vehicleInfo);
//        }
//
//    }


    @Override
    public List<Order> find(String identity, List<Integer> status) {

        QOrder qOrder = QOrder.order;

        return Lists.newArrayList(orderRepository.findAll(qOrder.identity.eq(identity).and(qOrder.status.in(status))));
    }

    public List<Order> find(Integer sid, Integer viid, @Nonnull Timestamp begin, @Nonnull Timestamp end) {

        QOrder qOrder = QOrder.order;

        BooleanExpression expression;

        if ( sid != null ) {
            expression = qOrder.status.eq(StatusEnum.RENTING.getStatus()).and(qOrder.realReturnSid.eq(sid))
                    .or(qOrder.status.eq(StatusEnum.PENDING.getStatus()).and(qOrder.beginTime.lt(end).and(qOrder.endTime.gt(begin))).and(qOrder.returnSid.eq(sid)));
        } else {
            expression = qOrder.status.eq(StatusEnum.RENTING.getStatus())
                    .or(qOrder.status.eq(StatusEnum.PENDING.getStatus()).and(qOrder.beginTime.lt(end).and(qOrder.endTime.gt(begin))));
        }

        if ( viid != null ) expression = expression == null ? qOrder.viid.eq(viid) : expression.and(qOrder.viid.eq(viid));

        return Lists.newArrayList(orderRepository.findAll(expression));
    }

    @Override
    public List<Order> findHistoryOrders(Integer viid, Integer vid, Timestamp begin, Timestamp end) {

        QOrder qOrder = QOrder.order;

        BooleanExpression expression  = null;

        if ( viid != null ) expression = qOrder.viid.eq(viid);
        else if ( vid != null ) expression = qOrder.vid.eq(vid);

        if ( begin != null && end != null ) {
            expression = expression == null ? qOrder.realBeginTime.loe(end).and(qOrder.realEndTime.goe(begin)) : expression.and(qOrder.realBeginTime.loe(end).and(qOrder.realEndTime.goe(begin)));
        }

        expression = expression == null ? qOrder.status.in(Lists.newArrayList(StatusEnum.DRAWBACK.getStatus(),StatusEnum.FINISHED.getStatus())) :
                expression.and(qOrder.status.in(Lists.newArrayList(StatusEnum.DRAWBACK.getStatus(),StatusEnum.FINISHED.getStatus())));


        return Lists.newArrayList(orderRepository.findAll(expression));
    }

    @Override
    public List<Order> findRentingOrders(Integer sid, Integer viid, @Nonnull Timestamp begin, Timestamp end) {
        QOrder qOrder = QOrder.order;

        BooleanExpression expression = qOrder.status.eq(StatusEnum.RENTING.getStatus());

        if ( viid != null ) expression = expression.and(qOrder.viid.eq(viid));

        if ( sid != null ) {
            expression = expression.and(qOrder.realReturnSid.eq(sid));
        }

        if (end == null) {
            expression = expression.and(qOrder.realEndTime.lt(begin));
        } else {
            expression = expression.and(qOrder.realBeginTime.loe(end).and(qOrder.realEndTime.goe(begin)));
        }

        if ( expression != null )
            return Lists.newArrayList(orderRepository.findAll(expression));
        else return Lists.newArrayList(orderRepository.findAll());
    }

    @Override
    public List<Order> findRentingOrders(Collection<Integer> sids, Integer viid, @Nonnull Timestamp begin, Timestamp end) {

        QOrder qOrder = QOrder.order;

        BooleanExpression expression = qOrder.status.eq(StatusEnum.RENTING.getStatus());

        if ( viid != null ) expression = expression.and(qOrder.viid.eq(viid));

        if ( sids != null && !sids.isEmpty() ) {
            expression = expression.and(qOrder.realReturnSid.in(sids));
        }


        if (end == null) {
            expression = expression.and(qOrder.realEndTime.lt(begin));
        } else {
            expression = expression.and(qOrder.realBeginTime.loe(end).and(qOrder.realEndTime.goe(begin)));
        }


        if ( expression != null )
            return Lists.newArrayList(orderRepository.findAll(expression));
        else return Lists.newArrayList(orderRepository.findAll());

    }


    @Override
    public List<Order> findPendingOrders(Integer sid, Integer viid, Timestamp begin, @Nonnull Timestamp end) {

        QOrder qOrder = QOrder.order;

        BooleanExpression expression = qOrder.status.eq(StatusEnum.PENDING.getStatus());

        if ( viid != null ) expression = expression.and(qOrder.viid.eq(viid));

        if ( sid != null ) {
            expression = expression.and(qOrder.rentSid.eq(sid).or(qOrder.returnSid.eq(sid)));
        }


        if ( begin != null ) expression = expression.and(qOrder.beginTime.loe(end).and(qOrder.endTime.goe(begin)));
        else expression = expression.and(qOrder.realBeginTime.loe(end));


        return Lists.newArrayList(orderRepository.findAll(expression));
    }

    @Override
    public List<Order> findPendingOrders(Collection<Integer> sids, Integer viid, Timestamp begin, @Nonnull Timestamp end) {
        QOrder qOrder = QOrder.order;

        BooleanExpression expression = qOrder.status.eq(StatusEnum.PENDING.getStatus());

        if ( viid != null ) expression = expression.and(qOrder.viid.eq(viid));

        if ( sids != null && !sids.isEmpty() ) {
            expression = expression.and(qOrder.rentSid.in(sids).or(qOrder.returnSid.in(sids)));
        }

        if ( begin != null ) expression = expression.and(qOrder.beginTime.loe(end).and(qOrder.endTime.goe(begin)));
        else expression = expression.and(qOrder.realBeginTime.loe(end));


        return Lists.newArrayList(orderRepository.findAll(expression));
    }

    @Override
    public void countByStatus(Integer status, Map<Integer, Integer> statusCount) {
        List<Order> orders = find(null, null, status);
        for ( Order order : orders ) {
            Integer count = statusCount.get(order.getStatus());
            if ( count == null ) count = 1;
            else count += 1;
            statusCount.put(order.getStatus(), count);
        }
    }

    @Override
    public Order insert(Order order) {
        return orderRepository.save(order);
    }

    @Modifying
    @Override
    public Order update(Order order) {
        Order o = orderRepository.findById(order.getId().intValue());
        if ( o == null ) return null;
        else {
            BeanCopyUtils.copyPropertiesIgnoreNull(order, o);
            return o;
        }
    }

    @Modifying
    @Override
    public Order update(int id, int status) {
        Order o = orderRepository.findById(id);
        if ( o == null ) return null;
        else {
            o.setStatus(status);
            return o;
        }
    }

    @Modifying
    @Override
    public Order delete(int id) {
        Order order = orderRepository.findById(id);

        if (order != null) orderRepository.delete(order);

        return order;
    }
}

package org.buaa.ly.MyCar.logic.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import java.sql.Timestamp;
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

    @Override
    public void find(Integer sid, Integer viid, Integer status, List<Order> orders,
                     Map<Integer, Vehicle> vehicleMap,
                     Map<Integer, VehicleInfo> vehicleInfoMap) {
        orders.addAll(find(sid, viid, status));

        for ( Order order : orders ) {
            Vehicle vehicle = order.getVehicle();
            if ( vehicle != null ) vehicleMap.put(vehicle.getId(), vehicle);

            VehicleInfo vehicleInfo = order.getVehicleInfo();
            if ( vehicleInfo != null ) vehicleInfoMap.put(vehicleInfo.getId(), vehicleInfo);
        }

    }

    public List<Order> find(Integer sid, Integer viid, Timestamp begin, Timestamp end) {

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
            expression = expression == null ? qOrder.realBeginTime.lt(end).and(qOrder.realEndTime.gt(begin)) : expression.and(qOrder.realBeginTime.lt(end).and(qOrder.realEndTime.gt(begin)));
        }

        expression = expression == null ? qOrder.status.in(Lists.newArrayList(StatusEnum.DRAWBACK.getStatus(),StatusEnum.FINISHED.getStatus())) :
                expression.and(qOrder.status.in(Lists.newArrayList(StatusEnum.DRAWBACK.getStatus(),StatusEnum.FINISHED.getStatus())));


        return Lists.newArrayList(orderRepository.findAll(expression));
    }

    @Override
    public List<Order> findRentingOrders(Integer sid, Integer viid, Timestamp begin, Timestamp end) {
        QOrder qOrder = QOrder.order;

        BooleanExpression expression = null;

        if ( viid != null ) expression = qOrder.viid.eq(viid);

        if ( sid != null ) {
            expression = (expression == null ? qOrder.realReturnSid.eq(sid) : expression.and(qOrder.realReturnSid.eq(sid)));
        }

        if ( begin != null ) {
            if (end == null) {
                expression = (expression == null ? qOrder.realEndTime.lt(begin) : expression.and(qOrder.realEndTime.lt(begin)));
            } else {
                expression = expression == null ? qOrder.realBeginTime.lt(end).and(qOrder.realEndTime.gt(begin)) : expression.and(qOrder.realBeginTime.lt(end).and(qOrder.realEndTime.gt(begin)));
            }
        }

        if ( expression != null )
            return Lists.newArrayList(orderRepository.findAll(expression));
        else return Lists.newArrayList(orderRepository.findAll());
    }

    @Override
    public List<Order> findPendingOrders(Integer sid, Integer viid, Timestamp begin, Timestamp end) {

        QOrder qOrder = QOrder.order;

        BooleanExpression expression = null;

        if ( viid != null ) expression = qOrder.viid.eq(viid);

        if ( sid != null ) {
            expression = (expression == null ? qOrder.realReturnSid.eq(sid).or(qOrder.realReturnSid.eq(sid)) : expression.and(qOrder.realReturnSid.eq(sid).or(qOrder.realReturnSid.eq(sid))));

        }

        if ( begin != null && end != null ) {
            expression = expression == null ? qOrder.beginTime.lt(end).and(qOrder.endTime.gt(begin)).and(qOrder.status.eq(StatusEnum.PENDING.getStatus())) : expression.and(qOrder.beginTime.lt(end).and(qOrder.endTime.gt(begin)).and(qOrder.status.eq(StatusEnum.PENDING.getStatus())));
        }

        if ( expression != null )
            return Lists.newArrayList(orderRepository.findAll(expression));
        else return Lists.newArrayList(orderRepository.findAll());

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
        if ( o == null ) return o;
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

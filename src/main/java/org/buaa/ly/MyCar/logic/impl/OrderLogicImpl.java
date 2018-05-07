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
    public void find(Integer sid, Integer viid, Integer status, List<Order> orderDTOS,
                     Map<Integer, Vehicle> vehicleMap,
                     Map<Integer, VehicleInfo> vehicleInfoMap) {
        List<Order> orders = find(sid, viid, status);

        for ( Order order : orders ) {
            orders.add(order);

            Vehicle vehicle = order.getVehicle();
            if ( vehicle != null ) vehicleMap.put(vehicle.getId(), vehicle);

            VehicleInfo vehicleInfo = order.getVehicleInfo();
            if ( vehicleInfo != null ) vehicleInfoMap.put(vehicleInfo.getId(), vehicleInfo);
        }

    }

    @Override
    public List<Order> find(Integer sid, Integer viid, Timestamp begin, Timestamp end) {

        QOrder qOrder = QOrder.order;

        BooleanExpression expression = null;

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
    public List<Order> findHistoryOrders(Integer viid, Integer vid, Timestamp begin, Timestamp end, List<Integer> status) {

        QOrder order = QOrder.order;

        BooleanExpression booleanExpression  = null;

        if ( viid != null ) booleanExpression = order.vehicleInfo.id.eq(viid);
        else if ( vid != null ) booleanExpression = order.vehicle.id.eq(vid);

        if ( begin != null && end != null ) {
            BooleanExpression expression = order.realBeginTime.lt(end).and(order.realEndTime.gt(begin));
            if ( booleanExpression != null ) booleanExpression.and(expression);
            else booleanExpression = expression;
        }
        if ( status != null ) {
            BooleanExpression expression = order.status.in(status);
            if ( booleanExpression != null ) booleanExpression.and(expression);
            else booleanExpression = expression;
        }
        if ( booleanExpression != null )
            return Lists.newArrayList(orderRepository.findAll(booleanExpression));
        else return Lists.newArrayList(orderRepository.findAll());

    }

    @Override
    public List<Order> findScheduleOrders(Integer viid, Integer sid, Timestamp begin, Timestamp end) {

        QOrder order = QOrder.order;

        BooleanExpression booleanExpression = null;

        if ( viid != null ) booleanExpression = order.vehicleInfo.id.eq(viid);

        if ( sid != null ) {
            if ( booleanExpression != null ) booleanExpression.and(order.rentStore.id.eq(sid));
            booleanExpression = order.rentStore.id.eq(sid);
        }

        BooleanExpression pendingStatus = order.beginTime.lt(end).and(order.endTime.gt(begin)).and(order.status.eq(StatusEnum.PENDING.getStatus()));
        BooleanExpression rentingStatus = order.realBeginTime.lt(end).and(order.realEndTime.gt(begin)).and(order.status.eq(StatusEnum.RENTING.getStatus()));

        if ( booleanExpression != null )
            booleanExpression.and(pendingStatus.or(rentingStatus));
        else booleanExpression = pendingStatus.or(rentingStatus);

        return Lists.newArrayList(orderRepository.findAll(booleanExpression));

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

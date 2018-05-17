package org.buaa.ly.MyCar.logic.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.logic.OrderLogic;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.utils.StatusEnum;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Slf4j
@Data
public class AlgorithmLogic {

    private VehicleLogic vehicleLogic;

    private OrderLogic orderLogic;

    private List<Vehicle> vehicles;

    private List<Order> rentingOrders;

    private List<Order> pendingOrders;

    Integer sid;

    Integer viid;

    Timestamp begin;

    Timestamp end;

    private Map<Integer, Map<Integer, List<Vehicle>>> stockVehicleMap;

    private Map<Integer, Map<Integer, List<Order>>> needOrderMap;


    public AlgorithmLogic(Integer sid, Integer viid, Timestamp begin, Timestamp end,
                          VehicleLogic vehicleLogic,
                          OrderLogic orderLogic) {
        this.sid = sid;
        this.viid = viid;
        this.begin = begin;
        this.end = end;

        this.vehicles = vehicleLogic.find(sid, viid, Lists.newArrayList(StatusEnum.DELETE.getStatus()), true);

        this.rentingOrders = orderLogic.findRentingOrders(sid, viid, begin, null);

        this.pendingOrders = orderLogic.findPendingOrders(sid, viid, begin, end);

        this.stockVehicleMap = Maps.newHashMap();

        this.needOrderMap = Maps.newHashMap();

        simulate();
    }

    private <T> void put(int sid, int viid, T object, Map<Integer, Map<Integer, List<T>>> maps) {
        if (!maps.containsKey(sid)) maps.put(sid, Maps.<Integer, List<T>>newHashMap());
        if (!maps.get(sid).containsKey(viid)) maps.get(sid).put(viid, Lists.<T>newArrayList());
        maps.get(sid).get(viid).add(object);
    }

    private void simulate() {

        for ( Vehicle vehicle : vehicles ) {
            int status = vehicle.getStatus();
            if ( status == StatusEnum.OK.getStatus() || status == StatusEnum.SPARE.getStatus() ) {
                put(vehicle.getSid(), vehicle.getViid(), vehicle, stockVehicleMap);
            } else if ( (status == StatusEnum.FIXING.getStatus() || status == StatusEnum.VALIDATE.getStatus()) ) {
                if ( vehicle.getBeginTime() == null || vehicle.getEndTime() == null ) continue;
                if( (vehicle.getEndTime().compareTo(begin) < 0 || vehicle.getBeginTime().compareTo(end) > 0 ))
                    put(vehicle.getSid(), vehicle.getViid(), vehicle, stockVehicleMap);
            }
        }

        for ( Order order : rentingOrders ) {

            int sid = order.getReturnSid();

            put(sid, order.getViid(), order.getVehicle(), stockVehicleMap);

        }

        for ( Order order : pendingOrders ) {
            int status = order.getStatus();

            if ( status == StatusEnum.PENDING.getStatus() && (order.getBeginTime().compareTo(end) < 0 && order.getEndTime().compareTo(begin) > 0) ) {
                put(order.getRentSid(), order.getViid(), order, needOrderMap);
            }
        }

    }


}

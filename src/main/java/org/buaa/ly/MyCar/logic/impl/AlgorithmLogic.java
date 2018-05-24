package org.buaa.ly.MyCar.logic.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.logic.OrderLogic;
import org.buaa.ly.MyCar.logic.StoreLogic;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.utils.StatusEnum;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Data
public class AlgorithmLogic {

    private Map<Integer, VehicleInfo> vehicleInfoMap = Maps.newHashMap();

    private List<Vehicle> vehicles;

    private List<Order> rentingOrders;

    private List<Order> pendingOrders;

    Integer sid;

    Integer viid;

    Timestamp begin;

    Timestamp end;

    private Map<Integer, String> sidCityMap = Maps.newHashMap();

    private Map<Integer, Map<Integer, List<Vehicle>>> stockMap = Maps.newHashMap();

    private Map<Integer, Map<Integer, Integer>> stockTransformMap = Maps.newHashMap();

    private Map<Integer, Map<Integer, List<Order>>> needOrderMap = Maps.newHashMap();

    private Map<String, Map<Integer, List<Vehicle>>> stockCityMap = Maps.newHashMap();;

    private Map<String, Map<Integer, Integer>> stockCityTransformMap = Maps.newHashMap();;

    private Map<String, Map<Integer, List<Order>>> needCityOrderMap = Maps.newHashMap();;

    public AlgorithmLogic(String city, Integer sid,
                          Integer viid, Timestamp begin, Timestamp end,
                          StoreLogic storeLogic,
                          VehicleLogic vehicleLogic,
                          OrderLogic orderLogic) {
        this.sid = sid;
        this.viid = viid;
        this.begin = begin;
        this.end = end;


        if ( city != null ) {

            List<Store> stores = storeLogic.findByCity(city);

            Set<Integer> sids = Sets.newHashSet();

            for ( Store store : stores ) {
                sids.add(store.getId());
                sidCityMap.put(store.getId(), store.getCity());
            }

            this.vehicles = vehicleLogic.find(sids, viid, Lists.newArrayList(StatusEnum.DELETE.getStatus()), true);

            this.rentingOrders = orderLogic.findRentingOrders(sids, viid, begin, null);

            this.pendingOrders = orderLogic.findPendingOrders(sids, viid, null, end);


        } else {

            this.vehicles = vehicleLogic.find(sid, viid, Lists.newArrayList(StatusEnum.DELETE.getStatus()), true);

            this.rentingOrders = orderLogic.findRentingOrders(sid, viid, begin, null);

            this.pendingOrders = orderLogic.findPendingOrders(sid, viid, null, end);

        }

        simulate();

        mergeList(stockMap, stockCityMap);
        mergeInteger(stockTransformMap, stockCityTransformMap);
        mergeList(needOrderMap, needCityOrderMap);

    }

    private <T> void mergeList(Map<Integer, Map<Integer, List<T>>> first, Map<String, Map<Integer, List<T>>> seconds) {

        for ( Map.Entry<Integer, Map<Integer, List<T>>> entry : first.entrySet() ) {
            if ( !sidCityMap.containsKey(entry.getKey()) ) continue;
            String city = sidCityMap.get(entry.getKey());

            if ( !seconds.containsKey(city) ) seconds.put(city, Maps.newHashMap(entry.getValue()));
            else {
                for ( Map.Entry<Integer, List<T>> orderEntry : entry.getValue().entrySet() ) {

                    if ( seconds.get(city).containsKey(orderEntry.getKey()) ) {
                        seconds.get(city).get(orderEntry.getKey()).addAll(orderEntry.getValue());
                    } else {
                        seconds.get(city).put(orderEntry.getKey(),orderEntry.getValue());
                    }
                }
            }
        }
    }

    private <T> void mergeInteger(Map<Integer, Map<Integer, Integer>> first, Map<String, Map<Integer, Integer>> seconds) {

        for ( Map.Entry<Integer, Map<Integer, Integer>> entry : first.entrySet() ) {
            if ( !sidCityMap.containsKey(entry.getKey()) ) continue;
            String city = sidCityMap.get(entry.getKey());

            if ( !seconds.containsKey(city) ) seconds.put(city, Maps.newHashMap(entry.getValue()));
            else {
                for ( Map.Entry<Integer, Integer> orderEntry : entry.getValue().entrySet() ) {

                    if ( seconds.get(city).containsKey(orderEntry.getKey()) ) {
                        int count = seconds.get(city).get(orderEntry.getKey());
                        seconds.get(city).put(orderEntry.getKey(), count + orderEntry.getValue());
                    } else {
                        seconds.get(city).put(orderEntry.getKey(),orderEntry.getValue());
                    }
                }
            }
        }
    }

    private <T> void put(int sid, int viid, T object, Map<Integer, Map<Integer, List<T>>> maps) {

        if (!maps.containsKey(sid)) maps.put(sid, Maps.<Integer, List<T>>newHashMap());
        if (!maps.get(sid).containsKey(viid)) maps.get(sid).put(viid, Lists.<T>newArrayList());
        maps.get(sid).get(viid).add(object);

    }

    private <T> void transform(T fromCityOrSid, T toCityOrSid, int viid, Map<T, Map<Integer, Integer>> maps) {

        if ( !maps.containsKey(fromCityOrSid) ) {
            maps.put(fromCityOrSid, Maps.<Integer, Integer>newHashMap());
            maps.get(fromCityOrSid).put(viid, -1);
        } else if ( !maps.get(fromCityOrSid).containsKey(viid) ) {
            maps.get(fromCityOrSid).put(viid, -1);
        } else {
            int count = maps.get(fromCityOrSid).get(viid);
            maps.get(fromCityOrSid).put(viid, count+1);
        }

        if ( !maps.containsKey(toCityOrSid) ) {
            maps.put(toCityOrSid, Maps.<Integer, Integer>newHashMap());
            maps.get(toCityOrSid).put(viid, 1);
        } else if ( !maps.get(toCityOrSid).containsKey(viid )) {
            maps.get(toCityOrSid).put(viid, 1);
        } else {
            int count = maps.get(toCityOrSid).get(viid);
            maps.get(toCityOrSid).put(viid, count+1);
        }
    }

    private void simulate() {

        for ( Vehicle vehicle : vehicles ) {

            int status = vehicle.getStatus();

            if ( !vehicleInfoMap.containsKey(vehicle.getViid()) ) vehicleInfoMap.put(vehicle.getViid(), vehicle.getVehicleInfo());

            if ( status == StatusEnum.OK.getStatus() ) {
                put(vehicle.getSid(), vehicle.getViid(), vehicle, stockMap);
            } else if ( (status == StatusEnum.FIXING.getStatus() || status == StatusEnum.VALIDATE.getStatus()) ) {
                if ( vehicle.getBeginTime() == null || vehicle.getEndTime() == null ) continue;
                if( (vehicle.getEndTime().compareTo(begin) < 0 || vehicle.getBeginTime().compareTo(end) > 0 ))
                    put(vehicle.getSid(), vehicle.getViid(), vehicle, stockMap);
            }


        }

        for ( Order order : rentingOrders ) {

            int sid = order.getReturnSid();

            put(sid, order.getViid(), order.getVehicle(), stockMap);

        }

        for ( Order order : pendingOrders ) {

            int rentId = order.getRentSid();
            int returnId = order.getReturnSid();

            if ( order.getEndTime().compareTo(begin) < 0 && rentId != returnId ) {
                transform(rentId, returnId, order.getViid(),  stockTransformMap);
            }

            if ( (order.getBeginTime().compareTo(end) < 0 && order.getEndTime().compareTo(begin) > 0) ) {
                put(order.getRentSid(), order.getViid(), order, needOrderMap);
            }

        }

    }

}

package org.buaa.ly.MyCar.logic.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.QVehicle;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.internal.Stock;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.repository.VehicleInfoRepository;
import org.buaa.ly.MyCar.repository.VehicleRepository;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component("vehicleLogic")
@Slf4j
@Transactional
public class VehicleLogicImpl implements VehicleLogic {

    private VehicleRepository vehicleRepository;

    @Autowired
    void setVehicleRepository(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle find(int id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Vehicle find(String number) {
        return vehicleRepository.findByNumber(number);
    }

    @Override
    public int count(Integer sid, Integer viid, Collection<Integer> status, boolean exclude) {
        QVehicle qVehicle = QVehicle.vehicle;

        BooleanExpression expression = null;

        if ( sid != null ) expression = qVehicle.sid.eq(sid);

        if ( viid != null ) expression = expression == null ? qVehicle.viid.eq(viid) : expression.and(qVehicle.viid.eq(viid));

        if ( status != null ) {
            if ( !exclude ) {
                expression = expression == null ? qVehicle.status.in(status) : expression.and(qVehicle.status.in(status));
            } else {
                expression = expression == null ? qVehicle.status.in(status) : expression.and(qVehicle.status.in(status).not());
            }
        }

        if ( expression != null ) return (int)vehicleRepository.count(expression);
        return (int)vehicleRepository.count();

    }

    @Override
    public List<Vehicle>  find(Integer sid, Integer viid, Collection<Integer> status, boolean exclude) {

        QVehicle qVehicle = QVehicle.vehicle;

        BooleanExpression expression = null;

        if ( sid != null ) {
            expression = qVehicle.sid.eq(sid);
        }

        if ( viid != null ) {
            expression = expression == null ? qVehicle.viid.eq(viid) : expression.and(qVehicle.viid.eq(viid));
        }

        if ( status != null || !status.isEmpty() ) {
            if ( !exclude ) {
                expression = expression == null ? qVehicle.status.in(status) : expression.and(qVehicle.status.in(status));
            } else {
                expression = expression == null ? qVehicle.status.in(status).not() : expression.and(qVehicle.status.in(status).not());
            }
        }

        if ( expression != null ) return Lists.newArrayList(vehicleRepository.findAll(expression));
        else return Lists.newArrayList(vehicleRepository.findAll());
    }

    @Override
    public List<Vehicle> find(Collection<Integer> sids, Integer viid, Collection<Integer> status, boolean exclude) {

        QVehicle qVehicle = QVehicle.vehicle;

        BooleanExpression expression = null;

        if (sids != null && !sids.isEmpty() ) expression = qVehicle.sid.in(sids);

        if (viid != null) expression = expression == null? qVehicle.viid.eq(viid) : expression.and(qVehicle.viid.eq(viid));


        if ( status != null && !status.isEmpty() ) {
            if ( !exclude ) {
                expression = expression == null ? qVehicle.status.in(status) : expression.and(qVehicle.status.in(status));
            } else {
                expression = expression == null ? qVehicle.status.in(status).not() : expression.and(qVehicle.status.in(status).not());
            }
        }

        if ( expression != null ) return Lists.newArrayList(vehicleRepository.findAll(expression));
        else return Lists.newArrayList(vehicleRepository.findAll());
    }

//    @Override
//    public List<Vehicle> findStatusNot(Integer viid, Integer sid, Integer status) {QVehicle vehicle = QVehicle.vehicle;
//        BooleanExpression expression = null;
//
//        if ( viid != null ) expression = vehicle.vehicleInfo.id.eq(viid);
//
//        if ( sid != null ) {
//            if ( expression == null )  expression = vehicle.store.id.eq(sid);
//            else expression.and(vehicle.store.id.eq(sid));
//        }
//
//        if ( status != null ) {
//            if ( expression == null ) expression = vehicle.status.eq(status).not();
//            else expression.and(vehicle.status.eq(status).not());
//        }
//
//        if ( expression != null ) return Lists.newArrayList(vehicleRepository.findAll(expression));
//        else return Lists.newArrayList(vehicleRepository.findAll());
//    }

//    @Override
//    public Map<Integer, Map<Integer, Map<Integer, List<Vehicle>>>> countByStoreAndVehicleInfoAndStatus(Integer viid, Integer sid, Collection<Integer> status, Timestamp timestamp) {
//        Map<Integer, Map<Integer, Map<Integer, List<Vehicle>>>> vehicles = findByStoreAndVehicleInfoAndStatus(viid, sid, status);
//
//        Map<Integer, Map<Integer, Map<Integer, Integer>>> count = Maps.newHashMap();
//
//        for ( Map.Entry<Integer, Map<Integer, Map<Integer, List<Vehicle>>>> storeEntry : vehicles.entrySet() ) {
//            int  storeId = storeEntry.getKey();
//            for ( Map.Entry<Integer, Map<Integer, List<Vehicle>>> vehicleInfoEntry : storeEntry.getValue().entrySet() ) {
//                int vehicleInfoId = vehicleInfoEntry.getKey();
//                for ( Map.Entry<Integer, List<Vehicle>> vehicleEntry : vehicleInfoEntry.getValue().entrySet() ) {
//
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Map<Integer, Map<Integer, Map<Integer, List<Vehicle>>>> findByStoreAndVehicleInfoAndStatus(Integer viid, Integer sid, Collection<Integer> status) {
//
//        Map<Integer, Map<Integer, Map<Integer, List<Vehicle>>>> result = Maps.newHashMap();
//        QVehicle qVehicle = QVehicle.vehicle;
//
//        BooleanExpression booleanExpression = null;
//        if ( sid != null ) booleanExpression = qVehicle.store.id.eq(sid);
//
//        if ( viid != null ) {
//            if ( booleanExpression == null ) booleanExpression = qVehicle.vehicleInfo.id.eq(viid);
//            else booleanExpression.and(qVehicle.vehicleInfo.id.eq(viid));
//        }
//
//        if ( status != null ) {
//            if ( booleanExpression == null ) booleanExpression = qVehicle.status.in(status);
//            else booleanExpression.and(qVehicle.status.in(status));
//        }
//
//        Iterable<Vehicle> iterator;
//        if ( booleanExpression != null )  iterator = vehicleRepository.findAll(booleanExpression);
//        else iterator = vehicleRepository.findAll();
//
//        for (Vehicle vehicle : iterator) {
//            int s = vehicle.getSid();
//            int v = vehicle.getViid();
//
//            if ( !result.containsKey(s) ) result.put(s, Maps.<Integer, Map<Integer, List<Vehicle>>>newHashMap());
//
//            Map<Integer, Map<Integer, List<Vehicle>>> storeVehicles = result.get(s);
//
//            if ( !storeVehicles.containsKey(v) ) storeVehicles.put(v, Maps.<Integer, List<Vehicle>>newHashMap());
//
//            Map<Integer, List<Vehicle>> viidVehicles = storeVehicles.get(v);
//
//            if ( !viidVehicles.containsKey(vehicle.getStatus()) ) viidVehicles.put(vehicle.getStatus(), Lists.<Vehicle>newArrayList());
//
//            List<Vehicle> vehicles = viidVehicles.get(vehicle.getStatus());
//
//            vehicles.add(vehicle);
//        }
//
//        return result;
//    }

    @Override
    public Vehicle insert(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Modifying
    @Override
    public Vehicle update(Vehicle vehicle) {
        Vehicle v = vehicleRepository.findById(vehicle.getId().intValue());
        if ( v != null ) {
            BeanCopyUtils.copyPropertiesIgnoreNull(vehicle, v);
        }
        return v;
    }

    @Override
    public Vehicle delete(int id) {
        return vehicleRepository.deleteById(id);
    }

    @Modifying
    @Override
    public Vehicle updateStatus(int id, int status) {
        Vehicle v = vehicleRepository.findById(id);
        if ( v == null ) return null;
        v.setStatus(status);
        return v;
    }
}

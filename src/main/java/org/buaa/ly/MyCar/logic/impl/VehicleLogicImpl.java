package org.buaa.ly.MyCar.logic.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.QVehicle;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.repository.VehicleInfoRepository;
import org.buaa.ly.MyCar.repository.VehicleRepository;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component("vehicleLogic")
@Slf4j
@Transactional
public class VehicleLogicImpl implements VehicleLogic {

    private VehicleRepository vehicleRepository;

    private VehicleInfoRepository vehicleInfoRepository;

    @Autowired
    void setVehicleRepository(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Autowired
    public void setVehicleInfoRepository(VehicleInfoRepository vehicleInfoRepository) {
        this.vehicleInfoRepository = vehicleInfoRepository;
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
    public List<Vehicle> find(Integer viid, Integer sid, Integer status) {
        QVehicle vehicle = QVehicle.vehicle;
        BooleanExpression expression = null;
        if ( viid != null ) {
            expression = vehicle.vehicleInfo.id.eq(viid);
        }

        if ( sid != null ) {
            Store store  = new Store();
            store.setId(sid);
            if ( expression == null )  expression = vehicle.store.id.eq(sid);
            else expression.and(vehicle.store.eq(store));

        }

        if ( status != null ) {
            if ( expression == null ) expression = vehicle.status.eq(status);
            else expression.and(vehicle.status.eq(status));
        }

        if ( expression != null ) return Lists.newArrayList(vehicleRepository.findAll(expression));
        else return Lists.newArrayList(vehicleRepository.findAll());
    }

    @Override
    public List<Vehicle> findStatusNot(Integer viid, Integer sid, Integer status) {QVehicle vehicle = QVehicle.vehicle;
        BooleanExpression expression = null;

        if ( viid != null ) expression = vehicle.vehicleInfo.id.eq(viid);

        if ( sid != null ) {
            if ( expression == null )  expression = vehicle.store.id.eq(sid);
            else expression.and(vehicle.store.id.eq(sid));
        }

        if ( status != null ) {
            if ( expression == null ) expression = vehicle.status.eq(status).not();
            else expression.and(vehicle.status.eq(status).not());
        }

        if ( expression != null ) return Lists.newArrayList(vehicleRepository.findAll(expression));
        else return Lists.newArrayList(vehicleRepository.findAll());
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> countByStoreAndVehicleInfoAndStatusNot(Integer viid, Integer sid, Integer status) {

        Map<Integer, Map<Integer, Integer>> result = Maps.newHashMap();
        QVehicle qVehicle = QVehicle.vehicle;

        BooleanExpression booleanExpression = null;
        if ( sid != null ) booleanExpression = qVehicle.store.id.eq(sid);

        if ( viid != null ) {
            if ( booleanExpression == null ) booleanExpression = qVehicle.vehicleInfo.id.eq(viid);
            else booleanExpression.and(qVehicle.vehicleInfo.id.eq(viid));
        }

        if ( status != null ) {
            if ( booleanExpression == null ) booleanExpression = qVehicle.status.ne(status);
            else booleanExpression.and(qVehicle.status.ne(status));
        }

        Iterable<Vehicle> iterator;
        if ( booleanExpression != null )  iterator = vehicleRepository.findAll(booleanExpression);
        else iterator = vehicleRepository.findAll();

        for (Vehicle vehicle : iterator) {
            int s = vehicle.getStore().getId();
            int v = vehicle.getVehicleInfo().getId();

            if ( !result.containsKey(s) ) result.put(s, Maps.<Integer, Integer>newHashMap());

            Map<Integer, Integer> storeVehicles = result.get(s);

            if ( !storeVehicles.containsKey(v) ) storeVehicles.put(v, 1);
            else storeVehicles.put(v, storeVehicles.get(v)+1);
        }

        return result;
    }

    @Override
    public Vehicle insert(int viid, Vehicle vehicle) {
        VehicleInfo vehicleInfo = vehicleInfoRepository.findById(viid);
        if ( vehicleInfo == null ) return null;
        else {
            vehicle.setVehicleInfo(vehicleInfo);
            return vehicleRepository.save(vehicle);
        }
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

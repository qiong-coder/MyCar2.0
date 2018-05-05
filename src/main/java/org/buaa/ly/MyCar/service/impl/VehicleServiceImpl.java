package org.buaa.ly.MyCar.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.exception.DuplicateError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.logic.OrderLogic;
import org.buaa.ly.MyCar.logic.VehicleInfoLogic;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Component("vehicleService")
@Slf4j
@Transactional
public class VehicleServiceImpl implements VehicleService {


    private VehicleLogic vehicleLogic;

    private VehicleInfoLogic vehicleInfoLogic;

    private OrderLogic orderLogic;

    @Autowired
    void setVehicleLogic(VehicleLogic vehicleLogic) {
        this.vehicleLogic = vehicleLogic;
    }

    @Autowired
    public void setOrderLogic(OrderLogic orderLogic) {
        this.orderLogic = orderLogic;
    }

    @Autowired
    public void setVehicleInfoLogic(VehicleInfoLogic vehicleInfoLogic) {
        this.vehicleInfoLogic = vehicleInfoLogic;
    }

    @Override
    public VehicleDTO find(int id) {
        Vehicle vehicle = vehicleLogic.find(id);
        if ( vehicle == null ) throw new NotFoundError(String.format("failure to find the vehicle - %s", id));
        return VehicleDTO.build(vehicle, VehicleDTO.class);
    }

    @Override
    public List<VehicleDTO> findBySidAndViidAndStatus(Integer sid, Integer viid, Collection<Integer> status, boolean exclude) {
        return VehicleDTO.build(vehicleLogic.find(sid, viid, status, exclude));
    }

    @Override
    public List<VehicleDTO> findByViidAndSidAndTimestamp(int viid, int sid, Timestamp begin, Timestamp end) {
        return null;
    }

    @Override
    public VehicleDTO insert(VehicleDTO vehicleDTO) {

        if ( vehicleLogic.find(vehicleDTO.getNumber()) != null ) throw new DuplicateError(String.format("vehicle with number:%s is duplicated", vehicleDTO.getNumber()));

        if ( vehicleInfoLogic.find(vehicleDTO.getViid()) == null ) throw new NotFoundError(String.format("failure to find the vehicle info:%d",vehicleDTO.getViid()));

        return VehicleDTO.build(vehicleLogic.insert(vehicleDTO.build()), VehicleDTO.class);
    }

    @Override
    public VehicleDTO update(VehicleDTO vehicleDTO) {

        Vehicle vehicle = vehicleDTO.build();

        if ( vehicleLogic.update(vehicle) == null ) throw new NotFoundError(String.format("failure to find the vehicle - %d",vehicleDTO.getId()));
        return VehicleDTO.build(vehicle);
    }

    @Modifying
    @Override
    public VehicleDTO update(int id, int status) {
        Vehicle vehicle = vehicleLogic.updateStatus(id, status);
        if ( vehicle == null ) throw new NotFoundError(String.format("failure to find the vehicle - %d", id));
        return VehicleDTO.build(vehicle);
    }

    @Override
    public VehicleDTO delete(int id) {
        Vehicle vehicle = vehicleLogic.delete(id);
        if ( vehicle == null ) throw new NotFoundError(String.format("failure to find the vehicle - %d", id));
        return VehicleDTO.build(vehicle);
    }
}

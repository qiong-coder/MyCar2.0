package org.buaa.ly.MyCar.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.exception.DuplicateError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("vehicleService")
@Slf4j
@Transactional
public class VehicleServiceImpl implements VehicleService {


    private VehicleLogic vehicleLogic;

    @Autowired
    void setVehicleLogic(VehicleLogic vehicleLogic) {
        this.vehicleLogic = vehicleLogic;
    }


    @Override
    public VehicleDTO find(int id) {
        Vehicle vehicle = vehicleLogic.find(id);
        if ( vehicle == null ) throw new NotFoundError(String.format("failure to find the vehicle - %s", id));
        return VehicleDTO.build(vehicle, VehicleDTO.class);
    }

    @Override
    public List<VehicleDTO> findByStatusNot(int status) {
        return VehicleDTO.build(vehicleLogic.findStatusNot(null, null, status), VehicleDTO.class);
    }


    @Override
    public List<VehicleDTO> findByViidAndStatusNot(int viid, int status) {
        return VehicleDTO.build(vehicleLogic.findStatusNot(viid, null, status), VehicleDTO.class);
    }

    @Override
    public List<VehicleDTO> findByViidAndSidAndStatus(int viid, int sid, int status) {
        return VehicleDTO.build(vehicleLogic.find(viid, sid, status), VehicleDTO.class);
    }

    @Override
    public VehicleDTO insert(int viid, VehicleDTO vehicleDTO) {
        Vehicle v = vehicleLogic.find(vehicleDTO.getNumber());
        if ( v != null ) throw new DuplicateError(String.format("vehicle with number:%s is duplicated", vehicleDTO.getNumber()));
        return VehicleDTO.build(vehicleLogic.insert(viid, vehicleDTO.build()), VehicleDTO.class);
    }

    @Modifying
    @Override
    public VehicleDTO update(int id, VehicleDTO vehicleDTO) {
        vehicleDTO.setId(id);
        Vehicle vehicle = vehicleDTO.build();

        if ( vehicleLogic.update(vehicle) == null ) throw new NotFoundError(String.format("failure to find the vehicle - %d",id));
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

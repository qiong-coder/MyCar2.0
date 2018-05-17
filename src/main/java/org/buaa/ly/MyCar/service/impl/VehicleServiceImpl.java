package org.buaa.ly.MyCar.service.impl;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.exception.DuplicateError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.logic.OrderLogic;
import org.buaa.ly.MyCar.logic.VehicleInfoLogic;
import org.buaa.ly.MyCar.logic.VehicleLogic;
import org.buaa.ly.MyCar.service.VehicleService;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
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
        // TODO: 实际的检测逻辑

        List<Vehicle> vehicles = vehicleLogic.find(sid, viid, Lists.newArrayList(StatusEnum.DELETE.getStatus()), true);

        Iterator<Vehicle> iterator = vehicles.iterator();

        while ( iterator.hasNext() ) {
            Vehicle vehicle = iterator.next();

            int status = vehicle.getStatus();

            if ( status == StatusEnum.OK.getStatus() || status == StatusEnum.SPARE.getStatus() ) continue;
            if ( status == StatusEnum.RENTING.getStatus() && (vehicle.getEndTime().compareTo(begin) < 0 || vehicle.getBeginTime().compareTo(end) > 0) ) continue;

            iterator.remove();
        }

        return VehicleDTO.build(vehicles);
    }

    @Override
    public VehicleDTO insert(VehicleDTO vehicleDTO) {

        Vehicle vehicle = vehicleLogic.find(vehicleDTO.getNumber());

        if ( vehicle != null ) {
            vehicleDTO.setId(vehicle.getId());
            vehicleDTO.setStatus(StatusEnum.OK.getStatus());
            if (vehicle.getStatus().compareTo(StatusEnum.DELETE.getStatus()) != 0 ) throw new DuplicateError("vehicle duplicate");
            else return VehicleDTO.build(vehicleLogic.update(vehicleDTO.build()));
        }

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

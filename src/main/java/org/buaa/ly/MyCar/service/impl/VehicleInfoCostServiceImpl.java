package org.buaa.ly.MyCar.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.request.CostInfoRequest;
import org.buaa.ly.MyCar.http.response.CostInfoResponse;
import org.buaa.ly.MyCar.http.response.CostInfoWithTimestamp;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;
import org.buaa.ly.MyCar.logic.VehicleInfoCostLogic;
import org.buaa.ly.MyCar.service.VehicleInfoCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Component
@Slf4j
@Transactional
public class VehicleInfoCostServiceImpl implements VehicleInfoCostService {

    private VehicleInfoCostLogic vehicleInfoCostLogic;

    @Autowired
    public void setVehicleInfoCostLogic(VehicleInfoCostLogic vehicleInfoCostLogic) {
        this.vehicleInfoCostLogic = vehicleInfoCostLogic;
    }

    @Override
    public CostInfoResponse find(int id) {
        VehicleInfoCost vehicleInfoCost = vehicleInfoCostLogic.find(id);
        if ( vehicleInfoCost == null ) throw new NotFoundError("failure to find vehicle info cost");
        return new CostInfoResponse(vehicleInfoCost);
    }

    @Override
    public CostInfoWithTimestamp find(int id, Timestamp begin, Timestamp end) {
        VehicleInfoCost vehicleInfoCost = vehicleInfoCostLogic.find(id);
        if ( vehicleInfoCost == null ) throw new NotFoundError("failure to find vehicle info cost");
        return CostInfoWithTimestamp.build(vehicleInfoCost, begin, end);
    }

    @Modifying
    @Override
    public VehicleInfoCost update(int id, CostInfoRequest costInfoRequest) {
        return vehicleInfoCostLogic.update(id, costInfoRequest.build());
    }
}
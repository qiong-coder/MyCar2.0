package org.buaa.ly.MyCar.logic.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.request.CostInfoRequest;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;
import org.buaa.ly.MyCar.logic.VehicleInfoCostLogic;
import org.buaa.ly.MyCar.repository.VehicleInfoRepository;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

@Component("vehicleInfoCostLogic")
@Slf4j
public class VehicleInfoCostLogicImpl implements VehicleInfoCostLogic {

    private VehicleInfoRepository vehicleInfoRepository;

    @Autowired

    void setVehicleInfoRepository(VehicleInfoRepository vehicleInfoRepository) {
        this.vehicleInfoRepository = vehicleInfoRepository;
    }

    @Override
    public VehicleInfoCost find(int id) {
        VehicleInfo vehicleInfo = vehicleInfoRepository.findById(id);
        if ( vehicleInfo == null ) return null;

        if ( vehicleInfo.getCost() == null ) {
            return update(id, CostInfoRequest.build(10000, 100));
        } else {
            return JSONObject.parseObject(vehicleInfo.getCost(), VehicleInfoCost.class);
        }
    }

    @Override
    public VehicleInfoCost insert(int id, VehicleInfoCost vehicleInfoCost) {
        return update(id, vehicleInfoCost);
    }

    @Modifying
    @Override
    public VehicleInfoCost update(int id, VehicleInfoCost vehicleInfoCost) {
        VehicleInfo vehicleInfo = vehicleInfoRepository.findById(id);
        if ( vehicleInfo == null ) return null;

        VehicleInfoCost vehicleInfoCost1 = vehicleInfo.getCost() == null? CostInfoRequest.build(10000, 100):
                JSONObject.parseObject(vehicleInfo.getCost(), VehicleInfoCost.class);

        BeanCopyUtils.copyPropertiesIgnoreNull(vehicleInfoCost, vehicleInfoCost1);
        vehicleInfo.setCost(JSONObject.toJSONString(vehicleInfoCost1));
        return vehicleInfoCost1;
    }
}

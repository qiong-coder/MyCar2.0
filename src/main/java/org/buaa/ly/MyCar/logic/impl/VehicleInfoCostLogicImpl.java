package org.buaa.ly.MyCar.logic.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.dto.VehicleInfoCostDTO;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;
import org.buaa.ly.MyCar.logic.VehicleInfoCostLogic;
import org.buaa.ly.MyCar.repository.VehicleInfoRepository;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("vehicleInfoCostLogic")
@Slf4j
@PropertySource("classpath:default-value.properties")
public class VehicleInfoCostLogicImpl implements VehicleInfoCostLogic {

    @Value("${vehicle.info.cost.insurance}") private String insurance;
    @Value("${vehicle.info.cost.day_cost}") private int cost;
    @Value("${vehicle.info.cost.discount}") private int discount;


    private VehicleInfoRepository vehicleInfoRepository;

    @Autowired
    void setVehicleInfoRepository(VehicleInfoRepository vehicleInfoRepository) {
        this.vehicleInfoRepository = vehicleInfoRepository;
    }

    private List<Integer> parseInsurance() {
        String[] list = insurance.split(",");
        List<Integer> ins = Lists.newArrayList(list.length);
        for ( String s : list ) {
            ins.add(Integer.parseInt(s));
        }
        return ins;
    }



    @Override
    public VehicleInfoCost find(int id) {
        VehicleInfo vehicleInfo = vehicleInfoRepository.findById(id);
        if ( vehicleInfo == null ) return null;

        if ( vehicleInfo.getCost() == null ) {
            return update(id, VehicleInfoCostDTO.build(10000, 100, null));
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

        if ( vehicleInfo.getCost() == null ) {
            vehicleInfo.setCost(JSONObject.toJSONString(vehicleInfoCost));
            return vehicleInfoCost;
        } else {
            VehicleInfoCost v = JSONObject.parseObject(vehicleInfo.getCost(), VehicleInfoCost.class);
            BeanCopyUtils.copyPropertiesIgnoreNull(vehicleInfoCost, v);
            vehicleInfo.setCost(JSONObject.toJSONString(v));
            return v;
        }
    }

    @Override
    public VehicleInfoCostDTO defaultCost() {
        return VehicleInfoCostDTO.build(cost, discount, parseInsurance());
    }
}

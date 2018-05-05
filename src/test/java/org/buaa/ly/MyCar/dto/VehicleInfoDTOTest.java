package org.buaa.ly.MyCar.dto;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;
import org.junit.Test;

import java.util.List;

@Slf4j
public class VehicleInfoDTOTest extends TestLoader {


    @Test
    public void vehicleInfoDTOTest() {

        VehicleInfo vehicleInfo = new VehicleInfo();
        VehicleInfoCost vehicleInfoCost = new VehicleInfoCost();
        vehicleInfoCost.setInsurance(Lists.newArrayList(10,20,30));

        List<List<Integer>> day_costs = Lists.newArrayList();
        day_costs.add(Lists.newArrayList(10,20));
        day_costs.add(Lists.newArrayList(30,40));
        vehicleInfoCost.setDay_costs(day_costs);

        vehicleInfo.setCost(JSON.toJSONString(vehicleInfoCost));

        log.info("vehicleInfo:{}", JSON.toJSONString(vehicleInfo));

        VehicleInfoDTO vehicleInfoDTO = JSON.parseObject(JSON.toJSONString(vehicleInfo), VehicleInfoDTO.class);

        log.info("vehicleInfoDTO:{}", JSON.toJSONString(vehicleInfoDTO));
    }

}

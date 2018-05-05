package org.buaa.ly.MyCar.dto;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
public class OrderDTOTest extends TestLoader{


    @Test
    public void test2DTO() {

        Order order = new Order();

        VehicleInfo vehicleInfo = new VehicleInfo();

        vehicleInfo.setId(1);

        order.setVehicleInfo(vehicleInfo);

        List<Integer> insurance = Lists.newArrayList(100,1000,10000);
        order.setInsurance(JSON.toJSONString(insurance));

        order.setBeginTime(new Timestamp(System.currentTimeMillis()));

        String json = JSON.toJSONString(order);

        log.info("json:{}",json);

        OrderDTO orderDTO = JSON.parseObject(json, OrderDTO.class);

        log.info("orderDTO:{}", orderDTO);
    }



}

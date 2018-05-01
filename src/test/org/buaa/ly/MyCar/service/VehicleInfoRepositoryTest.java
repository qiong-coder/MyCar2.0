package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.mock.web.MockPart;

import javax.servlet.http.Part;


public class VehicleInfoRepositoryTest extends TestLoader{

    @Autowired
    private VehicleInfoService vehicleInfoService;


    @Test
    public void insertTest() {
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setName("A6");
        vehicleInfo.setDisplacement("test");
        vehicleInfo.setGearbox("test");
        vehicleInfo.setBoxes("2");
        vehicleInfo.setManned("test");
        vehicleInfo.setOil("test");
        vehicleInfo.setDescription("test");
        vehicleInfo.setType("test");
        vehicleInfo.setCost("test");
        vehicleInfo.setPicture("test");
        Part attachment = new MockPart("test.jpg","c".getBytes());
        vehicleInfoService.insert(VehicleInfoDTO.build(vehicleInfo), attachment);
    }

}

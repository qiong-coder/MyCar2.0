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
        VehicleInfoDTO vehicleInfoDTO = new VehicleInfoDTO();
        vehicleInfoDTO.setName("A6");
        vehicleInfoDTO.setDisplacement("test");
        vehicleInfoDTO.setGearbox("test");
        vehicleInfoDTO.setBoxes("2");
        vehicleInfoDTO.setManned("test");
        vehicleInfoDTO.setOil("test");
        vehicleInfoDTO.setDescription("test");
        vehicleInfoDTO.setType("test");
        Part attachment = new MockPart("test.jpg","ccc".getBytes());
        vehicleInfoService.insert(vehicleInfoDTO, attachment);
    }

}

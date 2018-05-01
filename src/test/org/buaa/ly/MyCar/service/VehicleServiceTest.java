package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleServiceTest extends TestLoader {


    @Autowired private VehicleService vehicleService;

    @Test
    public void insertTest() {
        Vehicle vehicle = new Vehicle();
        vehicle.setDescription("null");
        vehicle.setNumber("test");

    }

    @Test
    public void findVehicleTest() {
        VehicleDTO vehicle = vehicleService.find(1);
    }
}

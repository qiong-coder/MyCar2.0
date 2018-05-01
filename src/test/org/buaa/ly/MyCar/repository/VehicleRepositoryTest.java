package org.buaa.ly.MyCar.repository;

import com.google.common.collect.Lists;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.entity.QVehicle;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class VehicleRepositoryTest extends TestLoader {


    @Autowired VehicleRepository vehicleRepository;

    @Autowired VehicleInfoRepository vehicleInfoRepository;



    @Test
    public void insertTest() {

        VehicleInfo vehicleInfo = vehicleInfoRepository.findById(1);

        assert(vehicleInfo != null);

        Vehicle vehicle = new Vehicle();
        vehicle.setDescription("null");
        vehicle.setNumber("test");
        vehicle.setVehicleInfo(vehicleInfo);

        vehicleRepository.save(vehicle);
        assert(vehicle.getId() != null);
    }

    @Test
    @Transactional
    public void findTest() {

        Vehicle vehicle = vehicleRepository.findById(1);

        assert(vehicle != null);

        assert(vehicle.getVehicleInfo() != null);

        assert(vehicle.getVehicleInfo().getId() == 1);

        VehicleInfo vehicleInfo = vehicleInfoRepository.findById(1);

        assert(vehicleInfo != null);

        List<Vehicle> vehicleList = vehicleInfo.getVehicles();

        assert(vehicleList != null && vehicleList.size() == 1 );

    }


    @Test
    public void testQueryDsl() {

        QVehicle vehicle = QVehicle.vehicle;

        List<Vehicle> vehicles = Lists.newArrayList(vehicleRepository.findAll(vehicle.store.id.eq(1)));
        //List<Vehicle> vehicles = Lists.newArrayList(vehicleRepository.findByStore_Id(1));

        assert(vehicles.size() == 1);





    }
}

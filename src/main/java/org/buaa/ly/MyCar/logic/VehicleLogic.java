package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.Vehicle;

import java.util.List;
import java.util.Map;

public interface VehicleLogic {

    Vehicle find(int id);

    Vehicle find(String number);

    List<Vehicle> find(Integer viid, Integer sid, Integer status);

    List<Vehicle> findStatusNot(Integer viid, Integer sid, Integer status);

    Map<Integer, Map<Integer, Integer>> countByStoreAndVehicleInfoAndStatusNot(Integer viid, Integer sid, Integer status);

    Vehicle insert(int viid, Vehicle vehicle);

    Vehicle update(Vehicle vehicle);

    Vehicle delete(int id);

    Vehicle updateStatus(int id, int status);
}

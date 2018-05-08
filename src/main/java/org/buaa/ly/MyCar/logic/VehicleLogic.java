package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.Vehicle;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface VehicleLogic {

    Vehicle find(int id);

    Vehicle find(String number);

    long count(Integer sid, Integer viid, Collection<Integer> status, boolean exclude);

    List<Vehicle> find(Integer sid, Integer viid, Collection<Integer> status, boolean exclude);

    Vehicle insert(Vehicle vehicle);

    Vehicle update(Vehicle vehicle);

    Vehicle delete(int id);

    Vehicle updateStatus(int id, int status);
}

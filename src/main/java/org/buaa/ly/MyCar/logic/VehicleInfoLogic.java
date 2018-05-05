package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.VehicleInfo;

import java.util.List;
import java.util.Map;

public interface VehicleInfoLogic {

    VehicleInfo find(int id);

    List<VehicleInfo> find(List<Integer> status, boolean exclude);

    VehicleInfo insert(VehicleInfo vehicleInfo);

    VehicleInfo update(VehicleInfo vehicleInfo);

    VehicleInfo update(int id, int status);

    VehicleInfo delete(int id);

}

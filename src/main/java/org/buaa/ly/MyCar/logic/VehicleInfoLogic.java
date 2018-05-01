package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.VehicleInfo;

import java.util.List;
import java.util.Map;

public interface VehicleInfoLogic {

    VehicleInfo find(int id);

    List<VehicleInfo> findByStatusNot(Integer status);

    Map<Integer, VehicleInfo> findVehicleInfoMap(Integer viid);

    VehicleInfo insert(VehicleInfo vehicleInfo);

    VehicleInfo update(VehicleInfo vehicleInfo);

    VehicleInfo update(int id, int status);

    VehicleInfo delete(int id);

}

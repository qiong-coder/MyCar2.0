package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.internal.VehicleInfoCost;

public interface VehicleInfoCostLogic  {

    VehicleInfoCost find(int id);

    VehicleInfoCost insert(int id, VehicleInfoCost vehicleInfoCost);

    VehicleInfoCost update(int id, VehicleInfoCost vehicleInfoCost);

}

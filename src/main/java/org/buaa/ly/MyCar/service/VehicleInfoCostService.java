package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.request.CostInfoRequest;
import org.buaa.ly.MyCar.http.response.CostInfoResponse;
import org.buaa.ly.MyCar.http.response.CostInfoWithTimestamp;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;

import java.sql.Timestamp;


public interface VehicleInfoCostService {

    CostInfoResponse find(int id);

    CostInfoWithTimestamp find(int id, Timestamp begin, Timestamp end);

    VehicleInfoCost update(int id, CostInfoRequest costInfoRequest);

}

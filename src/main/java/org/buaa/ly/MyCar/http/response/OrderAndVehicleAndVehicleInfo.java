package org.buaa.ly.MyCar.http.response;

import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;

@Data
@Builder
public class OrderAndVehicleAndVehicleInfo {

    OrderDTO order;

    VehicleDTO vehicle;

    VehicleInfoDTO vehicleInfo;

}

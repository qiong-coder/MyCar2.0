package org.buaa.ly.MyCar.http.response;

import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class OrdersAndVehiclesAndVehicleInfos {

    List<OrderDTO> orders;

    Map<Integer, VehicleInfoDTO> vehicleInfos;

    Map<Integer, VehicleDTO> vehicles;

}

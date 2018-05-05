package org.buaa.ly.MyCar.http.dto;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Vehicle;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class VehicleDTO extends DTOBase {

    Integer id;

    Integer viid;

    String number;

    String description;

    Integer status;

    Timestamp begin;

    Timestamp end;

    Integer sid;

    public static VehicleDTO build(Vehicle vehicle) {
        return build(vehicle, VehicleDTO.class);
    }

    public Vehicle build() {
        return build(this, Vehicle.class);
    }

    public static <K> Map<K, VehicleDTO> build(Map<K,Vehicle> vehicleMap) {
        return build(vehicleMap, VehicleDTO.class);
    }

    public static List<VehicleDTO> build(List<Vehicle> vehicles) {
        return build(vehicles, VehicleDTO.class);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

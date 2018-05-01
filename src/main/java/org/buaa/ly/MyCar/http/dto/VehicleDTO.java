package org.buaa.ly.MyCar.http.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Vehicle;

import java.sql.Timestamp;

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
}

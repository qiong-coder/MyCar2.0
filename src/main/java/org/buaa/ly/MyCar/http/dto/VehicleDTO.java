package org.buaa.ly.MyCar.http.dto;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "begin")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    Timestamp beginTime;

    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "end")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    Timestamp endTime;

    Integer sid;

    @JsonProperty(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    Timestamp createTime;

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

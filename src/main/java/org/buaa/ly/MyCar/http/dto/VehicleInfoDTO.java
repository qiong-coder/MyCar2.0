package org.buaa.ly.MyCar.http.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.VehicleInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleInfoDTO extends DTOBase {

    Integer id;

    String name;

    String displacement;

    String gearbox;

    String boxes;

    String manned;

    String oil;

    Integer spare;

    String description;

    String type;

    String picture;

    VehicleInfoCostDTO cost;

    Integer status;

    public static VehicleInfoDTO build(VehicleInfo vehicleInfo) {
        return build(vehicleInfo, VehicleInfoDTO.class);
    }

    public static <K> Map<K, VehicleInfoDTO> build(Map<K, VehicleInfo> vehicleInfoMap) {
        return build(vehicleInfoMap, VehicleInfoDTO.class);
    }

    public static List<VehicleInfoDTO> build(Collection<VehicleInfo> vehicleInfoCollection) {
        return build(vehicleInfoCollection, VehicleInfoDTO.class);
    }

    public VehicleInfo build() {
        return build(this, VehicleInfo.class);
    }
}

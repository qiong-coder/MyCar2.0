package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;

import javax.servlet.http.Part;
import java.util.List;

public interface VehicleInfoService {

    VehicleInfoDTO find(int id);

    List<VehicleInfoDTO> find(List<Integer> status, boolean exclude);

    VehicleInfoDTO insert(VehicleInfoDTO vehicleInfoDTO, Part attachment);

    VehicleInfoDTO update(VehicleInfoDTO vehicleInfoDTO, Part attachment);

    VehicleInfoDTO delete(int id, boolean force);

}

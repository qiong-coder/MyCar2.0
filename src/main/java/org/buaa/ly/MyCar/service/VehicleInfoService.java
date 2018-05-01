package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;

import javax.servlet.http.Part;
import java.util.List;

public interface VehicleInfoService {

    List<VehicleInfoDTO> findByStatusNot(Integer status);

    VehicleInfoDTO insert(VehicleInfoDTO vehicleInfoDTO, Part attachment);

    VehicleInfoDTO update(int id, VehicleInfoDTO vehicleInfoDTO, Part attachment);

    VehicleInfoDTO find(int id);

    VehicleInfoDTO delete(int id, int force);

}

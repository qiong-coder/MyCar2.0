package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {

    VehicleDTO find(int id);

    List<VehicleDTO> findByStatusNot(int status);

    List<VehicleDTO> findByViidAndStatusNot(int viid, int status);

    List<VehicleDTO> findByViidAndSidAndStatus(int viid, int sid, int status);

    VehicleDTO insert(int viid, VehicleDTO vehicleDTO);

    VehicleDTO update(int id, VehicleDTO vehicleDTO);

    VehicleDTO delete(int id);

    VehicleDTO update(int id, int status);
}

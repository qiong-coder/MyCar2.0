package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.utils.StatusEnum;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface VehicleService {

    VehicleDTO find(int id);

    List<VehicleDTO> findBySidAndViidAndStatus(Integer sid, Integer viid, Collection<Integer> status, boolean exclude);

    List<VehicleDTO> findByViidAndSidAndTimestamp(int viid, int sid, Timestamp begin, Timestamp end);

    VehicleDTO insert(VehicleDTO vehicleDTO);

    VehicleDTO update(VehicleDTO vehicleDTO);

    VehicleDTO delete(int id);

    VehicleDTO update(int id, int status);
}

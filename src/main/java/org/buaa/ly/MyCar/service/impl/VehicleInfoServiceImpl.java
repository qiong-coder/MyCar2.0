package org.buaa.ly.MyCar.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.logic.VehicleInfoLogic;
import org.buaa.ly.MyCar.service.UploadService;
import org.buaa.ly.MyCar.service.VehicleInfoService;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Part;
import java.util.List;

@Component("vehicleInfoService")
@Slf4j
@Transactional
public class VehicleInfoServiceImpl implements VehicleInfoService {

    private VehicleInfoLogic vehicleInfoLogic;
    private UploadService uploadService;

    @Autowired
    void setVehicleInfoLogic(VehicleInfoLogic vehicleInfoLogic) {
        this.vehicleInfoLogic = vehicleInfoLogic;
    }

    @Autowired
    void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Override
    public VehicleInfoDTO insert(VehicleInfoDTO vehicleInfoDTO, Part attachment) {
        vehicleInfoDTO.setPicture(uploadService.save(attachment));

        VehicleInfo vehicleInfo = vehicleInfoDTO.build();
        if ( vehicleInfoLogic.insert(vehicleInfo) == null ) throw new BaseError();

        return VehicleInfoDTO.build(vehicleInfo);
    }

    @Override
    public VehicleInfoDTO update(int id, VehicleInfoDTO vehicleInfoDTO, Part attachment) {
        vehicleInfoDTO.setId(id);
        if ( attachment.getSize() != 0 ) vehicleInfoDTO.setPicture(uploadService.save(attachment));

        VehicleInfo vehicleInfo = vehicleInfoLogic.update(vehicleInfoDTO.build());

        if ( vehicleInfo == null ) throw new NotFoundError("failure to find vehicle info");
        else return VehicleInfoDTO.build(vehicleInfo);
    }

    @Override
    public VehicleInfoDTO find(int id) {
        VehicleInfo vehicleInfo = vehicleInfoLogic.find(id);
        if ( vehicleInfo == null ) throw new NotFoundError(String.format("vehicle info not found -%d", id));
        return VehicleInfoDTO.build(vehicleInfo, VehicleInfoDTO.class);
    }

    @Override
    public List<VehicleInfoDTO> findByStatusNot(Integer status) {
        return VehicleInfoDTO.build(vehicleInfoLogic.findByStatusNot(status), VehicleInfoDTO.class);
    }

    @Override
    public VehicleInfoDTO delete(int id, int force) {
        VehicleInfo vehicleInfo = force == 0?
                vehicleInfoLogic.update(id, StatusEnum.DELETE.getStatus()):
                vehicleInfoLogic.delete(id);
        if ( vehicleInfo == null ) throw new NotFoundError("failure to find vehicle info");
        else return VehicleInfoDTO.build(vehicleInfo, VehicleInfoDTO.class);
    }

}

package org.buaa.ly.MyCar.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.http.request.CostInfoRequest;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;
import org.buaa.ly.MyCar.logic.VehicleInfoCostLogic;
import org.buaa.ly.MyCar.logic.VehicleInfoLogic;
import org.buaa.ly.MyCar.service.UploadService;
import org.buaa.ly.MyCar.service.VehicleInfoService;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

    private VehicleInfoCostLogic vehicleInfoCostLogic;

    @Autowired
    void setVehicleInfoLogic(VehicleInfoLogic vehicleInfoLogic) {
        this.vehicleInfoLogic = vehicleInfoLogic;
    }

    @Autowired
    void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Autowired
    public void setVehicleInfoCostLogic(VehicleInfoCostLogic vehicleInfoCostLogic) {
        this.vehicleInfoCostLogic = vehicleInfoCostLogic;
    }

    @Override
    public VehicleInfoDTO insert(VehicleInfoDTO vehicleInfoDTO, Part attachment) {
        if ( attachment != null && attachment.getSize() != 0 ) vehicleInfoDTO.setPicture(uploadService.save(attachment));
        else vehicleInfoDTO.setPicture("test.jpg");

        if ( vehicleInfoDTO.getCost() == null ) {
            vehicleInfoDTO.setCost(vehicleInfoCostLogic.defaultCost());
        }

        VehicleInfo vehicleInfo = vehicleInfoDTO.build();
        if ( vehicleInfoLogic.insert(vehicleInfo) == null ) throw new BaseError();

        return VehicleInfoDTO.build(vehicleInfo);
    }

    @Override
    public VehicleInfoDTO update(VehicleInfoDTO vehicleInfoDTO, Part attachment) {
        if ( attachment != null && attachment.getSize() != 0 ) vehicleInfoDTO.setPicture(uploadService.save(attachment));

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
    public List<VehicleInfoDTO> find(List<Integer> status, boolean exclude) {
        return VehicleInfoDTO.build(vehicleInfoLogic.find(status, exclude));
    }

    @Override
    public VehicleInfoDTO delete(int id, boolean force) {
        VehicleInfo vehicleInfo = force? vehicleInfoLogic.delete(id): vehicleInfoLogic.update(id, StatusEnum.DELETE.getStatus());
        if ( vehicleInfo == null ) throw new NotFoundError("failure to find vehicle info");
        else return VehicleInfoDTO.build(vehicleInfo, VehicleInfoDTO.class);
    }

}

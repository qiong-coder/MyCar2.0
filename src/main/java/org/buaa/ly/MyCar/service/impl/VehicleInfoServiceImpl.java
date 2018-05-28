package org.buaa.ly.MyCar.service.impl;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.logic.*;
import org.buaa.ly.MyCar.logic.impl.AlgorithmLogic;
import org.buaa.ly.MyCar.service.UploadService;
import org.buaa.ly.MyCar.service.VehicleInfoService;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Part;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component("vehicleInfoService")
@Slf4j
@Transactional
public class VehicleInfoServiceImpl implements VehicleInfoService {

    private VehicleInfoLogic vehicleInfoLogic;

    private VehicleLogic vehicleLogic;

    private UploadService uploadService;

    private VehicleInfoCostLogic vehicleInfoCostLogic;

    private OrderLogic orderLogic;

    private StoreLogic storeLogic;

    @Autowired
    void setVehicleInfoLogic(VehicleInfoLogic vehicleInfoLogic) {
        this.vehicleInfoLogic = vehicleInfoLogic;
    }

    @Autowired
    void setVehicleLogic(VehicleLogic vehicleLogic) {
        this.vehicleLogic = vehicleLogic;
    }

    @Autowired
    void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Autowired
    void setVehicleInfoCostLogic(VehicleInfoCostLogic vehicleInfoCostLogic) {
        this.vehicleInfoCostLogic = vehicleInfoCostLogic;
    }

    @Autowired
    void setOrderLogic(OrderLogic orderLogic) {
        this.orderLogic = orderLogic;
    }

    @Autowired
    void setStoreLogic(StoreLogic storeLogic) { this.storeLogic = storeLogic; }

    @Override
    public VehicleInfoDTO insert(VehicleInfoDTO vehicleInfoDTO, Part attachment) {
        if ( attachment != null && attachment.getSize() != 0 ) vehicleInfoDTO.setPicture(uploadService.save(attachment));
        //else vehicleInfoDTO.setPicture("test.jpg");

        if ( vehicleInfoDTO.getCost() == null ) {
            vehicleInfoDTO.setCost(vehicleInfoCostLogic.defaultCost());
        } else {
            vehicleInfoDTO.getCost().build(false);
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
        List<VehicleInfoDTO> vehicleInfoDTOS = VehicleInfoDTO.build(vehicleInfoLogic.find(status, exclude));
        for ( VehicleInfoDTO vehicleInfoDTO : vehicleInfoDTOS ) {
            vehicleInfoDTO.setVehicleCount(vehicleLogic.count(null,vehicleInfoDTO.getId(),
                    Lists.newArrayList(StatusEnum.OK.getStatus(), StatusEnum.SPARE.getStatus(), StatusEnum.RENTING.getStatus()),false));
        }
        return vehicleInfoDTOS;
    }

    @Override
    public List<VehicleInfoDTO> find(String city, Integer sid, Timestamp begin, Timestamp end) {

        AlgorithmLogic algorithmLogic = new AlgorithmLogic(city, sid, null, begin, end, storeLogic, vehicleLogic, orderLogic);
        List<VehicleInfoDTO> vehicleInfoDTOS = Lists.newArrayList();

        if ( city != null || sid != null ) {

            Map<Integer, VehicleInfo> vehicleInfoMap = algorithmLogic.getVehicleInfoMap();

            for ( Map.Entry<Integer, VehicleInfo> vehicleInfoEntry : vehicleInfoMap.entrySet() ) {

                int viid = vehicleInfoEntry.getKey();
                VehicleInfoDTO vehicleInfoDTO = VehicleInfoDTO.build(vehicleInfoEntry.getValue());

                int stock = 0;
                int transform = 0;
                int needed = 0;

                if ( city != null ) {
                    Map<String, Map<Integer, List<Vehicle>>> stockVehicleMap = algorithmLogic.getStockCityMap();
                    if ( stockVehicleMap.containsKey(city) && stockVehicleMap.get(city).containsKey(viid) )
                        stock = stockVehicleMap.get(city).get(viid).size();
                } else {
                    Map<Integer, Map<Integer, List<Vehicle>>> stockVehicleMap = algorithmLogic.getStockMap();
                    if ( stockVehicleMap.containsKey(sid) && stockVehicleMap.get(sid).containsKey(viid) )
                        stock = stockVehicleMap.get(sid).get(viid).size();
                }

                if ( city != null ) {
                    Map<String, Map<Integer, Integer>> transformMap = algorithmLogic.getStockCityTransformMap();
                    if ( transformMap.containsKey(city) && transformMap.get(city).containsKey(viid) )
                        transform = transformMap.get(city).get(viid);
                } else {
                    Map<Integer, Map<Integer, Integer>> transformMap = algorithmLogic.getStockTransformMap();
                    if ( transformMap.containsKey(sid) && transformMap.get(sid).containsKey(viid) )
                        transform = transformMap.get(sid).get(viid);
                }

                if ( city != null ) {
                    Map<String, Map<Integer, List<Order>>> orderMap = algorithmLogic.getNeedCityOrderMap();
                    if ( orderMap.containsKey(city) && orderMap.get(city).containsKey(viid))
                        needed = orderMap.get(city).get(viid).size();
                } else {
                    Map<Integer, Map<Integer, List<Order>>> orderMap = algorithmLogic.getNeedOrderMap();
                    if ( orderMap.containsKey(sid) && orderMap.get(sid).containsKey(viid))
                        needed = orderMap.get(sid).get(viid).size();
                }

                if ( stock + transform + vehicleInfoDTO.getSpare() != 0 && needed <= stock + transform + vehicleInfoDTO.getSpare() )
                    vehicleInfoDTO.setCan_rent(true);
                else vehicleInfoDTO.setCan_rent(false);

                vehicleInfoDTOS.add(vehicleInfoDTO);
            }

        }

        return vehicleInfoDTOS;
    }


    @Override
    public VehicleInfoDTO delete(int id, boolean force) {
        VehicleInfo vehicleInfo = force? vehicleInfoLogic.delete(id): vehicleInfoLogic.update(id, StatusEnum.DELETE.getStatus());
        if ( vehicleInfo == null ) throw new NotFoundError("failure to find vehicle info");
        else return VehicleInfoDTO.build(vehicleInfo, VehicleInfoDTO.class);
    }

}

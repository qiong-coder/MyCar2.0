package org.buaa.ly.MyCar.logic.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.logic.VehicleInfoLogic;
import org.buaa.ly.MyCar.repository.VehicleInfoRepository;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component("vehicleInfoLogic")
@Slf4j
public class VehicleInfoLogicImpl implements VehicleInfoLogic {

    private VehicleInfoRepository vehicleInfoRepository;

    @Autowired
    void setVehicleInfoRepository(VehicleInfoRepository vehicleInfoRepository) {
        this.vehicleInfoRepository = vehicleInfoRepository;
    }

    @Override
    public VehicleInfo find(int id) {
        return vehicleInfoRepository.findById(id);
    }

    @Override
    public List<VehicleInfo> findByStatusNot(Integer status) {
        if ( status != null ) return vehicleInfoRepository.findByStatusNot(status);
        else return Lists.newArrayList(vehicleInfoRepository.findAll());
    }

    @Override
    public Map<Integer, VehicleInfo> findVehicleInfoMap(Integer viid) {
        List<VehicleInfo> vehicleInfos;
        Map<Integer, VehicleInfo> vehicleInfoMap = Maps.newHashMap();
        if ( viid != null ) {
            VehicleInfo vehicleInfo = vehicleInfoRepository.findById(viid.intValue());
            if ( vehicleInfo != null ) vehicleInfos = Lists.newArrayList(vehicleInfo);
            else return vehicleInfoMap;
        }
        else vehicleInfos = Lists.newArrayList(vehicleInfoRepository.findAll());

        for ( VehicleInfo vehicleInfo : vehicleInfos ) {
            if ( !vehicleInfoMap.containsKey(vehicleInfo.getId()) )
                vehicleInfoMap.put(vehicleInfo.getId(), vehicleInfo);
        }

        return vehicleInfoMap;
    }

    @Override
    public VehicleInfo insert(VehicleInfo vehicleInfo) {
        return vehicleInfoRepository.save(vehicleInfo);
    }

    @Modifying
    @Override
    public VehicleInfo update(VehicleInfo vehicleInfo) {
        VehicleInfo v = find(vehicleInfo.getId());
        if ( v == null ) return null;
        BeanCopyUtils.copyPropertiesIgnoreNull(vehicleInfo,v);
        return v;
    }

    @Modifying
    @Override
    public VehicleInfo update(int id, int status) {
        VehicleInfo v = find(id);
        if ( v == null ) return null;
        else v.setStatus(status);
        return v;
    }

    @Override
    public VehicleInfo delete(int id) {
        return vehicleInfoRepository.deleteById(id);
    }


}

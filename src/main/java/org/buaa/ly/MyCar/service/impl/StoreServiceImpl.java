package org.buaa.ly.MyCar.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.http.dto.StoreDTO;
import org.buaa.ly.MyCar.logic.StoreLogic;
import org.buaa.ly.MyCar.service.StoreService;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component("storeService")
@Slf4j
@Transactional
public class StoreServiceImpl implements StoreService {


    private StoreLogic storeLogic;

    @Autowired
    public void setStoreLogic(StoreLogic storeLogic) {
        this.storeLogic = storeLogic;
    }

    @Override
    public StoreDTO find(int id) {
        Store store = storeLogic.find(id);
        if ( store == null ) throw new NotFoundError("failure to find the store");
        else return StoreDTO.build(store);
    }

    @Override
    public List<StoreDTO> find() {
        List<Store> stores = storeLogic.findByStatus(StatusEnum.OK.getStatus());
        if ( stores == null ) throw new NotFoundError("failure to find the store");
        else return StoreDTO.build(stores);
    }

    @Override
    public StoreDTO insert(StoreDTO storeDTO) {
        return StoreDTO.build(storeLogic.insert(storeDTO.build()));
    }

    @Modifying
    @Override
    public StoreDTO update(int id, StoreDTO storeDTO) {

        Store store = storeLogic.find(id);

        if ( store == null ) throw new NotFoundError("failure to find the store");

        BeanCopyUtils.copyPropertiesIgnoreNull(storeDTO.build(), store);

        return StoreDTO.build(store);

    }

    @Override
    public StoreDTO delete(int id) {
        Store store = storeLogic.updateStatus(id,StatusEnum.DELETE.getStatus());
        if ( store == null ) throw new NotFoundError("failure to find the store");
        else return StoreDTO.build(storeLogic.updateStatus(id,StatusEnum.DELETE.getStatus()));
    }
}

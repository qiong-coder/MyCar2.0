package org.buaa.ly.MyCar.logic.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.logic.StoreLogic;
import org.buaa.ly.MyCar.repository.StoreRepository;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("storeLogic")
@Slf4j
@Transactional
public class StoreLogicImpl implements StoreLogic {


    private StoreRepository storeRepository;

    @Autowired
    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Store find(int id) {
        return storeRepository.findById(id);
    }

    @Override
    public List<Store> findByStatus(int status) {
        return Lists.newArrayList(storeRepository.findByStatus(status));
    }

    @Override
    public List<Store> findByCity(String city) {
        return Lists.newArrayList(storeRepository.findByCityAAndStatus(city, StatusEnum.OK.getStatus()));
    }

    @Override
    public Store insert(Store store) {
        return storeRepository.save(store);
    }

    @Modifying
    @Override
    public Store update(Store store) {

        return storeRepository.save(store);
    }

    @Modifying
    @Override
    public Store updateStatus(int id, int status) {
        Store store = find(id);
        if ( store == null ) return null;
        else store.setStatus(status);
        return store;
    }

    @Override
    public Store delete(int id) {
        return storeRepository.deleteById(id);
    }

    @Override
    public int count(Integer id) {
        return storeRepository.countById(id);
    }
}

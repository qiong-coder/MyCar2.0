package org.buaa.ly.MyCar.logic.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.logic.StoreLogic;
import org.buaa.ly.MyCar.repository.StoreRepository;
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
    public List<Store> find() {
        return Lists.newArrayList(storeRepository.findAll());
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

    @Override
    public Store delete(int id) {
        return storeRepository.deleteById(id);
    }

    @Override
    public int count(Integer id) {
        return storeRepository.countById(id);
    }
}

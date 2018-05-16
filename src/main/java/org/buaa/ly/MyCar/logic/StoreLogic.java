package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.Store;

import java.util.List;

public interface StoreLogic {

    Store find(int id);

    List<Store> findByStatus(int status);

    Store insert(Store store);

    Store update(Store store);

    Store updateStatus(int id, int status);

    Store delete(int id);

    int count(Integer id);
}

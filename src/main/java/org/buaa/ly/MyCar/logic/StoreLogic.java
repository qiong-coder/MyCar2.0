package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.Store;

import java.util.List;

public interface StoreLogic {

    Store find(int id);

    List<Store> find();

    Store insert(Store store);

    Store update(Store store);

    Store delete(int id);

    int count(Integer id);
}

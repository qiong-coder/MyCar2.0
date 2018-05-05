package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.StoreDTO;

import java.util.List;

public interface StoreService {

    StoreDTO find(int id);

    List<StoreDTO> find();

    StoreDTO insert(StoreDTO storeDTO);

    StoreDTO update(int id, StoreDTO storeDTO);

    StoreDTO delete(int id);

}

package org.buaa.ly.MyCar.repository;


import org.buaa.ly.MyCar.entity.Store;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StoreRepository extends PagingAndSortingRepository<Store, Integer>,
        JpaSpecificationExecutor<Store> {

    Store findById(int id);

    List<Store> findByStatus(int status);

    Store deleteById(int id);

    int countById(int id);

}

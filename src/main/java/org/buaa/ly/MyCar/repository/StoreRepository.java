package org.buaa.ly.MyCar.repository;


import org.buaa.ly.MyCar.entity.Store;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StoreRepository extends PagingAndSortingRepository<Store, Integer>,
        JpaSpecificationExecutor<Store> {

    Store findById(int id);

    Store deleteById(int id);

    int countById(int id);

}

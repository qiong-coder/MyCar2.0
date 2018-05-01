package org.buaa.ly.MyCar.repository;

import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VehicleInfoRepository extends PagingAndSortingRepository<VehicleInfo, Integer>,
        JpaSpecificationExecutor<VehicleInfo>,
        QuerydslPredicateExecutor<VehicleInfo> {

    VehicleInfo findById(int id);

    List<VehicleInfo> findByStatus(int status);

    List<VehicleInfo> findByStatusNot(int status);

    VehicleInfo deleteById(int id);
}

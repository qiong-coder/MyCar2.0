package org.buaa.ly.MyCar.repository;

import org.buaa.ly.MyCar.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VehicleRepository extends PagingAndSortingRepository<Vehicle, Integer>,
        JpaSpecificationExecutor<Vehicle>, QuerydslPredicateExecutor<Vehicle> {

    Vehicle findById(int id);

    Vehicle findByNumber(String number);

    Vehicle deleteById(int id);

}

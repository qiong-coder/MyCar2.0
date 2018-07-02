package org.buaa.ly.MyCar.repository;

import org.buaa.ly.MyCar.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer>,
        JpaSpecificationExecutor<Order>,
        QuerydslPredicateExecutor<Order> {

    Order findById(int id);

    Order findByOid(String oid);
}

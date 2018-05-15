package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface OrderLogic {

    Order find(int id);

    List<Order> find(Integer sid, Integer viid, Integer status);

    List<Order> find(String identity, String phone, List<Integer> status, boolean exclude);

//    void find(String identity, String phone, List<Integer> status, boolean exclude,
//              List<Order> orders,
//              Map<Integer, Vehicle> vehicleMap,
//              Map<Integer, VehicleInfo> vehicleInfoMap);
//
//    void find(Integer sid, Integer viid, Integer status,
//              List<Order> orders,
//              Map<Integer, Vehicle> vehicleMap,
//              Map<Integer, VehicleInfo> vehicleInfoMap);

    List<Order> findHistoryOrders(Integer viid, Integer vid, Timestamp begin, Timestamp end);

    List<Order> findRentingOrders(Integer sid, Integer viid, Timestamp begin, Timestamp end);

    List<Order> findPendingOrders(Integer sid, Integer viid, Timestamp begin, Timestamp end);

    void countByStatus(Integer status,
                       Map<Integer, Integer> viidStatusCount);

    Order insert(Order order);

    Order update(Order order);

    Order update(int id, int status);

    Order delete(int id);

}

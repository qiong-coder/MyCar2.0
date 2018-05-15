package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.http.response.*;
import org.buaa.ly.MyCar.http.response.OrderSchedule;

import java.sql.Timestamp;
import java.util.List;


public interface OrderService {

    Order find(int id);

    OrdersAndVehiclesAndVehicleInfos find(String identity, String phone, List<Integer> status, boolean exclude);

    OrdersAndVehiclesAndVehicleInfos findByStatusAndVehiclesAndVehicleInfos(Integer status);

    List<OrderCountByStatus> findByOrdersCountByStatus();

    OrderAndVehicleAndVehicleInfo findByIdAndVehicleAndVehicleInfo(int id);

    OrderDTO insert(int viid, OrderDTO orderDTO);

    OrderDTO update(int id, OrderDTO orderDTO);

    OrderDTO delete(int id, int force);

    OrderDTO check(int id);

    OrderDTO rent(int id, String number, OrderDTO orderDTO);

    OrderDTO drawback(int id, OrderDTO order);

    OrderDTO finished(int id, OrderDTO order);

    OrderDTO cancel(int id, OrderDTO order);

    OrderHistory history(Integer viid, String number, Timestamp begin, Timestamp end);

    OrderSchedule schedule(Integer sid, Integer viid, Timestamp begin, Timestamp end);

    OrderConflict conflict(Integer sid, Integer viid, Timestamp begin, Timestamp end);

}

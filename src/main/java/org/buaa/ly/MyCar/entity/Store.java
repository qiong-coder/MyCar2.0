package org.buaa.ly.MyCar.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.imageio.plugins.common.LZWCompressor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Data
@Entity
@Table(name = "Store")
@DynamicInsert
@ToString(exclude = {"vehicles", "rentOrders", "returnOrders", "realRentOrders", "realReturnOrders"})
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "address")
    String address;

    @Column(name = "phone")
    String phone;

    @Column(name = "city")
    String city;

    @Column(name = "status")
    Integer status;

    @Column(name = "create_time")
    Timestamp createTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    @JSONField(deserialize = false, serialize = false)
    List<Vehicle> vehicles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rentStore")
    @JSONField(serialize = false, deserialize = false)
    List<Order> rentOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "returnStore")
    @JSONField(serialize = false, deserialize = false)
    List<Order> returnOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "realRentStore")
    @JSONField(serialize = false, deserialize = false)
    List<Order> realRentOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "realReturnStore")
    @JSONField(serialize = false, deserialize = false)
    List<Order> realReturnOrders;
}

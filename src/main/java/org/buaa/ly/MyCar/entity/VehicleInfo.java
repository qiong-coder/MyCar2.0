package org.buaa.ly.MyCar.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.buaa.ly.MyCar.entity.deserilizer.VehicleInfoCostDeserializer;
import org.buaa.ly.MyCar.entity.serializer.VehicleInfoCostSerializer;
import org.buaa.ly.MyCar.entity.serializer.VehicleInfoSerializer;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;
import org.hibernate.annotations.DynamicInsert;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "VehicleInfo")
@DynamicInsert
@ToString(exclude = {"vehicles", "orders"})
public class VehicleInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "displacement")
    String displacement;

    @Column(name = "gearbox")
    String gearbox;

    @Column(name = "boxes")
    String boxes;

    @Column(name = "manned")
    String manned;

    @Column(name = "oil")
    String oil;

    @Column(name = "spare")
    Integer spare;

    @Column(name = "description")
    String description;

    @Column(name = "type")
    String type;

    @Column(name = "picture")
    String picture;

    @Column(name = "cost")
    @JSONField(serializeUsing = VehicleInfoCostSerializer.class, deserializeUsing = VehicleInfoCostDeserializer.class)
    String cost;

    @Column(name = "status")
    Integer status;

    @Column(name = "create_time")
    Timestamp createTime;

    @OneToMany(mappedBy = "vehicleInfo")
    @JSONField(deserialize = false, serialize = false)
    List<Vehicle> vehicles;

    @OneToMany(mappedBy = "vehicleInfo")
    @JSONField(deserialize = false, serialize = false)
    List<Order> orders;
}

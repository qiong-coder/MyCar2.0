package org.buaa.ly.MyCar.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.buaa.ly.MyCar.entity.deserilizer.StoreDeserializer;
import org.buaa.ly.MyCar.entity.deserilizer.VehicleInfoDeserializer;
import org.buaa.ly.MyCar.entity.serializer.StoreSerializer;
import org.buaa.ly.MyCar.entity.serializer.VehicleInfoSerializer;
import org.hibernate.annotations.DynamicInsert;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "Vehicle")
@DynamicInsert
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "viid", referencedColumnName = "id")
    @JSONField(serializeUsing = VehicleInfoSerializer.class, deserializeUsing = VehicleInfoDeserializer.class)
    VehicleInfo vehicleInfo;

    @OneToMany(mappedBy = "vehicle")
    @JSONField(deserialize = false, serialize = false)
    List<Order> orders;

    @Column(name = "number", nullable = false, unique = true)
    String number;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    Integer status;

    @Column(name = "begin")
    Timestamp beginTime;

    @Column(name = "end")
    Timestamp endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sid", referencedColumnName = "id")
    @JSONField(serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    Store store;

}

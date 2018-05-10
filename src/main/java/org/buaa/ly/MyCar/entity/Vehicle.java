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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viid", referencedColumnName = "id", insertable = false, updatable = false)
    @JSONField(serialize = false, deserialize = false)
    //@JSONField(serializeUsing = VehicleInfoSerializer.class, deserializeUsing = VehicleInfoDeserializer.class)
    VehicleInfo vehicleInfo;

    @Column(name = "viid")
    Integer viid;

    @OneToMany(mappedBy = "vehicle")
    @JSONField(deserialize = false, serialize = false)
    List<Order> orders;

    @Column(name = "number", nullable = false, unique = true)
    String number;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    @GeneratedValue
    Integer status;

    @Column(name = "begin")
    //@JSONField(name = "begin") //, format = "yyyy-MM-dd HH:mm:ss")
    Timestamp beginTime;

    @Column(name = "end")
    //@JSONField(name = "end") //, format = "yyyy-MM-dd HH:mm:ss")
    Timestamp endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sid", referencedColumnName = "id", insertable = false, updatable = false)
    @JSONField(serialize = false, deserialize = false)
    //@JSONField(serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    Store store;

    @Column(name = "sid")
    Integer sid;
}

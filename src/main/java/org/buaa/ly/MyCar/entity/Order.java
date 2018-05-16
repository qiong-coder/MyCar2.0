package org.buaa.ly.MyCar.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.buaa.ly.MyCar.entity.deserilizer.*;
import org.buaa.ly.MyCar.entity.serializer.*;
import org.hibernate.annotations.DynamicInsert;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;



@Data
@Entity
@Table(name = "MyCarOrder")
@DynamicInsert
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "oid", nullable = false)
    private String oid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viid", nullable = false, insertable = false, updatable = false)
    //@JSONField(name = "viid", serializeUsing = VehicleInfoSerializer.class, deserializeUsing = VehicleInfoDeserializer.class)
    @JSONField(serialize = false, deserialize = false)
    private VehicleInfo vehicleInfo;

    @Column(name = "viid", nullable = false)
    private Integer viid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vid", insertable = false, updatable = false)
    //@JSONField(name = "vid", serializeUsing = VehicleSerializer.class, deserializeUsing = VehicleDeserializer.class)
    @JSONField(serialize = false, deserialize = false)
    private Vehicle vehicle;

    @Column(name = "vid")
    private Integer vid;

    @Column(name = "begin")
    //@JSONField(name = "begin") //, format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp beginTime;

    @Column(name = "end")
    //@JSONField(name = "end") //, format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_sid", nullable = false, insertable = false, updatable = false)
    //@JSONField(name = "rent_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    @JSONField(serialize = false, deserialize = false)
    private Store rentStore;

    @Column(name = "rent_sid")
    private Integer rentSid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_sid", nullable = false, insertable = false, updatable = false)
    //@JSONField(name = "return_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    @JSONField(serialize = false, deserialize = false)
    private Store returnStore;

    @Column(name = "return_sid")
    private Integer returnSid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "drivers", nullable = false)
    private String drivers;

    @Column(name = "identity", nullable = false)
    private String identity;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "bill")
    private String bill;

    @Column(name = "insurance")
    @JSONField(serializeUsing = String2ListIntegerSerializer.class, deserializeUsing = ListInteger2StringDeserializer.class)
    private String insurance;

    @Column(name = "pre_cost")
    @JSONField(serializeUsing = PreCostSerializer.class, deserializeUsing = PreCostDeseriliazer.class)
    private String preCost;

    @Column(name = "pay_info")
    @JSONField(serializeUsing = CostItemSerializer.class, deserializeUsing = CostItemDeserializer.class)
    private String payInfo;

    @Column(name = "rbegin")
    //@JSONField(name = "rbegin") //, format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp realBeginTime;

    @Column(name = "rend")
    //@JSONField(name = "rend") //, format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp realEndTime;

    @ManyToOne()
    @JoinColumn(name = "rrent_sid", insertable = false, updatable = false)
    //@JSONField(name = "rrent_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    @JSONField(serialize = false, deserialize = false)
    private Store realRentStore;

    @Column(name = "rrent_sid")
    private Integer realRentSid;

    @ManyToOne
    @JoinColumn(name = "rreturn_sid", insertable = false, updatable = false)
    @JSONField(serialize = false, deserialize = false)
    //@JSONField(name = "rreturn_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    private Store realReturnStore;

    @Column(name = "rreturn_sid")
    private Integer realReturnSid;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "cost_info")
    @JSONField(serializeUsing = CostItemSerializer.class, deserializeUsing = CostItemDeserializer.class)
    private String costInfo;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Timestamp createTime;
}

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

    @ManyToOne
    @JoinColumn(name = "viid", nullable = false)
    @JSONField(name = "viid", serializeUsing = VehicleInfoSerializer.class, deserializeUsing = VehicleInfoDeserializer.class)
    private VehicleInfo vehicleInfo;

    @ManyToOne
    @JoinColumn(name = "vid")
    @JSONField(name = "vid", serializeUsing = VehicleSerializer.class, deserializeUsing = VehicleDeserializer.class)
    private Vehicle vehicle;

    @Column(name = "begin")
    @JSONField(name = "begin")
    private Timestamp beginTime;

    @Column(name = "end")
    @JSONField(name = "end")
    private Timestamp endTime;

    @ManyToOne
    @JoinColumn(name = "rent_sid", nullable = false)
    @JSONField(name = "rent_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    private Store rentStore;

    @ManyToOne
    @JoinColumn(name = "return_sid", nullable = false)
    @JSONField(name = "return_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    private Store returnStore;

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
    @JSONField(name = "insurance", serializeUsing = String2ListIntegerSerializer.class, deserializeUsing = ListInteger2StringDeserializer.class)
    private String insurance;

    @Column(name = "pre_cost")
    @JSONField(name = "pre_cost", serializeUsing = PreCostSerializer.class, deserializeUsing = PreCostDeseriliazer.class)
    private String preCost;

    @Column(name = "pay_info")
    @JSONField(name = "pay_info", serializeUsing = CostItemSerializer.class, deserializeUsing = CostItemDeserializer.class)
    private String payInfo;

    @Column(name = "rbegin")
    @JSONField(name = "rbegin")
    private Timestamp realBeginTime;

    @Column(name = "rend")
    @JSONField(name = "rend")
    private Timestamp realEndTime;

    @ManyToOne()
    @JoinColumn(name = "rrent_sid")
    @JSONField(name = "rrent_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    private Store realRentStore;

    @ManyToOne
    @JoinColumn(name = "rreturn_sid")
    @JSONField(name = "rreturn_sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    private Store realReturnStore;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "cost_info")
    @JSONField(name = "cost_info", serializeUsing = CostItemSerializer.class, deserializeUsing = CostItemDeserializer.class)
    private String costInfo;

    @Column(name = "status")
    private Integer status;

}

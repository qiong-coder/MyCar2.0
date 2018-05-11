package org.buaa.ly.MyCar.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.buaa.ly.MyCar.entity.deserilizer.StoreDeserializer;
import org.buaa.ly.MyCar.entity.serializer.StoreSerializer;
import org.hibernate.annotations.DynamicInsert;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "Account")
@DynamicInsert
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sid", insertable = false, updatable = false)
    //@JSONField(name = "sid", serializeUsing = StoreSerializer.class, deserializeUsing = StoreDeserializer.class)
    @JSONField(serialize = false, deserialize = false)
    private Store store;

    @Column(name = "sid")
    private Integer sid;

    @Column(name = "role")
    private Integer role;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Timestamp createTime;
}

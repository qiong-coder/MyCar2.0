package org.buaa.ly.MyCar.http.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.internal.CostItem;
import org.buaa.ly.MyCar.internal.PreCost;
import org.buaa.ly.MyCar.utils.OrderUtils;

import java.sql.Timestamp;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO extends DTOBase {

    private Integer id;

    private String oid;

    private Integer viid;

    private Integer vid;

    //@JSONField(name = "begin") //, format = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "begin")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp beginTime;

    //@JSONField(name = "end") //, format = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "end")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp endTime;

    //@JSONField(name = "rent_sid")
    @JsonProperty(value = "rent_sid")
    private Integer rentSid;

    //@JSONField(name = "return_sid")
    @JsonProperty(value = "return_sid")
    private Integer returnSid;

    private String name;

    private String drivers;

    private String identity;

    private String phone;

    private String bill;

    //@JSONField(name = "insurance")
    private List<Integer> insurance;

    @JsonProperty(value = "pre_cost")
    private PreCost preCost;

    @JsonProperty(value = "pay_info")
    private List<CostItem> payInfo;

    @JsonProperty(value = "rbegin") //, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp realBeginTime;


    @JsonProperty(value = "rend") //, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp realEndTime;

    @JsonProperty(value = "rrent_sid")
    private Integer realRentSid;

    @JsonProperty(value = "rreturn_sid")
    private Integer realReturnSid;

    private Integer distance;

    @JsonProperty(value = "cost_info")
    private List<CostItem> costInfo;

    private Integer status;

    @JsonProperty(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp createTime;

    public Order build() {
        oid = OrderUtils.oid(returnSid, viid, identity);
        return build(this, Order.class);
    }

    public static OrderDTO build(Order order) {
        return build(order, OrderDTO.class);
    }

    public static List<OrderDTO> build(List<Order> orders) { return build(orders, OrderDTO.class); }
}

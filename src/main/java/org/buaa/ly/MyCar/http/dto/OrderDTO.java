package org.buaa.ly.MyCar.http.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.internal.CostItem;
import org.buaa.ly.MyCar.internal.PreCost;

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

    @JSONField(name = "begin")
    private Timestamp beginTime;

    @JSONField(name = "end")
    private Timestamp endTime;

    @JSONField(name = "rent_sid")
    private Integer rentStore;

    @JSONField(name = "return_sid")
    private Integer returnStore;

    private String name;

    private String drivers;

    private String identity;

    private String phone;

    private String bill;

    @JSONField(name = "insurance")
    private List<Integer> insurance;

    @JSONField(name = "pre_cost")
    private PreCost preCost;

    @JSONField(name = "pay_info")
    private List<CostItem> payInfo;

    @JSONField(name = "rbegin")
    private Timestamp realBeginTime;


    @JSONField(name = "rend")
    private Timestamp realEndTime;

    @JSONField(name = "rrent_sid")
    private Integer realRentStore;

    @JSONField(name = "rreturn_sid")
    private Integer realReturnStore;

    private Integer distance;

    @JSONField(name = "cost_info")
    private List<CostItem> costInfo;

    private Integer status;

    public Order build() {
        return build(this, Order.class);
    }

    public static OrderDTO build(Order order) {
        return build(order, OrderDTO.class);
    }

}

package org.buaa.ly.MyCar.internal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostItem {

    public CostItem(String reason, Integer value, String operator) {
        this.reason = reason;
        this.value = value.toString();
        this.operator = operator;
    }

    String reason;

    String value;

    String operator;

}

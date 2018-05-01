package org.buaa.ly.MyCar.internal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostItem {


    String reason;

    int value;

    String operator;


}

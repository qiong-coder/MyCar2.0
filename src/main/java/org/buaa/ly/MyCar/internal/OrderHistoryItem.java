package org.buaa.ly.MyCar.internal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderHistoryItem {

    String name;

    String number;

    String oid;

    int ret_day;

}

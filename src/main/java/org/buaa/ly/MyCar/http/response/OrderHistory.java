package org.buaa.ly.MyCar.http.response;

import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.internal.OrderHistoryItem;

import java.util.List;

@Data
@Builder
public class OrderHistory {

    int ret_day_total;

    int idle_day;

    List<OrderHistoryItem> history;

}

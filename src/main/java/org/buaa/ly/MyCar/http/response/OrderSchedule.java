package org.buaa.ly.MyCar.http.response;


import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.internal.ScheduleItem;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class OrderSchedule {

    // 门店 - 车型名称 - 用车需求
    Map<Integer, Map<String, List<ScheduleItem>>> schedule;

}

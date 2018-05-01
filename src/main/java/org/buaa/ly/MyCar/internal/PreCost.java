package org.buaa.ly.MyCar.internal;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.utils.TimeUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;


@Data
@Builder
public class PreCost {

    List<Integer> insurance;
    List<CostItem> day_costs;
    List<CostItem> discounts;
    Integer total_cost;



    public static PreCost build(VehicleInfoCost vehicleInfoCost,
                                Timestamp begin,
                                Timestamp end,
                                List<Integer> insurance) {
        Calendar bCalendar = Calendar.getInstance();
        Calendar eCalendar = Calendar.getInstance();
        bCalendar.setTime(begin);
        eCalendar.setTime(end);

        int bHour = bCalendar.get(Calendar.HOUR);
        int eHour = bCalendar.get(Calendar.HOUR);

        int total_cost = 0;

        List<CostItem> day_costs = Lists.newArrayList();
        List<CostItem> discounts = Lists.newArrayList();

        int day_insurance = 0;
        for ( Integer value : insurance ) day_insurance += value;

        while (eHour - bHour >= 6) {
            int day_cost = vehicleInfoCost.day_cost(bCalendar);
            int discount = vehicleInfoCost.discount(bCalendar);
            day_costs.add(CostItem.builder().reason(TimeUtils.getDateFormat(bCalendar)).value(day_cost).build());
            discounts.add(CostItem.builder().reason(TimeUtils.getDateFormat(bCalendar)).value(discount).build());
            total_cost += day_cost * discount / 100 + day_insurance;
            bCalendar.add(Calendar.DATE, 1);
        }

        if (eHour - bHour >= 5) {
            total_cost += 40000;
            day_costs.add(CostItem.builder().reason("超过5小时").value(40000).build());
        } else if (eHour - bHour >= 4) {
            total_cost += 30000;
            day_costs.add(CostItem.builder().reason("超过4小时").value(30000).build());
        } else if (eHour - bHour >= 3) {
            total_cost += 20000;
            day_costs.add(CostItem.builder().reason("超过3小时").value(20000).build());
        } else if (eHour - bHour >= 2) {
            total_cost += 10000;
            day_costs.add(CostItem.builder().reason("超过2小时").value(10000).build());
        }

        return PreCost.builder()
                .day_costs(day_costs)
                .insurance(insurance)
                .discounts(discounts)
                .total_cost(total_cost)
                .build();
    }
}

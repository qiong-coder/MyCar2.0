package org.buaa.ly.MyCar.internal;


import com.google.common.collect.Lists;
import lombok.Data;
import org.buaa.ly.MyCar.utils.TimeUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;


@Data
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

        if ( insurance != null ) {
            for ( int i = 0; i < insurance.size(); ++ i )
                if ( insurance.get(i) > 0 ) {
                    day_insurance += vehicleInfoCost.getInsurance().get(i);
                }
        }

        do {
            int day_cost = vehicleInfoCost.day_cost(bCalendar);
            int discount = vehicleInfoCost.discount(bCalendar);
            day_costs.add(new CostItem(TimeUtils.getDateFormat(bCalendar.getTime()),day_cost,null));
            discounts.add(new CostItem(TimeUtils.getDateFormat(bCalendar.getTime()),discount,null));
            total_cost += day_cost * discount / 100 + day_insurance;
            bCalendar.add(Calendar.DATE, 1);
        } while (eHour - bHour >= 6);

        if (eHour - bHour >= 5) {
            total_cost += 40000;
            day_costs.add(new CostItem("超过5小时",40000, null));
        } else if (eHour - bHour >= 4) {
            total_cost += 30000;
            day_costs.add(new CostItem("超过4小时",30000,null));
        } else if (eHour - bHour >= 3) {
            total_cost += 20000;
            day_costs.add(new CostItem("超过3小时",20000,null));
        } else if (eHour - bHour >= 2) {
            total_cost += 10000;
            day_costs.add(new CostItem("超过2小时", 10000, null));
        }

        PreCost preCost = new PreCost();
        preCost.setDay_costs(day_costs);
        preCost.setInsurance(vehicleInfoCost.getInsurance());
        preCost.setDiscounts(discounts);
        preCost.setTotal_cost(total_cost);
        return preCost;
    }
}

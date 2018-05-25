package org.buaa.ly.MyCar.http.response;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;
import org.buaa.ly.MyCar.utils.TimeUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CostInfoWithTimestamp {

    List<Integer> insurances;

    Map<String, Integer> overtime;

    Map<String, Integer> day_costs;

    Map<String, Integer> discounts;

    Integer total_cost;

    public static CostInfoWithTimestamp build(VehicleInfoCost vehicleInfoCost,
                                              Timestamp begin,
                                              Timestamp end) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begin);

        float bhour = TimeUtils.hour(begin);
        float ehour = TimeUtils.hour(end);

        int total_cost = 0;
        Map<String, Integer> day_costs = Maps.newHashMap();
        Map<String, Integer> discounts = Maps.newHashMap();
        Map<String, Integer> overtime = Maps.newHashMap();

        do {
            int day_cost = vehicleInfoCost.day_cost(calendar);
            int discount = vehicleInfoCost.discount(calendar);
            day_costs.put(TimeUtils.getDateFormat(calendar.getTime()), day_cost);
            discounts.put(TimeUtils.getDateFormat(calendar.getTime()), discount);
            total_cost += day_cost * discount / 10000 * 100;
            calendar.add(Calendar.DATE, 1);
            bhour += 24.0;
        } while ( ehour - bhour > 4.0 );

//        if ( ehour - bhour >= 5 ) {
//            total_cost += 40000;
//            overtime.put("5", 40000);
//        } else if ( ehour - bhour >= 4 ) {
//            total_cost += 30000;
//            overtime.put("4", 30000);
//        } else if ( ehour - bhour >= 3 ) {
//            total_cost += 20000;
//            overtime.put("3", 20000);
//        } else if ( ehour - bhour >= 2 ) {
//            total_cost += 10000;
//            overtime.put("2", 10000);
//        }

        if ( ehour - bhour > 0.5 ) {
            int cost = vehicleInfoCost.day_cost(calendar) * vehicleInfoCost.discount(calendar) / 2 / 10000 * 100 ;
            total_cost += cost;
            overtime.put(String.format("%.1f",ehour-bhour), cost);
        }

        return CostInfoWithTimestamp.builder()
                .insurances(vehicleInfoCost.getInsurance())
                .day_costs(day_costs)
                .discounts(discounts)
                .overtime(overtime)
                .total_cost(total_cost)
                .build();
    }
}

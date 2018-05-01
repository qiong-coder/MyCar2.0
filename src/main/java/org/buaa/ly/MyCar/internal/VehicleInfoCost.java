package org.buaa.ly.MyCar.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleInfoCost {

    List<Integer> insurance;

    List<List<Integer>> discounts;

    List<List<Integer>> day_costs;


    public VehicleInfoCost(VehicleInfoCost vehicleInfoCost) {
        this.day_costs = vehicleInfoCost.getDay_costs();
        this.discounts = vehicleInfoCost.getDiscounts();
        this.insurance = vehicleInfoCost.getInsurance();
    }

    public int discount(Calendar calendar) {
        return discounts.get(calendar.get(Calendar.MONTH)).get(calendar.get(Calendar.DAY_OF_MONTH)-1);
    }

    public int day_cost(Calendar calendar) {
        return day_costs.get(calendar.get(Calendar.MONTH)).get(calendar.get(Calendar.DAY_OF_MONTH)-1);
    }

}

package org.buaa.ly.MyCar.http.response;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;


import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CostInfoResponse extends VehicleInfoCost {

    List<List<Integer>> final_day_costs;

    public CostInfoResponse(VehicleInfoCost vehicleInfoCost) {
        super(vehicleInfoCost);

        final_day_costs = Lists.newArrayListWithCapacity(vehicleInfoCost.getDay_costs().size());

        for ( int i = 0; i < vehicleInfoCost.getDay_costs().size(); ++ i ) {

            List<Integer> month_day_costs = vehicleInfoCost.getDay_costs().get(i);
            List<Integer> month_discounts = vehicleInfoCost.getDiscounts().get(i);

            List<Integer> final_month_day_cost = Lists.newArrayListWithCapacity(month_day_costs.size());

            for ( int j = 0; j < month_day_costs.size(); ++ j ) {
                final_month_day_cost.add(month_day_costs.get(j)*month_discounts.get(j)/100);
            }

            final_day_costs.add(final_month_day_cost);
        }

    }

}

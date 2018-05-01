package org.buaa.ly.MyCar.http.request;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
public class CostInfoRequest extends VehicleInfoCost {

    Integer day_cost;

    Integer discount;

    private static List<List<Integer>> buildList(int value) {
        int month = 12;
        int days = 31;

        List<List<Integer>> lists = Lists.newArrayListWithCapacity(month);

        for ( int i = 0; i < month; ++ i ) {
            List<Integer> month_values = Lists.newArrayListWithCapacity(days);
            for ( int j = 0; j < days; ++ j ) {
                month_values.add(value);
            }
            lists.add(month_values);
        }
        return lists;
    }

    public static CostInfoRequest build(int day_cost, int discount) {
        CostInfoRequest costInfoRequest = new CostInfoRequest();
        costInfoRequest.setDiscount(discount);
        costInfoRequest.setDay_cost(day_cost);
        return costInfoRequest.build();
    }


    public CostInfoRequest build() {
        if ( day_cost != null ) {
            setDay_costs(buildList(day_cost));
        }

        if ( discount != null ) {
            setDiscounts(buildList(discount));
        }

        return this;
    }

}

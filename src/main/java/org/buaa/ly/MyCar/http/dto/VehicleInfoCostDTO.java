package org.buaa.ly.MyCar.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.internal.VehicleInfoCost;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleInfoCostDTO extends VehicleInfoCost {

    Integer day_cost;

    Integer discount;

    List<List<Integer>> final_day_costs;

    public VehicleInfoCostDTO() {}

    public VehicleInfoCostDTO(VehicleInfoCost vehicleInfoCost) {
        super(vehicleInfoCost);
    }

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

    public static VehicleInfoCostDTO build(int day_cost, int discount, List<Integer> insurance) {
        VehicleInfoCostDTO vehicleInfoCostDTO = new VehicleInfoCostDTO();
        vehicleInfoCostDTO.setDiscount(discount);
        vehicleInfoCostDTO.setDay_cost(day_cost);
        if ( insurance != null ) vehicleInfoCostDTO.setInsurance(insurance);
        return vehicleInfoCostDTO.build(false);
    }

    public void buildFinalDayCosts() {

        final_day_costs = Lists.newArrayListWithCapacity(getDay_costs().size());

        for ( int i = 0; i < getDay_costs().size(); ++ i ) {

            List<Integer> month_day_costs = getDay_costs().get(i);
            List<Integer> month_discounts = getDiscounts().get(i);

            List<Integer> final_month_day_cost = Lists.newArrayListWithCapacity(month_day_costs.size());

            for ( int j = 0; j < month_day_costs.size(); ++ j ) {
                final_month_day_cost.add(month_day_costs.get(j)*month_discounts.get(j)/100);
            }

            final_day_costs.add(final_month_day_cost);
        }
    }

    public VehicleInfoCostDTO build(boolean final_day_costs) {

        if ( day_cost != null ) {
            setDay_costs(buildList(day_cost));
        }

        if ( discount != null ) {
            setDiscounts(buildList(discount));
        }

        if ( final_day_costs ) {
            buildFinalDayCosts();
        }

        return this;
    }

}

package org.buaa.ly.MyCar.http.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.buaa.ly.MyCar.utils.OrderUtils;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class OrderRequest extends OrderDTO {

    public Order build(VehicleInfo vehicleInfo) {

        Order order = new Order();

        BeanCopyUtils.copyPropertiesIgnoreNull(this, order);

        if ( vehicleInfo!=null ) order.setVehicleInfo(vehicleInfo);

        order.setOid(OrderUtils.oid(order.getRentStore().getId(), vehicleInfo.getId(), order.getIdentity()));

        return order;
    }

}

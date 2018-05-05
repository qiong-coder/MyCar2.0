package org.buaa.ly.MyCar.internal;


import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class Stock {

    // sid - viid - status - 车辆列表
    Map<Integer, Map<Integer, Map<Integer, List<VehicleDTO>>>> vehicles = Maps.newHashMap();

    int count(int sid, int viid, int status) {
        if ( !vehicles.containsKey(sid) ) return 0;

        if ( !vehicles.get(sid).containsKey(viid) ) return 0;

        if ( !vehicles.get(sid).get(viid).containsKey(status) ) return 0;

        return vehicles.get(sid).get(viid).get(status).size();
    }

}

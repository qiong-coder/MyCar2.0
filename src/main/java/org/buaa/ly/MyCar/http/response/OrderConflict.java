package org.buaa.ly.MyCar.http.response;


import lombok.Builder;
import lombok.Data;
import org.buaa.ly.MyCar.http.dto.OrderDTO;

import java.util.List;

@Data
@Builder
public class OrderConflict {

    int total;

    int used;

    int to_used;

    int sparse;

    List<OrderDTO> orders;

}

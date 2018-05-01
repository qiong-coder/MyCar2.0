package org.buaa.ly.MyCar.http.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OrderCountByStatus {

    int status;

    int count;

}

package org.buaa.ly.MyCar.http.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayUrlResponse {

    private String name;

    private String total_fee;

    private String code;

}

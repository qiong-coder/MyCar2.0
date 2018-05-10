package org.buaa.ly.MyCar.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostItem {

    String reason;

    int value;

    String operator;

}

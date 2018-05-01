package org.buaa.ly.MyCar.internal;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ScheduleItem {

    Timestamp begin;

    Timestamp end;

    int count;

    int stock;

}

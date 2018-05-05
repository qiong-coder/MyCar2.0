package org.buaa.ly.MyCar.logic.impl;

import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.internal.Stock;
import org.buaa.ly.MyCar.logic.AlgorithmLogic;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;


@Component("algorithmLogic")
@Slf4j
public class AlgorithmLogicImpl implements AlgorithmLogic {

    private List<Vehicle> vehicleList;

    @Override
    public Stock simulate(Integer viid, Integer sid, Timestamp end) {

        return null;
    }
}

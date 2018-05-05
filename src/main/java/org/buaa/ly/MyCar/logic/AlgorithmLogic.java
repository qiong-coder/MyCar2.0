package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.internal.Stock;

import java.sql.Timestamp;

public interface AlgorithmLogic {

    Stock simulate(Integer viid, Integer sid, Timestamp end);

}

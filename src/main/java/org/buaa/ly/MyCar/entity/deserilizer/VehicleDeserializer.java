package org.buaa.ly.MyCar.entity.deserilizer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.buaa.ly.MyCar.entity.Vehicle;

import java.lang.reflect.Type;


public class VehicleDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        int id = defaultJSONParser.parseObject(int.class);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        return (T)vehicle;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}

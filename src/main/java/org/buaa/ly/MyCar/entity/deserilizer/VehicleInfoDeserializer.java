package org.buaa.ly.MyCar.entity.deserilizer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.buaa.ly.MyCar.entity.VehicleInfo;

import java.lang.reflect.Type;

public class VehicleInfoDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        int id = defaultJSONParser.parseObject(int.class);
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setId(id);
        return (T)vehicleInfo;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}

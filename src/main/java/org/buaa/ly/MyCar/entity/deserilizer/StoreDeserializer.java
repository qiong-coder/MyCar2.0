package org.buaa.ly.MyCar.entity.deserilizer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.buaa.ly.MyCar.entity.Store;

import java.lang.reflect.Type;

public class StoreDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        int id = defaultJSONParser.parseObject(int.class);
        Store store = new Store();
        store.setId(id);
        return (T)store;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}

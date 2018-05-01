package org.buaa.ly.MyCar.entity.deserilizer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;


public class ListInteger2StringDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        return (T)JSON.parseArray((String)o,Integer.class);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}

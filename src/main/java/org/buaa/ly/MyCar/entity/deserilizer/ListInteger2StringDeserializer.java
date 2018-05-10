package org.buaa.ly.MyCar.entity.deserilizer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.List;


public class ListInteger2StringDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        List<Integer> insurance = defaultJSONParser.parseArray(Integer.class);
        return (T) JSON.toJSONString(insurance);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}

package org.buaa.ly.MyCar.entity.deserilizer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.buaa.ly.MyCar.internal.PreCost;

import java.lang.reflect.Type;

public class PreCostDeseriliazer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        return (T)defaultJSONParser.parseObject(PreCost.class);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}

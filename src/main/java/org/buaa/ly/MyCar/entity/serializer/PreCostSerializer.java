package org.buaa.ly.MyCar.entity.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.buaa.ly.MyCar.internal.PreCost;

import java.io.IOException;
import java.lang.reflect.Type;

public class PreCostSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type type, int i) throws IOException {
        jsonSerializer.write(JSON.parseObject((String)object, PreCost.class));
    }

}

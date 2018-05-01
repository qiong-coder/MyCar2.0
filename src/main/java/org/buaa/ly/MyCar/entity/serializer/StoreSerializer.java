package org.buaa.ly.MyCar.entity.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.buaa.ly.MyCar.entity.Store;

import java.io.IOException;
import java.lang.reflect.Type;

public class StoreSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type type, int features) throws IOException {
        jsonSerializer.write(((Store)object).getId());
    }

}

package org.buaa.ly.MyCar.entity.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.buaa.ly.MyCar.entity.VehicleInfo;

import java.io.IOException;
import java.lang.reflect.Type;

public class VehicleInfoSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type type, int i) throws IOException {
        jsonSerializer.write(((VehicleInfo)object).getId());
    }
}

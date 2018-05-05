package org.buaa.ly.MyCar.http.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DTOBase {

    public static<T, S> T build(S s, Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(s), clazz);
    }

    public static <T, S> List<T> build(Collection<S> s, Class<T> clazz) {
        List<T> tList = Lists.newArrayListWithCapacity(s.size());
        for (S i : s) {
            tList.add(build(i, clazz));
        }
        return tList;
    }

    public static <K, T, S> Map<K,T> build(Map<K,S> map,  Class<T> clazz) {
        Map<K, T> ktMap = Maps.newHashMap();
        for ( Map.Entry<K,S> entry : map.entrySet() ) {
            ktMap.put(entry.getKey(), build(entry.getValue(), clazz));
        }
        return ktMap;
    }

}

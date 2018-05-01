package org.buaa.ly.MyCar.http.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;

import java.util.List;

public class DTOBase {

    public static<T, S> T build(S s, Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(s), clazz);
    }

    public static <T, S> List<T> build(List<S> s, Class<T> clazz) {
        List<T> tList = Lists.newArrayListWithCapacity(s.size());
        for (S i : s) {
            tList.add(build(i, clazz));
        }
        return tList;
    }

}

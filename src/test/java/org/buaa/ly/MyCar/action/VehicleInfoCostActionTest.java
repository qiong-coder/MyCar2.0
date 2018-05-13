package org.buaa.ly.MyCar.action;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.exception.advice.DefaultAdvice;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.http.dto.VehicleInfoCostDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class VehicleInfoCostActionTest extends TestLoader {


    private MockMvc mockMvc;

    @Autowired
    VehicleInfoCostAction vehicleInfoCostAction;

    @Autowired
    DefaultAdvice defaultAdvice;

    @PostConstruct
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleInfoCostAction)
                .setControllerAdvice(defaultAdvice)
                .build();
    }


    @Test
    public void findTest() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/vehicle/info/cost/{id}/", 1)
                .header("token","test");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,0,1);
        Timestamp begin = new Timestamp(calendar.getTimeInMillis());
        calendar.set(2018,0,3);
        Timestamp end = new Timestamp(calendar.getTimeInMillis());

        request = MockMvcRequestBuilders.get("/vehicle/info/cost/{id}/{begin}/{end}/", 1, begin, end);
        mockMvc.perform(request)
                .andDo(print())
                .andReturn();
    }

    @Test
    public void updateTest() throws Exception {
        VehicleInfoCostDTO vehicleInfoCostDTO = VehicleInfoCostDTO.build(1,2,Lists.newArrayList(1,2,3));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/vehicle/info/cost/{id}/", 1)
                .header("token","test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(vehicleInfoCostDTO));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn();

    }


}

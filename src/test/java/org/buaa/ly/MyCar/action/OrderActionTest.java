package org.buaa.ly.MyCar.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.exception.advice.DefaultAdvice;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.internal.CostItem;
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
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OrderActionTest extends TestLoader {

    @Autowired OrderAction orderAction;

    private MockMvc mockMvc;

    @Autowired
    DefaultAdvice defaultAdvice;

    @PostConstruct
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderAction)
                .setControllerAdvice(defaultAdvice)
                .build();
    }


    @Test
    public void findOrder() throws Exception {
        insertTest();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/orders/{status}/",0)
                .header("token", "test");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn();
    }

    @Test
    public void insertTest() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBeginTime(new Timestamp(0));
        orderDTO.setEndTime(new Timestamp(10000));
        orderDTO.setRentSid(1);
        orderDTO.setReturnSid(1);
        orderDTO.setName("test");
        orderDTO.setDrivers("test-drivers");
        orderDTO.setIdentity("test-identity");
        orderDTO.setPhone("188");
        orderDTO.setBill("test-bill");
        orderDTO.setInsurance(Lists.newArrayList(100,200,300));

        List<CostItem> costItemList = Lists.newArrayList(new CostItem("test",1,null));

        orderDTO.setCostInfo(costItemList);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/order/{viid}/",1)
                .header("token", "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(orderDTO));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn();
    }

}

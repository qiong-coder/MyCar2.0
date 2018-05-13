package org.buaa.ly.MyCar.action;

import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.exception.advice.DefaultAdvice;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class VehicleActionTest extends TestLoader {

    private MockMvc mockMvc;

    @Autowired VehicleAction vehicleAction;

    @Autowired
    DefaultAdvice defaultAdvice;

    @PostConstruct
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleAction)
                .setControllerAdvice(defaultAdvice)
                .build();
    }

    @Test
    public void findTest() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/vehicle/{id}/", 1)
                .header("token", "test");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andReturn();

        request = MockMvcRequestBuilders.get("/vehicle")
                .param("status", "0,10")
                .header("token", "test");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andReturn();

        request = MockMvcRequestBuilders.get("/vehicle/{id}/", 10)
                .header("token", "test");
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.NOT_FOUND.getStatus()))
                .andReturn();

    }

    @Test
    public void insertTest() throws Exception {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setViid(1);
        vehicleDTO.setNumber("123456");
        vehicleDTO.setDescription("test-description");
        vehicleDTO.setSid(2);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/vehicle/")
                .header("token", "test")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
                .content(vehicleDTO.toString());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(3));


        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/{id}/",3))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(0));

    }

}

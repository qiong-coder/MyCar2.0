package org.buaa.ly.MyCar.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.exception.advice.DefaultAdvice;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.http.dto.VehicleInfoCostDTO;
import org.buaa.ly.MyCar.mock.MyMockPart;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class VehicleInfoActionTest extends TestLoader {

    private MockMvc mockMvc;

    @Autowired VehicleInfoAction vehicleInfoAction;

    @Autowired DefaultAdvice defaultAdvice;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleInfoAction)
                .setControllerAdvice(defaultAdvice)
                .build();
    }

    @Test
    public void findTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/vehicle/info/{id}/", 1)
                .header("token", "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn();
    }

    @Test
    public void findByStatus() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/vehicle/info")
                .param("status","0,1")
                .header("token", "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andReturn();
    }


    @Test
    public void insertTest() throws Exception {
        VehicleInfoDTO vehicleInfoDTO = new VehicleInfoDTO();
        vehicleInfoDTO.setName("test-update");
        vehicleInfoDTO.setDisplacement("test-displacement");
        vehicleInfoDTO.setGearbox("test-gearbox");
        vehicleInfoDTO.setBoxes("test-boxes");
        vehicleInfoDTO.setManned("test-manned");
        vehicleInfoDTO.setOil("test-oil");
        vehicleInfoDTO.setSpare(4);
        vehicleInfoDTO.setDescription("test-description");
        vehicleInfoDTO.setType("test-type");

        VehicleInfoCostDTO vehicleInfoCostDTO = VehicleInfoCostDTO.build(100,100, Lists.newArrayList(10,10,10));

        vehicleInfoDTO.setCost(vehicleInfoCostDTO);

        MockPart attachment = new MyMockPart("attachment", "test.jpg", "test".getBytes());
        MockMultipartFile vehicleInfoDTO1 = new MockMultipartFile("vehicleInfoDTO", "vehicleInfoDTO.json", MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE,JSON.toJSONString(vehicleInfoDTO).getBytes());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.fileUpload("/vehicle/info/")
                .part(attachment)
                .file(vehicleInfoDTO1)
                .header("token", "test");

        String response = mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn().getResponse().getContentAsString();



        request = MockMvcRequestBuilders.get("/vehicle/info/{id}/", JSONObject.parseObject(response).getJSONObject("data").getInteger("id"))
                .header("token","test");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("test-update"))
                .andReturn();
    }

//    @Test
//    public void insertTestWithoutAttachment() throws Exception {
//        VehicleInfoDTO vehicleInfoDTO = new VehicleInfoDTO();
//        vehicleInfoDTO.setName("test-update");
//        vehicleInfoDTO.setDisplacement("test-displacement");
//        vehicleInfoDTO.setGearbox("test-gearbox");
//        vehicleInfoDTO.setBoxes("test-boxes");
//        vehicleInfoDTO.setManned("test-manned");
//        vehicleInfoDTO.setOil("test-oil");
//        vehicleInfoDTO.setSpare(4);
//        vehicleInfoDTO.setDescription("test-description");
//        vehicleInfoDTO.setType("test-type");
//
//        //VehicleInfoCostDTO costInfoRequest = VehicleInfoCostDTO.build(100,100, Lists.newArrayList(10,10,10));
//
//        //vehicleInfoDTO.setCost(costInfoRequest);
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/vehicle/info/")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(JSON.toJSONString(vehicleInfoDTO))
//                .header("token", "test");
//
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
//                .andReturn();
//    }

//    @Test
//    public void updateTest() throws Exception {
//        VehicleInfoDTO vehicleInfoDTO = new VehicleInfoDTO();
//        vehicleInfoDTO.setId(1);
//        vehicleInfoDTO.setName("test-update");
//
//        MockMultipartFile body = new MockMultipartFile("vehicleInfoDTO", "vehicleInfoDTO.json", MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE,JSON.toJSONString(vehicleInfoDTO).getBytes());
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/vehicle/info/")
//                .file(body)
//                .header("token", "test");
//
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
//                .andReturn();
//    }

    @Test
    public void deleteTest() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/vehicle/info/{id}/",1)
                .header("token", "test");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn();

        request = MockMvcRequestBuilders.get("/vehicle/info/{id}/", 1)
                .header("token", "test");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusEnum.DELETE.getStatus()))
                .andReturn();
    }
}

package org.buaa.ly.MyCar.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.exception.advice.DefaultAdvice;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.transform.Result;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


public class AccountActionTest extends TestLoader {

    private MockMvc mockMvc;

    @Autowired AccountAction accountAction;

    @Autowired DefaultAdvice defaultAdvice;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountAction)
                .setControllerAdvice(defaultAdvice)
                .build();
    }

    @Test
    public void testLogin() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("admin");
        accountDTO.setPassword("password");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/account/login/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(accountDTO));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andReturn();
    }

    @Test
    public void testNotFound() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("not-found");
        accountDTO.setPassword("not-found");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/account/login/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(accountDTO));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.NOT_FOUND.getStatus()))
                .andReturn();
    }

    @Test
    public void testLoginError() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("admin");
        accountDTO.setPassword("password-error");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/account/login/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(accountDTO));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.ACCOUNT_LOGIN_ERROR.getStatus()))
                .andReturn();
    }

    @Test
    public void testInsert() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("new-insert");
        accountDTO.setPassword("new-password");
        accountDTO.setPhone("18810346541");
        accountDTO.setName("test-name");
        accountDTO.setSid(2);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/account/register/")
                .header("token", "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(accountDTO));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andReturn();

    }


    @Test
    public void testUpdate() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setPassword("new-password");
        accountDTO.setName("test-name");
        accountDTO.setPhone("123456");
        accountDTO.setSid(2);
        accountDTO.setRole(2);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/account/{username}/", "admin")
                .header("token", "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(accountDTO));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("test-name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value("123456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.sid").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(2))
                .andReturn();

        request = MockMvcRequestBuilders.get("/store/{id}/", "2")
                .header("token", "test");
        mockMvc.perform(request)
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testFind() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/account/{username}/", "admin")
                .header("token","test");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResponseStatusMsg.SUCCESS.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("admin"))
                .andReturn();

    }
}

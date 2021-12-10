package com.javamaster.springsecurityjwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityJwtApplicationTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/game"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void contextLoads() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void objectsLoads() throws Exception {
        this.mockMvc.perform(get("/object"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void authenticationLoads() throws Exception {
        this.mockMvc.perform(get("/auth"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void requestsLoads() throws Exception {
        this.mockMvc.perform(get("/games"))
                .andDo(print())
                .andExpect(status().isOk());
    }






}


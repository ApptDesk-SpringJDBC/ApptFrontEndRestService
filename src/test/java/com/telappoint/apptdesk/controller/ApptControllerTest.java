package com.telappoint.apptdesk.controller;

import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Koti on 7/28/2016.
 */


@SuppressWarnings("ALL")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class)
public class ApptControllerTest {

    private MockMvc mockMvc;
    private MockHttpSession httpSession;
    public static final String CLIENT_CODE = "MSCHELPS";

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = {"com.telappoint.apptdesk", "com.telappoint.apptdesk.controller",
            "com.telappoint.apptdesk.common.components", "com.telappoint.apptdesk.common.masterdb.dao",
    "com.telappoint.apptdesk.common.masterdb.dao.impl", "org.springframework.jdbc.core",
    "com.telappoint.apptdesk.common",
    "com.telappoint.apptdesk.common.clientdb",
    "com.telappoint.apptdesk.common.clientdb.dao",
    "com.telappoint.apptdesk.common.clientdb.dao.impl",
    "com.telappoint.apptdesk.service",
    "com.telappoint.apptdesk.service.impl"
    })
    static class ContextConfiguration {
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    public ApptControllerTest() {
    }

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
        this.httpSession = new MockHttpSession();
    }

    @org.junit.Test
    public void testShouldHoldFirstAvailableAppointment() throws Exception {

        String json = "{\n" +
                "    \"companyId\": \"\",\n" +
                "    \"resourceId\": \"\",\n" +
                "    \"clientCode\": \"MSCHELPS\",\n" +
                "    \"transId\": \"25\",\n" +
                "    \"locationId\": \"2\",\n" +
                "    \"langCode\": \"us-en\",\n" +
                "    \"departmentId\": \"\",\n" +
                "    \"customerId\": \"34\",\n" +
                "    \"serviceId\": \"1\",\n" +
                "    \"device\": \"online\",\n" +
                "    \"procedureId\": \"12\"\n" +
                "}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/holdFirstAvailableAppointment").session(httpSession).content(json);

        testAssertion(request);


    }

    public void testAssertion(MockHttpServletRequestBuilder request) throws Exception {
        MvcResult result = mockMvc.perform(request).andReturn();
        Assert.assertSame(null, result.getResponse().getErrorMessage());
        Assert.assertEquals(200, result.getResponse().getStatus());
        assertFalse(null == result.getModelAndView().getModel());
    }

}
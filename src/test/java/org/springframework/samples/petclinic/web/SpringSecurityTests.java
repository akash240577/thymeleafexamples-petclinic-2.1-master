package org.springframework.samples.petclinic.web;

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.samples.context.SecurityRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.samples.context.SecurityRequestPostProcessors.userDeatilsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Basic example that includes Spring Security configuration.
 * <p/>
 * <p>Note that currently there are no {@link ResultMatcher}' built specifically
 * for asserting the Spring Security context. However, it's quite easy to put
 * them together as shown below and Spring Security extensions will become
 * available in the near future.
 * <p/>
 * <p>This also demonstrates a custom {@link RequestPostProcessor} which authenticates
 * a user to a particular {@link HttpServletRequest}.
 * <p/>
 *
 * @author Rob Winch
 * @author Rossen Stoyanchev
 *         // * @see SecurityRequestPostProcessors
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        {"classpath:spring/security-config.xml",
                "classpath:spring/business-config.xml",
                "classpath:spring/mvc-core-config.xml"})
@ActiveProfiles("jpa")
public class SpringSecurityTests {

    private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilters(this.springSecurityFilterChain).build();
    }

    @Test
    public void requiresAuthentication() throws Exception {
        ResultActions actions = mockMvc.perform(get("/"));
        actions.andDo(print());
        actions.andExpect(redirectedUrl("http://localhost/login"));
    }

    /*@Test
    public void accessGranted() throws Exception {
        this.mockMvc.perform(get("/").with(userDeatilsService("user")))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/layouts/standardLayout.jsp"));
    }

    @Test
    public void accessDenied() throws Exception {
        this.mockMvc.perform(get("/").with(user("user").roles("DENIED")))
                .andExpect(status().isForbidden());
    }*/

    @Test
    public void userAuthenticates() throws Exception {
        final String username = "admin";
        final String password = "admin";
        ResultActions actions = mockMvc.perform(
                post("/j_spring_security_check")
                        .param("j_username", username).param("j_password", password));
        actions.andDo(print());
        actions.andExpect(status().isMovedTemporarily());

        actions.andExpect(new ResultMatcher() {
            public void match(MvcResult mvcResult) throws Exception {
                HttpSession session = mvcResult.getRequest().getSession();
                SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
                Assert.assertEquals(securityContext.getAuthentication().getName(), username);
            }
        });
        actions.andExpect(redirectedUrl("/"));
    }

    @Test
    public void userAuthenticateFails() throws Exception {
        final String username = "user";
        mockMvc.perform(post("/j_spring_security_check").param("j_username", username).param("j_password", "invalid"))
                .andExpect(redirectedUrl("/spring_security_login?login_error"))
                .andExpect(new ResultMatcher() {
                    public void match(MvcResult mvcResult) throws Exception {
                        HttpSession session = mvcResult.getRequest().getSession();
                        SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
                        Assert.assertNull(securityContext);
                    }
                });
    }

    @Test
    public void logout() throws Exception{
        userAuthenticates();
        ResultActions actions = mockMvc.perform(get("/logout"));
        actions.andDo(print());
        actions.andExpect(redirectedUrl("/login?logged-out"));
    }

}
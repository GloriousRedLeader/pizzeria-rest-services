/*
 * Scary disclaimer notice - If this software causes significant harm to
 * you pc, mac, laptop, smart phone, dumb phone, tablet, tv, washing machine
 * or dishwasher - which it will - do not blame the author of this software.
 * He spent minutes researching best practices (read poor practices) and copying
 * and pasting lots of dangerous code from the online internet. Hence, this
 * application should not be compiled, executed, distributed, downloaded,
 * sold, purchased, or otherwise used without expressed written consent from
 * the National Football League.
 *
 * Under no circumstances should this program be run as-is, or even as-it-was
 * or will-be. There is no hope for it. You have been warned. Twice now really.
 * I can name one hundred other ways you can spend your time other than running
 * this application. Adopt a dog, walk the dog, feed the dog, pet the dog. I
 * guess what I'm trying to say is that the ITunes EULA actually forbids users
 * from building nuclear weapons if you can believe it:
 *
 * "You also agree that you will not use these products for any purposes
 * prohibited by United States law, including, without limitation, the development,
 * design, manufacture or production of nuclear, missiles, or chemical or
 * biological weapons.”
 */
package com.arturospizzeria.controller;

import com.arturospizzeria.model.AuthRequest;
import com.arturospizzeria.model.CustomerDetails;
import com.arturospizzeria.repo.CustomerDetailsRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CustomerDetailsRepo customerDetailsRepo;

    private MockMvc mvc;

    @BeforeAll
    public void setup() {
        CustomerDetails customerDetails = new CustomerDetails(CustomerDetails.Role.ROLE_ADMIN, "george@parros.com", "muffinman", "$2a$10$RWLH5wguY1akzNxu3f0MiuRjIKGlevOX/aqShVYyFy/vhmFT7ebim", "address");
        customerDetailsRepo.save(customerDetails);

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldAuthenticate() throws Exception {
        AuthRequest request = new AuthRequest("muffinman", "cookie");
        String body = objectMapper.writeValueAsString(request);

        mvc.perform(post("/auth/token")
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldNotAuthenticate() throws Exception {
        AuthRequest request = new AuthRequest("muffinman", "INVALID PASSWORD");
        String body = objectMapper.writeValueAsString(request);

        mvc.perform(post("/auth/token")
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(body))
                .andExpect(status().isUnauthorized());

    }
}
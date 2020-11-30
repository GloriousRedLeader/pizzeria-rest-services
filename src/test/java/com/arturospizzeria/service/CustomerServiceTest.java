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
package com.arturospizzeria.service;

import com.arturospizzeria.model.CustomerDetails;
import com.arturospizzeria.repo.CustomerDetailsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

    @TestConfiguration
    static class CustomerServiceTestContextConfiguration {

        @Bean
        public CustomerService customerService() {
            return new CustomerService();
        }

        @Bean
        public PasswordEncoder encoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CustomerDetailsRepo customerDetailsRepo;

    @Test
    public void shouldAddItemSuccesfully() {
        CustomerDetails customerDetails = new CustomerDetails(CustomerDetails.Role.ROLE_ADMIN, "churro@burro.com",
                "arturotheburro", "cookie", "1600 Pennsylvania Avenue NW · Washington, D.C");
        when(customerDetailsRepo.save(any(CustomerDetails.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThat(customerService.addCustomer(customerDetails).getUsername()).isEqualTo(customerDetails.getUsername());
    }

    @Test
    public void testShouldEncryptPassword() {
        CustomerDetails inputCustomerDetails = new CustomerDetails(CustomerDetails.Role.ROLE_ADMIN, "churro@burro.com",
                "arturotheburro", "cookie", "1600 Pennsylvania Avenue NW · Washington, D.C");
        when(customerDetailsRepo.save(any(CustomerDetails.class))).thenAnswer(i -> i.getArguments()[0]);

        CustomerDetails outputCustomerDetails = customerService.addCustomer(inputCustomerDetails);
        assertThat(outputCustomerDetails).matches(i -> passwordEncoder.matches("cookie", i.getPassword()));
    }
}

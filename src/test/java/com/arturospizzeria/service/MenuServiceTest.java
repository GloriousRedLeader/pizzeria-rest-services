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

import com.arturospizzeria.model.MenuItem;
import com.arturospizzeria.repo.MenuItemRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
public class MenuServiceTest {

    @TestConfiguration
    static class MenuServiceTestContextConfiguration {

        @Bean
        public MenuService menuService() {
            return new MenuService();
        }
    }

    @Autowired
    private MenuService menuService;

    @MockBean
    private MenuItemRepo menuItemRepo;

    @Test
    public void recordAlreadyExistsShouldNotCreateNewRecord() {
        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setAvailable(true);
        item.setCategory(MenuItem.Category.PIZZA_CHEESE);
        item.setPrice(1.88);
        item.setName("Mozzarella");
        item.setDescription("Mozzarella is a traditionally southern Italian cheese made from Italian buffalo's milk by the pasta filata method.");

        when(menuItemRepo.findById(any(Long.class))).thenAnswer(i -> Optional.of(item));

        assertThrows(IllegalArgumentException.class, () -> {
            menuService.addItem(item);
        });
    }

    @Test
    public void shouldAddItemSuccesfully() {
        MenuItem item = new MenuItem();
        item.setAvailable(true);
        item.setCategory(MenuItem.Category.PIZZA_CHEESE);
        item.setPrice(1.88);
        item.setName("Mozzarella");
        item.setDescription("Mozzarella is a traditionally southern Italian cheese made from Italian buffalo's milk by the pasta filata method.");

        when(menuItemRepo.save(any(MenuItem.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThat(menuService.addItem(item).getName()).isEqualTo(item.getName());
    }

    @Test
    public void shouldUpdateItem() {
        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setAvailable(true);
        item.setCategory(MenuItem.Category.PIZZA_CHEESE);
        item.setPrice(1.88);
        item.setName("Mozzarella");
        item.setDescription("Mozzarella is a traditionally southern Italian cheese made from Italian buffalo's milk by the pasta filata method.");

        when(menuItemRepo.findById(any(Long.class))).thenAnswer(i -> Optional.of(item));
        when(menuItemRepo.save(any(MenuItem.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThat(menuService.updateItem(1L, item).getName()).isEqualTo(item.getName());
    }
}
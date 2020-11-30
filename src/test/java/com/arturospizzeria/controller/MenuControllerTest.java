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

import com.arturospizzeria.model.MenuItem;
import com.arturospizzeria.repo.MenuItemRepo;
import com.arturospizzeria.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MenuController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.arturospizzeria.config.*")}
)
class MenuControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuItemRepo menuItemRepo;

    @TestConfiguration
    static class MenuControllerTestContextConfiguration {

        @Bean
        MenuService menuService() {
            return new MenuService();
        }

        @MockBean
        private MenuItemRepo menuItemRepo;
    }

    @Test
    void whenInputIsInvalidThenReturnsStatus400() throws Exception {
        MenuItem item = new MenuItem();
        item.setDescription("description");
        String body = objectMapper.writeValueAsString(item);
        mvc.perform(post("/menu/item")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRecordAlreadyExistsReturnStatus400() throws Exception {
        when(menuItemRepo.save(any(MenuItem.class))).thenThrow(new DataIntegrityViolationException("This record already exists"));

        MenuItem item = new MenuItem(1.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        String body = objectMapper.writeValueAsString(item);

        mvc.perform(post("/menu/item")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenInputIsValidThenReturnsStatus201() throws Exception {
        MenuItem item = new MenuItem(1.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        String body = objectMapper.writeValueAsString(item);
        mvc.perform(post("/menu/item")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateExistingRecord() throws Exception {
        MenuItem itemInDb = new MenuItem(1.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        itemInDb.setId(1L);

        when(menuItemRepo.findById(any(Long.class))).thenAnswer(i -> Optional.of(itemInDb));
        when(menuItemRepo.save(any(MenuItem.class))).thenAnswer(i -> i.getArguments()[0]);

        MenuItem itemToUpdate = new MenuItem(8.99, "extra large pizza", false, MenuItem.Category.PIZZA_SIZE, "X-Large Pizza, Yum");

        String body = objectMapper.writeValueAsString(itemToUpdate);
        mvc.perform(put("/menu/item/1")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(8.99))
                .andExpect(jsonPath("$.name").value("extra large pizza"))
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.category").value(MenuItem.Category.PIZZA_SIZE.toString()))
                .andExpect(jsonPath("$.description").value("X-Large Pizza, Yum"));
    }

    @Test
    void shouldReturnEntityById() throws Exception {
        MenuItem item = new MenuItem(1.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        item.setId(1L);

        when(menuItemRepo.findById(any(Long.class))).thenReturn(Optional.of(item));

        mvc.perform(get("/menu/item/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(1.99))
                .andExpect(jsonPath("$.name").value("extra large pizza"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.category").value(MenuItem.Category.PIZZA_SIZE.toString()))
                .andExpect(jsonPath("$.description").value("X-Large"));
    }

    @Test
    void shouldReturn404NotFoundForUnknownID() throws Exception {
        when(menuItemRepo.findById(any(Long.class))).thenReturn(Optional.empty());
        mvc.perform(get("/menu/item/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEntitiesByCategory() throws Exception {
        MenuItem item = new MenuItem(11.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        item.setId(1L);

        MenuItem item2 = new MenuItem(5.99, "medium pizza", true, MenuItem.Category.PIZZA_SIZE, "Medium");
        item.setId(2L);

        when(menuItemRepo.findByCategory(any(MenuItem.Category.class))).thenReturn(Arrays.asList(item, item2));

        mvc.perform(get("/menu/category/PIZZA_SIZE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldReturn400BadRequestUnrecognizedCategory() throws Exception {
        when(menuItemRepo.findByCategory(any(MenuItem.Category.class))).thenReturn(Arrays.asList());
        mvc.perform(get("/menu/category/SMURF"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAllEntities() throws Exception {
        MenuItem item = new MenuItem(11.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        item.setId(1L);

        MenuItem item2 = new MenuItem(0.99, "jalapeno", true, MenuItem.Category.PIZZA_TOPPING, "Spicy topping");
        item.setId(2L);

        MenuItem item3 = new MenuItem(1.99, "franziskaner", true, MenuItem.Category.BEVERAGE, "Adult Beverage");
        item.setId(3L);

        when(menuItemRepo.findAll()).thenReturn(Arrays.asList(item, item2, item3));

        mvc.perform(get("/menu/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
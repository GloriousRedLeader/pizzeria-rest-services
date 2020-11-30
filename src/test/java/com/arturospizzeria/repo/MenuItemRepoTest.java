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
package com.arturospizzeria.repo;

import com.arturospizzeria.model.MenuItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class MenuItemRepoTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MenuItemRepo menuItemRepo;

    @Test
    public void shouldFindRecordsByCategory() {
        MenuItem item1 = new MenuItem(10.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        MenuItem item2 = new MenuItem(9.99, "large pizza", true, MenuItem.Category.PIZZA_SIZE, "Large");
        MenuItem item3 = new MenuItem(8.99, "medium pizza", true, MenuItem.Category.PIZZA_SIZE, "Medium");
        MenuItem item4 = new MenuItem(0.00, "jalapenos", true, MenuItem.Category.PIZZA_TOPPING, "Spicy toppiing");
        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(item3);
        entityManager.persist(item4);
        entityManager.flush();

        List<MenuItem> found = menuItemRepo.findByCategory(MenuItem.Category.PIZZA_SIZE);

        assertThat(found.size()).isEqualTo(3);
    }

    @Test
    public void shouldNotInsertRecordWithConstraintViolations() {
        assertThrows(ConstraintViolationException.class, () -> {
            menuItemRepo.save(new MenuItem(null, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large"));
        });
    }

    @Test
    public void shouldNotInsertDuplicateName() {
        MenuItem item = new MenuItem(1.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large");
        entityManager.persist(item);
        entityManager.flush();

        assertThrows(DataIntegrityViolationException.class, () -> {
            menuItemRepo.save(new MenuItem(1.99, "extra large pizza", true, MenuItem.Category.PIZZA_SIZE, "X-Large"));
        });
    }
}

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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Manages business logic related to MenuItems. These are straight forward
 * so not a lot needs to be done here.
 * <p>
 * One important feature is the caching of the response objects for
 * important and heavily used methods.
 */
@Service
@Log4j2
public class MenuService {

    @Autowired
    private MenuItemRepo menuItemRepo;

    @Caching(evict = {
            @CacheEvict(value = "MENU_ITEMS", key = "'ALL_MENU_ITEMS'"),
            @CacheEvict(value = "MENU_CATEGORY_ITEMS", allEntries = true),
            @CacheEvict(value = "MENU_ITEM", key = "'MENU_ITEM: ' + #id")
    })
    public void removeItem(final Long id) {
        log.info("Deleting item with id {}", id);
        menuItemRepo.deleteById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "MENU_ITEMS", key = "'ALL_MENU_ITEMS'"),
            @CacheEvict(value = "MENU_CATEGORY_ITEMS", allEntries = true)
    })
    @CachePut(value = "MENU_ITEM", key = "'MENU_ITEM: ' + #item.id")
    public MenuItem updateItem(final Long id, final MenuItem item) {
        log.info("Updating item {}", item);
        final MenuItem itemInDb = menuItemRepo.findById(id).orElse(null);
        if (itemInDb != null) {
            itemInDb.setName(item.getName());
            itemInDb.setPrice(item.getPrice());
            itemInDb.setDescription(item.getDescription());
            itemInDb.setCategory(item.getCategory());
            itemInDb.setAvailable(item.getAvailable());
            return menuItemRepo.save(itemInDb);
        }
        throw new IllegalArgumentException("Could not find record to update");
    }

    @Caching(evict = {
            @CacheEvict(value = "MENU_ITEMS", key = "'ALL_MENU_ITEMS'"),
            @CacheEvict(value = "MENU_CATEGORY_ITEMS", allEntries = true)
    })
    @CachePut(value = "MENU_ITEM", key = "'MENU_ITEM: ' + #item.id")
    public MenuItem addItem(final MenuItem item) {
        log.info("Creating item {}", item);
        if (item.getId() != null) {
            throw new IllegalArgumentException("ID not expected. Record may already exist.");
        }
        return menuItemRepo.save(item);
    }

    @Cacheable(value = "MENU_ITEM", key = "'MENU_ITEM: ' + #id")
    public MenuItem getItem(final Long id) {
        log.info("Getting item with id {}", id);
        final Optional<MenuItem> item = menuItemRepo.findById(id);
        if (item.isPresent())
            return item.get();
        return null;
    }

    @Cacheable(value = "MENU_ITEMS", key = "'ALL_MENU_ITEMS'")
    public List<MenuItem> getAllItems() {
        log.info("Getting all items");
        return menuItemRepo.findAll();
    }

    @Cacheable(value = "MENU_CATEGORY_ITEMS", key = "#category")
    public List<MenuItem> getAllItemsInCategory(final MenuItem.Category category) {
        log.info("Getting all items in category {}", category);
        return menuItemRepo.findByCategory(category);
    }
}
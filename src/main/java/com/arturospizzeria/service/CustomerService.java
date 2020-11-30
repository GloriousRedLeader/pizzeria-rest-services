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
import com.arturospizzeria.model.UserDetailsImpl;
import com.arturospizzeria.repo.CustomerDetailsRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Customer related business logic. For example one security flaw
 * currently in place is that users can set their role to ROLE_ADMIN
 * when they call this endpoint. So obviously a lot of care and testing
 * needs to be done here.
 */
@Service
@Log4j2
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerDetailsRepo customerDetailsRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final CustomerDetails user = customerDetailsRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return new UserDetailsImpl(user);
    }

    @CacheEvict(value = "CUSTOMER_DETAILS", key = "'ID: ' + #id")
    public void removeCustomer(final Long id) {
        log.info("Deleting item with id {}", id);
        customerDetailsRepo.deleteById(id);
    }

    @CachePut(value = "CUSTOMER_DETAILS", key = "'ID: ' + #item.id")
    public CustomerDetails addCustomer(final CustomerDetails item) {
        log.info("Creating item {}", item);
        item.setPassword(passwordEncoder.encode(item.getPassword()));
        return customerDetailsRepo.save(item);
    }

    @Cacheable(value = "CUSTOMER_DETAILS", key = "'ID: ' + #id")
    public CustomerDetails getCustomer(final Long id) {
        log.info("Getting item with id {}", id);
        final Optional<CustomerDetails> item = customerDetailsRepo.findById(id);
        if (item.isPresent())
            return item.get();
        return null;
    }

    public List<CustomerDetails> getAllCustomers() {
        log.info("Getting all items");
        return customerDetailsRepo.findAll();
    }
}
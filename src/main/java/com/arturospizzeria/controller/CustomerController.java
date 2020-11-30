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

import com.arturospizzeria.model.CustomerDetails;
import com.arturospizzeria.service.CustomerService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Endpoints related to the customer entity. This should include
 * operations such as creation, deletion, updating, etc.
 */
@Log4j2
@RestController
@RequestMapping(value = "/customer")
@Api(value = "customer", description = "Operations pertaining to managing our customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "Adds customer. Can be called by an admin app or during user registration.", response = CustomerDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created user"),
            @ApiResponse(code = 400, message = "Syntactic or Semantic errors occurred. Check your input and try again.")
    })
    @PostMapping("/")
    ResponseEntity<CustomerDetails> addItem(@Valid @RequestBody final CustomerDetails item) {
        log.info("CustomerController::addCustomer called");
        return ResponseEntity.ok(customerService.addCustomer(item));
    }

    @ApiOperation(value = "Fetch customer by id", response = CustomerDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found item"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @GetMapping("/{id}")
    ResponseEntity getItem(@PathVariable("id") final Long id) {
        log.info("CustomerController::getCustomer called");
        final CustomerDetails item = customerService.getCustomer(id);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Delete Customer by id. No content is returned.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted customer"),
            @ApiResponse(code = 404, message = "Customer not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity removeItem(@PathVariable("id") final Long id) {
        log.info("CustomerController::removeCustomer called");
        customerService.removeCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Returns all customers from the database. Should add pagination features here.",
            response = CustomerDetails.class,
            responseContainer = "List"
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found customers"),
            @ApiResponse(code = 404, message = "No items found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    List<CustomerDetails> getItems() {
        log.info("CustomerController::getItems called");
        return customerService.getAllCustomers();
    }
}
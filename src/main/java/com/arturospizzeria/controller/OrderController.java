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

import com.arturospizzeria.model.OrderRequest;
import com.arturospizzeria.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * This should do a lot more than it does, which is essentially nothing.
 * All we're doing is exposing a REST endpoint that will invoke our
 * OrderService's placeOrder method. This is to demonstrate pub-sub
 * capabilities.
 */
@Log4j2
@RestController
@Api(value = "menu", description = "Operations pertaining to managing our catalogue of food products")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "This doesn't really do anything important. It accepts a simple request object - but doesn't return anything useful. It DOES however demonstrate pub/sub mechanics using redis. When the underlying service is called it should publish a message.", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created cheese"),
            @ApiResponse(code = 400, message = "Syntactic or Semantic errors occurred. Check your input and try again.")
    })
    @PostMapping("/order")
    ResponseEntity<?> addOrder(@Valid @RequestBody final OrderRequest request) {
        log.info("OrderController::addOrder called");
        orderService.placeOrder(request.getMessage());
        return new ResponseEntity<>(new HashMap<String, String>() {
            {
                put("status", "placed");
            }
        }, HttpStatus.OK);
    }
}
package com.arturospizzeria.controller;

import com.arturospizzeria.model.MenuItem;
import com.arturospizzeria.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Endpoints that manage MenuItems.
 */
@Log4j2
@RestController
@RequestMapping(value = "/menu")
@Api(value = "menu", description = "Operations pertaining to managing our catalogue of food products")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "Add an item to the menu. An item can be a topping, sauce, side order, or beverage.", response = MenuItem.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created cheese"),
            @ApiResponse(code = 400, message = "Syntactic or Semantic errors occurred. Check your input and try again."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    @PostMapping("/item")
    ResponseEntity<MenuItem> addItem(@Valid @RequestBody final MenuItem item) {
        log.info("MenuController::addCustomer called");
        return new ResponseEntity<>(menuService.addItem(item), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Updates an existing item. This expects a complete entity, including id.", response = MenuItem.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated item"),
            @ApiResponse(code = 400, message = "Syntactic or Semantic errors occurred. Check your input and try again."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    @PutMapping("/item/{id}")
    ResponseEntity<MenuItem> updateItem(@PathVariable("id") final Long id, @Valid @RequestBody final MenuItem item) {
        log.info("MenuController::updateItem called");
        return ResponseEntity.ok(menuService.updateItem(id, item));
    }

    @ApiOperation(value = "Get an item from the menu by id.", response = MenuItem.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found item"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @GetMapping("/item/{id}")
    ResponseEntity<MenuItem> getItem(@PathVariable("id") final Long id) {
        log.info("MenuController::getCustomer called");
        final MenuItem item = menuService.getItem(id);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Delete MenuItem by id. No content is returned")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted item"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @DeleteMapping("/item/{id}")
    ResponseEntity removeItem(@PathVariable("id") final Long id) {
        log.info("MenuController::removeCustomer called");
        menuService.removeItem(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Returns all items from a menu. No need to worry since the limit of menu items is small.", response = MenuItem.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found items"),
            @ApiResponse(code = 404, message = "No items found")
    })
    @GetMapping("/items")
    List<MenuItem> getItems() {
        log.info("MenuController::getItems called");
        return menuService.getAllItems();
    }

    @ApiOperation(value = "Returns all items from a menu based on provided category.", response = MenuItem.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found items"),
            @ApiResponse(code = 404, message = "No items found")
    })
    @GetMapping("/category/{category}")
    List<MenuItem> getCategory(@PathVariable final MenuItem.Category category) {
        log.info("MenuController::getAllItemsInCategory called");
        return menuService.getAllItemsInCategory(category);
    }

    // We could manage exceptions as such:
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public Map<String, String> handleDataIntegrityViolationExceptions(
//            DataIntegrityViolationException ex) {
//        Map<String, String> errors = new HashMap<>();
//        errors.put("error", "something went wrong");
//        return errors;
//    }
}
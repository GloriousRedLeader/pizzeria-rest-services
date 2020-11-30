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
import com.arturospizzeria.model.AuthResponse;
import com.arturospizzeria.model.MenuItem;
import com.arturospizzeria.security.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Manages user authentication. Uses JWT as the primary
 * vehicle. Currently only supports local accounts, but could
 * be expanded to integrate with known oauth providers.
 */
@Log4j2
@RestController
@RequestMapping(value = "/auth")
@Api(value = "auth", description = "Operations pertaining to managing JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthUtils authUtils;

    @ApiOperation(value = "Attempts to authenticate a user, returns a token if credentials are valid.", response = MenuItem.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created JWT and authenticated user"),
            @ApiResponse(code = 400, message = "Syntactic or Semantic errors occurred. Check your input and try again.")
    })
    @PostMapping("/token")
    ResponseEntity<AuthResponse> createToken(@Valid @RequestBody AuthRequest request) {
        log.info("AuthController::createToken called");

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = authUtils.generateJwtToken(authentication);

        //UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
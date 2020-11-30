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

import com.arturospizzeria.model.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

/**
 * Here we are demonstrating that we can catch ALL exceptions in a
 * Spring Boot app, and convert them into the correct response codes.
 * There is probably a better way of doing this. However I don't like the
 * idea of a service throwing a ResponseStatusException - that should be
 * the job of the web layer, not the service layer to make that decision.
 * The service should just tell whoever is calling it that there was a
 * specific problem, like an illegal argument.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleConflict(final Exception ex, final WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        // TODO: Should be more exhaustive!
        if (ex instanceof DataIntegrityViolationException) {
            errorResponse = new ErrorResponse("There was a a problem with your request");
        } else if (ex instanceof MethodArgumentNotValidException) {
            Optional<ObjectError> error = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().stream().findFirst();
            if (error.isPresent()) {
                errorResponse = new ErrorResponse(error.get().toString());
            }
        } else if (ex instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = new ErrorResponse("You are not authorized");
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse("You are forbidden from accessing this resource");
        }

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }
}

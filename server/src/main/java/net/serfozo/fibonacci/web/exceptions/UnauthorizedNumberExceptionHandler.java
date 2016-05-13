package net.serfozo.fibonacci.web.exceptions;

import net.serfozo.fibonacci.core.services.exceptions.UnauthorizedNumberException;
import net.serfozo.fibonacci.web.domain.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UnauthorizedNumberExceptionHandler {
    @ExceptionHandler(UnauthorizedNumberException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDto exceptionHandler(final UnauthorizedNumberException e) {
        return new ErrorDto("The current user is not authorized to use that number as parameter.");
    }
}

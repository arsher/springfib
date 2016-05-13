package net.serfozo.fibonacci.web;

import net.serfozo.fibonacci.core.services.FibonacciGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.List;

import static net.serfozo.fibonacci.web.Constants.FIBONACCI_NUMBER_PARAM;
import static net.serfozo.fibonacci.web.Constants.FIBONACCI_URL;

@RestController
public class FibonacciController {
    private final FibonacciGenerator generator;

    @Inject
    public FibonacciController(final FibonacciGenerator generator) {
        this.generator = generator;
    }

    @RequestMapping(path = FIBONACCI_URL, method = RequestMethod.GET)
    public List<BigInteger> generateFibonacci(@RequestParam(FIBONACCI_NUMBER_PARAM  ) final int number) {
        return generator.generate(number);
    }
}

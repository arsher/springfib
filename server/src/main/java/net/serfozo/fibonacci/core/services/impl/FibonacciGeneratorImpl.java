package net.serfozo.fibonacci.core.services.impl;

import net.serfozo.fibonacci.core.security.CustomUser;
import net.serfozo.fibonacci.core.services.FibonacciGenerator;
import net.serfozo.fibonacci.core.services.exceptions.UnauthorizedNumberException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FibonacciGeneratorImpl implements FibonacciGenerator {
    @Override
    @PreAuthorize("isAuthenticated()")
    public List<BigInteger> generate(final int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number must be greater than or equal to zero");
        }

        validateUser(number);

        return Stream.generate(new SupplierImpl())
                .skip(1)
                .limit(number)
                .collect(Collectors.toList());
    }

    private void validateUser(final int number) {
        try {
            final CustomUser principal = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal.getMaxnumber() > 0 && principal.getMaxnumber() < number) {
                throw new UnauthorizedNumberException();
            }
        } catch (final NullPointerException e) {
            //when there is no context
        }
    }

    private static class SupplierImpl implements Supplier<BigInteger> {
        private BigInteger fibo1 = new BigInteger("-1");
        private BigInteger fibo2 = BigInteger.ONE;

        @Override
        public BigInteger get() {
            final BigInteger result = fibo1.add(fibo2);
            fibo1 = fibo2;
            fibo2 = result;
            return result;
        }
    }
}

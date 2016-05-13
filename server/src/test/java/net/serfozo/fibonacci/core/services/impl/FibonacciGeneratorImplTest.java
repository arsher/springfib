package net.serfozo.fibonacci.core.services.impl;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class FibonacciGeneratorImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void thatNegativeNumberThrows() throws Exception {
        FibonacciGeneratorImpl generator = new FibonacciGeneratorImpl();
        generator.generate(-1);
    }

    @Test
    public void thatZeroReturnsEmptyList() {
        FibonacciGeneratorImpl generator = new FibonacciGeneratorImpl();
        List<BigInteger> list = generator.generate(0);

        assertEquals(0, list.size());
    }

    @Test
    public void thatFirst15ElementsCorrect() {
        List<BigInteger> expected = Arrays.stream(new long[]{1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610})
                .mapToObj((e) -> BigInteger.valueOf(e))
                .collect(Collectors.toList());

        FibonacciGeneratorImpl generator = new FibonacciGeneratorImpl();
        List<BigInteger> list = generator.generate(15);

        assertEquals(15, list.size());
        assertThat(list, contains(expected.toArray()));
    }
}
package net.serfozo.fibonacci.web;

import net.serfozo.fibonacci.core.services.FibonacciGenerator;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FibonacciControllerTest {
    @Test
    public void thatServiceCalled() throws Exception {
        final BigInteger[] expected = new BigInteger[]{BigInteger.ONE};
        final FibonacciGenerator mock = mock(FibonacciGenerator.class);
        when(mock.generate(intThat(equalTo(1)))).thenReturn(Arrays.asList(expected));

        final FibonacciController fibonacciController = new FibonacciController(mock);

        final List<BigInteger> bigIntegers = fibonacciController.generateFibonacci(1);

        assertThat(bigIntegers, contains(expected));
    }
}
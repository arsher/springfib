package net.serfozo.fibonacci.core.services;

import java.math.BigInteger;
import java.util.List;

public interface FibonacciGenerator {
    List<BigInteger> generate(int number);
}

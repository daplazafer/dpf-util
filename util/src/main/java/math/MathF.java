/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import java.math.BigInteger;

/**
 *
 * @author dpf
 */
public enum MathF {

    ;
    
    public static double factorial(int n) {
        return n == 0 || n == 1 ? 1 : n * factorial(n - 1);
    }

    public static double combinatorial(int a, int b) {
        if (a == 0 || a == b) {
            return 1l;
        }
        double top = 1;
        for (int i = 0; i < b; i++) {
            top *= (a - i);
        }
        return top / factorial(b);
    }

    public static BigInteger factorialBI(int n) {
        BigInteger fact = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            fact = fact.multiply(new BigInteger(i + ""));
        }
        return fact;
    }

    public static BigInteger combinatorialBI(int a, int b) {
        if (a == 0 || a == b) {
            return BigInteger.ONE;
        }
        BigInteger top = BigInteger.ONE;
        for (int i = 0; i < b; i++) {
            top = top.multiply(new BigInteger((a - i) + ""));
        }

        return top.divide(factorialBI(b));
    }
    
    public static double log(int base, double x){
        return Math.log10(x)/Math.log10(base);
    }

}


package math;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Daniel Plaza
 */
public class BigIntegerEx extends BigInteger implements Serializable {

    public static BigInteger TWO = BigIntegerEx.valueOf(2L);
    public static BigInteger THREE = BigIntegerEx.valueOf(3L);
    public static BigInteger FOUR = BigIntegerEx.valueOf(4L);
    public static BigInteger FIVE = BigIntegerEx.valueOf(5L);
    public static BigInteger SIX = BigIntegerEx.valueOf(6L);
    public static BigInteger SEVEN = BigIntegerEx.valueOf(7L);
    public static BigInteger EIGHT = BigIntegerEx.valueOf(8L);
    public static BigInteger NINE = BigIntegerEx.valueOf(9L);

    public BigIntegerEx(BigInteger bigint) {
        super(bigint.toByteArray());
    }
    
    public BigIntegerEx(byte[] val) {
        super(val);
    }

    public BigIntegerEx(int signum, byte[] magnitude) {
        super(signum, magnitude);
    }

    public BigIntegerEx(String val, int radix) {
        super(val, radix);
    }

    public BigIntegerEx(String val) {
        super(val);
    }

    public BigIntegerEx(int numBits, Random rnd) {
        super(numBits, rnd);
    }

    public BigIntegerEx(int bitLength, int certainty, Random rnd) {
        super(bitLength, certainty, rnd);
    }
    
    /**
     * Calculate if the BigInteger is a prime
     * @return true if prime, false if no prime
     */
    public boolean isPrime() {
        BigIntegerEx counter = BigIntegerEx.valueOf(3L);
        BigInteger target = sqrt();
        while (counter.compareTo(target) == -1) {
            if (this.mod(counter).compareTo(BigInteger.ZERO) == 0) {
                return false;
            }
            switch (counter.getLastDigit()) {
                case 1:
                case 7:
                case 9:
                    counter=counter.add(TWO);
                    break;
                case 3:
                    counter=counter.add(FOUR);
                    break;
                default:
                    counter=counter.add(ONE);
            }
        }
        return true;
    }
    
    /**
     * Efficiency O(1) 
     * @return the digit count
     */
    public int getDigitCount() {
        double factor = Math.log(2) / Math.log(10);
        int digitCount = (int) (factor * this.bitLength() + 1);
        if (BigInteger.TEN.pow(digitCount - 1).compareTo(this) > 0) {
            return digitCount - 1;
        }
        return digitCount;
    }
    
    /**
     * Indexes: ... 11 10 9 8 7 6 5 4 3 2 1 0 
     * @param index digit position
     * @return digit at the given position
     */
    public int intAtDigit(int index) {
        BigInteger[] resultAndRemainder=null;
        BigInteger n=new BigIntegerEx(this);
        for(int i=0;i<=index;i++){
            resultAndRemainder = n.divideAndRemainder(BigInteger.TEN);
            n = resultAndRemainder[0];
        }
        return resultAndRemainder[1].intValue();
    }
    
    /**
     * 
     * @return First digit of the number
     */
    public int getFirstDigit() {
        return intAtDigit(getDigitCount()-1);
    }
    
    /**
     * 
     * @return Last digit of the number
     */
    public int getLastDigit() {
        BigInteger[] resultAndRemainder;
        resultAndRemainder = divideAndRemainder(BigInteger.TEN);
        return Math.abs(resultAndRemainder[1].intValue());
    }
    
    /**
     * Search for a pattern in the number. May not work in big numbers
     * @param pattern
     * @return 
     */
    public int getFirstIndexOfPattern(String pattern){
        return this.toString().indexOf(pattern);
    }

    /*
    @SuppressWarnings("empty-statement")
    public BigIntegerEx sqrt()
            throws IllegalArgumentException {
        BigIntegerEx x = new BigIntegerEx(this);
        if (x.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }
        // square roots of 0 and 1 are trivial and
        // y == 0 will cause a divide-by-zero exception
        if (x.equals(ZERO) || x.equals(ONE)) {
            return x;
        } // end if
        BigIntegerEx two = BigIntegerEx.valueOf(2L);
        BigIntegerEx y;
        // starting with y = x / 2 avoids magnitude issues with x squared
        for (y = x.divide(two);
                y.compareTo(x.divide(y)) > 0;
                y = ((x.divide(y)).add(y)).divide(two));
        return y;
    }
    */
    
    /**
     * 
     * @return Square root of the number
     * @throws IllegalArgumentException if complex (negative)
     */
    @SuppressWarnings("empty-statement")
    public BigIntegerEx sqrt()
            throws IllegalArgumentException {
        BigIntegerEx x = new BigIntegerEx(this.toByteArray());
        if (x.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }
    // square roots of 0 and 1 are trivial and
        // y == 0 will cause a divide-by-zero exception
        if (x == ZERO || x == ONE) {
            return x;
        } // end if
        BigInteger two = BigIntegerEx.valueOf(2L);
        BigIntegerEx y;
        // starting with y = x / 2 avoids magnitude issues with x squared
        for (y = x.divide(two);
                y.compareTo(x.divide(y)) > 0;
                y = ((x.divide(y)).add(y)).divide(two));
        if (x.compareTo(y.multiply(y)) == 0) {
            return y;
        } else {
            return y.add(ONE);
        }
    }


    public static BigIntegerEx valueOf(long val) {
        return new BigIntegerEx(BigInteger.valueOf(val));
    }
    
    public static BigIntegerEx valueOf(int val) {
        return valueOf((long)val);
    }
    
    @Override
    public BigIntegerEx max(BigInteger val) {
        return new BigIntegerEx(super.max(val));
    }

    @Override
    public BigIntegerEx min(BigInteger val) {
        return new BigIntegerEx(super.min(val));
    }

    @Override
    public BigIntegerEx flipBit(int n) {
        return new BigIntegerEx(super.flipBit(n));
    }

    @Override
    public BigIntegerEx clearBit(int n) {
        return new BigIntegerEx(super.clearBit(n)); 
    }

    @Override
    public BigIntegerEx setBit(int n) {
        return new BigIntegerEx(super.setBit(n));
    }

    @Override
    public BigIntegerEx andNot(BigInteger val) {
        return new BigIntegerEx(super.andNot(val));
    }

    @Override
    public BigIntegerEx not() {
        return new BigIntegerEx(super.not());
    }

    @Override
    public BigIntegerEx xor(BigInteger val) {
        return new BigIntegerEx(super.xor(val));
    }

    @Override
    public BigIntegerEx or(BigInteger val) {
        return new BigIntegerEx(super.or(val));
    }

    @Override
    public BigIntegerEx and(BigInteger val) {
        return new BigIntegerEx(super.and(val));
    }

    @Override
    public BigIntegerEx shiftRight(int n) {
        return new BigIntegerEx(super.shiftRight(n));
    }

    @Override
    public BigIntegerEx shiftLeft(int n) {
        return new BigIntegerEx(super.shiftLeft(n));
    }

    @Override
    public BigIntegerEx modInverse(BigInteger m) {
        return new BigIntegerEx(super.modInverse(m));
    }

    @Override
    public BigIntegerEx modPow(BigInteger exponent, BigInteger m) {
        return new BigIntegerEx(super.modPow(exponent, m));
    }

    @Override
    public BigIntegerEx mod(BigInteger m) {
        return new BigIntegerEx(super.mod(m));
    }

    @Override
    public BigIntegerEx negate() {
        return new BigIntegerEx(super.negate());
    }

    @Override
    public BigIntegerEx abs() {
        return new BigIntegerEx(super.abs());
    }

    @Override
    public BigIntegerEx gcd(BigInteger val) {
        return new BigIntegerEx(super.gcd(val));
    }

    @Override
    public BigIntegerEx pow(int exponent) {
        return new BigIntegerEx(super.pow(exponent));
    }

    @Override
    public BigIntegerEx remainder(BigInteger val) {
        return new BigIntegerEx(super.remainder(val));
    }
    
    public BigIntegerEx remainder(long val) {
        return remainder(BigIntegerEx.valueOf(val)); 
    }
    
    public BigIntegerEx remainder(int val) {
        return remainder((long)val);
    }
    
    @Override
    public BigIntegerEx divide(BigInteger val) {
        return new BigIntegerEx(super.divide(val));
    }
    
    public BigIntegerEx divide(long val) {
        return divide(BigIntegerEx.valueOf(val)); 
    }
    
    public BigIntegerEx divide(int val) {
        return divide((long)val);
    }

    @Override
    public BigIntegerEx multiply(BigInteger val) {
        return new BigIntegerEx(super.multiply(val)); 
    } 
    
    public BigIntegerEx multiply(long val) {
        return multiply(BigIntegerEx.valueOf(val)); 
    }
    
    public BigIntegerEx multiply(int val) {
        return multiply((long)val);
    }
    
    @Override
    public BigIntegerEx subtract(BigInteger val) {
        return new BigIntegerEx(super.subtract(val));
    }
    
    public BigIntegerEx subtract(long val) {
        return subtract(BigIntegerEx.valueOf(val)); 
    }
    
    public BigIntegerEx subtract(int val) {
        return subtract((long)val);
    }

    @Override
    public BigIntegerEx add(BigInteger val) {
        return new BigIntegerEx(super.add(val));
    }
    
    public BigIntegerEx add(int val) {
        return add((long)val);
    }
    
    public BigIntegerEx add(long val) {
        return add(BigIntegerEx.valueOf(val));
    }

    @Override
    public BigIntegerEx nextProbablePrime() {
        return new BigIntegerEx(super.nextProbablePrime());
    }

}

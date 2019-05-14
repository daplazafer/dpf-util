package math;

import collections.ModularList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for functions. Some patterns examples: 
 *  - 3*x^4-x*log2(512*x+1)-pi^x
 *  - 4*x-sin(pi)-e^(x-1) - 5*x^3-2*x^2+4*x-2
 *
 * The next arguments must be the variables used in the function String. By
 * default the only variable is 'x' like the examples if no variables are
 * specified. Examples:
 *  - Function f1 = new Function("x*log(6*x^2-x+5)");
 *  - Function f2 = new Function("3*x-6*y^2-17","x","y");
 *
 * Recommended names for variables (to avoid overwriting other keywords): 
 *  - var1, var2, var3, var4, etc.
 *  - x, y, z, w or X, Y, Z, W.
 *  - Any capital letter except 'E' (used for exponential 10^).
 *
 * Some of the methods only work for 1 variable functions.
 *
 * Advice: Use parenthesis () and use * to multiply blocks of () and variables.
 * Never use as variable name any or a part of any of the following keywords.
 *
 * Keywords: 
 *  - The available operators are: +, -, *, /, ^, E.
 *  - The available math functions are: log, ln, sqrt, sin, cos, tan, sinh,
 * cosh, tanh. 
 *  - The available numbers are: pi, e.
 *
 * @author Daniel Plaza
 */
public class Function {

    /*
     * KEYWORDS
     */
    
    // parenthesis
    private static final char PAR_O = '(';
    private static final char PAR_C = ')';

    // Operations
    private static final char SUM = '+';
    private static final char MIN = '-';
    private static final char MULT = '*';
    private static final char DIV = '/';
    private static final char POW = '^';
    private static final char EXP = 'E';  

    // Functions
    private static final String LOG = "log";
    private static final String LN = "ln";
    private static final String SQRT = "sqrt";
    private static final String SIN = "sin";
    private static final String COS = "cos";
    private static final String TAN = "tan";
    private static final String SINH = "sinh";
    private static final String COSH = "cosh";
    private static final String TANH = "tanh";
    
    // Other functions
    private static final char FACTORIAL = '!';

    // Numbers
    private static final String PI = "pi";
    private static final String E = "e";

    /*
     * VARIABLES
     */
    
    public static final String DEFAULTVARIABLE = "x"; // used when any var introduced

    private String function;
    private final List<String> variables;

    private double precision = 1E-5;
    private boolean rad = true; // radians or degrees

    /**
     * Check the class documentation for info.
     *
     * @param function String with the formatted function.
     * @param variables variables of the function.
     */
    public Function(String function, String... variables) {
        // format the input text
        String f = function;
        f = f.replaceAll(" ", "");

        this.function = f;
        this.variables = new LinkedList<>();
        if (variables.length == 0) {
            this.variables.add(DEFAULTVARIABLE);
        }
        this.variables.addAll(Arrays.asList(variables));
    }

    public double image(double... x) {
        validateArgs(x);
        String f = function;
        int i = 0;
        for (String v : variables) {
            f = f.replaceAll(v, "" + x[i]);
            i++;
        }
        f = f.replaceAll(PI, "" + Math.PI);
        f = f.replaceAll(E, "" + Math.E);
        return evaluate(f);
    }

    /**
     * Newton 5 point method
     *
     * @param approach approach of the solution
     * @return solution, +-precision
     */
    public double solve(double approach) {
        double e = precision;
        double a;
        double s = approach;
        do {
            a = s;
            s = a - (image(a) / f_5point(a));
        } while (Math.abs(a - s) > e);
        return s;
    }

    public double solve() {
        return solve(Math.random() * 10);
    }

    private double f_5point(double x) {
        double h = precision;
        return (-image(x + 2 * h) + 8 * image(x + h) - 8 * image(x - h) + image(x - 2 * h)) / (22 * h);
    }

    /**
     * Definition of derivative: f'(x)=(f(x+h)-f(x))/h
     *
     * @param x
     * @return
     */
    public double derivative(double... x) {
        validateArgs(x);
        double h = precision;
        double xh[] = new double[x.length];
        for (int i = 0; i < xh.length; i++) {
            xh[i] = x[i] + h;
        }
        return (image(xh) - image(x)) / h;
    }

    /**
     * Simpson´s rule for integration
     *
     * @param interval_a
     * @param interval_b
     * @return
     */
    public double integral(double interval_a, double interval_b) {
        return ((interval_b - interval_a) / 6) * (image(interval_a) + 4 * image((interval_a + interval_b) / 2) + image(interval_b));
    }

    public boolean radians() {
        return rad;
    }

    public boolean degrees() {
        return !rad;
    }

    public void toRadians() {
        this.rad = true;
    }

    public void toDegrees() {
        this.rad = false;
    }

    private double evaluate(String exp) {
        return new Object() {

            private int pos = -1, ch;

            private void nextChar() {
                ch = (++pos < exp.length()) ? exp.charAt(pos) : -1;
            }

            private boolean eat(int charToEat) {
                while (ch == ' ') {
                    nextChar();
                }
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            private double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < exp.length()) {
                    throw new SyntaxErrorException((char) ch);
                }
                return x;
            }

            /**
             * Grammar: expression = term | expression `+` term | expression `-`
             * term term = factor | term `*` factor | term `/` factor factor =
             * `+` factor | `-` factor | `(` expression `)` | number |
             * functionName factor | factor `^` factor
             */
            private double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat(SUM)) {
                        x += parseTerm(); // addition
                    } else if (eat(MIN)) {
                        x -= parseTerm(); // subtraction
                    } else {
                        return x;
                    }
                }
            }

            private double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat(MULT)) {
                        x *= parseFactor(); // multiplication
                    } else if (eat(DIV)) {
                        x /= parseFactor(); // division
                    } 
                    /*
                ---  FACTORIAL
                */
                else if(eat(FACTORIAL)){
                    double num;
                    long iPart;
                    double fPart;
                    num = x;
                    iPart = (long) num;
                    fPart = num - iPart;
                    if(fPart != 0){
                        throw new MathErrorException(x);
                    }
                    int x_ = 1;
                    for(int i = (int)x ; i>1; i--){
                        x_ *= i;
                    }
                    x = (double)x_;
                }
                /*
                 -----
                */
                    
                    
                    else {
                        return x;
                    }
                }
            }

            private double parseFactor() {
                if (eat(SUM)) {
                    return parseFactor(); // unary plus
                }
                if (eat(MIN)) {
                    return -parseFactor(); // unary minus
                }
                double x;
                int startPos = this.pos;
                if (eat(PAR_O)) { // parentheses
                    x = parseExpression();
                    eat(PAR_C);
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    x = Double.parseDouble(exp.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') {
                        nextChar();
                    }
                    String func = exp.substring(startPos, this.pos);
                    double base = 10;
                    if (LOG.equals(func)) { // logX
                        while ((ch >= '0' && ch <= '9') || ch == '.') {
                            nextChar();
                        }
                        String b = exp.substring(startPos, this.pos);
                        b = b.replaceAll(LOG, "");
                        if (!"".equals(b)) {
                            base = Double.parseDouble(b);
                        }
                    }
                    x = parseFactor();

                    if (!rad) { // to degrees
                        switch (func) {
                            case SIN:
                            case COS:
                            case TAN:
                            case SINH:
                            case COSH:
                            case TANH:
                                x = Math.toRadians(x);
                        }
                    }

                    switch (func) {
                        case SQRT:
                            x = Math.sqrt(x);
                            break;
                        case SIN:
                            x = Math.sin(x);
                            break;
                        case COS:
                            x = Math.cos(x);
                            break;
                        case TAN:
                            x = Math.tan(x);
                            break;
                        case LOG:
                            if (base == 10) {
                                x = Math.log10(x);
                            } else {
                                x = Math.log10(x) / Math.log10(base);
                            }
                            break;
                        case LN:
                            x = Math.log(x);
                            break;
                        case SINH:
                            x = Math.sinh(x);
                            break;
                        case COSH:
                            x = Math.cosh(x);
                            break;
                        case TANH:
                            x = Math.tanh(x);
                            break;
                        default:
                            throw new SyntaxErrorException(func);
                    }
                } else {
                    throw new SyntaxErrorException((char) ch);
                }

                if (eat(POW)) {
                    x = Math.pow(x, parseFactor()); // exponentiation
                } else if (eat(EXP)) {
                    x = x * Math.pow(10, parseFactor()); // exponentiation
                }
                return x;
            }
        }.parse();
    }

    public List<String> getVariables() {
        return variables;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    @Override
    public String toString() {
        String f = "f(";
        for (int i = 0; i < variables.size(); i++) {
            if (i != 0) {
                f += ", ";
            }
            f += variables.get(i);
        }
        f += ") = " + function;
        return f;
    }

    public String toString(double... x) {
        String f = "f(";
        for (int i = 0; i < x.length; i++) {
            if (i != 0) {
                f += ", ";
            }
            f += x[i];
        }
        f += ") = " + image(x);
        return f;
    }

    private void validateArgs(double[] x) {
        if (variables.size() != x.length) {
            throw new IllegalArgumentException("The function has " + variables.size() + " argumet(s) but the method recived " + x.length);
        }
    }

    private class SyntaxErrorException extends RuntimeException {

        private SyntaxErrorException(String function) {
            super("Unknown function: " + function);
        }

        private SyntaxErrorException(char ch) {
            super("Unexpected: " + ch);
        }
    }
    
    private class MathErrorException extends RuntimeException{
        
        private MathErrorException(Double x) {
            super("Not integer value: " + x);
        }
        
    }

}

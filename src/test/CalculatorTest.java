package test;
import main.Calculator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    @Test
    void add_twoPositiveNumbers_returnsSum() {
        Calculator calc = new Calculator();
        int result = calc.add(2, 3);
        assertEquals(5, result);
    }

    @Test
    void divide_divisionByZero_throwsException() {
        Calculator calc = new Calculator();
        assertThrows(IllegalArgumentException.class,
                     () -> calc.divide(10, 0));
    }
}

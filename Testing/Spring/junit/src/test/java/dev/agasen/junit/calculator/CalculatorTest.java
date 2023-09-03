package dev.agasen.junit.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CalculatorTest {

  @Test // a unit test method
  void testAdd() {
    Calculator calculator = new Calculator();
    double result = calculator.add(10, 50);
    assertEquals(60d, result, 0d, "Test Result Is Correct.");
  }

  @RepeatedTest(
    value = 5,
    name = "{displayName} - repetition {currentRepetition}/{totalRepetitions}"
  )
  @DisplayName("Test add operation")
  void testRepeatedAdd() {
    Calculator calculator = new Calculator();
    assertEquals(2, calculator.add(1, 1), 0, 
        "1 + 1 should equal 2");
  }


  @ParameterizedTest
  @ValueSource(doubles = {
    10, 20, 30
  })
  void testParameterizedTest(double d) {
    assertEquals(10+d, 10+d, 0, d + " + 10 should be equal to " + (d+10));
  }
}
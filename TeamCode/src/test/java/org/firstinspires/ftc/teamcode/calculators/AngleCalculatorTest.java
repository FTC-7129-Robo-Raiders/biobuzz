package org.firstinspires.ftc.teamcode.calculators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Angle Calculator Tests")
public class AngleCalculatorTest {
    @ParameterizedTest
    @CsvSource({
            "360, 360, 0",
            "10, 10, 0",
            "0, 0, 0",
            "0, 1, -1",
            "1, 0, 1",
            "90.5, 80.2, 10.3",
            "90.5, 245.0, -154.5",
            "12, -20.0, 32",
            "-12, 20.0, -32",
            "180, 270, -90",
            "270, 0, -90",
            "270, 180, 90",
            "0, 180, 180",
    })
    void testGetShortestAngleBetween(double targetAngle, double actualAngle, double expected) {
        double result = AngleCalculator.getShortestAngleBetween(targetAngle, actualAngle);
        // Add a little tolerance for floating-point precision loss
        double tolerance = 0.001;
        assertThat(expected, closeTo(result, tolerance));
    }
}

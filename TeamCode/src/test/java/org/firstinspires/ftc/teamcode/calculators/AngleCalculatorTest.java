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
            "1, 0, -1",
            "0, 1, 1",
            "80.2, 90.5, 10.3",
            "245.0, 90.5, -154.5",
            "-20.0, 12, 32",
            "20.0, -12, -32",
            "270, 180, -90",
            "180, 270, 90",
            "0, 270, -90",
            "180, 270, 90",
            "180, 0, 180",
            "180.902111, 12.12345, -168.778661",
    })
    void testGetShortestAngleBetween(double startingAngle, double endAngle, double expected) {
        double result = AngleCalculator.getShortestAngleBetween(startingAngle, endAngle);
        // Add a little tolerance for floating-point precision loss; any loss up to five thousandths
        // of a degree seems acceptable for practical purposes
        double tolerance = 0.005;
        assertThat(expected, closeTo(result, tolerance));
    }
}

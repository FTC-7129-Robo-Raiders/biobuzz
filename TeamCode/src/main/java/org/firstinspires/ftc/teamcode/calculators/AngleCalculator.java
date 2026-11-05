package org.firstinspires.ftc.teamcode.calculators;

public class AngleCalculator {
    /**
     * Get the shortest possible angle (in degrees) to close the gap between the target and
     * actual angles.
     * @param targetAngle The target angle being aimed for (in degrees)
     * @param currentAngle The current angle (in degrees)
     * @return The shortest angle to close the gap. This result should be somewhere between -180
     * to 180 degrees
     */
    public static double getDegreeAngleToCloseGap(double targetAngle, double currentAngle) {
        double angleDifference = targetAngle - currentAngle;
        angleDifference %= 360;
        angleDifference += 360;
        angleDifference %= 360;
        if (angleDifference > 180) {
            angleDifference -= 360;
        }
        return angleDifference;
    }
}

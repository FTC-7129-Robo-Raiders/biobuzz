package org.firstinspires.ftc.teamcode.calculators;

public class AngleCalculator {
    /**
     * Get the shortest possible angle (in degrees) between two angles.
     * This is useful for use-cases like when your robot is facing
     * one direction, and you want it to face another direction; this method can be used to
     * determine how far the robot should turn
     * @param targetAngle The target angle being aimed for (in degrees)
     * @param currentAngle The current angle (in degrees)
     * @return The shortest angle to close the gap. This result should be somewhere between -180
     * to 180 degrees
     */
    public static double getShortestAngleBetween(double targetAngle, double currentAngle) {
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

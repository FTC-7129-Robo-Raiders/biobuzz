package org.firstinspires.ftc.teamcode.calculators;

public class AngleCalculator {
    /**
     * Get the shortest possible angle (in degrees) between two angles.
     * This is useful for use-cases like when your robot is facing
     * one direction, and you want it to face another direction; this method can be used to
     * determine how far the robot should turn
     * @param startingAngle The starting angle (in degrees)
     * @param endAngle The end angle (in degrees)
     * @return The shortest angle between the starting angle and the end angle.
     * This result should be somewhere between -180 to 180 degrees
     */
    public static double getShortestAngleBetween(double startingAngle, double endAngle) {
        double angleDifference = endAngle - startingAngle;
        angleDifference %= 360;
        angleDifference += 360;
        angleDifference %= 360;
        if (angleDifference > 180) {
            angleDifference -= 360;
        }
        return angleDifference;
    }
}

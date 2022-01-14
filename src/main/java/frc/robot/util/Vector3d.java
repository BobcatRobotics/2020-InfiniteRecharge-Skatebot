package frc.robot.util;

public class Vector3d {

    private double[] values;

    public Vector3d(double x, double y, double z) {
        values = new double[] {x, y, z};
        values[0] = x;
        values[1] = y;
        values[2] = z;
    }

    public Vector3d() {
        values = new double[] {0, 0, 0};
    }

    public double getX() {
        return values[0];
    }

    public double getY() {
        return values[1];
    }
    
    public double getZ() {
        return values[2];
    }

    public double[] getValues() {
        return values;
    }

    public static Vector3d add(Vector3d first, Vector3d second) {
        return new Vector3d(first.getX() + second.getX(), first.getY() + second.getY(), first.getZ() + second.getZ());
    }

    public static Vector3d scalarMultiply(Vector3d vec, double scalar) {
        return new Vector3d(scalar*vec.getX(), scalar*vec.getY(), scalar*vec.getZ());
    }

    public static Vector3d matrixVectorMultiply(Vector3d[] matrix, Vector3d vec) {
        
        Vector3d result = new Vector3d();
        
        for (int i = 0; i < 3; i++) {
            result = add(result, scalarMultiply(matrix[i], vec.getValues()[i]));
        }

        return result;
    }

    public String toString() {
        return String.format("<%f,%f,%f>", values[0], values[1], values[2]);
    }
}
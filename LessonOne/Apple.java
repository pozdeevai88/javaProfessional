package ru.geekbrains;

public class Apple extends Fruit{
    private static final float MASS = 1f;

    @Override
    public float getSingleMass() {
        return MASS;
    }
}

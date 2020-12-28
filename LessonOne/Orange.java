package ru.geekbrains;

public class Orange extends Fruit{
    private static final float MASS = 1.5f;

    @Override
    public float getSingleMass() {
        return MASS;
    }
}

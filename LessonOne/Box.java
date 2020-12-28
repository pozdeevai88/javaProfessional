package ru.geekbrains;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private final ArrayList<T> fruits;
    private static final float MASS_LIMIT = 10f;
    private final T type;

    public Box(T type) {
        this.type = type;
        this.fruits = new ArrayList<>();
    }

    public boolean compare(Box<?> other) {
        return this.getBoxMass() == other.getBoxMass();
    }

    public float getBoxMass() {
        return type.getSingleMass() * fruits.size();
    }

    public void addFruit(T newFruit) {
        if (!isFull()) {
            fruits.add(newFruit);
        } else {
            System.out.println("Can't add. Box is full");
        }
    }

    private boolean isFull() {
        if (fruits.size() * type.getSingleMass() >= MASS_LIMIT) return true;
        return false;
    }

    public void moveTo(Box<T> newBox) {
        int n = fruits.size();
        for (int i = 0; i < n; i++) {
            if (!newBox.isFull()) {
                newBox.addFruit(fruits.get(0));
                fruits.remove(0);
            } else {
                System.out.println("New box is full. Try another box");
                break;
            }
        }
    }
}

package ru.geekbrains;

import java.util.ArrayList;
import java.util.Arrays;

public class Array1<T> {

    private T[] array;

    @SafeVarargs
    public Array1(T... array) {
        this.array = array;
    }

    public void swapElement(int index1, int index2) {
        T temp = array[index2];
        array[index2] = array[index1];
        array[index1] = temp;
    }

    public void printArray() {
        System.out.println(Arrays.toString(array));
    }

    public ArrayList<T> getAsArrayList () {
        return new ArrayList<T>(Arrays.asList(array));
    }
}

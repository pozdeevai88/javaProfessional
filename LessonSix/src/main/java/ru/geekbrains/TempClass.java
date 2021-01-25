package ru.geekbrains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TempClass {

    public static void main(String[] args) {
        int[] array1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        try {
            System.out.println(Arrays.toString(getAfterLastFour(array1)));
        } catch (RuntimeException e) {
            System.err.println("В массиве нет числа 4");
        }
        int[] array2 = new int[]{1, 4, 1, 4};
        System.out.println(checkOneAndFour(array2));
    }

    public static int[] getAfterLastFour(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 4) break;
            list.add(array[i]);
        }
        if (list.size() == array.length) throw new RuntimeException();
        Collections.reverse(list);
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) result[i] = list.get(i);
        return result;
    }

    public static boolean checkOneAndFour(int[] array) {
        boolean one = false;
        boolean four = false;
        for (int c : array) {
            if (c == 1) one = true;
            if (c == 4) four = true;
            if (one && four) return true;
        }
        return false;
    }

}

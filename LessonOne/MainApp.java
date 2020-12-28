package ru.geekbrains;

public class MainApp {

    public static void main(String[] args) {

        /*
         * Задание 1
         * Написать метод, который меняет два элемента массива местами
         */

        Array1<String> array1 = new Array1<>("123", "456", "789", "abc", "def");
        array1.printArray();
        array1.swapElement(0, 4);
        array1.printArray();

        /*
         * Задание 2
         * Написать метод, который преобразует массив в ArrayList
         */

        System.out.println(array1.getAsArrayList());
        System.out.println(array1.getAsArrayList().getClass().getName());  // проверка типа

        /*
         * Задание 3
         */

        // Коробкидля фруктов
        Box<Apple> appleBox1 = new Box<>(new Apple());
        Box<Orange> orangeBox1 = new Box<>(new Orange());
        Box<Orange> orangeBox2 = new Box<>(new Orange());

        // Заполним первую коробку
        appleBox1.addFruit(new Apple());
        appleBox1.addFruit(new Apple());
        appleBox1.addFruit(new Apple());
        appleBox1.addFruit(new Apple());
        appleBox1.addFruit(new Apple());
        appleBox1.addFruit(new Apple());

        // Вторую
        orangeBox1.addFruit(new Orange());
        orangeBox1.addFruit(new Orange());
        orangeBox1.addFruit(new Orange());
        orangeBox1.addFruit(new Orange());
        orangeBox1.addFruit(new Orange());
        orangeBox1.addFruit(new Orange());

        // И третью
        orangeBox2.addFruit(new Orange());
        orangeBox2.addFruit(new Orange());
        orangeBox2.addFruit(new Orange());

        // Запросим массу каждой коробкм
        System.out.println(appleBox1.getBoxMass());
        System.out.println(orangeBox1.getBoxMass());
        System.out.println(orangeBox2.getBoxMass());

        // Сравним массы разных коробок
        System.out.println(orangeBox1.compare(orangeBox2));
        System.out.println(orangeBox1.compare(appleBox1));

        // Пересыпем апельсины из одной в другую
        orangeBox1.moveTo(orangeBox2);

        // Посмотрим, что получилось
        System.out.println(orangeBox1.getBoxMass());
        System.out.println(orangeBox2.getBoxMass());


    }

}

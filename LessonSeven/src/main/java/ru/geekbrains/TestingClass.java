package ru.geekbrains;

import ru.geekbrains.annotations.AfterSuite;
import ru.geekbrains.annotations.BeforeSuite;
import ru.geekbrains.annotations.Test;

public class TestingClass {

    @BeforeSuite
    public static void setUp (){
        System.out.println("SetUp");
    }

    @AfterSuite
    public static void tearDown (){
        System.out.println("TearDown");
    }

    @Test(priority = 1)
    public static void test1 () {
        System.out.println("Test 1");
    }

    @Test(priority = 2)
    public static void test2 () {
        System.out.println("Test 2");
    }

    @Test(priority = 3)
    public static void test3 () {
        System.out.println("Test 3");
    }

    @Test(priority = 4)
    public static void test4 () {
        System.out.println("Test 4");
    }

    @Test(priority = 5)
    public static void test5 () {
        System.out.println("Test 5");
    }


}

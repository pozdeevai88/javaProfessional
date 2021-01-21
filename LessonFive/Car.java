package ru.geekbrains;

import java.util.concurrent.CountDownLatch;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static CountDownLatch onStart;
    private static CountDownLatch onFinish;
    private final Race race;
    private final int speed;
    private final String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CountDownLatch start, CountDownLatch finish) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        onStart = start;
        onFinish = finish;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            onStart.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            onStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        System.out.println(this.name + " занял " + (CARS_COUNT - onFinish.getCount() + 1) + " место");
        onFinish.countDown();
        try {
            onFinish.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

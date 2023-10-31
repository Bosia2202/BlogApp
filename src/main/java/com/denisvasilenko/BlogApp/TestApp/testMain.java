package com.denisvasilenko.BlogApp.TestApp;

import java.sql.Date;
import java.util.UUID;

public class testMain {
    public static void main(String[] args) {
        UUID uuid=UUID.randomUUID();
        System.out.println("First value "+uuid);
        String i=uuid.toString()+"BOSIA";
        System.out.println("Second value "+i);

        Long mills=System.currentTimeMillis();
        Date date =new Date(mills);
        System.out.println(date);
    }
}

package org.ulukraat;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите name персонажа");
        Scanner scanner = new Scanner(System.in);
        String nameUserChar = scanner.nextLine();

        System.out.println(nameUserChar);
        scanner.close();
    }
}
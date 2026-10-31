package com.mehdi8s.hastane.ekran;

import java.util.NoSuchElementException;
import java.util.Scanner;

public final class TarayiciKonsol implements Konsol {
    private final Scanner scanner;

    public TarayiciKonsol() {
        this(new Scanner(System.in));
    }

    TarayiciKonsol(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void yaz(String metin) {
        System.out.println(metin);
    }

    @Override
    public String oku(String istem) {
        System.out.print(istem + ": ");
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException ex) {
            return "0";
        }
    }
}

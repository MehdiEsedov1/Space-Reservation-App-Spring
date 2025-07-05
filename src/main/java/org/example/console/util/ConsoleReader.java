package org.example.console.util;

import org.example.exception.InvalidInputException;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public final class ConsoleReader {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleReader() {
    }

    public static String readLine() {
        return SCANNER.nextLine();
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

    public static int readInt() throws InvalidInputException {
        try {
            int value = SCANNER.nextInt();
            SCANNER.nextLine();
            return value;
        } catch (InputMismatchException e) {
            SCANNER.nextLine();
            throw new InvalidInputException("Please enter a valid integer number (e.g., 42).");
        }
    }

    public static int readInt(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        return readInt();
    }

    public static BigDecimal readBigDecimal() throws InvalidInputException {
        try {
            BigDecimal value = SCANNER.nextBigDecimal();
            SCANNER.nextLine();
            return value;
        } catch (Exception e) {
            SCANNER.nextLine();
            throw new InvalidInputException("Please enter a valid decimal number (e.g., 19.99).");
        }
    }

    public static BigDecimal readBigDecimal(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        return readBigDecimal();
    }
}

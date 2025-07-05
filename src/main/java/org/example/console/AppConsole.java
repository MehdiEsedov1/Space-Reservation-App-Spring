package org.example.console;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AppConsole {

    private static final Scanner SCANNER = new Scanner(System.in);

    private final UserConsole userConsole;
    private final AdminConsole adminConsole;

    public AppConsole(UserConsole userConsole, AdminConsole adminConsole) {
        this.userConsole = userConsole;
        this.adminConsole = adminConsole;
    }

    public void mainMenu() {
        System.out.println("\n== Welcome to the SPACE RESERVATION SYSTEM ==");

        boolean running = true;
        while (running) {
            printMainMenu();
            String option = SCANNER.nextLine().trim();

            switch (option) {
                case "1" -> adminConsole.menu();
                case "2" -> userConsole.menu();
                case "0" -> running = false;
                default -> System.out.println("Invalid selection. Please choose a valid option from the menu.");
            }
        }

        System.out.println("\nThank you for using the system. Goodbye!");
    }

    private void printMainMenu() {
        System.out.print("""
                \nPlease log in:
                1 - Admin access
                2 - User access

                0 - Exit the application

                > """);
    }
}

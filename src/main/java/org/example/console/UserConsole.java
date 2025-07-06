package org.example.console;

import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.exception.DatetimeParseException;
import org.example.exception.InvalidInputException;
import org.example.exception.InvalidTimeIntervalException;
import org.example.service.ReservationService;
import org.example.service.WorkspaceService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static org.example.console.util.ConsoleReader.readInt;
import static org.example.console.util.ConsoleReader.readLine;

@Component
public class UserConsole {

    private final WorkspaceConsole workspaceConsole;
    private final ReservationConsole reservationConsole;
    private final IntervalConsole intervalConsole;
    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public UserConsole(
            WorkspaceConsole workspaceConsole,
            ReservationConsole reservationConsole,
            IntervalConsole intervalConsole,
            WorkspaceService workspaceService,
            ReservationService reservationService) {
        this.workspaceConsole = workspaceConsole;
        this.reservationConsole = reservationConsole;
        this.intervalConsole = intervalConsole;
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    public void menu() {
        System.out.println("\n== Welcome to the USER CONSOLE ==");

        boolean active = true;
        while (active) {
            String option = readLine("""
                    
                    Please select an option:
                    1 - View available workspaces
                    2 - Make a reservation
                    3 - View my reservations
                    4 - Cancel a reservation

                    0 - Return to main menu

                    > """);

            switch (option) {
                case "1" -> workspaceConsole.listAvailableWorkspaces();
                case "2" -> makeReservation();
                case "3" -> viewMyReservations();
                case "4" -> cancelReservation();
                case "0" -> active = false;
                default -> System.out.println("Invalid selection. Please choose a valid option from the menu.");
            }
        }
    }

    private void makeReservation() {
        System.out.println("\n== MAKE A RESERVATION ==\n");

        Interval interval = getValidInterval();
        if (interval == null) return;

        if (interval.getStartTime().before(new Date())) {
            System.out.println("Reservation cannot be made for a past time.");
            return;
        }

        List<Workspace> available = workspaceService.getAvailableWorkspaces(interval);
        if (available.isEmpty()) {
            System.out.println("There are no available workspaces for the selected interval.");
            return;
        }

        workspaceConsole.printWorkspaces(available);

        int spaceId = readIdWithPrompt("Enter the ID of the workspace you want to reserve (0 - Cancel): ");
        if (spaceId == 0) return;

        String name = readLine("Enter a name for your reservation: ");
        reservationService.makeReservation(name, spaceId, interval);

        System.out.println("Your reservation was created successfully.");
    }

    private void viewMyReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("You currently have no reservations.");
            return;
        }

        System.out.println("\n== MY RESERVATIONS ==\n");
        reservationConsole.printReservations(reservations);
    }

    private void cancelReservation() {
        System.out.println("\n== CANCEL A RESERVATION ==\n");

        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("There are no reservations to cancel.");
            return;
        }

        reservationConsole.printReservations(reservations);

        int id = readIdWithPrompt("Enter the ID of the reservation to cancel (0 - Cancel): ");
        if (id == 0) return;

        boolean cancelled = reservationService.cancelReservation(id);
        if (cancelled) {
            System.out.println("The reservation was cancelled successfully.");
        } else {
            System.out.println("No reservation found with the provided ID.");
        }
    }

    private Interval getValidInterval() {
        try {
            return intervalConsole.getInterval();
        } catch (InvalidTimeIntervalException | DatetimeParseException e) {
            System.out.println("Failed to create interval: " + e.getMessage());
            return null;
        }
    }

    private int readIdWithPrompt(String prompt) {
        try {
            return readInt(prompt);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}

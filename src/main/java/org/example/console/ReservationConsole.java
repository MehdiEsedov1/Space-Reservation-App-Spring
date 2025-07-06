package org.example.console;

import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.service.ReservationService;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@Component
public class ReservationConsole {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);
    private static final String HEADER_FORMAT = "%3s: %-30s %-30s %-20s %-20s\n";

    private final ReservationService reservationService;

    public ReservationConsole(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void listReservations() {
        System.out.println("\n== LIST OF ALL RESERVATIONS ==\n");
        List<Reservation> reservations = reservationService.getAllReservations();
        printReservations(reservations);
    }

    public void printReservations(List<Reservation> reservations) {
        List<Reservation> validReservations = reservations.stream()
                .filter(Objects::nonNull)
                .toList();

        if (validReservations.isEmpty()) {
            System.out.println("There are currently no reservations to display.");
            return;
        }

        System.out.printf(HEADER_FORMAT, "ID", "Name", "Workspace", "Start Time", "End Time");

        for (Reservation reservation : validReservations) {
            Workspace workspace = reservation.getWorkspace();
            String workspaceName = workspace != null ? workspace.getName() : "(not assigned)";

            String startTime = DATE_FORMAT.format(reservation.getInterval().getStartTime());
            String endTime = DATE_FORMAT.format(reservation.getInterval().getEndTime());

            System.out.printf(HEADER_FORMAT,
                    reservation.getId(),
                    reservation.getName(),
                    workspaceName,
                    startTime,
                    endTime
            );
        }
    }
}

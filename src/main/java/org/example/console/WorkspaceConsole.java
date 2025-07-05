package org.example.console;

import org.example.entity.Interval;
import org.example.entity.Workspace;
import org.example.exception.DatetimeParseException;
import org.example.exception.InvalidTimeIntervalException;
import org.example.service.WorkspaceService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class WorkspaceConsole {

    private static final String HEADER_FORMAT = "%3s: %-30s %-10s %s\n";
    private static final String ROW_FORMAT = "%3d: %-30s %-10s %.2f\n";

    private final WorkspaceService workspaceService;
    private final IntervalConsole intervalConsole;

    public WorkspaceConsole(WorkspaceService workspaceService, IntervalConsole intervalConsole) {
        this.workspaceService = workspaceService;
        this.intervalConsole = intervalConsole;
    }

    public void listWorkspaces() {
        System.out.println("\n== LIST OF ALL WORKSPACES ==\n");
        List<Workspace> allWorkspaces = workspaceService.getAllWorkspaces();
        printWorkspaces(allWorkspaces);
    }

    public void listAvailableWorkspaces() {
        System.out.println("\n== AVAILABLE WORKSPACES FOR SPECIFIED INTERVAL ==\n");
        System.out.println("Please enter the date and time interval to check availability:");

        Interval interval = getValidInterval();
        if (interval == null) return;

        List<Workspace> availableWorkspaces = workspaceService.getAvailableWorkspaces(interval);
        printWorkspaces(availableWorkspaces);
    }

    public void printWorkspaces(List<Workspace> workspaces) {
        List<Workspace> validWorkspaces = workspaces.stream()
                .filter(Objects::nonNull)
                .toList();

        if (validWorkspaces.isEmpty()) {
            System.out.println("No workspaces found for the selected criteria.");
            return;
        }

        System.out.printf(HEADER_FORMAT, "ID", "Name", "Type", "Price");

        for (Workspace workspace : validWorkspaces) {
            System.out.printf(ROW_FORMAT,
                    workspace.getId(),
                    workspace.getName(),
                    workspace.getType(),
                    workspace.getPrice()
            );
        }
    }

    private Interval getValidInterval() {
        try {
            return intervalConsole.getInterval();
        } catch (InvalidTimeIntervalException | DatetimeParseException e) {
            System.out.println("Failed to parse interval: " + e.getMessage());
            return null;
        }
    }
}

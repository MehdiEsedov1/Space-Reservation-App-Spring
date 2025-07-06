package org.example.console;

import org.example.entity.Workspace;
import org.example.enums.WorkspaceType;
import org.example.exception.InvalidInputException;
import org.example.exception.NotFoundException;
import org.example.exception.WorkspaceIsReservedException;
import org.example.exception.WorkspaceSaveFailed;
import org.example.service.WorkspaceService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static org.example.console.util.ConsoleReader.*;

@Component
public class AdminConsole {

    private final WorkspaceService workspaceService;
    private final WorkspaceConsole workspaceConsole;
    private final ReservationConsole reservationConsole;

    public AdminConsole(WorkspaceService workspaceService, WorkspaceConsole workspaceConsole, ReservationConsole reservationConsole) {
        this.workspaceService = workspaceService;
        this.workspaceConsole = workspaceConsole;
        this.reservationConsole = reservationConsole;
    }

    public void menu() {
        System.out.println("\n== Welcome to the ADMIN CONSOLE ==");

        boolean active = true;
        while (active) {
            String option = readLine("""
                    \nPlease select an option:
                    1 - Create a new workspace
                    2 - Edit a workspace
                    3 - Delete a workspace
                    4 - List all workspaces
                    5 - List all available workspaces
                    6 - List all reservations

                    0 - Back

                    > """);

            switch (option) {
                case "1" -> createWorkspace();
                case "2" -> editWorkspace();
                case "3" -> deleteWorkspace();
                case "4" -> workspaceConsole.listWorkspaces();
                case "5" -> workspaceConsole.listAvailableWorkspaces();
                case "6" -> reservationConsole.listReservations();
                case "0" -> active = false;
                default -> System.out.println("Invalid option. Please select a number from the menu.");
            }
        }
    }

    private void createWorkspace() {
        System.out.println("\n== Create a new workspace ==\n");

        String name = readLine("Enter workspace name: ");
        WorkspaceType type = readWorkspaceType();
        if (type == null) return;

        BigDecimal price = readPrice();
        if (price == null) return;

        Workspace workspace = new Workspace(name, type, price);
        int id = workspaceService.createWorkspace(workspace);
        System.out.println("Workspace was created successfully.\nAssigned ID: " + id);
    }

    private void editWorkspace() {
        System.out.println("\n== Edit a workspace ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("There are no workspaces to edit.");
            return;
        }
        workspaceConsole.printWorkspaces(workspaces);

        int id = readWorkspaceId("Enter workspace ID to edit (0 - Cancel): ");
        if (id == 0) return;

        Workspace workspace;
        try {
            workspace = workspaceService.getWorkspaceById(id);
        } catch (NotFoundException e) {
            System.out.println("No workspace found with the given ID.");
            return;
        }

        updateWorkspaceFields(workspace);

        try {
            workspaceService.editWorkspace(id, workspace);
            System.out.println("Workspace updated successfully. ID: " + id);
        } catch (WorkspaceSaveFailed e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteWorkspace() {
        System.out.println("\n== Delete a workspace ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("There are no workspaces to delete.");
            return;
        }
        workspaceConsole.printWorkspaces(workspaces);

        int id = readWorkspaceId("Enter workspace ID to delete (0 - Cancel): ");
        if (id == 0) return;

        try {
            boolean deleted = workspaceService.deleteWorkspace(id);
            System.out.println(deleted
                    ? "Workspace was deleted successfully."
                    : "No workspace found with ID: " + id);
        } catch (WorkspaceIsReservedException e) {
            System.out.println(e.getMessage());
        }
    }

    private WorkspaceType readWorkspaceType() {
        try {
            int typeNo = readInt("Enter workspace type (1 - OPEN; 2 - PRIVATE; 3 - ROOM): ");
            return getTypeFromNumber(typeNo);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private BigDecimal readPrice() {
        try {
            return readBigDecimal("Enter workspace price: ");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private int readWorkspaceId(String prompt) {
        try {
            return readInt(prompt);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    private void updateWorkspaceFields(Workspace workspace) {
        String nameInput = readLine("Enter new workspace name [" + workspace.getName() + "] (Enter to keep the same): ");
        if (!nameInput.isBlank()) {
            workspace.setName(nameInput);
        }

        String typeInput = readLine("Enter new workspace type [" + workspace.getType() + "]\n(1 - OPEN; 2 - PRIVATE; 3 - ROOM; Enter to keep the same): ");
        if (!typeInput.isBlank()) {
            try {
                WorkspaceType newType = getTypeFromNumber(Integer.parseInt(typeInput));
                if (newType == null) {
                    System.out.println("Invalid workspace type. Please enter 1, 2, or 3.");
                } else {
                    workspace.setType(newType);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Type must be a number: 1, 2, or 3.");
            }
        }

        String priceInput = readLine("Enter new workspace price [" + workspace.getPrice() + "] (Enter to keep the same): ");
        if (!priceInput.isBlank()) {
            try {
                workspace.setPrice(new BigDecimal(priceInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Please enter a valid number (e.g., 29.99).");
            }
        }
    }

    private WorkspaceType getTypeFromNumber(int typeNo) {
        return switch (typeNo) {
            case 1 -> WorkspaceType.OPEN;
            case 2 -> WorkspaceType.PRIVATE;
            case 3 -> WorkspaceType.ROOM;
            default -> {
                System.out.println("Invalid workspace type number: " + typeNo + ". Please enter 1, 2, or 3.");
                yield null;
            }
        };
    }
}

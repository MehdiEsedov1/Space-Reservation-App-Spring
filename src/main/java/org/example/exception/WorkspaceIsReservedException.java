package org.example.exception;

public class WorkspaceIsReservedException extends RuntimeException {
    public WorkspaceIsReservedException(String message) {
        super(message);
    }
}

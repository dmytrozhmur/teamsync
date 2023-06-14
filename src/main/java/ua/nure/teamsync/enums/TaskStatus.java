package ua.nure.teamsync.enums;

public enum TaskStatus {
    READY("Ready to start"),
    STARTED("In process"),
    DONE("Completed");

    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

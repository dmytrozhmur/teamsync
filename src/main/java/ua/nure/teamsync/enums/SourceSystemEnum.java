package ua.nure.teamsync.enums;

public enum SourceSystemEnum {
    TEAMS_SERVICE("ts_admin-panel"),
    TEAM_BOARD("ts_board"),
    TASKS_ORGANIZER("ts_organizer"),
    CONDITIONS_CHECKER("ts_observer");

    private String name;

    SourceSystemEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

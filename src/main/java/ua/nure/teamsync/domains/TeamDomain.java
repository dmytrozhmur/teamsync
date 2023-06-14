package ua.nure.teamsync.domains;

import lombok.Data;
import ua.nure.teamsync.entities.Performer;
import ua.nure.teamsync.entities.PerformerId;
import ua.nure.teamsync.entities.TaskId;
import ua.nure.teamsync.entities.udts.PerformerUDT;
import ua.nure.teamsync.entities.udts.TaskUDT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TeamDomain {
    private PerformerId performerId;
    private String login;
    private String name;
    private String phoneNumber;
    private Performer.Condition healthCondition = Performer.Condition.NORMAL;
    private Performer.Condition environmentCondition = Performer.Condition.NORMAL;
    private boolean managerAccount;
    private LocalDateTime registeredAt = LocalDateTime.now();
    private List<TaskUDT> tasks;
    private UUID teamId;
    private List<PerformerUDT> members;
    private String teamName;
    private String passPhrase;
    private LocalDate createdAt = LocalDate.now();
    private TaskId taskId;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createTime = LocalDateTime.now();
    private String actionType;
}

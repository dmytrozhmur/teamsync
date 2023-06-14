package ua.nure.teamsync.payload;

import org.springframework.data.rest.core.config.Projection;
import ua.nure.teamsync.entities.Performer;
import ua.nure.teamsync.entities.PerformerId;
import ua.nure.teamsync.entities.udts.TaskUDT;

import java.time.LocalDateTime;
import java.util.List;

@Projection(name = "performer-view", types = Performer.class)
public interface PerformerView {
    PerformerId getPerformerId();
    String getName();
    String getPhoneNumber();
    Performer.Condition getHealthCondition();
    Performer.Condition getEnvironmentCondition();
    LocalDateTime getRegisteredAt();
    List<TaskUDT> getTasks();

//    void setPerformerId();
//    void setName();
//    void setPhoneNumber();
//    void setHealthCondition();
//    void setEnvironmentCondition();
//    void setRegisteredAt();
//    void setTasks();
}

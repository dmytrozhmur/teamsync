package ua.nure.teamsync.entities.udts;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@UserDefinedType("task")
@NoArgsConstructor
public class TaskUDT {
    private Long taskNum;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createTime;

    public TaskUDT(Long taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskUDT taskUDT = (TaskUDT) o;
        return Objects.equals(taskNum, taskUDT.taskNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskNum);
    }
}

package ua.nure.teamsync.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Data
@Table("tasks")
public class Task {
    @PrimaryKey
    private TaskId taskId = new TaskId();
    private String title;
    private String description;
    private String status;
    private LocalDateTime createTime = LocalDateTime.now();

    public void setPerformerId(PerformerId performerId) {
        taskId.setPerformerLogin(performerId.getLogin());
    }

    public void setTaskNum(Long num) {
        taskId.setTaskNum(num);
    }
}

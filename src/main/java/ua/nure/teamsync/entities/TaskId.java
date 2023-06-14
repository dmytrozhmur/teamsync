package ua.nure.teamsync.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Data
@PrimaryKeyClass
@NoArgsConstructor
@AllArgsConstructor
public class TaskId implements Serializable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String performerLogin;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private Long taskNum = new Random().nextLong();

    @Override
    public String toString() {
        return String.format("%s_%s",
                URLEncoder.encode(performerLogin, StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(taskNum), StandardCharsets.UTF_8));
    }
}

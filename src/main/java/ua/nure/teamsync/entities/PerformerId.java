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
import java.util.UUID;

@Data
@PrimaryKeyClass
@NoArgsConstructor
@AllArgsConstructor
public class PerformerId implements Serializable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID teamId;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    //private UUID memberId = UUID.randomUUID();
    private String login;

    @Override
    public String toString() {
        return String.format("%s_%s",
                URLEncoder.encode(teamId.toString(), StandardCharsets.UTF_8),
                //URLEncoder.encode(memberId.toString(), StandardCharsets.UTF_8),
                URLEncoder.encode(login, StandardCharsets.UTF_8));
    }
}

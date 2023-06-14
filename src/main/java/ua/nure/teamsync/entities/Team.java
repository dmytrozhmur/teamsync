package ua.nure.teamsync.entities;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.nure.teamsync.entities.udts.PerformerUDT;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table("teams")
public class Team {
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PrimaryKey
    private UUID teamId = Uuids.timeBased();
    private String teamName;
    private String passPhrase;
    private LocalDate createdAt = LocalDate.now();
    @Column("team_members")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UDT, userTypeName = "team_member")
    private List<PerformerUDT> members = new ArrayList<>();

    public void setPassPhrase(String passPhrase) {
        if (passPhrase == null) return;
        this.passPhrase = passwordEncoder.encode(passPhrase);
    }
}

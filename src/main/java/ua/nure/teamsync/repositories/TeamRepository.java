package ua.nure.teamsync.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ua.nure.teamsync.entities.Team;
import ua.nure.teamsync.entities.udts.PerformerUDT;

import java.util.UUID;

public interface TeamRepository extends CrudRepository<Team, UUID> {
    Team findTeamByTeamName(String teamName);
    @RestResource(exported = false)
    Team findTeamByMembersContaining(PerformerUDT performer);
}

package ua.nure.teamsync.repositories;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ua.nure.teamsync.entities.Performer;
import ua.nure.teamsync.entities.PerformerId;
import ua.nure.teamsync.payload.PerformerView;
import ua.nure.teamsync.entities.udts.TaskUDT;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(
        path = "team_member",
        excerptProjection = PerformerView.class)
public interface PerformerRepository extends CrudRepository<Performer, PerformerId> {
//    @RestResource(exported = false)
//    Collection<Performer> findBy();
    @AllowFiltering
    @RestResource(path = "performersByTeamId", rel = "performers")
    List<PerformerView> findAllByPerformerIdTeamId(UUID teamId);
    @AllowFiltering
    @RestResource(exported = false)
    List<Performer> findAllByPerformerIdLogin(String login);
    @AllowFiltering
    @RestResource(exported = false)
    List<Performer> findAllByHealthCondition(String healthCondition);
    @AllowFiltering
    @RestResource(exported = false)
    Performer findPerformerByTasksContaining(TaskUDT task);
    boolean existsNotByPerformerIdTeamIdAndPerformerIdLogin(UUID teamId, String login);
}

package ua.nure.teamsync.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ua.nure.teamsync.entities.Task;
import ua.nure.teamsync.entities.TaskId;

import java.util.List;

@RepositoryRestResource(
        path = "performer_tasks"
)
public interface TaskRepository extends CrudRepository<Task, TaskId> {
    @RestResource(path = "tasksByPerformerLogin", rel = "tasks")
    List<Task> findAllByTaskIdPerformerLogin(String login);
    @RestResource(exported = false)
    Integer countAllByTaskIdPerformerLogin(String login);
    boolean existsNotByTaskIdPerformerLoginAndTaskIdTaskNum(String login, Long num);
}

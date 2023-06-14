package ua.nure.teamsync.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ua.nure.teamsync.domains.TeamDomain;
import ua.nure.teamsync.entities.Performer;
import ua.nure.teamsync.entities.Task;
import ua.nure.teamsync.repositories.PerformerRepository;
import ua.nure.teamsync.repositories.TaskRepository;

import java.util.List;

@Service
public class HelpTaskService implements IService<TeamDomain> {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PerformerRepository performerRepository;

    @Override
    public void service(TeamDomain domain) {
        Performer helper = defineHelper();
        Task taskEntity = populateTaskEntity(helper, domain);
        taskRepository.save(taskEntity);
    }

    private Task populateTaskEntity(Performer helper, TeamDomain teamDomain) {
        Task taskEntity = new Task();
        taskEntity.setPerformerId(helper.getPerformerId());
        taskEntity.setTitle("To help teammate");
        taskEntity.setDescription(String.format("Comrade %s is in trouble, help him!\nFor communication: %s",
                teamDomain.getName(), teamDomain.getPhoneNumber()));
        taskEntity.setTaskNum(taskRepository.count() + 1);
        return taskEntity;
    }

    private Performer defineHelper() {
        List<Performer> possibleHelpers = performerRepository
                .findAllByHealthCondition(Performer.Condition.GOOD.name());
        if (CollectionUtils.isEmpty(possibleHelpers)) {
            possibleHelpers.addAll(performerRepository
                    .findAllByHealthCondition(Performer.Condition.NORMAL.name()));
        }
        return possibleHelpers.stream().min(this::comparePerformersByTaskNum).orElseThrow();
    }

    private int comparePerformersByTaskNum(Performer performer1, Performer performer2) {
        int size1 = taskRepository.countAllByTaskIdPerformerLogin(performer1.getPerformerId().getLogin());
        int size2 = taskRepository.countAllByTaskIdPerformerLogin(performer2.getPerformerId().getLogin());
        return size1 < size2 ? 1 : -1;
    }
}

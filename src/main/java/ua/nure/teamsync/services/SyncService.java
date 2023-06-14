package ua.nure.teamsync.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ua.nure.teamsync.entities.*;
import ua.nure.teamsync.enums.SourceSystemEnum;
import ua.nure.teamsync.payload.PerformerView;
import ua.nure.teamsync.repositories.PerformerRepository;
import ua.nure.teamsync.repositories.TaskRepository;
import ua.nure.teamsync.repositories.TeamRepository;
import ua.nure.teamsync.entities.udts.PerformerUDT;
import ua.nure.teamsync.entities.udts.TaskUDT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SyncService implements IService<String> {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PerformerRepository performerRepository;
    @Autowired
    private TaskRepository taskRepository;

    public void service(String source) throws IllegalAccessException {
        if (SourceSystemEnum.TEAMS_SERVICE.getName().equalsIgnoreCase(source)) {
            for (Team team : teamRepository.findAll()) {
                handlePerformersUpdate(team);
            }

//            for (Performer entity : performerEntities) {
//                TeamDomain performerDomain = new TeamDomain();
//                PerformerUDT performerUDT = new PerformerUDT(entity.getPerformerId().getLogin());
//                Team performerTeam = teamRepository.findTeamByMembersContaining(performerUDT);
//                if (performerTeam != null) {
//                    performerUDT = performerTeam.getMembers().stream()
//                            .filter(udt -> entity.getPerformerId().getLogin().equals(udt.getLogin()))
//                            .findFirst().orElse(new PerformerUDT());
//                    BeanUtils.copyProperties(entity, performerUDT);
//                    teamRepository.save(performerTeam);
//                } else {
//                    performerDomain.setTeamId(entity.getPerformerId().getTeamId());
//                    performerDomain.setLogin(entity.getPerformerId().getLogin());
//                    performerDomain.setActionType("DELETE");
//                    teamRepository.save(p)
//                }
//                domains.add(performerDomain);
//            }
        } else if (SourceSystemEnum.TEAM_BOARD.getName().equalsIgnoreCase(source)) {
            for (Team team : teamRepository.findAll()) {
                handleTeamUpdate(team);
//                TeamDomain teamDomain;
//                PerformerUDT performerUDT = new PerformerUDT(team.getPerformerId().getLogin());
//                Team team = teamRepository.findTeamByMembersContaining(performerUDT);
//                performerUDT = team.getMembers().stream()
//                        .filter(member -> team.getPerformerId().getLogin().equals(member.getLogin()))
//                        .findFirst()
//                        .orElseThrow();
//                BeanUtils.copyProperties(team, performerUDT);
//                BeanUtils.copyProperties(team, domain);
//                domains.add(domain);
            }
            for (Performer performer : performerRepository.findAll()) {
                handleTasksUpdate(performer);
            }
        } else if (SourceSystemEnum.TASKS_ORGANIZER.getName().equalsIgnoreCase(source)) {
            for (Performer performer : performerRepository.findAll()) {
                handleUpdatePerformer(performer);

//            List<Task> taskEntities = taskRepository.findAll();
//            List<TeamDomain> taskDomains = new ArrayList<>();
//            for (Task entity : taskEntities) {
//                TeamDomain domain = new TeamDomain();
//                TaskUDT taskUDT = new TaskUDT(entity.getTaskId().getTaskNum());
//                Performer performer = performerRepository.findPerformerByTasksContaining(taskUDT);
//                taskUDT = performer.getTasks().stream()
//                        .filter(task -> entity.getTaskId().getTaskNum().equals(task.getTaskNum()))
//                        .findFirst().orElseThrow();
//                BeanUtils.copyProperties(entity, taskUDT);
//                PerformerUDT performerUDT = new PerformerUDT();
//                BeanUtils.copyProperties(performer, performerUDT);
//                domain.getMembers().add(performerUDT);
            }
//            handlePerformersUpdate(taskDomains);
        } else {
            throw new IllegalAccessException();
        }
    }

    private void handleUpdatePerformer(Performer performer) {
        for (TaskUDT taskUDT : List
                .copyOf(Optional.ofNullable(performer.getTasks()).orElse(new ArrayList<>()))) {
            if (taskRepository
                    .existsNotByTaskIdPerformerLoginAndTaskIdTaskNum(performer.getPerformerId().getLogin(), taskUDT.getTaskNum())) {
                performer.getTasks().remove(taskUDT);
            }
        }
        for (Task task : taskRepository
                .findAllByTaskIdPerformerLogin(performer.getPerformerId().getLogin())) {
            TaskUDT oldTask = Optional.ofNullable(performer.getTasks()).orElse(new ArrayList<>()).stream()
                    .filter(udt -> task.getTaskId().getTaskNum().equals(udt.getTaskNum()))
                    .findFirst()
                    .orElse(new TaskUDT());
            if(oldTask.getTaskNum() == null) {
                performer.getTasks().add(oldTask);
            }
            BeanUtils.copyProperties(task, oldTask);
            oldTask.setTaskNum(task.getTaskId().getTaskNum());
        }
        performerRepository.save(performer);
    }

    private void handleTasksUpdate(Performer performer) {
        for (Task task : taskRepository
                .findAllByTaskIdPerformerLogin(performer.getPerformerId().getLogin())) {
            if (!performer.getTasks().contains(new TaskUDT(task.getTaskId().getTaskNum()))) {
                taskRepository.delete(task);
            }
        }
        for (TaskUDT taskUDT : Optional.ofNullable(performer.getTasks()).orElse(new ArrayList<>())) {
            Task task = new Task();
            BeanUtils.copyProperties(taskUDT, task);
            task.setTaskId(new TaskId(performer.getPerformerId().getLogin(), taskUDT.getTaskNum()));
            taskRepository.save(task);
        }
    }

    private void handleTeamUpdate(Team team) {
        for (PerformerUDT performerUDT : List
                .copyOf(Optional.ofNullable(team.getMembers()).orElse(new ArrayList<>()))) {
            if (performerRepository
                    .existsNotByPerformerIdTeamIdAndPerformerIdLogin(team.getTeamId(), performerUDT.getLogin())) {
                team.getMembers().remove(performerUDT);
            }
        }
        for (PerformerView performerView :
                performerRepository.findAllByPerformerIdTeamId(team.getTeamId())) {
            PerformerUDT oldPerformer = team.getMembers().stream()
                .filter(member -> performerView.getPerformerId().getLogin().equals(member.getLogin()))
                .findFirst()
                .orElse(new PerformerUDT());
            if (oldPerformer.getLogin() == null) {
                team.getMembers().add(oldPerformer);
            }
            BeanUtils.copyProperties(performerView, oldPerformer);
            oldPerformer.setLogin(performerView.getPerformerId().getLogin());
            log.info("Updated performer: " + oldPerformer);
        }
        teamRepository.save(team);
    }

    private void handlePerformersUpdate(Team team) {
        for (PerformerView performerView : performerRepository
                .findAllByPerformerIdTeamId(team.getTeamId())) {
            if (!team.getMembers().contains(new PerformerUDT(performerView.getPerformerId().getLogin()))) {
                performerRepository.deleteById(performerView.getPerformerId());
            }
        }
        for (PerformerUDT performerUDT : Optional.ofNullable(team.getMembers()).orElse(new ArrayList<>())) {
            Performer performer = new Performer();
            BeanUtils.copyProperties(performerUDT, performer);
            performer.setPerformerId(new PerformerId(team.getTeamId(), performerUDT.getLogin()));
            performerRepository.save(performer);
        }
    }

//    private void handleTasksUpdate(List<TeamDomain> domainList) {
//        for (TeamDomain domain : domainList) {
//            for (Performer performer : domain.getMembers()) {
//                for (TaskUDT task : performer.getTasks()) {
//                    Task taskEntity = new Task();
//                    taskEntity.setTaskId(new TaskId(performer.getPerformerId().getLogin(), task.getTaskNum()));
//                    taskEntity.setTitle(task.getTitle());
//                    taskEntity.setDescription(task.getDescription());
//                    taskEntity.setStatus(task.getStatus());
//                    taskEntity.setCreateTime(task.getCreateTime());
//                    taskRepository.save(taskEntity);
//                }
//            }
//        }
//    }
//
//    private void handlePerformersUpdate(List<TeamDomain> domainList) {
//        for (TeamDomain domain : domainList) {
//            Performer performerEntity = new Performer();
//            performerEntity.setPerformerId(domain.getPerformerId());
//            performerEntity.setName(domain.getName());
//            performerEntity.setTasks(domain.getTasks());
//            performerEntity.setPhoneNumber(domain.getPhoneNumber());
//            performerEntity.setManagerAccount(domain.isManagerAccount());
//            performerEntity.setRegisteredAt(domain.getRegisteredAt());
//            performerEntity.setHealthCondition(domain.getHealthCondition().name());
//            performerEntity.setEnvironmentCondition(domain.getEnvironmentCondition().name());
//            performerRepository.save(performerEntity);
//
//        }
//    }
//
//    private void handleTeamsUpdate(List<TeamDomain> domainList) {
//        for (TeamDomain domain : domainList) {
//            Team teamEntity = new Team();
//            teamEntity.setTeamId(domain.getTeamId());
//            teamEntity.setPassPhrase(domain.getPassPhrase());
//            teamEntity.setTeamName(domain.getTeamName());
//            teamEntity.setCreatedAt(domain.getCreatedAt());
//            teamEntity.setMembers(domain.getMembers());
//            teamRepository.save(teamEntity);
//        }
//    }
}

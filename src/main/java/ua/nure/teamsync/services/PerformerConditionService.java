package ua.nure.teamsync.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.teamsync.domains.TeamDomain;
import ua.nure.teamsync.entities.Performer;
import ua.nure.teamsync.repositories.PerformerRepository;

@Service
public class PerformerConditionService implements IService<TeamDomain> {
    @Autowired
    private PerformerRepository performerRepository;
    @Autowired
    private HelpTaskService helpTaskService;

    @Override
    public void service(TeamDomain performerDomain) {
        Performer performerEntity = populatePerformerEntity(performerDomain);
        if (performerEntity.isHelpNeeded()) {
            performerDomain.setName(performerEntity.getName());
            performerDomain.setPhoneNumber(performerEntity.getPhoneNumber());
        }
        performerRepository.save(performerEntity);
        helpTaskService.service(performerDomain);
    }

    private Performer populatePerformerEntity(TeamDomain performerDomain) {
        Performer performerEntity = performerRepository
                .findAllByPerformerIdLogin(performerDomain.getLogin()).stream()
                .findFirst().orElseThrow();

        if (performerDomain.getHealthCondition() != null) {
            performerEntity.setHealthCondition(performerDomain.getHealthCondition().name());
        }
        if (performerDomain.getEnvironmentCondition() != null) {
            performerEntity.setEnvironmentCondition(performerDomain.getEnvironmentCondition().name());
        }
        return performerEntity;
    }
}

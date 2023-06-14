package ua.nure.teamsync.converters;

import org.springframework.stereotype.Component;
import ua.nure.teamsync.domains.TeamDomain;
import ua.nure.teamsync.entities.Performer;
import ua.nure.teamsync.payload.PerformerRequest;
import ua.nure.teamsync.payload.UpdateConditionsRequest;

import java.util.UUID;

@Component
public class PerformerTransformer implements ITransformer<PerformerRequest> {
    @Override
    public TeamDomain transform(PerformerRequest performerRequest) {
        return null;
    }

    public TeamDomain transformConditions(UpdateConditionsRequest conditionsRequest) {
        TeamDomain teamDomain = new TeamDomain();
        teamDomain.setLogin(conditionsRequest.getPerformerId());
        teamDomain.setHealthCondition(Performer.Condition
                .valueOf(conditionsRequest.getHealthCondition()));
        teamDomain.setEnvironmentCondition(Performer.Condition
                .valueOf(conditionsRequest.getEnvironmentCondition()));
        return teamDomain;
    }
}

package ua.nure.teamsync.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.teamsync.domains.TeamDomain;
import ua.nure.teamsync.payload.UpdateConditionsRequest;
import ua.nure.teamsync.services.PerformerConditionService;
import ua.nure.teamsync.converters.PerformerTransformer;

@RestController("/team_members")
public class PerformerController {
    @Autowired
    private PerformerConditionService performerConditionService;
    @Autowired
    private PerformerTransformer performerTransformer;


    @PostMapping(path = "/{performerId}/conditions/update")
    private ResponseEntity<Object> conditionsChanged(@RequestBody UpdateConditionsRequest updateConditionsRequest,
                                             @PathVariable String performerId) {
        updateConditionsRequest.setPerformerId(performerId);
        TeamDomain teamDomain = performerTransformer.transformConditions(updateConditionsRequest);
        performerConditionService.service(teamDomain);
        return ResponseEntity.ok().build();
    }
}

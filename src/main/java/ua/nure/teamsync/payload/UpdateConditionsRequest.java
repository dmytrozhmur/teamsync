package ua.nure.teamsync.payload;

import lombok.Data;

@Data
public class UpdateConditionsRequest {
    private String performerId;
    private String healthCondition;
    private String environmentCondition;
}

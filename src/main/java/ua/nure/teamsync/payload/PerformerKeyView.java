package ua.nure.teamsync.payload;

import org.springframework.data.rest.core.config.Projection;
import ua.nure.teamsync.entities.Performer;

import java.util.UUID;

@Projection(name = "performer-key-view", types = Performer.class)
public interface PerformerKeyView {
    UUID getTeamId();
    UUID getMemberId();
}

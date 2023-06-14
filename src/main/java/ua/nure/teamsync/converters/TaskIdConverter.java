package ua.nure.teamsync.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.nure.teamsync.entities.PerformerId;
import ua.nure.teamsync.entities.Task;
import ua.nure.teamsync.entities.TaskId;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class TaskIdConverter implements Converter<String, TaskId> {

    @Override
    public TaskId convert(String source) {
        String[] idParts = source.split("_");
        return new TaskId(
                URLDecoder.decode(idParts[0], StandardCharsets.UTF_8),
                Long.parseLong(idParts[1]));
    }
}
package ua.nure.teamsync.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.nure.teamsync.entities.PerformerId;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class PerformerIdConverter implements Converter<String, PerformerId> {

    @Override
    public PerformerId convert(String source) {
        String[] idParts = source.split("_");
        return new PerformerId(
                UUID.fromString(URLDecoder.decode(idParts[0], StandardCharsets.UTF_8)),
                //UUID.fromString(URLDecoder.decode(idParts[1], StandardCharsets.UTF_8)),
                idParts[1]);
    }
}

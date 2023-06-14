package ua.nure.teamsync.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import ua.nure.teamsync.converters.PerformerIdConverter;
import ua.nure.teamsync.converters.TaskIdConverter;

@Configuration
public class TeamRepositoriesRestConfiguration implements RepositoryRestConfigurer {
    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {
        RepositoryRestConfigurer.super.configureConversionService(conversionService);
        conversionService.addConverter(new PerformerIdConverter());
        conversionService.addConverter(new TaskIdConverter());
    }
}

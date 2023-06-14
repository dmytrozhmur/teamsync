package ua.nure.teamsync.converters;

import ua.nure.teamsync.domains.TeamDomain;

public interface ITransformer<T> {
    TeamDomain transform(T t);
}

package ua.nure.teamsync.services;

import ua.nure.teamsync.domains.TeamDomain;

public interface IService<T> {
    void service(T domain) throws Exception;
}

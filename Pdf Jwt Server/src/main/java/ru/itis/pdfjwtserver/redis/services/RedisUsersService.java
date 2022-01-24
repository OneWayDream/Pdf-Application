package ru.itis.pdfjwtserver.redis.services;

import ru.itis.pdfjwtserver.models.DataAccessUser;

public interface RedisUsersService {

    void addTokenToUser(DataAccessUser user, String token);

    void addAllTokensToBlackList(DataAccessUser user);

}

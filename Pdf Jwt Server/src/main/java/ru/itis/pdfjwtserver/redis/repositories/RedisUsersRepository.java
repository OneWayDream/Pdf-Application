package ru.itis.pdfjwtserver.redis.repositories;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.itis.pdfjwtserver.redis.models.RedisUser;

public interface RedisUsersRepository extends KeyValueRepository<RedisUser, String> {

}

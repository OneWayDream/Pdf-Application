package ru.itis.pdfgenerator.redis.repositories;

public interface BlacklistRepository {

    void save(String token);

    boolean exists(String token);

}

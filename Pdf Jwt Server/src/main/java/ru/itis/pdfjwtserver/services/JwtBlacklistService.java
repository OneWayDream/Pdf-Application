package ru.itis.pdfjwtserver.services;

public interface JwtBlacklistService {

    void add(String token);

    boolean exists(String token);

}
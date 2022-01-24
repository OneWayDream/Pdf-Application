package ru.itis.pdfjwtserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "data_access_user", indexes = {
        @Index(name = "id_index", columnList = "id"),
        @Index(name = "login_id", columnList = "login")
})
public class DataAccessUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String login;

    protected String hashPassword;

    @Enumerated(value = EnumType.STRING)
    protected State state;

    @Enumerated(value = EnumType.STRING)
    protected Role role;

    protected String redisId;

    public enum State {
        ACTIVE, BANNED;

    }

    public enum Role {
        USER, ADMIN, MODER
    }

}

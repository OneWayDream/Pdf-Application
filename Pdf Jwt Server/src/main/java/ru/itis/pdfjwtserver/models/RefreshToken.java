package ru.itis.pdfjwtserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "refresh_data_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String token;

    @Transient
    protected Date expiredTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected DataAccessUser user;

}


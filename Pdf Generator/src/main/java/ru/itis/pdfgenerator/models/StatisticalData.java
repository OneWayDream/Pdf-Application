package ru.itis.pdfgenerator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statistics")
public class StatisticalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Long requestsAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(unique = true)
    protected PatternType patternType;

    protected Boolean isDeleted;

    public enum PatternType {
        JAVA_LAB, LINUX
    }
}

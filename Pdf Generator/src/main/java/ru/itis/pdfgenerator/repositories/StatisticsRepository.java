package ru.itis.pdfgenerator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.pdfgenerator.models.StatisticalData;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<StatisticalData, Long> {

    Optional<StatisticalData> findByPatternTypeAndIsDeletedIsNull(StatisticalData.PatternType patternType);

    List<StatisticalData> findAllByIsDeletedIsNull();

    Optional<StatisticalData> findByIdAndIsDeletedIsNull(Long aLong);

}

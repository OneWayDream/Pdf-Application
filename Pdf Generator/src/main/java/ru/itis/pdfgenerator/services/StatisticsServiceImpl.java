package ru.itis.pdfgenerator.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.itis.pdfgenerator.exceptions.EntityNotExistsException;
import ru.itis.pdfgenerator.exceptions.EntityNotFoundException;
import ru.itis.pdfgenerator.models.StatisticalData;
import ru.itis.pdfgenerator.repositories.StatisticsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    protected final StatisticsRepository statisticsRepository;

    @Override
    public List<StatisticalData> findAll() {
        return statisticsRepository.findAllByIsDeletedIsNull();
    }

    @Override
    public void delete(StatisticalData statisticalData) {
        StatisticalData statisticalDataForDelete = statisticsRepository.findById(statisticalData.getId())
                .filter(data -> data.getIsDeleted()==null)
                .orElseThrow(() -> new EntityNotExistsException("Statistics data for pattern "
                        + statisticalData.getPatternType().toString() + " is not exists."));
        statisticalDataForDelete.setIsDeleted(true);
        statisticsRepository.save(statisticalDataForDelete);
    }

    @Override
    public StatisticalData add(StatisticalData statisticalData) {
        statisticalData.setIsDeleted(null);
        return statisticsRepository.save(statisticalData);
    }

    @Override
    public StatisticalData findById(Long aLong) {
        return statisticsRepository.findByIdAndIsDeletedIsNull(aLong).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public StatisticalData update(StatisticalData statisticalData) {
        StatisticalData statisticalDataForUpdate = statisticsRepository.findById(statisticalData.getId())
                .filter(data -> data.getIsDeleted()==null)
                .orElseThrow(() -> new EntityNotExistsException("Statistics data for pattern "
                        + statisticalData.getPatternType().toString() + " is not exists."));
        return statisticsRepository.save(statisticalDataForUpdate);
    }

    @Override
    public StatisticalData getStatisticsByPattern(StatisticalData.PatternType patternType) {
        return statisticsRepository.findByPatternTypeAndIsDeletedIsNull(patternType).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void increaseCounterForPattern(StatisticalData.PatternType patternType, Long num) {
        StatisticalData statisticalDataForIncrease = statisticsRepository.findByPatternTypeAndIsDeletedIsNull(patternType)
                .filter(data -> data.getIsDeleted()==null)
                .orElse(StatisticalData.builder()
                        .requestsAmount(0L)
                        .patternType(patternType)
                        .build());
        statisticalDataForIncrease.setRequestsAmount(statisticalDataForIncrease.getRequestsAmount() + num);
        statisticsRepository.save(statisticalDataForIncrease);
    }
}

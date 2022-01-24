package ru.itis.pdfgenerator.services;

import ru.itis.pdfgenerator.models.StatisticalData;

public interface StatisticsService extends CrudService<StatisticalData, Long>  {

    StatisticalData getStatisticsByPattern (StatisticalData.PatternType patternType);

    void increaseCounterForPattern (StatisticalData.PatternType patternType, Long num);

}

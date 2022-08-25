package ru.magnit.magreportbackend.service.domain.metricsfilter;

import ru.magnit.magreportbackend.dto.inner.olap.MetricResult;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterDefinition;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterGroup;


public class MetricFilters {

    public static MetricFilterNode createFilter(MetricResult[][][] metricResults, MetricFilterGroup filterGroup){
        if(filterGroup == null) return new EmptyFilter();
        return new MetricResultFilterGroup().init(metricResults,filterGroup);
    }

    public static MetricFilterNode createFilter(MetricResult[][][] metricResults, MetricFilterDefinition filterDefinition) {
       if(filterDefinition == null) return new EmptyFilter();

       return switch (filterDefinition.getFilterType()){
           case EMPTY -> new EmptyFilter();
           case EQUALS -> new MetricEqualsFilter().init(metricResults,filterDefinition);
           case GREATER -> new MetricGreaterFilter().init(metricResults, filterDefinition);
           case LESSER -> new MetricLesserFilter().init(metricResults, filterDefinition);
           case GREATER_OR_EQUALS -> new MetricGreaterOrEqualsFilter().init(metricResults, filterDefinition);
           case LESSER_OR_EQUALS -> new MetricLesserOrEqualsFilter().init(metricResults, filterDefinition);
           case BETWEEN -> new MetricBetweenFilter().init(metricResults, filterDefinition);
       };
    }

}

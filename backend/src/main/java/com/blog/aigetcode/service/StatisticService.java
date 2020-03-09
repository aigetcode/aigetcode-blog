package com.blog.aigetcode.service;

import com.blog.aigetcode.DTO.StatisticDto;

public interface StatisticService {

    StatisticDto getTotalVisitorsByMonth();
    void addVisitor();

}

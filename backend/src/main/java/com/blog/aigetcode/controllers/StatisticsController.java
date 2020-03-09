package com.blog.aigetcode.controllers;


import com.blog.aigetcode.DTO.StatisticDto;
import com.blog.aigetcode.entity.Authority;
import com.blog.aigetcode.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private StatisticService statisticService;

    @Autowired
    public StatisticsController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Secured({Authority.Role.Code.USER, Authority.Role.Code.ADMIN})
    @GetMapping
    public StatisticDto getStatisticData() {
        return statisticService.getTotalVisitorsByMonth();
    }

    @GetMapping("/visit")
    public void addVisitor() {
        statisticService.addVisitor();
    }

}

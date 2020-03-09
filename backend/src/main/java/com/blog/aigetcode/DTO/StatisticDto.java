package com.blog.aigetcode.DTO;

import java.util.List;
import java.util.Map;

public class StatisticDto {

    private Long totalVisitors;
    private Long totalCountPosts;
    private Map<String, List<Long>> statisticByMonth;

    public Long getTotalVisitors() {
        return totalVisitors;
    }

    public void setTotalVisitors(Long totalVisitors) {
        this.totalVisitors = totalVisitors;
    }

    public Long getTotalCountPosts() {
        return totalCountPosts;
    }

    public void setTotalCountPosts(Long totalCountPosts) {
        this.totalCountPosts = totalCountPosts;
    }

    public Map<String, List<Long>> getStatisticByMonth() {
        return statisticByMonth;
    }

    public void setStatisticByMonth(Map<String, List<Long>> statisticByMonth) {
        this.statisticByMonth = statisticByMonth;
    }
}

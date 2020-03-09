package com.blog.aigetcode.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "statistics")
public class StatisticsAmountUsersByMonth extends SuperEntity {

    private Long amount;
    private AmountUsersMonth month;
    private Long createAt;

    public StatisticsAmountUsersByMonth() {
        createAt = (new Date()).getTime();
    }

    public StatisticsAmountUsersByMonth(Long amount, AmountUsersMonth month) {
        this.amount = amount;
        this.month = month;
        createAt = (new Date()).getTime();
    }

    public enum AmountUsersMonth {
        JANUARY(0),
        FEBRUARY(1),
        MARCH(2),
        APRIL(3),
        MAY(4),
        JUNE(5),
        JULY(6),
        AUGUST(7),
        SEPTEMBER(8),
        OCTOBER(9),
        NOVEMBER(10),
        DECEMBER(11),
        TOTAL(12);

        private Integer value;
        private static final Map<Integer, AmountUsersMonth> BY_LABEL = new HashMap<>();

        static {
            for (AmountUsersMonth e: values()) {
                BY_LABEL.put(e.value, e);
            }
        }

        public static AmountUsersMonth valueOfLabel(Integer label) {
            return BY_LABEL.get(label);
        }

        public Integer getValue() {
            return value;
        }

        AmountUsersMonth(Integer value) {
            this.value = value;
        }
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public AmountUsersMonth getMonth() {
        return month;
    }

    public void setMonth(AmountUsersMonth month) {
        this.month = month;
    }

    public Long getCreateAt() {
        return createAt;
    }
}

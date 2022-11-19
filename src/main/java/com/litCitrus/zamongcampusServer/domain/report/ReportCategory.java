package com.litCitrus.zamongcampusServer.domain.report;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.litCitrus.zamongcampusServer.exception.report.ReportCategoryNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReportCategory {
    REPORTCATEGORY0001(""),
    REPORTCATEGORY0002("프로필"),
    REPORTCATEGORY0003("피드"),
    REPORTCATEGORY0004("피드댓글"),
    REPORTCATEGORY0005("채팅");

    private final String name;

    ReportCategory(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}

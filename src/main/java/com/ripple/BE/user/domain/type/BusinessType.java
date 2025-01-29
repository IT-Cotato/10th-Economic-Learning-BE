package com.ripple.BE.user.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum BusinessType {
    FINANCE_INSURANCE("금융/보험"),
    IT_SOFTWARE("IT/소프트웨어"),
    ELECTRONICS_SEMICONDUCTOR("전자/반도체"),
    MANUFACTURING("제조업"),
    CONSTRUCTION_REAL_ESTATE("건설/부동산"),
    HEALTHCARE_PHARMACEUTICALS("의료/제약"),
    EDUCATION_PUBLISHING("교육/출판"),
    RETAIL_LOGISTICS("유통/물류"),
    ENERGY_ENVIRONMENT("에너지/환경"),
    AGRICULTURE_LIVESTOCK("농업/축산업"),
    MEDIA_ADVERTISING("미디어/광고"),
    TRAVEL_TOURISM("여행/관광"),
    GOVERNMENT_NPO("공공기관/비영리단체"),
    STARTUP_VENTURE("스타트업/벤처"),
    OTHER("기타");

    private final String description;

    BusinessType(String description) {
        this.description = description;
    }

    @JsonValue // JSON 응답 시 한글 반환
    public String getDescription() {
        return description;
    }

    @JsonCreator // JSON 요청 시 한글 값을 Enum으로 변환
    public static BusinessType from(String value) {
        return Arrays.stream(BusinessType.values())
                .filter(businessType -> businessType.description.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업종: " + value));
    }
}

package com.ripple.BE.user.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Job {
    // 전문직
    DOCTOR_NURSE_HEALTHCARE("의사/간호사/보건의료"),
    LAWYER_LEGAL("변호사/법무사"),
    ACCOUNTANT_TAX_CONSULTANT("회계사/세무사"),
    TEACHER_INSTRUCTOR("교사/강사"),
    RESEARCHER_PROFESSOR("연구원/교수"),

    // 기업/사무직
    PLANNING_STRATEGY("기획/전략"),
    MARKETING_ADVERTISING_PR("마케팅/광고/홍보"),
    SALES_BUSINESS_DEVELOPMENT("영업/판매"),
    FINANCE_ACCOUNTING("재무/회계"),
    HR_TRAINING("인사/교육"),
    CUSTOMER_SERVICE_SUPPORT("고객 서비스/상담"),
    LEGAL_AUDIT("법무/감사"),

    // IT/기술
    DEVELOPER_FRONTEND_BACKEND("개발자(프론트엔드/백엔드)"),
    DATA_ANALYST_ENGINEER("데이터 분석/엔지니어"),
    UI_UX_DESIGNER("UI/UX 디자이너"),
    NETWORK_SECURITY_EXPERT("네트워크/보안 전문가"),
    IT_MANAGER("IT 관리자"),
    CONSTRUCTION_CIVIL_ENGINEERING("건설/토목/설계"),
    RESEARCH_DEVELOPMENT("연구개발(R&D)"),

    // 서비스/창업
    FOOD_SERVICE_CHEF("외식업/요리사"),
    TRAVEL_PLANNER("관광/여행 플래너"),
    STARTUP_ENTREPRENEUR("창업/스타트업 운영자"),
    FREELANCER("프리랜서"),

    // 기타
    PUBLIC_ADMINISTRATION_OFFICIAL("공공/행정직 공무원"),
    MILITARY_POLICE_FIRE_OFFICER("군인/경찰/소방관"),
    ENTERTAINER_ARTIST("엔터테이너/예술가"),
    OTHER("기타");

    private final String description;

    Job(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static Job from(String value) {
        return Arrays.stream(Job.values())
                .filter(job -> job.description.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직무: " + value));
    }
}

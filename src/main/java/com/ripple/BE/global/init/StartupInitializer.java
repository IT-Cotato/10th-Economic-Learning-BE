package com.ripple.BE.global.init;

import com.ripple.BE.learning.application.learningset.LearningAdminService;
import com.ripple.BE.post.application.impl.toktok.ToktokAdminService;
import com.ripple.BE.term.application.TermAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class StartupInitializer implements ApplicationRunner {

    private final LearningAdminService learningAdminService;
    private final ToktokAdminService toktokAdminService;
    private final TermAdminService termAdminService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("[Startup] 엑셀 기반 초기 데이터 생성 시작");

            learningAdminService.createLearningSetByExcel();
            toktokAdminService.createToktokByExcel();
            termAdminService.createTermByExcel();

            log.info("[Startup] 초기 데이터 생성 완료");
        } catch (Exception e) {
            log.error("[Startup] 초기 데이터 생성 실패");
        }
    }
}

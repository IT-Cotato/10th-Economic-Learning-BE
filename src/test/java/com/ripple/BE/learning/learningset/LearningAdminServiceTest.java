package com.ripple.BE.learning.learningset;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import com.ripple.BE.learning.application.learningset.LearningAdminService;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.persistence.LearningSetRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
import groovy.util.logging.Slf4j;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LearningAdminServiceTest {

    private static final Logger log = LoggerFactory.getLogger(LearningAdminServiceTest.class);

    @Autowired private LearningAdminService learningAdminService;

    @Autowired private UserRepository userRepository;

    @Autowired private LearningSetRepository learningSetRepository;

    @Autowired private UserLearningSetRepository userLearningSetRepository;

    private List<LearningSet> learningSetList;

    @BeforeEach
    void setUp() {
        User user1 =
                User.basicBuilder().accountEmail("user1@test.com").password("pw12345678@").buildBasicUser();
        user1.updateNickname("user1");

        User user2 =
                User.basicBuilder().accountEmail("user2@test.com").password("pw12345678@").buildBasicUser();
        user2.updateNickname("user2");

        userRepository.saveAll(List.of(user1, user2));

        LearningSet set1 = LearningSet.withoutId("세트1");
        LearningSet set2 = LearningSet.withoutId("세트2");
        learningSetList = learningSetRepository.saveAll(List.of(set1, set2));
    }

    @Test
    void addUserLearningSetsForNewLearningSets_shouldGenerateAllUserLevelCombinations() {
        // when
        learningAdminService.addUserLearningSetsForNewLearningSets(learningSetList);

        // then
        List<UserLearningSet> all = userLearningSetRepository.findAll();
        log.info("All UserLearningSets: {}", all);

        // 2 users × 2 sets × 3 levels = 12
        assertThat(all).hasSize(12);
        assertThat(all)
                .allMatch(
                        uls ->
                                uls.getLearningSetName().equals("세트1") || uls.getLearningSetName().equals("세트2"));
    }
}

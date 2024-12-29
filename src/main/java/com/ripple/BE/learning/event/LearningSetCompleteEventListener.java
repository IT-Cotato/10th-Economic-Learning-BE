package com.ripple.BE.learning.event;

import com.ripple.BE.learning.domain.LearningSet;
import com.ripple.BE.learning.domain.LearningSetComplete;
import com.ripple.BE.learning.repository.LearningSetRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LearningSetCompleteEventListener {

    private final LearningSetRepository learningSetRepository;
    private final UserRepository userRepository;

    /**
     * 사용자 추가 시 학습 세트 추가 이벤트 핸들러
     *
     * @param user
     */
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLearningSetCompleteEvent(User user) {
        List<LearningSet> learningSetList = learningSetRepository.findAll();

        List<LearningSetComplete> learningSetCompletes =
                learningSetList.stream()
                        .map(learningSet -> LearningSetComplete.toLearningSetComplete(user, learningSet))
                        .toList();

        user.getLearningSetCompleteList().addAll(learningSetCompletes);

        userRepository.save(user);
    }

    /**
     * 학습 세트 추가 시 사용자에게 학습 세트 배포 이벤트 핸들러
     *
     * @param learningSetList
     */
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLearningSetCompleteEvent(List<LearningSet> learningSetList) {
        List<User> userList = userRepository.findAllWithLock();

        List<LearningSetComplete> learningSetCompletes =
                learningSetList.stream()
                        .flatMap(
                                learningSet ->
                                        userList.stream()
                                                .map(user -> LearningSetComplete.toLearningSetComplete(user, learningSet)))
                        .toList();

        userList.forEach(user -> user.getLearningSetCompleteList().addAll(learningSetCompletes));
    }
}

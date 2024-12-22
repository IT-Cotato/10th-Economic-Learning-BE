package com.ripple.BE.learning.service;

import com.ripple.BE.learning.dto.AddLevelTestQuizRequest;
import com.ripple.BE.learning.dto.LevelTestQuizResponse;
import com.ripple.BE.learning.repository.QuizRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
@Slf4j
public class LevelTestService {

    private final QuizRepository quizRepository;

    @Transactional
    public void addLevelTestQuiz(AddLevelTestQuizRequest request) {
        quizRepository.save(request.toEntity());
    }

    public List<LevelTestQuizResponse> getLevelTestQuizList() {
        List<LevelTestQuizResponse> list =
                quizRepository.findAll().stream().map(LevelTestQuizResponse::from).toList();

        // 추후 학습, 용어 사전 데이터 추가 후 4지선다 답안 랜덤으로 넣는 로직 추가
        return list;
    }
}

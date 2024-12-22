package com.ripple.BE.learning.service;

import com.ripple.BE.learning.dto.request.AddLevelTestQuizRequest;
import com.ripple.BE.learning.dto.response.LevelTestQuizListResponse;
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

    public List<LevelTestQuizListResponse> getLevelTestQuizList() {
        List<LevelTestQuizListResponse> list =
                quizRepository.findAll().stream().map(LevelTestQuizListResponse::from).toList();

        // 추후 학습, 용어 사전 데이터 추가 후 4지선다 답안 랜덤으로 넣는 로직 추가
        return list;
    }
}

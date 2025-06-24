package com.ripple.BE.user.service;

import static com.ripple.BE.user.domain.User.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.auth.dto.kakao.KakaoUserInfoResponse;
import com.ripple.BE.chatbot.repository.ChatbotRepository;
import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.persistence.ImageRepository;
import com.ripple.BE.image.persistence.jpa.entity.ImageJpaEntity;
import com.ripple.BE.learning.persistence.ConceptScrapRepository;
import com.ripple.BE.learning.persistence.FailQuizRepository;
import com.ripple.BE.learning.persistence.QuizScrapRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.news.persistence.NewsScrapRepository;
import com.ripple.BE.notification.persistence.NotificationRepository;
import com.ripple.BE.post.application.impl.common.CommentCommandService;
import com.ripple.BE.post.application.impl.post.PostCommandService;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.term.persistence.TermScrapRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.UserGoal;
import com.ripple.BE.user.domain.type.LoginType;
import com.ripple.BE.user.dto.UserGoalDTO;
import com.ripple.BE.user.dto.UserInfoDTO;
import com.ripple.BE.user.dto.request.PatchUserProfileRequest;
import com.ripple.BE.user.dto.request.UpdateUserProfileRequest;
import com.ripple.BE.user.dto.request.UserGoalRequest;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.AttendanceLogRepository;
import com.ripple.BE.user.repository.AttendanceRepository;
import com.ripple.BE.user.repository.QuestRepository;
import com.ripple.BE.user.repository.UserGoalRepository;
import com.ripple.BE.user.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final UserGoalRepository userGoalRepository;
    private final AttendanceLogRepository attendanceLogRepository;
    private final AttendanceRepository attendanceRepository;
    private final QuestRepository questRepository;
    private final ChatbotRepository chatbotRepository;
    private final NotificationRepository notificationRepository;
    private final NewsScrapRepository newsScrapRepository;
    private final TermScrapRepository termScrapRepository;

    private final ConceptScrapRepository conceptScrapRepository;
    private final UserLearningSetRepository userLearningSetRepository;
    private final QuizScrapRepository quizScrapRepository;
    private final FailQuizRepository failQuizRepository;
    private final CommentRepository commentRepository;

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostScrapRepository postScrapRepository;

    private final AttendanceService attendanceService;
    private final PostCommandService postCommandService;
    private final CommentCommandService commentCommandService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long findOrCreateUser(KakaoUserInfoResponse response) {
        User user =
                userRepository
                        .findByKeyCode(response.id().toString())
                        .orElse(
                                User.kakaoBuilder()
                                        .accountEmail(response.kakao_account().email())
                                        .loginType(LoginType.KAKAO)
                                        .keyCode(response.id().toString())
                                        .buildKakaoUser());

        userRepository.save(user);

        return user.getId();
    }

    @Transactional
    public void updateProfile(UpdateUserProfileRequest request, Long userId) {
        User user = findUserById(userId);
        if (userRepository.existsByNickname(request.nickname())) { // 닉네임 중복 확인
            throw new UserException(DUPLICATED_NICKNAME);
        }

        Image image = null;
        if (request.imageId() != null) {
            image =
                    imageRepository
                            .findById(request.imageId())
                            .orElseThrow(() -> new UserException(IMAGE_NOT_FOUND));
        }

        if (image != null) {
            user.updateProfileImage(ImageJpaEntity.from(image)); // 추후 수정 필요
        }
        user.updateProfile(request);
        attendanceService.createAttendance(user);
    }

    @Transactional(readOnly = true)
    public User findUser(String accountEmail) {
        return userRepository
                .findByAccountEmail(accountEmail)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Transactional
    public void createUser(String accountEmail, String password) {
        // 이미 존재하는 이메일인지 확인
        userRepository
                .findByAccountEmail(accountEmail)
                .ifPresent(
                        user -> {
                            throw new UserException(INVALID_EMAIL);
                        });

        userRepository.save(
                basicBuilder()
                        .accountEmail(accountEmail)
                        .password(passwordEncoder.encode(password))
                        .buildBasicUser());
    }

    @Transactional
    public void updateAlarm(final boolean alarm, final long userId) {

        User user = findUserById(userId);
        user.setCoummunityAlarmAllowed(alarm);
    }

    public UserInfoDTO getUserInfo(final long userId) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        String profileImageURL =
                Optional.ofNullable(user.getProfileImage())
                        .map(image -> image.getS3Info().getUrl())
                        .orElse(null);

        Date birthDate = user.getBirthDate() == null ? null : user.getBirthDate();
        String profileIntro = user.getProfileIntro() == null ? null : user.getProfileIntro();

        String businessType =
                user.getBusinessType() == null ? null : user.getBusinessType().getDescription();
        String job = user.getJob() == null ? null : user.getJob().getDescription();

        Long quizCorrectRate =
                user.getQuizCount() == 0 ? 0L : user.getCorrectCount() * 100L / user.getQuizCount();

        return UserInfoDTO.builder()
                .userId(user.getId())
                .profileImageURL(profileImageURL)
                .nickname(user.getNickname())
                .birthDate(birthDate)
                .profileIntro(profileIntro)
                .businessType(businessType)
                .job(job)
                .currentStreak(attendanceService.getCurrentStreak(userId))
                .level(user.getCurrentLevel())
                .quizCorrectRate(quizCorrectRate)
                .isLevelTestCompleted(user.isLevelTestCompleted())
                .build();
    }

    public UserGoalDTO getUserGoal(final long userId) {
        UserGoal userGoal =
                userGoalRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserException(USER_GOAL_NOT_FOUND));

        return new UserGoalDTO(
                userGoal.getConceptGoal(), userGoal.getQuizGoal(), userGoal.getArticleGoal());
    }

    @Transactional
    public void updateUserGoal(final UserGoalRequest userGoalRequest, final long userId) {
        UserGoal userGoal =
                userGoalRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserException(USER_GOAL_NOT_FOUND));

        userGoal.updateQuizGoal(UserGoalDTO.toUserGoalDTO(userGoalRequest));
    }

    @Transactional
    public void patchUserProfile(
            final PatchUserProfileRequest updateUserProfileRequest, final long userId) {
        User user = findUserById(userId);

        if (updateUserProfileRequest.nickname() != null) {
            if (userRepository.existsByNickname(updateUserProfileRequest.nickname())) {
                throw new UserException(DUPLICATED_NICKNAME);
            }
            user.updateNickname(updateUserProfileRequest.nickname());
        }

        if (updateUserProfileRequest.businessType() != null) {
            user.updateBusinessType(updateUserProfileRequest.businessType());
        }

        if (updateUserProfileRequest.job() != null) {
            user.updateJob(updateUserProfileRequest.job());
        }

        if (updateUserProfileRequest.birthDate() != null) {
            user.updateBirthDate(updateUserProfileRequest.birthDate());
        }

        if (updateUserProfileRequest.gender() != null) {
            user.updateGender(updateUserProfileRequest.gender());
        }

        if (updateUserProfileRequest.profileIntro() != null) {
            user.updateProfileIntro(updateUserProfileRequest.profileIntro());
        }

        if (updateUserProfileRequest.isLearningAlarmAllowed() != null) {
            user.updateLearningAlarmAllowed(updateUserProfileRequest.isLearningAlarmAllowed());
        }

        if (updateUserProfileRequest.isCommunityAlarmAllowed() != null) {
            user.updateCommunityAlarmAllowed(updateUserProfileRequest.isCommunityAlarmAllowed());
        }

        if (updateUserProfileRequest.imageId() != null) {
            Image image =
                    imageRepository
                            .findById(updateUserProfileRequest.imageId())
                            .orElseThrow(() -> new UserException(IMAGE_NOT_FOUND));

            user.updateProfileImage(ImageJpaEntity.from(image)); // 추후 수정 필요
        }
    }

    public void deleteUser(final long userId) {
        User user = findUserById(userId);

        chatbotRepository.deleteAllByUserId(userId);

        // 학습 관련 데이터 삭제
        conceptScrapRepository.deleteAllByUserId(userId);
        userLearningSetRepository.deleteAllByUserId(userId);
        quizScrapRepository.deleteAllByUserId(userId);
        failQuizRepository.deleteAllByUserId(userId);
        newsScrapRepository.deleteAllByUserId(userId);
        termScrapRepository.deleteAllByUserId(userId);

        // 출석 관련 데이터 삭제
        attendanceLogRepository.deleteAllByUserId(userId);
        attendanceRepository.deleteAllByUserId(userId);

        // 유저 관련 데이터 삭제
        userGoalRepository.deleteAllByUserId(userId);
        questRepository.deleteAllByUserId(userId);
        notificationRepository.deleteAllByUserId(userId);
        if (user.getProfileImage() != null) {
            imageRepository.deleteById(user.getProfileImage().getId());
        }

        // 게시글 관련 데이터 삭제
        commentLikeRepository.deleteAllByUserId(userId);
        postLikeRepository.deleteAllByUserId(userId);
        postScrapRepository.deleteAllByUserId(userId);
        List<Comment> comments = commentRepository.findAllByCommenterId(userId);
        for (Comment comment : comments) {
            commentCommandService.removeCommentFromPost(userId, comment.getPostId(), comment.getId());
        }
        postCommandService.deleteAllPostsByUserId(userId);

        // 유저 삭제
        userRepository.delete(user);
    }
}

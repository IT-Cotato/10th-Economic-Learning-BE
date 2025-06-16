package com.ripple.BE.post.application.impl.toktok;

import static com.ripple.BE.post.domain.type.PostType.*;
import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.global.excel.ExcelUtils;
import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.domain.S3Info;
import com.ripple.BE.image.exception.ImageException;
import com.ripple.BE.image.persistence.ImageRepository;
import com.ripple.BE.image.s3.S3Uploader;
import com.ripple.BE.post.application.ToktokAdminUseCase;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.ToktokRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ToktokAdminService implements ToktokAdminUseCase {

    private static final String FILE_PATH = "static/excel/toktok.xlsx";
    private static final String TITLE_COLUMN = "title";
    private static final String IMAGE_COLUMN = "imageUrl";
    private static final String CONTENT_COLUMN = "content";
    private static final String TOKTOK_FOLDER_NAME = "toktok";
    private static final int TOKTOK_SHEET_INDEX = 0;

    private final ToktokRepository toktokRepository;
    private final ImageRepository imageRepository;

    private final S3Uploader s3Uploader;

    @Override
    /** 엑셀을 기반으로 Toktok 게시글을 생성 */
    public void createToktokByExcel() {
        try {
            // 1. 엑셀 파일 파싱
            List<Map<String, String>> excelDataList =
                    ExcelUtils.parseExcelFile(FILE_PATH, TOKTOK_SHEET_INDEX);

            // 2. 이미 존재하는 제목 조회
            Set<String> existingTitles = toktokRepository.findAllTitles();

            // 3. 각 엑셀 행 데이터를 기반으로 게시글 생성
            for (Map<String, String> excelRow : excelDataList) {
                createToktok(excelRow, existingTitles);
            }

        } catch (Exception e) {
            log.error("경제 톡톡 엑셀 파일 저장 실패", e);
            throw new PostException(TOKTOK_SAVE_EXCEL_FILE_FAILED);
        }
    }

    /** 개별 Toktok 게시글을 생성하여 저장 */
    private void createToktok(Map<String, String> excelData, Set<String> existingTitles) {
        String title = excelData.get(TITLE_COLUMN);
        String content = excelData.get(CONTENT_COLUMN);
        String imageUrl = excelData.get(IMAGE_COLUMN);

        if (existingTitles.contains(title)) {
            return; // 기존 게시물은 건너뛰기
        }

        Post toktokPost = Post.withoutId(title, content, null, ECONOMY_TALK, null);

        toktokPost = toktokRepository.save(toktokPost);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            addImageToPost(toktokPost, imageUrl);
        }
    }

    /** 이미지 추가 로직 */
    private void addImageToPost(Post post, String imageUrl) {
        Image image = findOrCreateImage(imageUrl);
        image = image.updatePostId(post.getId());
        imageRepository.save(image);
    }

    /** S3 URL을 기반으로 Image 엔티티 조회 또는 생성 */
    private Image findOrCreateImage(String imageUrl) {
        return imageRepository.findByS3InfoUrl(imageUrl).orElseGet(() -> createNewImage(imageUrl));
    }

    /** 새로운 Image 엔티티 생성 */
    private Image createNewImage(String imageUrl) {
        if (!s3Uploader.isValidS3Url(TOKTOK_FOLDER_NAME, imageUrl)) {
            log.warn("유효하지 않은 S3 URL: {}", imageUrl);
            throw new ImageException(IMAGE_NOT_FOUND);
        }

        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        S3Info s3Info = S3Info.of(TOKTOK_FOLDER_NAME, fileName, imageUrl);

        return Image.withoutId(s3Info, null, null);
    }

    @Override
    // 오늘의 게시물을 저장하는 메서드
    @Scheduled(cron = "0 0 0 * * *") // 매일 00시 00분 00초에 실행
    public void updateTodayToktok() {

        Random random = new Random();

        // 오늘 이전에 선정된 게시글을 제외하고 게시글 목록 조회
        List<Post> unusedPosts = toktokRepository.findNewToktokPosts();

        if (unusedPosts.isEmpty()) {
            throw new PostException(TOKTOK_POST_NOT_FOUND);
        }

        Post selectedPost = unusedPosts.get(random.nextInt(unusedPosts.size()));

        toktokRepository.save(selectedPost.updateUsedDate(LocalDate.now()));
    }
}

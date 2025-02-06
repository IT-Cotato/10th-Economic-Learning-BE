package com.ripple.BE.post.service;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.global.excel.ExcelUtils;
import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.exception.ImageException;
import com.ripple.BE.image.repository.ImageRepository;
import com.ripple.BE.image.s3.S3Uploader;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.ToktokDTO;
import com.ripple.BE.post.repository.post.PostRepository;
import com.ripple.BE.term.exception.TermException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class ToktokAdminService {

    private static final String FILE_PATH = "static/excel/toktok.xlsx";
    private static final String TITLE_COLUMN = "title";
    private static final String IMAGE_COLUMN = "imageId";
    private static final int TOKTOK_SHEET_INDEX = 0;

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createToktokByExcel() {
        try {
            List<Post> newToktokList = parseToktokFromExcel();

            if (!newToktokList.isEmpty()) {
                postRepository.saveAll(newToktokList); // 새로운 게시물만 저장
            }

        } catch (Exception e) {
            log.error("경제 톡톡 엑셀 파일 저장 실패", e);
            throw new TermException(TOKTOK_SAVE_EXCEL_FILE_FAILED);
        }
    }

    private List<Post> parseToktokFromExcel() throws Exception {
        // 기존 '경제 톡톡' 게시물 제목 조회
        Set<String> existingTitles = postRepository.findExistingTitlesByType(PostType.ECONOMY_TALK);

        List<Map<String, String>> excelDataList =
                ExcelUtils.parseExcelFile(FILE_PATH, TOKTOK_SHEET_INDEX);

        return excelDataList.stream()
                .map(
                        excelData -> {
                            String title = excelData.get(TITLE_COLUMN);

                            // 기존 게시물은 무시
                            if (existingTitles.contains(title)) {
                                return null;
                            }

                            // 새로운 게시물 생성
                            Post post = Post.toPostEntity(ToktokDTO.toToktokDTO(excelData));

                            // imageId가 존재하면 이미지 조회 및 추가
                            String imageIdStr = excelData.get(IMAGE_COLUMN);
                            if (imageIdStr != null && !imageIdStr.isEmpty()) {
                                try {
                                    Long imageId = Long.parseLong(imageIdStr);
                                    Image image =
                                            imageRepository
                                                    .findById(imageId)
                                                    .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));

                                    post.addImage(image);
                                } catch (NumberFormatException e) {
                                    log.warn("잘못된 imageId 형식: {}", imageIdStr);
                                }
                            }

                            return post;
                        })
                .filter(Objects::nonNull) // 기존 게시물 제외
                .collect(Collectors.toList());
    }
}

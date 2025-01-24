package com.ripple.BE.term.service;

import static com.ripple.BE.term.exception.errorcode.TermErrorCode.*;

import com.ripple.BE.global.excel.ExcelUtils;
import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.dto.TermDTO;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.term.repository.TermJdbcRepository;
import com.ripple.BE.term.repository.TermRepository;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class TermAdminService {

    private final TermJdbcRepository termJdbcRepository;
    private final TermRepository termRepository;

    private static final char[] CHO_SUNG = {
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final String FILE_PATH = "static/excel/term.xlsx";
    private static final int TERM_SHEET_INDEX = 0;

    private static final int HANGUL_START = 0xAC00;
    private static final int HANGUL_END = 0xD7A3;

    @Transactional
    public void createTermByExcel() {
        try {

            File file = new ClassPathResource(FILE_PATH).getFile();

            List<Term> termList = parseTermFromExcel(file.getPath());
            Set<String> existingTitles =
                    termRepository.findAll().stream().map(Term::getTitle).collect(Collectors.toSet());

            List<Term> newTermList =
                    termList.stream()
                            .filter(term -> !existingTitles.contains(term.getTitle()))
                            .collect(Collectors.toList());

            newTermList.forEach(
                    term -> {
                        String initial = getInitialSound(term.getTitle());
                        term.setInitial(initial);
                    });

            termJdbcRepository.saveAllTermsByJdbcTemplate(newTermList);

        } catch (Exception e) {
            log.error("용어 엑셀 파일 저장 실패", e);
            throw new TermException(SAVE_TERM_EXCEL_FILE_FAILED);
        }
    }

    private List<Term> parseTermFromExcel(String filePath) throws Exception {
        return ExcelUtils.parseExcelFile(filePath, TERM_SHEET_INDEX).stream()
                .map(TermDTO::toTermDTO)
                .map(Term::toTermEntity)
                .collect(Collectors.toList());
    }

    /** 문자열에서 초성 전체 추출 */
    public static String getInitialSound(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (isHangul(ch)) {
                int unicode = ch - HANGUL_START;
                int choSungIndex = unicode / (21 * 28);
                result.append(CHO_SUNG[choSungIndex]);
            } else {
                result.append(ch); // 한글이 아닌 경우 그대로 입력
            }
        }
        return result.toString();
    }

    /** 한글 여부 확인 */
    private static boolean isHangul(char ch) {
        return HANGUL_START <= ch && ch <= HANGUL_END;
    }
}

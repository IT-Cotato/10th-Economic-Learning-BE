package com.ripple.BE.term.application;

import static com.ripple.BE.term.exception.errorcode.TermErrorCode.*;

import com.ripple.BE.global.excel.ExcelUtils;
import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.dto.excel.TermExcelDTO;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.term.persistence.TermRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class TermAdminService {

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
            List<Term> termList = parseTermFromExcel();
            Set<String> existingTitles =
                    termRepository.findAll().stream().map(Term::getTitle).collect(Collectors.toSet());

            List<Term> newTerms =
                    termList.stream()
                            .filter(term -> !existingTitles.contains(term.getTitle()))
                            .map(term -> term.updateInitial(getInitialSound(term.getTitle())))
                            .toList();

            termRepository.saveAllTerms(newTerms);

        } catch (Exception e) {
            log.error("용어 엑셀 파일 저장 실패", e);
            throw new TermException(SAVE_TERM_EXCEL_FILE_FAILED);
        }
    }

    /** 엑셀 파일을 파싱하여 용어 리스트로 변환 */
    private List<Term> parseTermFromExcel() throws Exception {
        return ExcelUtils.parseExcelFile(FILE_PATH, TERM_SHEET_INDEX).stream()
                .map(TermExcelDTO::from)
                .map(dto -> Term.withoutId(dto.title(), dto.description(), "")) // 초기 초성은 빈 문자열
                .collect(Collectors.toList());
    }

    /** 문자열에서 초성 전체 추출 */
    private static String getInitialSound(String text) {
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

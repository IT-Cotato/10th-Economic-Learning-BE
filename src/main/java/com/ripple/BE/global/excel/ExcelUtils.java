package com.ripple.BE.global.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

public class ExcelUtils {

    // 엑셀 파일을 파싱하여 데이터 리스트로 반환하는 메서드
    public static List<Map<String, String>> parseExcelFile(String filePath, int sheetNumber)
            throws Exception {
        List<Map<String, String>> rows = new ArrayList<>();

        // InputStream으로 파일 읽기
        try (InputStream is = getInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();

            // 헤더 추출
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // 각 행의 데이터를 읽어 Map으로 저장
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    rowData.put(headers.get(j), getCellValueAsString(cell));
                }
                rows.add(rowData);
            }
        }
        return rows;
    }

    // filePath로부터 InputStream 생성
    private static InputStream getInputStream(String filePath) throws Exception {
        try {
            // ClassPathResource로 JAR 내부 리소스 파일 처리
            ClassPathResource resource = new ClassPathResource(filePath);
            return resource.getInputStream();
        } catch (Exception e) {
            // 파일이 JAR 내부가 아니라면 FileInputStream으로 처리
            return new FileInputStream(new File(filePath));
        }
    }

    // 셀의 값을 문자열로 변환하는 유틸리티 메서드
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}

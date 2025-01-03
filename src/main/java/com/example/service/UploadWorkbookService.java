package com.example.service;

import com.example.repository.UploadMapper;
import com.example.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class UploadWorkbookService {
    private final UploadMapper uploadMapper;

    public UploadWorkbookService(UploadMapper uploadMapper) {
        this.uploadMapper = uploadMapper;
    }

    public void insertBooks(ArrayList<HashMap<String, Object>> data) {
        uploadMapper.insertBooks(data);
    }

    public Map<String, Object> uploadWorkbook(MultipartFile file) throws IOException, InvalidFormatException {
        Map<String, Object> response = new HashMap<>();

        // stopwatch로 시작 시간 측정
        // ---------------- 시작 ----------------
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // ---------------- 끝 ----------------

        XSSFWorkbook xlsxWorkbook = null;
        HSSFWorkbook xlsWorkbook = null;
        XSSFSheet xlsxSheet = null;
        HSSFSheet xlsSheet = null;
        HashMap<String, Object> sheetData = null;
        int sheetNum = 0;
        int columnRow = 1;      // 컬럼명이 있는 행은 1~2번째 행
        int startDataRow = 2;   // 데이터는 3번째 행부터 시작함

        String fileName = file.getOriginalFilename();
        String fileKind = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".")+1);
        if ( "xls".equals(fileKind) ) {
            xlsWorkbook = new HSSFWorkbook(file.getInputStream()); // HSS(엑셀 97~2003버전)

            // stopwatch로 종료 시간 측정
            // ---------------- 시작 ----------------
            stopWatch.stop();
            log.debug(">>>>>>>>>> [poi-uploadWorkbook][service] new HSSFWorkbook 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
            response.put("[service] new HSSFWorkbook 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
            stopWatch.start();
            // ---------------- 끝 ----------------

            // 시트번호 선택 후 예외처리
            if(xlsWorkbook.getNumberOfSheets() < sheetNum+1) {
                response.put("errorMessage", "isNotExist Sheet(IS_LARGE=false)");
            }

            xlsSheet = xlsWorkbook.getSheetAt(sheetNum);

            // stopwatch로 종료 시간 측정
            // ---------------- 시작 ----------------
            stopWatch.stop();
            log.debug(">>>>>>>>>> [poi-uploadWorkbook][service] xlsWorkbook.getSheetAt 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
            response.put("[service] xlsWorkbook.getSheetAt 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
            stopWatch.start();
            // ---------------- 끝 ----------------

            sheetData = xlsParsing(xlsWorkbook, xlsSheet, columnRow, startDataRow);

            // stopwatch로 종료 시간 측정
            // ---------------- 시작 ----------------
            stopWatch.stop();
            log.debug(">>>>>>>>>> [poi-uploadWorkbook][service] xlsParsing 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
            response.put("[service] xlsParsing 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
            stopWatch.start();
            // ---------------- 끝 ----------------
        }
        else {
//            if(StringUtil.isNotEmpty(inParams.getVariable("IS_LARGE")) && Boolean.valueOf(inParams.getVariableAsString("IS_LARGE"))){ // SaxParser 처리
//                ExcelSheetXMLHandler excelSheetXmlHandler = new ExcelSheetXMLHandler(destFile, columnRow, startRow, listColNames, sheetNum);
//                sheetData = excelSheetXmlHandler.getParsingData();
//            } else{
                xlsxWorkbook = new XSSFWorkbook(file.getInputStream()); // XSSF(엑셀 2007이상 버전)

                // stopwatch로 종료 시간 측정
                // ---------------- 시작 ----------------
                stopWatch.stop();
                log.debug(">>>>>>>>>> [poi-uploadWorkbook][service] new XSSFWorkbook 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
                response.put("[service] new XSSFWorkbook 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
                stopWatch.start();
                // ---------------- 끝 ----------------

                // 시트번호 선택 후 예외처리
                if(xlsxWorkbook.getNumberOfSheets() < sheetNum+1) {
                    response.put("errorMessage", "isNotExist Sheet(IS_LARGE=false)");
                }

                xlsxSheet = xlsxWorkbook.getSheetAt(sheetNum);

                // stopwatch로 종료 시간 측정
                // ---------------- 시작 ----------------
                stopWatch.stop();
                log.debug(">>>>>>>>>> [poi-uploadWorkbook][service] xlsWorkbook.getSheetAt 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
                response.put("[service] xlsWorkbook.getSheetAt 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
                stopWatch.start();
                // ---------------- 끝 ----------------

                sheetData = xlsxParsing(xlsxWorkbook, xlsxSheet, columnRow, startDataRow);

                // stopwatch로 종료 시간 측정
                // ---------------- 시작 ----------------
                stopWatch.stop();
                log.debug(">>>>>>>>>> [poi-uploadWorkbook][service] xlsParsing 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
                response.put("[service] xlsParsing 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
                stopWatch.start();
                // ---------------- 끝 ----------------
//            }
        }
        response.put("sheet1", sheetData);

        //// validation
        // validation: 컬럼명, 컬럼타입, 컬럼길이, null허용여부, PK중복여부
        // ㄴ 컬럼명: 첫행 건너뛰고, 두번째 행(영문명)을 DB컬럼명과 비교 체크 (validation: 컬럼명)
        List<Map<String, Object>> schemaInfo = uploadMapper.getTargetTableSchemaInfo("test", "books");
        // isValid가 false인 경우 if문으로 빠져나가도록 구현
        response.putAll(validateColumnNames(schemaInfo, sheetData));    // 컬럼명
        if (!(Boolean) response.get("isValid")) {
            return response;
        }
        response.putAll(validateDataTypes(schemaInfo, sheetData));      // 컬럼타입
        if (!(Boolean) response.get("isValid")) {
            return response;
        }
        response.putAll(validateColumnLengths(schemaInfo, sheetData));  // 컬럼길이
        if (!(Boolean) response.get("isValid")) {
            return response;
        }
        response.putAll(validateNullability(schemaInfo, sheetData));    // null허용여부
        if (!(Boolean) response.get("isValid")) {
            return response;
        }
        response.putAll(validatePrimaryKeyUniqueness(schemaInfo, sheetData));   // PK중복여부
        if (!(Boolean) response.get("isValid")) {
            return response;
        }

        // DB 저장
        insertBooks((ArrayList<HashMap<String, Object>>) sheetData.get("rowData"));

        // 자원 반납
        if(fileKind.equals("xls") && xlsWorkbook != null) {
            xlsWorkbook.close();
        } else if(xlsxWorkbook != null) {
            xlsxWorkbook.close();
        }

        // stopwatch로 종료 시간 측정
        // ---------------- 시작 ----------------
        stopWatch.stop();
        log.debug(">>>>>>>>>> [poi-uploadWorkbook][service] 엑셀 업로드 총 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
        response.put("[service] 엑셀 업로드 총 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
        // ---------------- 끝 ----------------

        return response;
    }

    /**
     *
     * @description : validation - 컬럼명 체크
     *
     */
    public static Map<String, Object> validateColumnNames(List<Map<String, Object>> schemaInfo, Map<String, Object> sheetData) {
        Map<String, Object> result = new HashMap<>();
        List<String> dbColumnNames = new ArrayList<>();
        for (Map<String, Object> columnInfo : schemaInfo) {
            dbColumnNames.add((String) columnInfo.get("COLUMN_NAME"));
        }

        ArrayList<String> rowData = (ArrayList<String>) sheetData.get("columnName");
        if (rowData.isEmpty()) {
            result.put("isValid", false);
            result.put("errorMessage", "컬럼명이 비어 있습니다.");
            return result;
        }
        List<String> sheetColumnNames = new ArrayList<>(rowData);

        if (dbColumnNames.size() != sheetColumnNames.size()) {
            result.put("isValid", false);
            result.put("errorMessage", "컬럼 개수가 일치하지 않습니다.");
            return result;
        }

        for (int i = 0; i < dbColumnNames.size(); i++) {
            if (!dbColumnNames.get(i).equals(sheetColumnNames.get(i))) {
                result.put("isValid", false);
                result.put("errorMessage", String.format("컬럼명이 일치하지 않습니다. (행: %d, 열: %d, 컬럼: %s)", 2, i + 1, sheetColumnNames.get(i)));
                return result;
            }
        }

        result.put("isValid", true);
        return result;
    }

    /**
     *
     * @description : validation - 데이터 타입 체크
     *
     */
    public static Map<String, Object> validateDataTypes(List<Map<String, Object>> schemaInfo, Map<String, Object> sheetData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> dbColumnTypes = new HashMap<>();
        for (Map<String, Object> columnInfo : schemaInfo) {
            dbColumnTypes.put((String) columnInfo.get("COLUMN_NAME"), (String) columnInfo.get("DATA_TYPE"));
        }

        ArrayList<HashMap<String, Object>> rowData = (ArrayList<HashMap<String, Object>>) sheetData.get("rowData");
        if (rowData.isEmpty()) {
            result.put("isValid", false);
            result.put("errorMessage", "데이터가 비어 있습니다.");
            return result;
        }

        for (int rowIndex = 0; rowIndex < rowData.size(); rowIndex++) {
            HashMap<String, Object> row = rowData.get(rowIndex);
            int columnIndex = 0;
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String columnName = entry.getKey();
                Object value = entry.getValue();
                String expectedType = dbColumnTypes.get(columnName);

                if (expectedType == null || !isTypeMatching(value, expectedType)) {
                    result.put("isValid", false);
                    result.put("errorMessage", String.format("데이터 타입이 일치하지 않습니다. (행: %d, 열: %s, 값: %s, 컬럼: %s, 기대 타입: %s)", rowIndex + 1, columnIndex + 1, value, columnName, expectedType));

                    return result;
                }
                columnIndex++;
            }
        }

        result.put("isValid", true);
        return result;
    }
    private static boolean isTypeMatching(Object value, String expectedType) {
        switch (expectedType.toUpperCase()) {
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
                return value instanceof String;
            case "INT":
            case "INTEGER":
            case "BIGINT":
            case "SMALLINT":
            case "TINYINT":
                return value instanceof Integer;
            case "DECIMAL":
            case "NUMERIC":
            case "FLOAT":
            case "DOUBLE":
                return value instanceof Number;
            case "DATE":
            case "TIME":
            case "TIMESTAMP":
                return value instanceof java.util.Date;
            default:
                return false;
        }
    }

    /**
     *
     * @description : validation - 데이터 길이 체크
     *
     */
    public static Map<String, Object> validateColumnLengths(List<Map<String, Object>> schemaInfo, Map<String, Object> sheetData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> dbColumnLengths = new HashMap<>();
        Map<String, String> dbColumnTypes = new HashMap<>();
        for (Map<String, Object> columnInfo : schemaInfo) {
            dbColumnLengths.put((String) columnInfo.get("COLUMN_NAME"), (Integer) columnInfo.get("CHARACTER_MAXIMUM_LENGTH"));
            dbColumnTypes.put((String) columnInfo.get("COLUMN_NAME"), (String) columnInfo.get("DATA_TYPE"));
        }

        ArrayList<HashMap<String, Object>> rowData = (ArrayList<HashMap<String, Object>>) sheetData.get("rowData");
        if (rowData.isEmpty()) {
            result.put("isValid", false);
            result.put("errorMessage", "데이터가 비어 있습니다.");
            return result;
        }

        for (int rowIndex = 0; rowIndex < rowData.size(); rowIndex++) {
            HashMap<String, Object> row = rowData.get(rowIndex);
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String columnName = entry.getKey();
                Object value = entry.getValue();
                Integer maxLength = dbColumnLengths.get(columnName);
                String dataType = dbColumnTypes.get(columnName);

                if (maxLength == null) {
                    continue;
                }

                if (dataType != null && (dataType.equalsIgnoreCase("varchar") || dataType.equalsIgnoreCase("char") || dataType.equalsIgnoreCase("text"))) {
                    if (value instanceof String && ((String) value).length() > maxLength) {
                        result.put("isValid", false);
                        result.put("errorMessage", String.format("컬럼 길이가 초과되었습니다. (행: %d, 컬럼: %s, 값: %s)", rowIndex + 1, columnName, value));
                        return result;
                    }
                }
            }
        }

        result.put("isValid", true);
        return result;
    }

    /**
     *
     * @description : validation - NULL여부 체크
     *
     */
    public static Map<String, Object> validateNullability(List<Map<String, Object>> schemaInfo, Map<String, Object> sheetData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Boolean> dbColumnNullability = new HashMap<>();
        for (Map<String, Object> columnInfo : schemaInfo) {
            dbColumnNullability.put((String) columnInfo.get("COLUMN_NAME"), "YES".equals(columnInfo.get("IS_NULLABLE")));
        }

        ArrayList<HashMap<String, Object>> rowData = (ArrayList<HashMap<String, Object>>) sheetData.get("rowData");
        if (rowData.isEmpty()) {
            result.put("isValid", false);
            result.put("errorMessage", "데이터가 비어 있습니다.");
            return result;
        }

        for (int rowIndex = 0; rowIndex < rowData.size(); rowIndex++) {
            HashMap<String, Object> row = rowData.get(rowIndex);
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String columnName = entry.getKey();
                Object value = entry.getValue();
                Boolean isNullable = dbColumnNullability.get(columnName);

                if (isNullable == null) {
                    continue;
                }

                if (!isNullable && value == null) {
                    result.put("isValid", false);
                    result.put("errorMessage", String.format("NULL 값을 허용하지 않는 컬럼에 NULL 값이 있습니다. (행: %d, 컬럼: %s)", rowIndex + 1, columnName));
                    return result;
                }
            }
        }

        result.put("isValid", true);
        return result;
    }

    /**
     *
     * @description : validation - PK중복여부 체크
     *
     */
    public static Map<String, Object> validatePrimaryKeyUniqueness(List<Map<String, Object>> schemaInfo, Map<String, Object> sheetData) {
        Map<String, Object> result = new HashMap<>();
        List<String> primaryKeyColumns = new ArrayList<>();
        for (Map<String, Object> columnInfo : schemaInfo) {
            if ("PRI".equals(columnInfo.get("COLUMN_KEY"))) {
                primaryKeyColumns.add((String) columnInfo.get("COLUMN_NAME"));
            }
        }

        ArrayList<HashMap<String, Object>> rowData = (ArrayList<HashMap<String, Object>>) sheetData.get("rowData");
        if (rowData.isEmpty()) {
            result.put("isValid", false);
            result.put("errorMessage", "데이터가 비어 있습니다.");
            return result;
        }

        Set<String> primaryKeySet = new HashSet<>();

        for (int rowIndex = 0; rowIndex < rowData.size(); rowIndex++) {
            HashMap<String, Object> row = rowData.get(rowIndex);
            StringBuilder primaryKeyValue = new StringBuilder();
            for (String pkColumn : primaryKeyColumns) {
                primaryKeyValue.append(row.get(pkColumn)).append("|");
            }

            if (!primaryKeySet.add(primaryKeyValue.toString())) {
                result.put("isValid", false);
                result.put("errorMessage", String.format("중복된 PK 값이 있습니다. (행: %d, PK 값: %s)", rowIndex + 1, primaryKeyValue.toString()));
                return result;
            }
        }

        result.put("isValid", true);
        return result;
    }


    /**
     *
     * @description : xlsx 파싱
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static HashMap<String, Object> xlsxParsing(XSSFWorkbook xlsxWorkbook, XSSFSheet xlsxSheet, int columnRow, int startDataRow) {
        FormulaEvaluator evaluator = xlsxWorkbook.getCreationHelper().createFormulaEvaluator();
        String data = null;
        HashMap<String, Object> allData = new HashMap<String, Object>();
        ArrayList<ArrayList> allCellData = new ArrayList<ArrayList>(); // Cell데이터 리스트 목록
        ArrayList headerNames_data = null;
        ArrayList dbColumnId_data = null; // DB컬럼명 리스트
        ArrayList cellDatas_data = null; // Cell 데이터 리스트
        int rows = xlsxSheet.getPhysicalNumberOfRows(); // 엑셀 전체 rows

        for(int rownum = 0; rownum < rows; rownum++) {
            XSSFRow xlsxRow = xlsxSheet.getRow(rownum);

            if(rownum == -1) { // 헤더 정보가 있는 경우
                headerNames_data = new ArrayList();
            }
//            else if(columnRow == NOT_USE_COLROW_VAL && listColNames != null && rownum != -1 && rownum < startDataRow) {
//            else if(rownum != -1 && rownum < startDataRow) { // 커스텀 컬럼 정보가 있는 경우
//                if(dbColumnId_data == null) {
////                    dbColumnId_data = (ArrayList<String>)listColNames;
//                }
//            }
//            else if(columnRow != NOT_USE_COLROW_VAL && rownum == columnRow) {
            else if(rownum == columnRow) { // 첫 행인 경우
                if(dbColumnId_data == null)
                    dbColumnId_data = new ArrayList();
            } else if(rownum >= startDataRow) { // 두번째 행 부터 데이터가 있는 경우
                cellDatas_data = new ArrayList();
            }

            if(xlsxRow != null) {
                int cells = xlsxRow.getLastCellNum();

                for(int cellnum = 0; cellnum < cells; cellnum++) {
                    XSSFCell cell = xlsxRow.getCell(cellnum);

                    if(cell != null) {

                        switch(cell.getCellType()) {

                            case NUMERIC:
                                if(DateUtil.isCellDateFormatted(cell)) { // 날짜형식인 경우
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    data = formatter.format(cell.getDateCellValue());

                                    if(rownum == -1) {
                                        headerNames_data.add(data);
                                    } else if(rownum == columnRow) {
//                                        if(listColNames == null
//                                                && dbColumnId_data != null) {
                                        if(dbColumnId_data != null) {
                                            dbColumnId_data.add(data);
                                        }
                                    } else if(rownum >= startDataRow
                                            && cellDatas_data != null) {
                                        cellDatas_data.add(data);
                                    }
                                } else { // 숫자형식인 경우
                                    double ddata = cell.getNumericCellValue();
                                    data = String.valueOf(ddata);

                                    if(rownum == -1) {
                                        headerNames_data.add(cell.getNumericCellValue());
                                    } else if(rownum == columnRow) {
//                                        if(listColNames == null && dbColumnId_data != null) {
                                        if(dbColumnId_data != null) {
                                            dbColumnId_data.add(cell.getNumericCellValue());
                                        }
                                    } else if(rownum >= startDataRow && cellDatas_data != null) {
                                        cellDatas_data.add(cell.getNumericCellValue());
                                    }
                                }

                                break;

                            case FORMULA:

                                if(!"".equals(cell.toString())) {
                                    if(evaluator.evaluateFormulaCell(cell) == CellType.NUMERIC) {
                                        double fddata = cell.getNumericCellValue();
                                        data = String.valueOf(fddata);
                                    } else if(evaluator.evaluateFormulaCell(cell) == CellType.STRING) {
                                        data = cell.getStringCellValue();
                                    } else if(evaluator.evaluateFormulaCell(cell) == CellType.BOOLEAN) {
                                        boolean fbdata = cell.getBooleanCellValue();
                                        data = String.valueOf(fbdata);
                                    }
                                    if(rownum == -1) {
                                        headerNames_data.add(data);
                                    } else if(rownum == columnRow) {
//                                        if(listColNames == null && dbColumnId_data != null) {
                                        if(dbColumnId_data != null) {
                                            dbColumnId_data.add(data);
                                        }
                                    } else if(rownum >= startDataRow && cellDatas_data != null) {
                                        cellDatas_data.add(data);
                                    }
                                }
                                break;

                            case STRING:
                                if(rownum == -1) { // 헤더 정보인듯?
                                    headerNames_data.add(cell.getStringCellValue());
                                } else if(rownum == columnRow) { // 1열(한글제목)
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getStringCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) { // 2열(데이터)
                                    cellDatas_data.add(cell.getStringCellValue());
                                }
                                cell.getStringCellValue();
                                break;

                            case BOOLEAN:
                                if(rownum == -1) {
                                    headerNames_data.add(cell.getBooleanCellValue());
                                } else if(rownum == columnRow) {
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getBooleanCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) {
                                    cellDatas_data.add(cell.getBooleanCellValue());
                                }
                                break;

                            case BLANK:
                                if(rownum == -1) {
                                    headerNames_data.add(cell.getStringCellValue());
                                } else if(rownum == columnRow) {
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getStringCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) {
                                    cellDatas_data.add(cell.getStringCellValue());
                                }
                                break;

                            case ERROR:
                                if(rownum == -1) {
                                    headerNames_data.add(cell.getErrorCellValue());
                                } else if(rownum == columnRow) {
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getErrorCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) {
                                    cellDatas_data.add(cell.getErrorCellValue());
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        if(rownum == -1) {
                            headerNames_data.add("");
                        } else if(rownum == columnRow) {
//                            if(listColNames == null && dbColumnId_data != null) {
                            if(dbColumnId_data != null) {
                                dbColumnId_data.add("");
                            }
                        } else if(rownum >= startDataRow && cellDatas_data != null) {
                            cellDatas_data.add("");
                        }
                    }
                }
                if(rownum >= startDataRow) {
                    allCellData.add(cellDatas_data);
                }
            }
        }
        allData.put("header", headerNames_data);
        allData.put("columnName", dbColumnId_data);
        ArrayList rowsData = new ArrayList();
        HashMap<String, Object> rowData = null;
        for(int r = 0; r < allCellData.size(); r++) {
            rowData = new HashMap<String, Object>();
            ArrayList cdata = allCellData.get(r);
            boolean[] isRowData = new boolean[cdata.size()]; // 해당 로우의 사이즈 정보
            int isRowTrueCnt = 0; // cell의 empty 카운트 수

            if(cdata == null) {
                continue;
            }

            for(int i = 0; dbColumnId_data != null && i < dbColumnId_data.size(); i++) {
                // row 중 Cell 전체가 isEmpty이면 true
                if(cdata.size() > i) {
                    if (StringUtil.isEmpty(cdata.get(i))) {
                        isRowData[i] = true;
                    } else {
                        isRowData[i] = false;
                    }
                }

                // ("컬렴명","컬럼Value") 형식
                rowData.put((String)dbColumnId_data.get(i), (cdata.size() <= i ? "" : cdata.get(i)));
            }

            // isRowData 중 Cell empty : true, 아니면 false
            for(int empty = 0; empty < isRowData.length; empty++) {
                if(isRowData[empty] == true) {
                    isRowTrueCnt++;
                }
            }

            // 모든 row의 Cell이 빈값일 경우 분기처리
            if(isRowData.length == isRowTrueCnt) {
                break;
            } else {
                rowsData.add(rowData);
            }
        }
        allData.put("rowData", rowsData);

        return allData;
    }

    /**
     *
     * @description : xls 파싱
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static HashMap<String, Object> xlsParsing(HSSFWorkbook xlsWorkbook, HSSFSheet xlsSheet, int columnRow, int startDataRow) {
        FormulaEvaluator evaluator = xlsWorkbook.getCreationHelper().createFormulaEvaluator();
        String data = null;
        DecimalFormat df = new DecimalFormat();
        HashMap<String, Object> allData = new HashMap<String, Object>();
        ArrayList<ArrayList> allCellData = new ArrayList<ArrayList>();
        ArrayList headerNames_data = null;
        ArrayList dbColumnId_data = null;
        ArrayList cellDatas_data = null;
        int rows = xlsSheet.getPhysicalNumberOfRows();

        for(int rownum = 0; rownum < rows; rownum++) {
            HSSFRow xlsRow = xlsSheet.getRow(rownum);
            if(rownum == -1) {
                headerNames_data = new ArrayList();
            }
//            else if(columnRow == NOT_USE_COLROW_VAL && listColNames != null && rownum != -1 && rownum < startDataRow) {
            else if(rownum != -1 && rownum < startDataRow) {
                if(dbColumnId_data == null) {
//                    dbColumnId_data = (ArrayList<String>)listColNames;
                }
            }
//            else if(columnRow != NOT_USE_COLROW_VAL && rownum == columnRow) {
            else if(rownum <= columnRow) {
                if(dbColumnId_data == null)
                    dbColumnId_data = new ArrayList();
            }
            else if(rownum >= startDataRow) {
                cellDatas_data = new ArrayList();
            }

            if(xlsRow != null) {
                int cells = xlsRow.getLastCellNum();
                for(int cellnum = 0; cellnum < cells; cellnum++) {
                    HSSFCell cell = xlsRow.getCell(cellnum);

                    if(cell != null) {
                        switch(cell.getCellType()) {
                            case NUMERIC:
                                if(DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    data = formatter.format(cell.getDateCellValue());

                                    if(rownum == -1) {
                                        headerNames_data.add(data);
                                    } else if(rownum <= columnRow) {
//                                        if(listColNames == null && dbColumnId_data != null) {
                                        if(dbColumnId_data != null) {
                                            dbColumnId_data.add(data);
                                        }
                                    } else if(rownum >= startDataRow && cellDatas_data != null) {
                                        cellDatas_data.add(data);
                                    }
                                } else {
                                    double ddata = cell.getNumericCellValue();
                                    data = df.format(ddata);

                                    if(rownum == -1) {
                                        headerNames_data.add(cell.getNumericCellValue());
                                    } else if(rownum <= columnRow) {
//                                        if(listColNames == null && dbColumnId_data != null) {
                                        if(dbColumnId_data != null) {
                                            dbColumnId_data.add(cell.getNumericCellValue());
                                        }
                                    } else if(rownum >= startDataRow && cellDatas_data != null) {
                                        cellDatas_data.add(cell.getNumericCellValue());
                                    }
                                }
                                break;

                            case FORMULA:
                                if(!"".equals(cell.toString())) {
                                    if(evaluator.evaluateFormulaCell(cell) == CellType.NUMERIC) {
                                        double fddata = cell.getNumericCellValue();
                                        data = df.format(fddata);
                                    } else if(evaluator.evaluateFormulaCell(cell) == CellType.STRING) {
                                        data = cell.getStringCellValue();
                                    } else if(evaluator.evaluateFormulaCell(cell) == CellType.BOOLEAN) {
                                        boolean fbdata = cell.getBooleanCellValue();
                                        data = String.valueOf(fbdata);
                                    }

                                    if(rownum == -1) {
                                        headerNames_data.add(data);
                                    } else if(rownum <= columnRow) {
//                                        if(listColNames == null && dbColumnId_data != null) {
                                            dbColumnId_data.add(data);
//                                        }
                                    } else if(rownum >= startDataRow && cellDatas_data != null) {
                                        cellDatas_data.add(data);
                                    }
                                }
                                break;

                            case STRING:
                                if(rownum == -1) {
                                    headerNames_data.add(cell.getStringCellValue());
                                } else if(rownum <= columnRow) {
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getStringCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) {
                                    cellDatas_data.add(cell.getStringCellValue());
                                }
                                cell.getStringCellValue();
                                break;

                            case BOOLEAN:
                                if(rownum == -1) {
                                    headerNames_data.add(cell.getBooleanCellValue());
                                } else if(rownum <= columnRow) {
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getBooleanCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) {
                                    cellDatas_data.add(cell.getBooleanCellValue());
                                }
                                break;

                            case BLANK:
                                if(rownum == -1) {
                                    headerNames_data.add(cell.getStringCellValue());
                                } else if(rownum <= columnRow) {
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getStringCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) {
                                    cellDatas_data.add(cell.getStringCellValue());
                                }
                                break;

                            case ERROR:
                                if(rownum == -1) {
                                    headerNames_data.add(cell.getErrorCellValue());
                                } else if(rownum <= columnRow) {
//                                    if(listColNames == null && dbColumnId_data != null) {
                                    if(dbColumnId_data != null) {
                                        dbColumnId_data.add(cell.getErrorCellValue());
                                    }
                                } else if(rownum >= startDataRow && cellDatas_data != null) {
                                    cellDatas_data.add(cell.getErrorCellValue());
                                }

                                break;
                            default:
                                break;
                        }
                    } else {
                        if(rownum == -1) {
                            headerNames_data.add("");
                        } else if(rownum <= columnRow) {
//                            if(listColNames == null && dbColumnId_data != null) {
                            if(dbColumnId_data != null) {
                                dbColumnId_data.add("");
                            }
                        } else if(rownum >= startDataRow && cellDatas_data != null) {
                            cellDatas_data.add("");
                        }
                    }
                }
                if(rownum >= startDataRow) {
                    allCellData.add(cellDatas_data);
                }
            }
        }
        allData.put("header", headerNames_data);
        allData.put("columnName", dbColumnId_data);

        ArrayList rowsData = new ArrayList();
        HashMap<String, Object> rowData = null;
        for(int r = 0; r < allCellData.size(); r++) {
            rowData = new HashMap<String, Object>();
            ArrayList cdata = allCellData.get(r);
            boolean[] isRowData = new boolean[cdata.size()]; // 해당 로우의 사이즈 정보
            int isRowTrueCnt = 0; // cell의 empty 카운트 수

            if(cdata == null) {
                continue;
            }

            for(int i = 0; dbColumnId_data != null && i < dbColumnId_data.size(); i++) {
                // row 중 Cell 전체가 isEmpty이면 true
                if(cdata.size() > i) {
                    if (StringUtil.isEmpty(cdata.get(i))) {
                        isRowData[i] = true;
                    } else {
                        isRowData[i] = false;
                    }
                }

                rowData.put((String)dbColumnId_data.get(i), (cdata.size() <= i ? "" : cdata.get(i)));
            }

            // isRowData 중 Cell empty : true, 아니면 false
            for(int empty = 0; empty < isRowData.length; empty++) {
                if(isRowData[empty] == true) {
                    isRowTrueCnt++;
                }
            }

            // 모든 row의 Cell이 빈값일 경우 분기처리
            if(isRowData.length == isRowTrueCnt) {
                break;
            }else {
                rowsData.add(rowData);
            }
        }
        allData.put("rowData", rowsData);
        return allData;
    }
}

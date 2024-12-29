package com.example.service;

import com.example.repository.UploadMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class UploadXSSFService {

    private final UploadMapper uploadMapper;

    public UploadXSSFService(UploadMapper uploadMapper){
        this.uploadMapper = uploadMapper;
    }

    public void insertAddress(List<Map<String, Object>> data) {
        uploadMapper.insertAddress(data);
    }

    public Map<String, Object> processExcelFile(MultipartFile file) throws IOException, InvalidFormatException {
        Map<String, Object> response = new HashMap<>();

        // ---------------- 시작 ----------------
        long startTime = System.currentTimeMillis();
        // ---------------- 끝 ----------------

        // 엑셀파일 열기 및 읽기 (2번 사용함 - 엑셀 버전이 2007 이상인 경우)
        // [참고]
        // 1. HSSF : 엑셀 97 ~ 2003 버전, 65,536 행, 256 열 까지 사용 가능
        // 2. XSSF : 엑셀 2007 이상 버전, 1,048,576 행, 16,384 열 까지 사용 가능
        // 3. SXSSF : XSSF의 확장 버전, 메모리를 적게 사용하며 대용량 데이터 처리에 적합
        OPCPackage opcPackage = OPCPackage.open(file.getInputStream());
        XSSFWorkbook wb = new XSSFWorkbook(opcPackage);

        // ---------------- 시작 ----------------
        // 종료 시간 측정
        long endTime = System.currentTimeMillis();
        // 실행 시간 계산
        long duration = (endTime - startTime);
        log.debug(">>>>>>>>>> [poi] OPCPackage 및 XSSFWorkbook 생성 시간: {} milliseconds", duration);
        response.put("create_opcpackage_xssfworkbook_duration", duration);
        // 시작 시간 측정
        startTime = System.currentTimeMillis();
        // ---------------- 끝 ----------------

        List<Map<String, Object>> excelUploadData = new ArrayList<>();
        int sheetNum = wb.getNumberOfSheets();  // Sheet 수

        for (int i = 0; i < sheetNum; i++) {
            XSSFSheet sheet = wb.getSheetAt(i);             // Sheet를 순서대로 가져옴
            int rows = sheet.getPhysicalNumberOfRows();     // 행의 수

            for (int j = 0; j < rows; j++) {
                XSSFRow row = sheet.getRow(j);              // 행을 순서대로 가져옴
                int cells = row.getPhysicalNumberOfCells(); // 그 행의 셀 수

                Map<String, Object> paramMap = new HashMap<>();
                for (int k = 0; k < cells; k++) {
                    XSSFCell cell = row.getCell(k);         // 셀을 순서대로 가져옴
                    String value = "";

                    if (cell == null) {
                        value = null;
                    } else {
                        switch (cell.getCellType()) {
                            case FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                    }

                    switch (k) {
                        case 0:
                            paramMap.put("postal_code", value);
                            break;
                        case 1:
                            paramMap.put("city", value);
                            break;
                        case 2:
                            paramMap.put("city_english", value);
                            break;
                        case 3:
                            paramMap.put("district", value);
                            break;
                        case 4:
                            paramMap.put("district_english", value);
                            break;
                        case 5:
                            paramMap.put("town", value);
                            break;
                        case 6:
                            paramMap.put("town_english", value);
                            break;
                        case 7:
                            paramMap.put("road_code", value);
                            break;
                        case 8:
                            paramMap.put("road_name", value);
                            break;
                        case 9:
                            paramMap.put("road_name_english", value);
                            break;
                        case 10:
                            paramMap.put("underground", value);
                            break;
                        case 11:
                            paramMap.put("building_main_number", value);
                            break;
                        case 12:
                            paramMap.put("building_sub_number", value);
                            break;
                        case 13:
                            paramMap.put("building_management_number", value);
                            break;
                        case 14:
                            paramMap.put("bulk_delivery_name", value);
                            break;
                        case 15:
                            paramMap.put("district_building_name", value);
                            break;
                        case 16:
                            paramMap.put("legal_dong_code", value);
                            break;
                        case 17:
                            paramMap.put("legal_dong_name", value);
                            break;
                        case 18:
                            paramMap.put("ri_name", value);
                            break;
                        case 19:
                            paramMap.put("administrative_dong_name", value);
                            break;
                        case 20:
                            paramMap.put("mountain", value);
                            break;
                        case 21:
                            paramMap.put("lot_main_number", value);
                            break;
                        case 22:
                            paramMap.put("town_serial_number", value);
                            break;
                        case 23:
                            paramMap.put("lot_sub_number", value);
                            break;
                        case 24:
                            paramMap.put("old_postal_code", value);
                            break;
                        case 25:
                            paramMap.put("postal_code_serial_number", value);
                            break;
                    }
                }
//                paramMap.put("index", j);  // 행번호 추가하고 싶을 때, 적용하면 됨

                if (j == 0) {
                    continue;
                }

                // 행 데이터를 List에 담음
                excelUploadData.add(paramMap);
            }
        }

        // ---------------- 시작 ----------------
        // 종료 시간 측정
        endTime = System.currentTimeMillis();
        // 실행 시간 계산
        duration = (endTime - startTime);
        log.debug(">>>>>>>>>> [poi] 파싱 및 List 담는 시간: {} milliseconds", duration);
        response.put("parsing_addList_duration", duration);
        // 시작 시간 측정
        startTime = System.currentTimeMillis();
        // ---------------- 끝 ----------------

        // 엑셀파일 데이터를 DB에 저장
        int batchSize = 1000;
        for (int i = 0; i < excelUploadData.size(); i += batchSize) {
            log.debug("i: {}", i);
            if (i % batchSize == 0) {
                int end = Math.min(i + batchSize, excelUploadData.size());
                List<Map<String, Object>> batchList = excelUploadData.subList(i, end);
                insertAddress(batchList);
            }
        }

        // ---------------- 시작 ----------------
        // 종료 시간 측정
        endTime = System.currentTimeMillis();
        // 실행 시간 계산
        duration = (endTime - startTime);
        log.debug(">>>>>>>>>> [poi] db insert 시간: {} milliseconds", duration);
        response.put("db_insert_duration", duration);
        // ---------------- 끝 ----------------

        return response;
    }
}

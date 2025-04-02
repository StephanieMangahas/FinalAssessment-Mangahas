package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class ExcelReader {
    private final String filePath;
    private final String sheetName;

    public ExcelReader(String filePath, String sheetName) {
        this.filePath = filePath;
        this.sheetName = sheetName;
    }

    public Optional<String> getCellData(int row, int col) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return Optional.empty();

            Row excelRow = sheet.getRow(row);
            if (excelRow == null) return Optional.empty();

            Cell cell = excelRow.getCell(col);
            if (cell == null || cell.getCellType() == CellType.BLANK) return Optional.empty();

            switch (cell.getCellType()) {
                case STRING:
                    return Optional.of(cell.getStringCellValue().trim());
                case NUMERIC:
                    return Optional.of(String.valueOf((int) cell.getNumericCellValue()));
                case BOOLEAN:
                    return Optional.of(String.valueOf(cell.getBooleanCellValue()));
                default:
                    return Optional.empty();
            }
        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            return Optional.empty();
        }
    }

    public int getRowCount() {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            return (sheet != null) ? sheet.getLastRowNum() : 0;
        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            return 0;
        }
    }
}

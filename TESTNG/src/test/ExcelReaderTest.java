package test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ExcelReader;

public class ExcelReaderTest {
    private ExcelReader excelReader;
    private static final String FILE_PATH = "src/data/credentials.xlsx";
    private static final String SHEET_NAME = "Login";

    @BeforeClass
    public void setUp() {
        excelReader = new ExcelReader(FILE_PATH, SHEET_NAME);
    }

    @Test
    public void testValidCellData() {
        String username = excelReader.getCellData(1, 0).orElse(""); 
        String password = excelReader.getCellData(1, 1).orElse("");
        
        Assert.assertFalse(username.isEmpty(), "Username should not be empty");
        Assert.assertFalse(password.isEmpty(), "Password should not be empty");
    }

    @Test
    public void testInvalidCellData() {
        String emptyCell = excelReader.getCellData(100, 100).orElse("");
        Assert.assertEquals(emptyCell, "", "Empty cell should return an empty string");
    }

    @Test
    public void testNumericData() {
        String numericData = excelReader.getCellData(4, 2).orElse(""); 
        Assert.assertTrue(numericData.matches("\\d+"), "Numeric data should be converted to a string");
    }
}

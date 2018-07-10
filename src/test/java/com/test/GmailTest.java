package com.test;

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GmailTest {

	private WebDriver driver;
	private static final String GMAIL_URL = "https://gmail.com";

	static Map<String, Object[]> testresultdata;
	// define an Excel Work Book
	static HSSFWorkbook workbook;
	// define an Excel Work sheet
	static HSSFSheet sheet;

	private static final String INPUT_DATA = "E:\\creds-1.xlsx";
	private static final String TEST_RESULTS = "E:\\result.xls";

	@BeforeTest
	public void beforeTest() throws EncryptedDocumentException, InvalidFormatException, IOException {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chrome-driver\\chromedriver.exe");
		// driver = new FirefoxDriver();
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Test Result");
		testresultdata = new LinkedHashMap<String, Object[]>();
		testresultdata.put("0", new Object[] { "Email", "Password", "Expected Result", "Actual Result", "Status" });

	}

	@Test(priority = 1)
	public void testLoginUsers()
			throws EncryptedDocumentException, InvalidFormatException, IOException, InterruptedException {

		Workbook workbook = WorkbookFactory.create(new File(INPUT_DATA));

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.rowIterator();
		int count = 1;
		String email = "";
		String password = "";
		String expectedResult = "";
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getCell(0) == null) {

				email = "";
			} else {
				row.getCell(0).setCellType(CellType.STRING);
				email = row.getCell(0).toString();
			}
			if (row.getCell(1) == null) {
				password = "";
			} else {
				row.getCell(1).setCellType(CellType.STRING);
				password = row.getCell(1).toString();
			}
			if (row.getCell(2) == null) {
				expectedResult = "";
			} else {
				row.getCell(2).setCellType(CellType.STRING);
				expectedResult = row.getCell(2).toString();
			}
			testAllUsers(count, email, password, expectedResult);
			driver.close();
			count++;

		}
		workbook.close();

	}

	private void testAllUsers(int count, String email, String password, String expectedResult)
			throws InterruptedException {

		driver = new ChromeDriver();
		driver.get(GMAIL_URL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));

		email_phone.sendKeys(email);
		driver.findElement(By.id("identifierNext")).click();
		String errorMessage = driver.findElement(By.xpath("//div[@jsname='B34EJ']")).getText();
		WebElement pass = null;

		if (driver.findElements(By.id("passwordNext")).size() != 0) {

			pass = driver.findElement(By.xpath("//input[@name='password']"));
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.elementToBeClickable(pass));
			pass.sendKeys(password);
			driver.findElement(By.id("passwordNext")).click();
			TimeUnit.SECONDS.sleep(2);

			// THIS MEANS THAT YOU HAVE LOGGED IN SUCCESSFULLY
			if (driver.findElements(By.xpath("//div[@jsname='B34EJ']")).size() == 0) {
				String sentMail = driver.findElement(By.partialLinkText("Sent Mail")).getText().toString();
				try {
					assertEquals("Sent Mail", sentMail);
					testresultdata.put(String.valueOf(count),
							new Object[] { email, password, expectedResult, sentMail, "Pass" });
				} catch (AssertionError assertionError) {
					testresultdata.put(String.valueOf(count),
							new Object[] { email, password, expectedResult, sentMail, "Fail" });
				}
			} else { // YOU DIDN'T LOGIN, YOU ARE ON PASSWORD PAGE,
				// RIGHT EMAIL NO PASSWORD OR WRONG PASSWORD
				try {
					errorMessage = driver.findElement(By.xpath("//div[@jsname='B34EJ']")).getText();
					assertEquals(expectedResult, errorMessage);
					testresultdata.put(String.valueOf(count),
							new Object[] { email, password, expectedResult, errorMessage, "Pass" });
				} catch (AssertionError assertionError) {
					testresultdata.put(String.valueOf(count),
							new Object[] { email, password, expectedResult, errorMessage, "Fail" });
				}
			}
		} else { // NO EMAIL
			String errorMessageEmail = driver.findElement(By.xpath("//div[@jsname='B34EJ']")).getText();
			testresultdata.put(String.valueOf(count),
					new Object[] { email, password, expectedResult, errorMessageEmail, "Pass" });
		}
	}

	@AfterTest
	public void AfterAll() {
		writeResultsToFile(TEST_RESULTS);
	}

	private static void writeResultsToFile(String resultFile) {
		Set<String> keyset = testresultdata.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = testresultdata.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof Date)
					cell.setCellValue((Date) obj);
				else if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				else if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Double)
					cell.setCellValue((Double) obj);
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(resultFile));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.test;

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
	Map<String, Object[]> testresultdata;
	// define an Excel Work Book
	HSSFWorkbook workbook;
	// define an Excel Work sheet
	HSSFSheet sheet;

	@BeforeTest
	public void beforeTest() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chrome-driver\\chromedriver.exe");
		// driver = new FirefoxDriver();
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Test Result");
		testresultdata = new LinkedHashMap<String, Object[]>();
		testresultdata.put("1", new Object[] { "Id", "Action", "Expected Result", "Status" });

	}

	@Test(priority = 1)
	public void successfulLogin() {
		try {
			driver = new ChromeDriver();
			driver.get(GMAIL_URL);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));
			email_phone.sendKeys("");
			driver.findElement(By.id("identifierNext")).click();
			WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.elementToBeClickable(password));
			password.sendKeys("");
			driver.findElement(By.id("passwordNext")).click();

			String sentMail = driver.findElement(By.partialLinkText("Sent Mail")).getText().toString();
			assertEquals(sentMail, "Sent Mail");

			testresultdata.put("2", new Object[] { 1d, "login with valid credentials", "login successful", "Pass" });
		} catch (Exception ex) {
			testresultdata.put("2", new Object[] { 1d, "login with valid credentials", "login successful", "Fail" });
		}
	}

	@Test(priority = 2)
	public void invalidPassword() {
		try {
			driver = new ChromeDriver();
			driver.get(GMAIL_URL);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));

			email_phone.sendKeys("");
			driver.findElement(By.id("identifierNext")).click();

			WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.elementToBeClickable(password));

			driver.findElement(By.id("passwordNext")).click();

			String message = driver.findElement(By.xpath("//div[@jsname='B34EJ']")).getText();
			System.out.println("###########" + message);

			assertEquals(message, "Enter a password");

			testresultdata.put("3", new Object[] { 2d, "login with no password", "Enter a password", "Pass" });
		} catch (Exception ex) {
			testresultdata.put("3", new Object[] { 2d, "login with no password", "Enter a password", "Fail" });
		}
	}

	@Test(priority = 3)
	public void enterWithoutCreds() {
		try {
			driver = new ChromeDriver();
			driver.get(GMAIL_URL);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));
			email_phone.sendKeys("");
			driver.findElement(By.id("identifierNext")).click();
			String message = driver.findElement(By.xpath("//div[@jsname='B34EJ']")).getText();
			System.out.println("###########" + message);

			assertEquals(message, "Enter an email or phone number");

			testresultdata.put("4",
					new Object[] { 3d, "login without credentials", "Enter an email or phone number", "Pass" });
		} catch (Exception ex) {
			testresultdata.put("4",
					new Object[] { 3d, "login without credentials", "Enter an email or phone number", "Fail" });
		}
	}

	@AfterTest
	public void afterTest() {
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
			FileOutputStream out = new FileOutputStream(new File("E:\\TestResult.xls"));
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

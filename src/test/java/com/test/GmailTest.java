package com.test;

import java.util.concurrent.TimeUnit;

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

	@BeforeTest
	public void beforeTest() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chrome-driver\\chromedriver.exe");
		// driver = new FirefoxDriver();
		driver = new ChromeDriver();
	}

	@Test
	public void successfulLogin() {
		driver.get(GMAIL_URL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));
		email_phone.sendKeys("suryamounica.2@gmail.com");
		driver.findElement(By.id("identifierNext")).click();
		WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(password));
		password.sendKeys("123");
		driver.findElement(By.id("passwordNext")).click();
		driver.findElement(By.id(":al")).click();
	}

	// @Test
	public void invalidPassword() {
		driver.get(GMAIL_URL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));

		email_phone.sendKeys("suryamounica.2@gmail.com");
		driver.findElement(By.id("identifierNext")).click();

		WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(password));

		password.sendKeys("123");
		driver.findElement(By.id("passwordNext")).click();
	}

	// @Test
	public void enterWithoutCreds() {
		driver.get(GMAIL_URL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));
		email_phone.sendKeys("");
		driver.findElement(By.id("identifierNext")).click();
	}

	// @BeforeTest
	public void LoginToGmail() {
		driver.get("http://gmail.com.com");
	}

	public void SelectDropDown() {
		driver.findElement(By.id("get-started-btn")).click();

	}

	@AfterTest
	public void afterTest() {
		// driver.quit();
	}
}

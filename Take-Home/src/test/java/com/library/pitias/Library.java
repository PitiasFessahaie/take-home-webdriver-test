package com.library.pitias;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Library {

	Logger logger = Logger.getLogger(Library.class);
	private WebDriver driver;

	public Library(WebDriver _driver) {
		driver = _driver;
	}

	public WebDriver startBrowser(String browser) {
		try {

			if (browser.toLowerCase().contains("chrome")) {
				driver = startChrome();
			} else if (browser.toLowerCase().contains("firefox")) {
				driver = firefox();
			} else {
				logger.info("The " + browser + " typed is not recognized Starting a default Chrome Browser");
				driver = startChrome();
			}

		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public WebDriver firefox() {
		try {
			logger.info("Start Firefox .....");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public WebDriver startChrome() {
		try {
			logger.info("Starting Chrome.....");

			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public String getCurrentTime() {
		String finalTime = null;
		try {
			Date date = new Date();
			String tempTime = new Timestamp(date.getTime()).toString();
			logger.info(tempTime);
			finalTime = tempTime.replace("-", "").replace(" ", "").replace(":", "").replace(".", "");
			logger.info("Generating clock " + finalTime);

		} catch (Exception e) {
			logger.error(e.getMessage());
			assertTrue(false);
		}

		return finalTime;
	}

	public String screenCapture(String screenshotFileName) {
		String filePath = null;
		String fileName = null;
		try {
			fileName = screenshotFileName + getCurrentTime() + ".png";
			filePath = "target/screenshots/";
			File tempfile = new File(filePath);
			if (!tempfile.exists()) {
				tempfile.mkdirs();
			}
			filePath = tempfile.getAbsolutePath();

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile, new File(filePath + "/" + fileName));

		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		logger.info(filePath + "/" + fileName);
		return filePath + "/" + fileName;
	}

	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}

	public void hiddenClick(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}

	public void customWait(double inSecs) {
		try {
			Thread.sleep((long) (inSecs * 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dragandDrop(WebElement source, WebElement dest) {
		Actions action = new Actions(driver);
		action.clickAndHold(source).moveToElement(dest).release(source).build().perform();
		customWait(2);
	}
	
	public void waitForPageToLoad(long timeOutInSeconds) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		try {
			System.out.println("Waiting for page to load...");
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(expectation);
		} catch (Throwable error) {
			System.out.println(
					"Timeout waiting for Page Load Request to complete after " + timeOutInSeconds + " seconds");
		}
	}
	public void waitForStaleElement(WebElement element) {
		int y = 0;
		while (y <= 15) {
			try {
				element.isDisplayed();
				break;
			} catch (StaleElementReferenceException st) {
				y++;
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (WebDriverException e) {
				y++;
				try {
					Thread.sleep(300);
				} catch (InterruptedException ew) {
					e.printStackTrace();
				}
			}
		}
	}

	public void click(WebElement element) {
		element.click();
		customWait(1);
	}

	public void rightClick(WebElement element) {
		Actions act = new Actions(driver);
		act.contextClick(element).perform();
		// customWait(2);
	}

	public void dropDown(WebElement element, int index) {
		Select sel = new Select(element);
		sel.selectByIndex(index);
		customWait(1);

	}

	public String readDropDown(WebElement element) {
		Select sel = new Select(element);
		String text = sel.getFirstSelectedOption().getText();
		return text;
	}

	public void explicitWait(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 6);
		wait.until(ExpectedConditions.visibilityOf(element));
		customWait(2);
	}

	public WebElement fluentWait(WebElement element, By by) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(15))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class);

		element = wait.until(new Function<WebDriver, WebElement>() {

			public WebElement apply(WebDriver driver) {
				return driver.findElement(by);
			}
		}); return element;

	}

	public boolean isFileDownloaded(String downloadPath, String fileName) {
		File dir = new File(downloadPath);
		if (dir != null) {
			File[] dirContents = dir.listFiles();

			for (int i = 0; i < dirContents.length; i++) {
				if (dirContents[i].getName().equals(fileName)) {
					// File has been found, it can now be deleted:
					dirContents[i].delete();
					return true;
				} 
			}

		}
		return false;
	}

	public void scrollDown(int y_axis) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("scroll(0," + y_axis + ")");
	}

	public void moveToElement(WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).perform();
		customWait(2);
	}

	public WebDriver switchWindows() {
		for (String windows : driver.getWindowHandles()) {
			driver.switchTo().window(windows);
		}
		return driver;
	}

	public void closeBrowser() {
		WebElement browser = driver.findElement(By.tagName("body"));
		browser.sendKeys(Keys.chord(Keys.COMMAND, "w"));
	}

}

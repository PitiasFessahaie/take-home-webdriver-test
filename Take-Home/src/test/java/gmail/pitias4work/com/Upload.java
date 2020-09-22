package gmail.pitias4work.com;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;
import com.library.pitias.Base;

public class Upload extends Base {
	Logger logger = Logger.getLogger(Base.class);

	public void upload() {

		try {
			driver.get("http://localhost:7080/upload");
			logger.info("Title is :" + driver.getTitle());

			assertEquals(driver.getTitle(), "The Internet");

			// Test uses Upload Button or Drag and Drop to upload a file.
			WebElement upload = driver.findElement(By.xpath("//*[@id=\"file-upload\"]"));

			lib.hiddenClick(upload);

			lib.customWait(3);
			upload.sendKeys("/Users/pitiasfessahaie/Downloads/some-file.txt");

			WebElement submit = driver.findElement(By.cssSelector("#file-submit"));
			lib.click(submit);

			// Assertion upload

			if (driver.findElement(By.cssSelector("#uploaded-files")).isDisplayed()) {
				assertTrue(true, "The File is uploaded sucessfully");
				test.log(Status.INFO, "File upload Assertion Success!!");
			} else {
				assertTrue(false, "The File did not uploaded ");
				test.log(Status.FAIL, "File not uploaded Assertion Fail!!");
			}

			lib.customWait(2);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			test.error(e.getMessage());
		}

	}
}

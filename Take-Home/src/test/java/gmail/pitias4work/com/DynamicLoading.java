package gmail.pitias4work.com;

import static org.testng.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;
import com.library.pitias.Base;

public class DynamicLoading extends Base {
	Logger logger = Logger.getLogger(Base.class);

	public void dynamic_Loading() {

		try {

			driver.get(prop.readProperties("dynamicloading_url"));
			logger.info("Title is :" + driver.getTitle());
			assertEquals(driver.getTitle(), "The Internet");

			WebElement start = driver.findElement(By.xpath(prop.readProperties("start")));
			lib.click(start);

			Thread.sleep(2000);
			WebElement finish = driver.findElement(By.cssSelector(prop.readProperties("finishTxt")));
			lib.explicitWait(finish);

			// Assertion
			System.out.println(finish.getText());
			assertEquals(finish.getText(), "Hello World!");
			test.log(Status.INFO, "Hello World! Assert Success!!");

			Thread.sleep(3000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			test.error(e.getMessage());
		}

	}
}

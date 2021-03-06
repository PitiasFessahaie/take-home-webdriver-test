package gmail.pitias4work.com;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;
import com.library.pitias.Base;


public class FileDownload extends Base {
	Logger logger = Logger.getLogger(Base.class);

	boolean check = false;

	public void download() {

		try {
			driver.get(prop.readProperties("download_url"));
			logger.info(driver.getTitle());
			assertEquals(driver.getTitle(), "The Internet");
			WebElement download = driver.findElement(By.xpath(prop.readProperties("downloadbtn")));
			download.click();

			check = lib.isFileDownloaded(prop.readProperties("filePath"), prop.readProperties("fileName"));
			lib.customWait(8);
			assertTrue(check);
			test.log(Status.INFO, "Download Compelete Assert Success!!");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			test.error(e.getMessage());
		}

	}
}

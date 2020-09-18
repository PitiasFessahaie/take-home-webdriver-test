package gmail.pitias4work.com;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.library.pitias.Base;
@Test
public class FileDownload extends Base {
Logger logger = Logger.getLogger(Base.class);

	boolean check = false;

	public void download() {

		try {
			driver.get("http://localhost:7080/download");
			logger.info(driver.getTitle());
			assertEquals(driver.getTitle(), "The Internet");
			WebElement download = driver.findElement(By.xpath("//*[@id=\"content\"]/div/a"));
			download.click();

			check = lib.isFileDownloaded("/Users/pitiasfessahaie/Downloads", "some-file.txt");
			lib.customWait(5);
			assertTrue(check);

			

		} catch (Exception e) {
			System.out.println(e.getMessage());
			test.error(e.getMessage());
		}

	}
}

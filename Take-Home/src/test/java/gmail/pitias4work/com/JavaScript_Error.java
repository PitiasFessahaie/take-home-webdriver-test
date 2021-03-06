package gmail.pitias4work.com;

import static org.testng.Assert.assertEquals;
import org.apache.log4j.Logger;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import com.aventstack.extentreports.Status;
import com.library.pitias.Base;

public class JavaScript_Error extends Base {

	Logger logger = Logger.getLogger(Base.class);

	public void jse() {
		String error = null;

		try {
			driver.get(prop.readProperties("javaScriptError_url"));

			LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
			// Test finds the JavaScript error on the page.

			for (LogEntry entry : logEntries) {
				logger.info("JavaScript Error :" + entry.getLevel() + " " + entry.getMessage() + "\n");

				error = entry.getMessage();
			}

			// Test asserts that the page contains error: Cannot read property 'xyz' of
			// undefined

			logger.info("Page Error :" + error.substring(64));
			assertEquals(error.substring(64), "Cannot read property 'xyz' of undefined");
			test.log(Status.INFO, "Console Error Assertion Success!!");
		} catch (Exception e) {
			logger.error(e.getMessage());
			test.error(e.getMessage());
		}

	}
}

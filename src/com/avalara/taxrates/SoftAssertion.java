package com.avalara.taxrates;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;

public class SoftAssertion extends Assertion {

	private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
	private String assertMessage = null;
	
	@Override
    protected void doAssert(IAssert a) {
        onBeforeAssert(a);
        try {
            assertMessage = a.getMessage();
            a.doAssert();
            onAssertSuccess(a);
        } catch (AssertionError ex) {
        	onAssertFailure(a, ex);
            try {
				saveScreenshot(assertMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
            m_errors.put(ex, a);
        } finally {
            onAfterAssert(a);
        }
    }

	private void saveScreenshot(String assertMessage) throws IOException {
		WebDriver driver=Taxrates.driver;
		String Date= getDate();
		TakesScreenshot screenshot=(TakesScreenshot) driver;
		File src = screenshot.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File(".//"+ Date +"//screenshot_fail"+ ".png"));
		
	}
	
	private String getDate()
	{
		SimpleDateFormat date= new SimpleDateFormat("mm_ss");
		Date d= new Date();
		String timeStamp=date.format(d);
		System.out.println(timeStamp);
		return timeStamp;
	}

}

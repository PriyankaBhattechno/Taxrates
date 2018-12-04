package com.avalara.taxrates;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

public class Taxrates {
	static WebDriver driver;
	@BeforeTest
	public void launchBrowser()
	{
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
		driver= new ChromeDriver();
		
	}
	@BeforeClass
	public void openURL()
	{
		driver.get("https://www.avalara.com/taxrates/en/state-rates.html");
		//driver.get("https://avalara-stage.adobemsbasic.com/taxrates/en/state-rates.html");
		driver.manage().window().maximize();
	}
	
	@Test
	public void get_stateRates() throws InterruptedException
	{
		
		int total_states= driver.findElements(By.xpath("//div[@class='container pdn-t30 pdn-b30']/div//div[@class='col-control']/div/div[1]//p/a")).size();
		System.out.println(total_states);
		SoftAssert softAssertion= new SoftAssert();
		for(int i=1; i<=total_states;i++)
		{	 
			//Get the states from 1st row each row
			
			driver.findElement(By.xpath("//div[@class='container pdn-t30 pdn-b30']/div//div[@class='col-control']/div/div[1]//p["+i+"]/a")).click();
			
			LinkedHashMap<String,String> rateTable = new LinkedHashMap<String,String>();
			 String rates=null;
			 String[] rate=null;
			 List<WebElement> cityList= null;
			
			//Get cities of each state
			driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
			try {
				cityList= driver.findElements(By.xpath("//div[@class='col-6 col-sm-9 col-md-9 col-lg-9 clearfix']/div//a"));
				  rates= driver.findElement(By.xpath("//div[@class='col-6 col-sm-3 col-md-3 col-lg-3 clearfix']/div/p")).getText();
				 rate = rates.split("\n");
				 int count=0;
				 // Get the rates of each city
				 
			        for(WebElement city : cityList)
			        {
			            rateTable.put(city.getText(),rate[count++]);
			        }
			        System.out.println("Cities with their rates----" + rateTable);
			        //Click on each city of a state
			        //driver.findElement(By.xpath("//div[@class='col-6 col-sm-9 col-md-9 col-lg-9 clearfix']/div//a["+i+"]")).click();
			        for(int j=1;j<cityList.size();j++)
			        {
			        	
			        	WebElement currentCity=driver.findElement(By.xpath("//div[@class='col-6 col-sm-9 col-md-9 col-lg-9 clearfix']/div//a["+j+"]"));
			        	String cityName=currentCity.getText();
			        	System.out.println("Rate for" + cityName +""+ rateTable.get(cityName));
			        	String cityRate=rateTable.get(cityName);
			        	currentCity.click();
			        	//New page
			        	String individualCityRate= driver.findElement(By.xpath("//p[@class='rate-number']")).getText();
			        	System.out.println(individualCityRate);
			        		softAssertion.assertTrue(individualCityRate.equals(cityRate));
			        }
			}
			catch(NoSuchElementException na)
			{
				continue;
			}
			 
			finally {
				driver.navigate().back();
	        	
			}
			 driver.navigate().back();
			        	
			 }
			
		softAssertion.assertAll();        
			
		}
		
		
	}



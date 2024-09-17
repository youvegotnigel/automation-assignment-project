package com.vitalhub.automation.ui.scripts.steps
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import org.apache.commons.lang.StringEscapeUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot

import com.googlecode.javacv.CanvasFrame.Exception
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import cucumber.api.PendingException
import cucumber.api.Scenario
import cucumber.api.java.After
import cucumber.api.java.AfterStep
import cucumber.api.java.Before
import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import internal.GlobalVariable
import io.cucumber.datatable.DataTable
import com.kms.katalon.core.configuration.RunConfiguration

trait BaseSteps {

	
	def beforeTestcase() {
		// open Chrome browser
		// maximize the browser window
		// navigate to the login URL
	}

	
	def afterStep(Scenario scenario){
		WebUI.waitForPageLoad(GlobalVariable.ELEMENT_TIMEOUT)

		def driver = DriverFactory.getWebDriver()
		final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)
		scenario.embed(screenshot, "image/png")
	}


	def getValueAndIndex(String value) {
		def values = value.tokenize('[')
		values[1] = values[1].replaceAll('[^\\d.]', '')
		return values
	}
	
	
	// implement the rest of code as you see fit
	
	
	
	
}
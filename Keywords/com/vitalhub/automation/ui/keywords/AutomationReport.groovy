package com.vitalhub.automation.ui.keywords

import java.text.MessageFormat

import org.apache.commons.lang3.StringUtils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords
import com.kms.katalon.core.cucumber.keyword.CucumberReporter
import com.kms.katalon.core.cucumber.keyword.CucumberRunnerResult
import com.kms.katalon.core.cucumber.keyword.internal.CucumberRunnerResultImpl
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.model.RunningMode

import cucumber.api.cli.Main
import internal.GlobalVariable

public class AutomationReport extends CucumberBuiltinKeywords {

	private static final KeywordLogger logger = KeywordLogger.getInstance(CucumberBuiltinKeywords.class)
	/**
	 * Runs the given Feature file with <code>featureId</code> by invoking
	 * {@link cucumber.api.cli.Main#run(String[], ClassLoader)}.
	 * </p>
	 * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/cucumber_report/&lt;current_time_stamp&gt;<code> 
	 * 
	 *  @param tag
	 * Cucumber option tag of Feature file
	 * @param featureFilePath
	 * featureFilePath of Feature file
	 * @param flowControl
	 * an instance {@link FailureHandling} that controls the running flow
	 * @return
	 * an instance of {@link CucumberRunnerResult} that includes status of keyword and report folder location.
	 * 
	 * @since 5.7
	 * @see CucumberRunnerResult
	 */
	@Keyword
	public static CucumberRunnerResult runFeatureFile(String tag, String featureFilePath, FailureHandling flowControl, String defaultOrg = "Default") {
		return KeywordMain.runKeyword({
			if (StringUtils.isEmpty(featureFilePath)) {
				throw new IllegalArgumentException("featureRelativeFilePath param must not be null or empty")
			}

			def featureFile = RunConfiguration.getProjectDir()+"/" + featureFilePath
			def folder = new File( featureFile)

			//TODO: Logic to run Feature file from Default location if no specific Org level feature folder
			if( !folder.exists() ) {
				logger.logInfo(
						MessageFormat.format("FeatureFile: ''{0}'' does not exist",	featureFilePath))

				featureFile = RunConfiguration.getProjectDir()+"/" + featureFilePath.replace(GlobalVariable.org, defaultOrg)


			}

			String reportDir = RunConfiguration.getProjectDir() + "/Reports/" + featureFilePath.replace(".feature", "") + "/cucumber_report/" + System.currentTimeMillis()

			RunningMode runningMode = RunConfiguration.getRunningMode()

			logger.logInfo(
					MessageFormat.format("Starting run keyword runFeatureFile: ''{0}'' and extract report to folder: ''{1}''...",
					featureFilePath, reportDir))
			String[] argv = [
				"-g",
				"com.vitalhub.automation.ui.scripts.steps",
				featureFile,
				"--strict",
				//                "--plugin",
				//                "pretty",
				//"-d",
				"--plugin",
				"html:" + reportDir + "/html",
				"--plugin",
				"json:" + reportDir + "/cucumber.json",
				"--plugin",
				"junit:"+ reportDir + "/cucumber.xml",
				"--plugin",
				CucumberReporter.class.getName()
			]

			if (!tag.equals("")){
				argv = argv + ["--tags", tag]

			}

			if (runningMode == RunningMode.CONSOLE) {
				argv = argv + ["--monochrome"]
			}



			boolean runSuccess = Main.run(argv, CucumberBuiltinKeywords.class.getClassLoader()) == 0
			CucumberRunnerResultImpl cucumberResult = new CucumberRunnerResultImpl(
					runSuccess ? 'passed' : 'failed', reportDir)
			if (runSuccess) {
				logger.logPassed(MessageFormat.format("Feature file: ''{0}'' was passed", featureFilePath))
			} else {
				KeywordMain.stepFailed(MessageFormat.format("Feature file ''{0}'' was failed", featureFilePath), flowControl)
			}
			return cucumberResult
		}, flowControl, "Keyword runFeatureFile was failed")
	}


}

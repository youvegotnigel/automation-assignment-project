package com.vitalhub.automation.ui.keywords
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import kms.turing.katalon.plugins.helper.WebElementHelper
import kms.turing.katalon.plugins.helper.XPathHelper
import kms.turing.katalon.plugins.helper.XPathHelper.CompareOptions
import kms.turing.katalon.plugins.helper.table.HTMLTableHelper
import kms.turing.katalon.plugins.helper.table.WebTableHelper.CellTextOptions

public class HTMLTable extends HTMLTableHelper{


	private static final String BACKEND_TABLE_HEADER_ROW_XPATH = "//table[@class='list_table summary tablesorter float_header']/thead"
	private static final String BACKEND_TABLE_BODY_ROW_XPATH   = "//table[@class='list_table summary tablesorter float_header']/tbody"
	private static final String FIND_TABLE_BY_ROW_XPATH = "//table[*[tr[@{rowCriteria}]]]"


	/**
	 * identify a web table header based on provided column headers
	 * @param columnHeaders the list of column headers used for detecting the table
	 * @param timeout time for searching the web table in seconds
	 * @return WebElement
	 */
	@Keyword
	public static boolean identifyBackendTableHeaderRow(List<String> columnHeaders, int timeout = 0, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE) {

		String headerXpath = ""
		List<String> columnsXpath = []
		boolean found = false

		try {
			WebElement table = WebElementHelper.findWebElement(null, By.xpath(BACKEND_TABLE_HEADER_ROW_XPATH), timeout)

			columnHeaders.each { header ->
				columnsXpath += ".//*[normalize-space(.)='${header}']"
			}

			headerXpath = ".//tr[${columnsXpath.join(" and ")}]"

			WebElement table_header = WebElementHelper.findWebElement(table, By.xpath(headerXpath), timeout)

			found = table_header != null
			handleErrorIf(!found, "Could not find any column header with values '$columnHeaders'", flowControl)
			return found
		}catch(ex){
			handleError(ex, flowControl)
			return found
		}
	}


	/**
	 * identify a web table row based on provided table data values
	 * @param tableData the list of  table data used for detecting the table row
	 * @param timeout time for searching the web table in seconds
	 * @return WebElement
	 */
	@Keyword
	public static boolean identifyBackendTableBodyRow(List<String> tableData, int timeout = 0, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE) {

		String rowXpath = ""
		List<String> columnsXpath = []
		boolean found = false

		try {
			WebElement table = WebElementHelper.findWebElement(null, By.xpath(BACKEND_TABLE_BODY_ROW_XPATH), timeout)

			tableData.each { val ->

				val = BCareVariables.getGlobalVariable(val)
				columnsXpath += ".//*[normalize-space(.)='${val}']"
			}

			rowXpath = ".//tr[${columnsXpath.join(" and ")}]"

			WebElement table_row = WebElementHelper.findWebElement(table, By.xpath(rowXpath), timeout)

			found = table_row != null
			return found
		}catch(ex){
			handleErrorIf(!found, "Could not find any table row with values '$tableData'", flowControl)
			return found
		}
	}

	/**
	 * identify a web table based on provided column headers
	 * @param columnHeaders the list of column headers used for detecting the table
	 * @param elementIndex elementIndex of columnHeader
	 * @param timeout time for searching the web table in seconds
	 * @return WebElement
	 */
	@Keyword
	public static WebElement identifyTableByColumnHeaders(List<String> columnHeaders, int elementIndex = 1, int timeout = 0, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "identify web table using headers: $columnHeaders"
		try{
			String xpath = ""

			List<String> headerTextXpaths = []
			columnHeaders.each{ header ->
				def thTextXpath = XPathHelper.makeTextComparisionXPath('.', header, CompareOptions.CONTAINS)
				thTextXpath = "th[$thTextXpath]"
				headerTextXpaths.add(thTextXpath)
			}

			String rowCriteria = headerTextXpaths.join(' and ')
			xpath = FIND_TABLE_BY_ROW_XPATH.replace("@{rowCriteria}", rowCriteria)

			def usingTdXpath = xpath.replaceAll("th\\[", "td\\[")
			xpath="($xpath|$usingTdXpath)[$elementIndex]"
			println"xpath :: $xpath"
			WebElement table = WebElementHelper.findWebElement(null, By.xpath(xpath), timeout)
			backupElement(table, null, By.xpath(xpath))
			cachedHeaders = getHeaderIndexes(table)
			return table
		}
		catch(ex){
			handleError ex, flowControl
			return null
		}
	}

	/**
	 * identify a web table based on provided column headers
	 * @param tableHeader the headers used for detecting the table
	 * @param elementIndex elementIndex of columnHeader
	 * @param timeout time for searching the web table in seconds
	 * @return WebElement
	 */
	@Keyword
	public static WebElement identifyTableByTableHeader(String tableHeader, int elementIndex = 1, int timeout = 0, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "identify web table using headers: $tableHeader"
		try{
			String xpath = "(//h6[contains(text(), '${tableHeader}')]/following::table)[${elementIndex}]|(//h5[contains(text(), '${tableHeader}')]/following::table)[${elementIndex}]|(//h3[contains(text(), '${tableHeader}')]/following::table)[${elementIndex}]|(//table[contains(@class,'${tableHeader}')])[${elementIndex}]|(//h3[contains(text(), '${tableHeader}')]/../../../parent::table)[${elementIndex}]|(//table[@id='${tableHeader}'])[${elementIndex}]|(//span[contains(text(),'${tableHeader}')])[${elementIndex}]/ancestor::table[@class='noteTable']"

			WebElement table = WebElementHelper.findWebElement(null, By.xpath(xpath), timeout)
			backupElement(table, null, By.xpath(xpath))
			cachedHeaders = getHeaderIndexes(table)
			return table
		}
		catch(ex){
			handleError ex, flowControl
			return null
		}
	}




	/**
	 * Get row index using it's cells info with table row index
	 * @param table the WebElement table which the row belonging to	
	 * @param rowIndex table row index of the table which the row belonging to
	 * @param cellsInfo map of cells info including their column headers and cell values	
	 * @return integer, starts from 1
	 */
	@Keyword
	public static int getRowIndexByCellsInfo(WebElement table, int rowIndex, Map<String, Object> cellsInfo, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "Get index of the row containing the following cells $cellsInfo"
		try{
			table = realocateIfStale(table)
			List<Map<String, Object>> cells = []
			cellsInfo.each{header, text ->
				cells.add([(COLUMN_HEADER): header, (VALUE): text, (CELL_TEXT_OPTION): CellTextOptions.CONTENT_TEXT, (COMPARE_OPTION): CompareOptions.EQUALS])
			}

			List<WebElement> rows = getMatchedAndPrecedingRows(table, cells, rowIndex)
			handleErrorIf(!rows, "Could not find any rows match to searching criteria ${cellsInfo}", flowControl)

			return isHeaderSeparated(table) ? rows.size(): rows.size() - 1
		}catch(ex){
			handleError(ex, flowControl)
			return -1
		}
	}

	/**
	 * Get row index using it's cells info with table column index
	 * @param table the WebElement table which the row belonging to	
	 * @param cellsInfo map of cells info including their column headers and cell values
	 * @param index table  index of table which the row belonging to
	 * @return integer, starts from 1
	 */
	@Keyword
	public static int getRowIndexByCellsInfo(WebElement table, Map<String, Object> cellsInfo, int colIndex=1, int rowIndex=1, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "Get index of the row containing the following cells $cellsInfo"
		List<WebElement> rows
		try{
			table = realocateIfStale(table)
			List<Map<String, Object>> cells = []
			cellsInfo.each{header, text ->
				cells.add([(COLUMN_HEADER): header, (VALUE): text, (CELL_TEXT_OPTION): CellTextOptions.CONTENT_TEXT, (COMPARE_OPTION): CompareOptions.EQUALS])
			}

			if (colIndex==1 && rowIndex==1) {
				rows = getMatchedAndPrecedingRows(table, cells)
				println "no of rows :::" + rows.size()
			}else {
				rows = getMatchedAndPrecedingRows(table, cells, rowIndex, colIndex)
			}


			handleErrorIf(!rows, "Could not find any rows match to searching criteria ${cellsInfo}", flowControl)

			return isHeaderSeparated(table) ? rows.size(): rows.size() - 1
		}catch(ex){
			handleError(ex, flowControl)
			return -1
		}
	}



	/**
	 * Click to check a radio button inside the cell passed in
	 * @param cell the cell that the radio button locating
	 *
	 */
	@Keyword
	public static void setRadio(WebElement cell, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "Click to check the radio button inside the cell $cell"
		try{
			setRadioInCell(cell, true)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Identity a cell web element using row and column indexes
	 * @param table the WebElement table which the row belonging to
	 * @param columnIndex position of the column in the table
	 * @param rowIndex position of the row in the table
	 * @return WebElement
	 */
	@Keyword
	public static WebElement identifyCellByIndexes(WebElement table, int columnIndex, int rowIndex, boolean useColspan=false, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "Identify a cell in table with row index:$rowIndex, column index: $columnIndex"

		try{
			table = realocateIfStale(table)
			def rows = WebElementHelper.findWebElements(table, By.tagName('tr'), FIND_ELEMENT_TIMEOUT)
			def rowNumber = rows.size()
			println "no of rows::: $rowNumber"

			if(rowIndex <= 0 || rowIndex > rowNumber){
				handleError("Invalid row index: $rowIndex. It should start from 1", flowControl)
				return null
			}

			def row = rows[rowIndex-1]
			println "rows::: $rows"
			println "rowIndex::: $rowIndex"
			println "row::: $row"
			def cols = row.findElements(By.xpath(".//td|.//th"))
			println "cols::: $cols"

			if (useColspan) {
				// Scan all of columns to see if any columns use colspan attribute
				def originalValue = columnIndex
				def spanSum = 0
				def currentIndex = 0
				for (def col in cols){
					currentIndex++
					def colspan = col.getAttribute("colspan")
					if (colspan != null){
						def increasement = colspan.toInteger() - 1
						spanSum += increasement
						// If any colspan used, the colspan value subtracted from the index
						columnIndex = columnIndex - increasement
					}
					if (currentIndex + spanSum >= originalValue){
						break
					}
				}
			}

			WebElement cell = cols[columnIndex-1]
			backupElement(cell, table, By.xpath(XPathHelper.GetElementXPath(cell)))
			return cell
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Check/Uncheck a check box base on input value passed in
	 * @param cell the cell that the link belonging to
	 * @param checked the check box state need to be set
	 */
	public static void setRadioInCell(WebElement cell, boolean checked = true){
		info "Set state checked as '$checked' for the checkbox inside the cell $cell"
		WebElement radio = null
		try{
			cell = realocateIfStale(cell)
			String nestedElementXpath = ".//input[@type = 'radio']"
			radio = WebElementHelper.findWebElement(cell, By.xpath(nestedElementXpath), FIND_ELEMENT_TIMEOUT)
			handleErrorIf(!radio, "Could not find any radio controls inside of the cell $cell", null)
			if(radio.selected != checked){
				radio.click()
			}
		}catch(ex){
			if(radio){
				String nearByElementXpath = ".//following-sibling::*|.//preceding-sibling::*"
				WebElement nearbyElement = WebElementHelper.findWebElement(radio, By.xpath(nearByElementXpath), FIND_ELEMENT_TIMEOUT)
				if(nearbyElement){
					nearbyElement.click()
				}
			}
			else{
				throw ex
			}
		}
	}

	/**
	 * Verify a cell with specific text displaying in a table based on column header it's belonging
	 * @param table the table that the cell belonging to
	 * @param columnHeader the column header that the cell belonging to
	 * @param text the text display in cell
	 * @return true/false
	 */
	@Keyword
	public static boolean verifyCellPresentWithText(WebElement table, String columnHeader, String text,	CellTextOptions textOption = CellTextOptions.CONTENT_TEXT, CompareOptions compareOption = CompareOptions.CONTAINS,  FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "Verify existing a cell with value '${text}' in the table $table"
		boolean found = false
		try{
			table = realocateIfStale(table)
			List<Map<String, Object>>cellsInfo = [
				[(COLUMN_HEADER): columnHeader, (COMPARE_OPTION): compareOption, (CELL_TEXT_OPTION): textOption, (VALUE): text]
			]

			WebElement row = getMatchedRow(table, cellsInfo)
			found = row != null
			handleErrorIf(!found, "Could not find any cell with value '$text'", flowControl)
			return found
		}catch(ex){
			handleError(ex, flowControl)
			return found
		}
	}

	/**
	 * Verify a row with specific text displaying under a column header it's belonging
	 * @param colHeader the column header that the cell belonging to
	 * @param cellValue the text display in cell
	 * @return true/false
	 */
	@Keyword
	public static boolean verifyRowPresentWithText(String colHeader, String cellValue) {
		info "Verify existing a cell with value '${cellValue}' under the column header $colHeader"
		boolean found = false
		try {

			String xpath = ".//th[normalize-space(.)='${colHeader}']/following-sibling::td[normalize-space(.)='${cellValue}']"
			return WebElementHelper.findWebElements(null, By.xpath(xpath), FIND_ELEMENT_TIMEOUT)
		}catch(ex) {
			return found
		}
	}

	/**
	 * @param table
	 * @return Total row count of the table
	 */
	@Keyword
	public static int getTotalRowCount(WebElement table){
		def trXpath = "//tbody/tr"

		info "Find no of rows xpath: ${trXpath}"
		List<WebElement> rows = WebElementHelper.findWebElements(table, By.xpath(trXpath), FIND_ELEMENT_TIMEOUT)
		return rows.size()
	}

	private static List<WebElement>getMatchedAndPrecedingRows(WebElement cell){
		String rowsXpath = ".//ancestor-or-self::tr/preceding-sibling::tr"
		return WebElementHelper.findWebElements(cell, By.xpath(rowsXpath), FIND_ELEMENT_TIMEOUT)
	}

	private static List<WebElement>getMatchedAndPrecedingRows(WebElement table, int index, List<Map<String, Object>> cellsInfo){
		String rowsXpath = prepareRowXpath(table, cellsInfo)
		rowsXpath = "($rowsXpath|$rowsXpath/preceding-sibling::tr)[$index]"
		return WebElementHelper.findWebElements(table, By.xpath(rowsXpath), FIND_ELEMENT_TIMEOUT)
	}

	private static List<WebElement>getMatchedAndPrecedingRows(WebElement table, List<Map<String, Object>> cellsInfo){
		String rowsXpath = prepareRowXpath(table, cellsInfo)
		rowsXpath = "$rowsXpath|$rowsXpath/preceding-sibling::tr"
		println "rowsXpath:: $rowsXpath"
		return WebElementHelper.findWebElements(table, By.xpath(rowsXpath), FIND_ELEMENT_TIMEOUT)
	}

	private static String prepareRowXpath(WebElement table, List<Map<String, Object>>cellsInfo){

		String rowXpath = ""
		List<String> columnsXpath = []

		cellsInfo.each{cell ->
			def columnIndex = cell.getAt(COLUMN_INDEX)
			if(!columnIndex){
				String columnHeader = cell.getAt(COLUMN_HEADER)
				columnIndex = getColumnIndex(table, '.', columnHeader)
				handleErrorIf(columnIndex < 0, "Could not find any columns with header '$columnHeader'", null)
			}

			def compareOption = cell.getAt(COMPARE_OPTION)
			if(!compareOption){
				compareOption = CompareOptions.EQUALS
			}

			def cellTextOption = cell.getAt(CELL_TEXT_OPTION)
			if(!cellTextOption){
				compareOption = CellTextOptions.CONTENT_TEXT
			}
			def caseSensitive = cell.getAt(CASE_SENSITIVE)
			if(caseSensitive == null){
				caseSensitive = false
			}

			String value = cell.getAt(VALUE)
			String cellCriteria = makeCellTextComparisonXpath(cellTextOption, compareOption, value, caseSensitive)
			columnsXpath += ".//*[${cellCriteria} and position()=${columnIndex} and (local-name()='td' or local-name()='th')]"
		}
		rowXpath = columnsXpath.join(" and ")
		return "(//tr[$rowXpath])[1]"
	}

	private static List<String> prepareColumnsXpath(WebElement table, List<Map<String, Object>>cellsInfo){

		List<String> columnsXpath = []

		cellsInfo.each{cell ->
			def columnIndex = cell.getAt(COLUMN_INDEX)
			if(!columnIndex){
				String columnHeader = cell.getAt(COLUMN_HEADER)
				columnIndex = getColumnIndex(table, '.', columnHeader)
				handleErrorIf(columnIndex < 0, "Could not find any columns with header '$columnHeader'", null)
			}

			def compareOption = cell.getAt(COMPARE_OPTION)
			if(!compareOption){
				compareOption = CompareOptions.EQUALS
			}

			def cellTextOption = cell.getAt(CELL_TEXT_OPTION)
			if(!cellTextOption){
				compareOption = CellTextOptions.CONTENT_TEXT
			}
			def caseSensitive = cell.getAt(CASE_SENSITIVE)
			if(caseSensitive == null){
				caseSensitive = false
			}

			String value = cell.getAt(VALUE)
			String cellCriteria = makeCellTextComparisonXpath(cellTextOption, compareOption, value, caseSensitive)
			columnsXpath += "*[${cellCriteria} and position()=${columnIndex} and (local-name()='td' or local-name()='th')]"
		}
		return columnsXpath
	}

	private static List<String> prepareColumnsXpathStrikeTable(WebElement table, List<Map<String, Object>>cellsInfo){

		List<String> columnsXpath = []

		cellsInfo.each{cell ->
			def columnIndex = cell.getAt(COLUMN_INDEX)
			if(!columnIndex){
				String columnHeader = cell.getAt(COLUMN_HEADER)
				columnIndex = getColumnIndex(table, '.', columnHeader)
				handleErrorIf(columnIndex < 0, "Could not find any columns with header '$columnHeader'", null)
			}

			def compareOption = cell.getAt(COMPARE_OPTION)
			if(!compareOption){
				compareOption = CompareOptions.EQUALS
			}

			def cellTextOption = cell.getAt(CELL_TEXT_OPTION)
			if(!cellTextOption){
				compareOption = CellTextOptions.CONTENT_TEXT
			}
			def caseSensitive = cell.getAt(CASE_SENSITIVE)
			if(caseSensitive == null){
				caseSensitive = false
			}

			String value = cell.getAt(VALUE)
			String cellCriteria = makeCellTextComparisonXpath(cellTextOption, compareOption, value, caseSensitive)
			columnsXpath += "td[position()=${columnIndex} and strike[${cellCriteria}]]|tr[position()=${columnIndex} and strike[${cellCriteria}]]"
		}
		return columnsXpath
	}

	/**
	 * Get position of a column based on it's attribute
	 */
	private static int getColumnIndex(WebElement table, String attribute, String value){
		int index
		if(attribute != '.' && attribute != 'text()'){
			attribute = "@$attribute"
		}
		List<WebElement> columns = findColumnHeaders(table, attribute, value)
		if(!columns){
			Map<String, Integer> headerIndexes = getHeaderIndexes(table)
			int cahedIndex = getCachedHeaderIndex(value)
			def newHeader = headerIndexes.getAt(cahedIndex)
			if(newHeader && newHeader.toLowerCase().contains(value.toLowerCase())){
				columns = findColumnHeaders(table, attribute, newHeader)
			}
		}

		return columns ? columns.size() : - 1
	}

	private static List<WebElement> findColumnHeaders(WebElement table, String attribute, String value){
		def textXpath = XPathHelper.makeTextComparisionXPath(attribute, value, CompareOptions.CONTAINS)
		def thTextXpath = "th[$textXpath]"
		def tdTextXpath = "td[$textXpath]"
		def columsXpath = ".//tr/$thTextXpath|.//tr/$tdTextXpath|.//tr/$thTextXpath/preceding-sibling::th|.//tr/$tdTextXpath/preceding-sibling::td"
		info "Find columns matching to search criteira xpath: ${columsXpath}"
		List<WebElement> columns = WebElementHelper.findWebElements(table, By.xpath(columsXpath), FIND_ELEMENT_TIMEOUT)
		return columns
	}

	private static WebElement getMatchedRow(WebElement table, List<Map<String, Object>> cellsInfo){
		String rowXpath = prepareRowXpath(table, cellsInfo)
		info "rowXpath: ${rowXpath}"
		return WebElementHelper.findWebElement(table, By.xpath(rowXpath), FIND_ELEMENT_TIMEOUT)
	}

	public static boolean verifyTableCellPresentWithText(WebElement table, List<Map<String, String>> values, CellTextOptions textOption = CellTextOptions.CONTENT_TEXT, CompareOptions compareOption = CompareOptions.CONTAINS,  FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "Verify existing a cell with value '${values}' in the table $table"

		String rowXpath = ""
		List<String> columnsXpath = []
		try{
			table = realocateIfStale(table)
			for (value in values){
				value.each {
					if(!it.value.equals('')){
						List<Map<String, Object>>cellsInfo = [
							[(COLUMN_HEADER): it.key, (COMPARE_OPTION): compareOption, (CELL_TEXT_OPTION): textOption, (VALUE): it.value]
						]
						columnsXpath += prepareColumnsXpath(table, cellsInfo)
					}
				}
			}
			rowXpath = ".//tr[${columnsXpath.join(" and ")}]"
			info "rowXpath :: $rowXpath"

			WebElement row = WebElementHelper.findWebElement(table, By.xpath(rowXpath), FIND_ELEMENT_TIMEOUT)
			info "row :: $row"

			// Convert the WebElement to TestObject
			TestObject testObject = new TestObject()
			testObject.addProperty("xpath", ConditionType.EQUALS, rowXpath)

			WebUI.scrollToElement(testObject, FIND_ELEMENT_TIMEOUT, FailureHandling.OPTIONAL)

			return row != null ? true : false
		} catch (NoSuchElementException ex) {
			info("No such element found: " + ex.getMessage());
			return false
		} catch (Exception ex) {
			handleError(ex, flowControl);
			return false
		}
	}

	public static boolean verifyTableCellPresentWithStrikeText(WebElement table, List<Map<String, String>> values, CellTextOptions textOption = CellTextOptions.CONTENT_TEXT, CompareOptions compareOption = CompareOptions.CONTAINS,  FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		info "Verify existing a cell with value '${values}' in the table $table"

		String rowXpath = ""
		List<String> columnsXpath = []
		try{
			table = realocateIfStale(table)
			for (value in values){
				value.each {
					if(!it.value.equals('')){
						List<Map<String, Object>>cellsInfo = [
							[(COLUMN_HEADER): it.key, (COMPARE_OPTION): compareOption, (CELL_TEXT_OPTION): textOption, (VALUE): it.value]
						]
						columnsXpath += prepareColumnsXpathStrikeTable(table, cellsInfo)
					}
				}
			}
			rowXpath = ".//tr[${columnsXpath.join(" and ")}]"
			info "rowXpath :: $rowXpath"

			WebElement row = WebElementHelper.findWebElement(table, By.xpath(rowXpath), FIND_ELEMENT_TIMEOUT)
			info "row :: $row"

			// Convert the WebElement to TestObject
			TestObject testObject = new TestObject()
			testObject.addProperty("xpath", ConditionType.EQUALS, rowXpath)

			WebUI.scrollToElement(testObject, FIND_ELEMENT_TIMEOUT, FailureHandling.OPTIONAL)

			return row != null ? true : false
		} catch (NoSuchElementException ex) {
			info("No such element found: " + ex.getMessage());
			return false
		} catch (Exception ex) {
			handleError(ex, flowControl);
			return false
		}
	}
}
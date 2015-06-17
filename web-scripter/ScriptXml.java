package com.igloosec.SpDbReader.entity.script;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.google.common.base.Preconditions;
import com.igloosec.SpDbReader.common.Conf;
import com.igloosec.SpDbReader.common.JsEncoder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.BaseException;

public class ScriptXml {
	private long id;
	private long periodInSec;
	private long expiredTimeInHour;
	private String delimiter;
	private List<BindedTable> bindedTables = new ArrayList<BindedTable>();
	
	public ScriptXml() {
		this.id = System.currentTimeMillis();
	} //INIT
	
	public ScriptXml(long periodInSec, long expiredTimeInHour, String delimiter){
		this.periodInSec = periodInSec;
		this.expiredTimeInHour = expiredTimeInHour;
		this.delimiter = delimiter;
		this.id = System.currentTimeMillis();
	} //INIT
	
	public ScriptXml bindTable(BindedTable bindedTable) throws IllegalArgumentException{
		try{
			Preconditions.checkArgument(bindedTable.getType() == BindedTable.TYPE_SELECT_COLUMN_WRITE_FILE ||
										bindedTable.getType() == BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE||
										bindedTable.getType() == BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE);
			
			Preconditions.checkNotNull(bindedTable.getTableName());
			Preconditions.checkNotNull(bindedTable.getDbName());
			Preconditions.checkNotNull(bindedTable.getSelectColumn());
			Preconditions.checkNotNull(bindedTable.getOutputPath());
			Preconditions.checkNotNull(bindedTable.getCharset());
			switch(bindedTable.getType()){
				case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE:
					Preconditions.checkNotNull(((DateBindedTable)bindedTable).getConditionColumn());
					break;
				case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE:
					Preconditions.checkNotNull(((SeqBindedTable)bindedTable).getConditionColumn());
					break;
			} //switch
			
			bindedTables.add(bindedTable);
			return this;
		} catch(IllegalArgumentException e){
			throw new  IllegalArgumentException("invalid type : " + bindedTable.getType(), e);
		} catch(NullPointerException e){
			throw new  IllegalArgumentException(e);
		} //catch
	} //bindTable
	
	public long getPeriodInSec() {
		return periodInSec;
	}
	
	public long getExpiredTimeInHour(){
		return expiredTimeInHour;
	} //getExpiredTimeInHour

	public void setPeriodInSec(long periodInSec) {
		this.periodInSec = periodInSec;
	}

	public void setExpiredTimeInHour(long expiredTimeInHour){
		this.expiredTimeInHour=expiredTimeInHour;
	} 
	
	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public List<BindedTable> getBindedTables() {
		return bindedTables;
	}

	public void setBindedTables(List<BindedTable> bindedTables) {
		this.bindedTables = bindedTables;
	}

	public String toXml() throws IOException {
		Document doc = DocumentHelper.createDocument();
		Element scriptXmlNode = doc.addElement("ScriptXml");
		scriptXmlNode.addElement("periodInSec").setText(getPeriodInSec()+"");
		scriptXmlNode.addElement("expiredTimeInHour").setText(getExpiredTimeInHour()+"");
		scriptXmlNode.addElement("delimiter").setText(getDelimiter());
		Element bindedTablesNode = scriptXmlNode.addElement("bindedTables").addAttribute("count", getBindedTables().size()+"");
		for(BindedTable bindedTable : getBindedTables()){
			Element bindedTableNode = bindedTablesNode.addElement("bindedTable");
			bindedTableNode.addElement("type").setText(bindedTable.getType()+"");
			bindedTableNode.addElement("tableName").setText(bindedTable.getTableName());
			bindedTableNode.addElement("dbName").setText(bindedTable.getDbName());
			bindedTableNode.addElement("selectColumn").setText(bindedTable.getSelectColumn());
			bindedTableNode.addElement("outputPath").setText(bindedTable.getOutputPath());
			bindedTableNode.addElement("charset").setText(bindedTable.getCharset());
	
			switch(bindedTable.getType()){
			case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE:{
				DateBindedTable dateBindedTable = (DateBindedTable) bindedTable;
				bindedTableNode.addElement("conditionColumn").setText(dateBindedTable.getConditionColumn());
				break;
			} //case date table binding
			case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE:{
				SeqBindedTable seqBindedTable = (SeqBindedTable) bindedTable;
				bindedTableNode.addElement("conditionColumn").setText(seqBindedTable.getConditionColumn());
				break;
			} //case seq table binding
			} //switch
		} //for bindedTable

		StringWriter out = new StringWriter();
		XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
		writer.write(doc);
		writer.flush();
		return out.toString();
	} //toXml

	public static ScriptXml fromXml(String xml) throws IllegalArgumentException{
		try {
			ScriptXml scriptXml = new ScriptXml();
			Document doc = DocumentHelper.parseText(xml);
			scriptXml.setPeriodInSec(Long.parseLong(((Element)doc.selectNodes("/ScriptXml/periodInSec").get(0)).getText()));
			scriptXml.setExpiredTimeInHour(Long.parseLong(((Element)doc.selectNodes("/ScriptXml/expiredTimeInHour").get(0)).getText()));
			scriptXml.setDelimiter(((Element)doc.selectNodes("/ScriptXml/delimiter").get(0)).getText());
			for(Element bindedTableNode : (List<Element>)doc.selectNodes("/ScriptXml/bindedTables/bindedTable")){
				String tableName = ((Element)bindedTableNode.selectNodes("tableName").get(0)).getText();
				String dbName = ((Element)bindedTableNode.selectNodes("dbName").get(0)).getText();
				String selectColumn = ((Element)bindedTableNode.selectNodes("selectColumn").get(0)).getText();
				String outputPath = ((Element)bindedTableNode.selectNodes("outputPath").get(0)).getText();
				String charset = ((Element)bindedTableNode.selectNodes("charset").get(0)).getText();
				int type = Integer.parseInt(((Element)bindedTableNode.selectNodes("type").get(0)).getText());
				switch(type){
				case BindedTable.TYPE_SELECT_COLUMN_WRITE_FILE:{
					scriptXml.bindTable(new SimpleBindedTable(tableName, dbName, selectColumn, outputPath, charset));
					break;
				} //case simple binding
				case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE:{
					String conditionColumn = ((Element)bindedTableNode.selectNodes("conditionColumn").get(0)).getText();
					scriptXml.bindTable(new DateBindedTable(tableName, dbName, selectColumn, outputPath, charset, conditionColumn));
					break;
				} //case date binding
				case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE:{
					String conditionColumn = ((Element)bindedTableNode.selectNodes("conditionColumn").get(0)).getText();
					scriptXml.bindTable(new SeqBindedTable(tableName, dbName, selectColumn, outputPath, charset, conditionColumn));
					break;
				} //case seq binding
				} //switch
			} //for bindedTableNode
			return scriptXml;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		} //catch
	} //fromXml

	private String getScript_commonVariable(){
		StringBuilder scriptSb=new StringBuilder();

		for(int i=0; i<getBindedTables().size(); i++){
			BindedTable bindedTable = getBindedTables().get(i);
			scriptSb.append(String.format("//%03d, type : %s\n", i+1, bindedTable.getType()));
			scriptSb.append(String.format("var dbName%03d = \"%s\";\n", i+1, bindedTable.getDbName()));
			scriptSb.append(String.format("var originalTableName%03d = \"%s\";\n", i+1, bindedTable.getTableName()));
			scriptSb.append(String.format("var selectColumn%03d = \"%s\";\n", i+1, bindedTable.getSelectColumn()));
			scriptSb.append(String.format("var outputPath%03d = \"%s\";\n", i+1, bindedTable.getOutputPath()));
			scriptSb.append(String.format("var charset%03d = \"%s\";\n", i+1, bindedTable.getCharset()));
			switch(bindedTable.getType()){
			case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE :
				scriptSb.append(String.format("var conditionColumn%03d = \"%s\";\n", i+1, ((DateBindedTable)bindedTable).getConditionColumn()));
				break;
			case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE :
				scriptSb.append(String.format("var conditionColumn%03d = \"%s\";\n", i+1, ((SeqBindedTable)bindedTable).getConditionColumn()));
				break;
			} //switch
			scriptSb.append("\n");
		} //for bindedTable

		return scriptSb.toString();
	} //getScript_commonVariable

//	private String getScript_convertTableName(){
//		StringBuilder scriptSb = new StringBuilder();
//		scriptSb.append(String.format("\t\tvar currentTime = dateUtil.currentTimeMillis();\n"));
//		scriptSb.append(String.format("\t\tvar yyyy = dateUtil.format(currentTime, 'yyyy');\n"));
//		scriptSb.append(String.format("\t\tvar mm = dateUtil.format(currentTime, 'MM');\n"));
//		scriptSb.append(String.format("\t\tvar dd = dateUtil.format(currentTime, 'dd');\n"));
//		scriptSb.append(String.format("\t\tvar hh = dateUtil.format(currentTime, 'HH');\n"));
//		scriptSb.append(String.format("\t\tvar mi = dateUtil.format(currentTime, 'mm');\n\n"));
//		
//		for (int i = 0; i < getBindedTables().size(); i++) {
//			scriptSb.append(String.format(
//					"\t\tvar tableName%03d = originalTableName%03d"
//					+ ".replace(\"$yyyy\", yyyy)"
//					+ ".replace(\"$mm\", mm)"
//					+ ".replace(\"$dd\", dd)"
//					+ ".replace(\"$hh\", hh)"
//					+ ".replace(\"$mi\", mi);\n", i+1, i+1));
//		} //for i
//
//		return scriptSb.toString();
//	} //getScript_convertTableName
	
	private String getScript_getTableNameFunction(){
		StringBuilder scriptSb = new StringBuilder();
		scriptSb.append("function getTableName(originalTableName){ \n");
		scriptSb.append("\tvar currentTime = dateUtil.currentTimeMillis();\n");
		scriptSb.append("\tvar yyyy = dateUtil.format(currentTime, 'yyyy');\n");
		scriptSb.append("\tvar mm = dateUtil.format(currentTime, 'MM');\n");
		scriptSb.append("\tvar dd = dateUtil.format(currentTime, 'dd');\n");
		scriptSb.append("\tvar hh = dateUtil.format(currentTime, 'HH');\n");
		scriptSb.append("\tvar mi = dateUtil.format(currentTime, 'mm');\n\n");
		scriptSb.append("\treturn originalTableName"
										+ ".replace(\"$yyyy\", yyyy)"
										+ ".replace(\"$mm\", mm)"
										+ ".replace(\"$dd\", dd)"
										+ ".replace(\"$hh\", hh)"
										+ ".replace(\"$mi\", mi);\n");
		scriptSb.append(String.format("} //getTableName \n"));
		
		return scriptSb.toString();
	} //getScript_getTableNameFunction
	
	private String getScript_getMaxQuery(){
		StringBuilder scriptSb = new StringBuilder();

		for (int i = 0; i < getBindedTables().size(); i++) {
			BindedTable bindedTable = getBindedTables().get(i);
			if(bindedTable.getType() == BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE)
//				scriptSb.append(String.format("var getMaxQuery%03d = \"select max(\" + conditionColumn%03d + \") from \" + tableName%03d;\n", i+1, i+1, i+1));
				scriptSb.append(String.format("\t\t\tvar getMaxQuery%03d = \"select max(\" + conditionColumn%03d + \") from \" + getTableName(originalTableName%03d);\n", i+1, i+1, i+1));
		} //for i

		return scriptSb.toString();
	} //getScript_getMaxQuery

	private String getScript_getSmallerConditionFromSimpleRepo(){
		StringBuilder scriptSb = new StringBuilder();

		for (int i = 0; i < getBindedTables().size(); i++) {
			BindedTable bindedTable = getBindedTables().get(i);
			switch(bindedTable.getType()){
			case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE :
				scriptSb.append(String.format("//%03d\n", i+1));
				scriptSb.append(String.format("var smallerCondition%03d = simpleRepo.load(id + \"%03d\");\n", i+1, i+1));
				scriptSb.append(String.format("if(smallerCondition%03d === null)\n", i+1));
				scriptSb.append(String.format("\tsmallerCondition%03d = dateUtil.format(0, \"yyyy-MM-dd HH:mm:ss\");", i+1));
				scriptSb.append("\n");
				break;
			case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE :
				scriptSb.append(String.format("//%03d\n", i+1));
				scriptSb.append(String.format("var smallerCondition%03d = simpleRepo.load(id + \"%03d\");\n", i+1, i+1));
				scriptSb.append(String.format("if(smallerCondition%03d === null)\n", i+1));
				scriptSb.append(String.format("\tsmallerCondition%03d = \"0\";", i+1));
				scriptSb.append("\n");
				break;
			} //switch
		} //for i

		return scriptSb.toString();
	} //getScript_getSmallerConditionFromSimpleRepo

	private String getScript_getBiggerConditionFromDb(){
		StringBuilder scriptSb = new StringBuilder();

		for (int i = 0; i < getBindedTables().size(); i++) {
			BindedTable bindedTable = getBindedTables().get(i);

			switch(bindedTable.getType()){
			case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE :
				scriptSb.append(String.format("\t\t\t//%03d\n", i+1));
				scriptSb.append(String.format("\t\t\tvar biggerCondition%03d = dateUtil.format(dateUtil.currentTimeMillis(), \"yyyy-MM-dd HH:mm:ss\");\n", i+1)); 
				break;
			case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE :
				scriptSb.append(String.format("\t\t\t//%03d\n", i+1));
				scriptSb.append(String.format("\t\t\tvar biggerCondition%03d = dbHandler.selectQuery(dbName%03d, getMaxQuery%03d);\n", i+1, i+1, i+1));
				scriptSb.append(String.format("\t\t\tif(biggerCondition%03d === null)\n", i+1));
				scriptSb.append(String.format("\t\t\t\tbiggerCondition%03d = \"0\";\n\n", i+1));
				break;
			} //switch
		} //for i

		return scriptSb.toString();
	} //getScript_getBIggerConditionFromDb

	private String getScript_queryAndWriteFile() throws IllegalArgumentException{
		StringBuilder scriptSb = new StringBuilder();

		for (int i = 0; i < getBindedTables().size(); i++) {
			BindedTable bindedTable = getBindedTables().get(i);
			switch(bindedTable.getType()){
			case BindedTable.TYPE_SELECT_COLUMN_WRITE_FILE : {
//				scriptSb.append(String.format("\t\tvar query%03d = \"select \" + selectColumn%03d + \" from \" + tableName%03d;\n", i+1, i+1, i+1));
				scriptSb.append(String.format("\t\t\tvar query%03d = \"select \" + selectColumn%03d + \" from \" + getTableName(originalTableName%03d);\n", i+1, i+1, i+1));
				break;
			} //case simple binding
			case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE : {
				String dbVendor = Conf.getDbVendor(bindedTable.getDbName());
				if(dbVendor == null)
					throw new IllegalArgumentException(String.format("failed to find db : %s", bindedTable.getDbName()));
				if(dbVendor.equals(Conf.DB_VENDOR_ORACLE) || dbVendor.equals(Conf.DB_VENDOR_DB2) || dbVendor.equals(Conf.DB_VENDOR_TIBERO) || dbVendor.equals(Conf.DB_VENDOR_ETC)){
					scriptSb.append(String.format("\t\t\tvar query%03d = \"select \" + selectColumn%03d + \" from \" + getTableName(originalTableName%03d) + \n", i+1, i+1, i+1));
					scriptSb.append(String.format("\t\t\t\t\t\" where \" + conditionColumn%03d + \" > to_date('\" + smallerCondition%03d + \"', 'YYYY-MM-DD HH24:MI:SS') \" + \n", i+1, i+1));
					scriptSb.append(String.format("\t\t\t\t\t\" and \" + conditionColumn%03d + \" <= to_date('\" + biggerCondition%03d + \"', 'YYYY-MM-DD HH24:MI:SS') \";\n", i+1, i+1));
				} else if(dbVendor.equals(Conf.DB_VENDOR_MYSQL)){
					scriptSb.append(String.format("\t\t\tvar query%03d = \"select \" + selectColumn%03d + \" from \" + getTableName(originalTableName%03d) + \n", i+1, i+1, i+1));
					scriptSb.append(String.format("\t\t\t\t\t\" where \" + conditionColumn%03d + \" > str_to_date('\" + smallerCondition%03d + \"'", i+1, i+1)).append(", '%Y-%m-%d %H:%i:%s') \" + \n");
					scriptSb.append(String.format("\t\t\t\t\t\" and \" + conditionColumn%03d + \" <= str_to_date('\" + biggerCondition%03d + \"'", i+1, i+1)).append("'%Y-%m-%d %H:%i:%s') \";\n");
				} else if(dbVendor.equals(Conf.DB_VENDOR_MSSQL)){
					scriptSb.append(String.format("\t\t\tvar query%03d = \"select \" + selectColumn%03d + \" from \" + getTableName(originalTableName%03d) + \n", i+1, i+1, i+1));
					scriptSb.append(String.format("\t\t\t\t\t\" where \" + conditionColumn%03d + \" > '\" + smallerCondition%03d + \"'\"+\n", i+1, i+1));
					scriptSb.append(String.format("\t\t\t\t\t\" and \" + conditionColumn%03d + \" <= '\" + biggerCondition%03d + \"'\";\n", i+1, i+1));
				} //if
				break;
			} //case date binding
			case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE : {
				scriptSb.append(String.format("\t\t\tvar query%03d = \"select \" + selectColumn%03d + \" from \" + getTableName(originalTableName%03d) + \n", i+1, i+1, i+1)); 
				scriptSb.append(String.format("\t\t\t\t\t\" where \" + conditionColumn%03d + \" > \" + smallerCondition%03d + \n", i+1, i+1));
				scriptSb.append(String.format("\t\t\t\t\t\" and \" + conditionColumn%03d + \" <= \" + biggerCondition%03d;\n", i+1, i+1));
				break;
			} //case date binding
			} //switch

			scriptSb.append(String.format("\t\t\tvar outputFilename%03d = outputPath%03d + getTableName(originalTableName%03d) + \"_\" + dateUtil.format(dateUtil.currentTimeMillis(), \"yyyyMMddHH\") + \".txt\";\n", i+1, i+1, i+1));
			scriptSb.append(String.format("\t\t\tdbHandler.selectAndAppend(dbName%03d, query%03d, delimiter, outputFilename%03d, charset%03d);\n", i+1, i+1, i+1, i+1));
			scriptSb.append(String.format("\n"));
		} //for i

		return scriptSb.toString();
	} //getScript_queryAndWriteFile

	private String getScript_setBiggerConditionToSimpleRepo(){
		StringBuilder scriptSb = new StringBuilder();

		for (int i = 0; i < getBindedTables().size(); i++) {
			BindedTable bindedTable = getBindedTables().get(i);

			switch(bindedTable.getType()){
			case BindedTable.TYPE_SELECT_COLUMN_BY_DATE_WRITE_FILE :
			case BindedTable.TYPE_SELECT_COLUMN_BY_SEQ_WRITE_FILE :
				scriptSb.append(String.format("\t\t\t//%03d\n", i+1));
				scriptSb.append(String.format("\t\t\tsmallerCondition%03d = biggerCondition%03d;\n", i+1, i+1));
				scriptSb.append(String.format("\t\t\tsimpleRepo.store(id + \"%03d\", biggerCondition%03d);\n\n", i+1, i+1));
				break;
			} //switch
		} //for i

		return scriptSb.toString();
	} //getScript_setBiggerConditionToSimpleRepo

	public String toScript()throws IllegalArgumentException{
		StringBuilder scriptSb=new StringBuilder();

		scriptSb.append(String.format("var id=\"%s\";\n\n", this.id));

		scriptSb.append(getScript_getTableNameFunction()).append("\n\n");
		
		scriptSb.append("//common\n");
		scriptSb.append(String.format("var period = %s * 1000;\n", this.periodInSec));
		if(this.expiredTimeInHour>0){
			scriptSb.append(String.format("var expiredTime= %s * 60 * 60 * 1000\n", this.expiredTimeInHour));
			scriptSb.append("outputFileDeleteTask.startMonitoring(10*1000, expiredTime);\n");
		} //if

		scriptSb.append("\n");
		scriptSb.append(String.format("var delimiter = \"%s\";\n", JsEncoder.encode(this.delimiter))); 

		scriptSb.append(getScript_commonVariable()).append("\n");
		scriptSb.append("//-----------------------------------------------------------------------\n");

		scriptSb.append("logger.info(\"script started\");\n");
		scriptSb.append(getScript_getSmallerConditionFromSimpleRepo()).append("\n");

		scriptSb.append("scheduler.schedule(period, new java.lang.Runnable(){\n");
		scriptSb.append("\trun:function(){\n");
		
		scriptSb.append("\t\ttry{\n");
		
		scriptSb.append("\t\t\tlogger.info(\"task started\");\n\n");
		
		scriptSb.append(getScript_getMaxQuery()).append("\n");
		
		scriptSb.append(getScript_getBiggerConditionFromDb()).append("\n");
		scriptSb.append(getScript_queryAndWriteFile()).append("\n");
		scriptSb.append(getScript_setBiggerConditionToSimpleRepo()).append("\n");

		scriptSb.append("\t\t\tlogger.info(\"task finished\");\n");
		
		scriptSb.append("\t\t} catch(e){\n");
		scriptSb.append("\t\t\tlogger.error(e);\n");
		scriptSb.append("\t\t} //catch\n");
		
		scriptSb.append("\t} //run\n");
		scriptSb.append("});\n");

		return scriptSb.toString();
	} //toScript

	@Override
	public String toString() throws IllegalArgumentException{
		return toScript();
	} //toScript
} //class
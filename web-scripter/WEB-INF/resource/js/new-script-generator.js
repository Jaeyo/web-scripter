NewScriptGenerator = function(dbName, dbVendor, selectColumn, tableName, bindingType, period, expiredTimeInHour, delimiter, outputPath, charset){
	this.id = new Date().getTime();
	this.dbName = dbName;
	this.dbVendor = dbVendor;
	this.selectColumn = selectColumn;
	this.tableName = tableName;
	this.bindingType = bindingType;
	this.period = period;
	this.expiredTimeInHour = expiredTimeInHour;
	this.delimiter = delimiter;
	this.outputPath = outputPath;
	this.charset = charset;
}; //INIT
NewScriptGenerator.prototype = {
	getScript: function(){
		var script = '';
		script += 'var id = "' + this.id + '";\n\n';
		
		script += this.getScript_getTableNameFunction() + '\n\n';
		
		script += '//common\n';
		script += 'var period = ' + this.period + ' * 1000;\n';
		if(this.expiredTimeInHour > 0){
			script += 'var expiredTime = ' + this.expiredTimeInHour + ' * 60 * 60 * 1000\n';
			script += 'outputFileDeleteTask.startMonitoring(10*1000, expiredTime);\n';
		} //if
		
		script += '\n';
		script += 'var delimiter = "' + this.delmiter + '";\n';
		
		script += this.getScript_commonVariable() + '\n';
		script += '//-----------------------------------------------------------------\n';
		
		script += 'logger.info("script started");\n';
		if(this.bindingType != 'simple')
			script += getScript_getSmallerConditionFromSimpleRepo() + '\n';
		
		script += 'scheduler.schedule(period, new java.lang.Runnable(){\n';
		script += '\trun: function(){\n';
		script += '\t\ttry {\n';
		script += '\t\t\tlogger.info("task started");\n\n';
		
		if(this.bindingType === 'sequence')
			script += getScript_getMaxQuery() + '\n';
		
		if(this.bindingType != 'simple')
			script += getScript_getBiggerConditionFromDb() + '\n';
		
		script += getScript_queryAndWriteFile() + '\n';
		if(this.bindingType != 'simple')
			script += getScript_setBiggerConditionToSimpleRepo() + '\n';
	
		script += '\t\t\tlogger.info("task finished");\n';
		script += '\t\t} catch(e){ \n';
		script += '\t\t\tlogger.error(e);\n';
		script += '\t\t} //catch\n';
			
		script += '\t} //run\n';
		script += '});\n';
		
		return script;
	}, //getScript
	
	getScript_getTableNameFunction: function(){
		var script = '';
		script += 'function getTableName(originalTableName){ \n';
		script += '\tvar currentTime = dateUtil.currentTimeMillis();\n';
		script += '\tvar yyyy = dateUtil.format(currentTime, "yyyy");\n';
		script += '\tvar mm = dateUtil.format(currentTime, "MM");\n';
		script += '\tvar dd = dateUtil.format(currentTime, "dd");\n';
		script += '\tvar hh = dateUtil.format(currentTime, "HH");\n';
		script += '\tvar mi  = dateUtil.format(currentTime, "mm");\n';
		script += '\treturn originalTableName'
						+ '.replace("$yyyy", yyyy)'
						+ '.replace("$mm", mm)'
						+ '.replace("$dd", dd)'
						+ '.replace("$hh", hh)'
						+ '.replace("$mi", mi);\n';
		script += '} //getTableName \n';
		return script;
	}, //getScript_getTableNameFunction
	
	getScript_commonVariable: function(){
		var script = '';
		script += 'var dbName = "' + this.dbName + '";\n';
		script += 'var originalTableName = "' + this.tableName + '";\n';
		script += 'var selectColumn = "' + this.selectColumn + '";\n';
		script += 'var outputPath = "' + this.outputPath + '";\n';
		script += 'var charset = "' + this.charset+ '";\n';
		
		if(this.bindingType === 'sequence'){
			script += 'var conditionColumn = "' + this.sequenceColumn + '";\n';
		} else if(this.bindingType === 'date'){
			script += 'var conditionColumn = "' + this.dateColumn + '";\n';
		} //if
		
		script += '\n';
		return script;
	}, //getScript_commonVariable
	
	getScript_getSmallerConditionFromSimpleRepo: function(){
		var script = '';
		if(this.bindingType === 'sequence'){
			script += 'var smallerCondition = simpleRepo.load(id);\n';
			script += 'if(smallerCondition === null)\n';
			script += '\tsmallerCondition = dateUtil.format(0, "yyyy-MM-dd HH:mm:ss");\n'
			script += '\n';
		} else if(this.bindingType === 'date'){
			script += 'var smallerCondition = simpleRepo.load(id);\n';
			script += 'if(smallerCondition === null)\n';
			script += '\tsmallerCondition = 0;\n';
			script += '\n';
		} //if
		
		return script;
	}, //getScript_getSmallerConditionFromSimpleRepo
	
	getScript_getMaxQuery: function(){
		var script = '';
		if(this.bindingType === 'sequence')
			script += '\t\t\tvar getMaxQuery = "select max(" + conditionColumn + ") from " + getTableName(originalTableName);\n';
		return script;
	}, //getScript_getMaxQuery
	
	getScript_getBiggerConditionFromDb: function(){
		var script = '';
		if(this.bindingType ==='sequence'){
			script += '\t\t\tvar biggerCondition = dateUtil.format(dateUtil.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");\n';
		} else if(this.bindingType === 'date'){
			script += '\t\t\tvar biggerCondition = dbHandler.selectQuery(dbName, getMaxQuery);\n';
			script += '\t\t\tif(biggerCondition === null)\n';
			script += '\t\t\tbiggerCondition = "0";\n';
		} //if
		return script;
	}, //getScript_getBiggerConditionFromDb
	
	getScript_queryAndWriteFile: function(){
		var script = '';
		
		if(this.bindngType === 'simple'){
			script += '\t\t\tvar query = "select " + selectColumn + " from " + getTableName(originalTableName);\n';
		} else if(this.bindingType === 'sequence'){
			script += '\t\t\tvar query = "select " + selectColumn + " from " + getTableName(originalTableName) + \n';
			script += '\t\t\t\t\t" where " + conditionColumn + " > " + smallerCondition + \n';
			script += '\t\t\t\t\t" and " + conditionColumn + " <= " + biggerCondition;\n';
		} else if(this.bindingType === 'date'){
			script += '\t\t\tvar query = "select " + selectColumn + " from " + getTableName(originalTableName) + \n';
			if(this.dbVendor === 'oracle' || this.dbVendor === 'db2' || this.dbVendor === 'tibero' || this.dbVendor === 'etc'){
				script += '\t\t\t\t\t" where " + conditionColumn + " > to_date(\'" + smallerCondition + "\', \'YYYY-MM-DD HH24:MI:SS\') "\n';
				script += '\t\t\t\t\t" and " + conditionColumn + " <= to_date(\'" + biggerCondition + "\', \'YYYY-MM-DD HH24:MI:SS\') "\n';
			} else if(this.dbVendor === 'mysql'){
				script += '\t\t\t\t\t" where " + conditionColumn + " > str_to_date(\'" + smallerCondition + "\', \'%Y-%m-%d %H:%i:%s\') "\n';
				script += '\t\t\t\t\t" and " + conditionColumn + " <= str_to_date(\'" + biggerCondition + "\', \'%Y-%m-%d %H:%i:%s\') "\n';
			} else if*(this.dbVendor === 'mssql'){
				script += '\t\t\t\t\t" where " + conditionColumn + " > \'" + smallerCondition + "\'"\n';
				script += '\t\t\t\t\t" and " + conditionColumn + " <= \'" + biggerCondition + "\'";\n';
			} //if
		} //if
		
		script += '\t\t\tvar outputFilename = outputPath + getTableName(originalTableName) + "_" + dateUtil.format(dateUtil.currentTImeMillis(), "yyyyMMddHH") + ".txt";\n';
		script += '\t\t\tdbHandler.selectAndAppend(dbName, query, delimiter, outputFilename, charset);\n;';
		script += '\n';
		return script;
	}, //getScript_queryAndWriteFile
	
	getScript_setBiggerConditionToSimpleRepo: function(){
		var script = '';
		if(this.bindingType != 'simple'){
			script += '\t\t\tsmallerCondition = biggerCondition;\n';
			script += '\t\t\tsimpleRepo.store(id, biggerCondition);\n\n';
		} //if
		return script;
	} //getScript_setBiggerConditionToSimpleRepo
}; //NewScriptGenerator
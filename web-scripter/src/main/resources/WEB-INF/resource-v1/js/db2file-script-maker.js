Db2FileScriptMaker= function(){
	this.id = new Date().getTime();
}; //INIT
Db2FileScriptMaker.prototype = {
	getScript: function(){
		var script = '';
		script += 'var id = "{id}";\n\n'.format(this);
		
		script += '//common\n';
		script += 'var period = {period} * 1000;\n'.format(this);
		if(this.expiredTimeInHour > 0){
			script += 'var expiredTime = {expiredTimeInHour} * 60 * 60 * 1000;\n'.format(this);
			script += 'outputFileDeleteTask.startMonitoring(10*1000, expiredTime);\n';
		} //if
		
		script += '\n';
		script += 'var delimiter = "{delimiter}";\n'.format(this);
		script += '{getScript_commonVariable}\n'.format(this);
	
		script += 'var main = function(){\n';
		script += '\tlogger.info("task started");\n\n';
		
		if(this.bindingType != 'simple')
			script += '{getScript_getSmallerConditionFromSimpleRepo}\n'.format(this);
		
		if(this.bindingType === 'sequence')
			script += '{getScript_getMaxQuery}\n'.format(this);
		
		if(this.bindingType != 'simple')
			script += '{getScript_getBiggerConditionFromDb}\n'.format(this);
		
		script += '{getScript_queryAndWriteFile}\n'.format(this);
		
		if(this.bindingType != 'simple')
			script += '{getScript_setBiggerConditionToSimpleRepo}\n'.format(this);
	
		script += '\tlogger.info("task finished");\n'.format(this);
		script += '} //main\n';
		
		script += '//-----------------------------------------------------------------\n';
		script += '{getScript_getTableNameFunction}\n\n'.format(this);
		
		script += 'logger.info("script started");\n';
		
		script += 'scheduler.schedule(period, new java.lang.Runnable(){\n';
		script += '\trun: function(){\n';
		script += '\t\ttry {\n';
		script += '\t\t\tmain();\n';
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
		script += 'var dbName = "{dbName}";\n'.format(this);
		script += 'var originalTableName = "{tableName}";\n'.format(this);
		script += 'var selectColumn = "{selectColumn}";\n'.format(this);
		script += 'var outputPath = "{outputPath}";\n'.format(this);
		script += 'var charset = "{charset}";\n'.format(this);
		
		if(this.bindingType === 'sequence'){
			script += 'var conditionColumn = "{sequenceColumn}";\n'.format(this);
		} else if(this.bindingType === 'date'){
			script += 'var conditionColumn = "{dateColumn}";\n'.format(this);
		} //if
		
		script += '\n';
		return script;
	}, //getScript_commonVariable
	
	getScript_getSmallerConditionFromSimpleRepo: function(){
		var script = '';
		if(this.bindingType === 'sequence'){
			script += '\tvar smallerCondition = simpleRepo.load(id);\n';
			script += '\tif(smallerCondition === null)\n';
			script += '\t\tsmallerCondition = dateUtil.format(0, "yyyy-MM-dd HH:mm:ss");\n'
		} else if(this.bindingType === 'date'){
			script += '\tvar smallerCondition = simpleRepo.load(id);\n';
			script += '\tif(smallerCondition === null)\n';
			script += '\t\tsmallerCondition = 0;\n';
		} //if
		
		return script;
	}, //getScript_getSmallerConditionFromSimpleRepo
	
	getScript_getMaxQuery: function(){
		var script = '';
		if(this.bindingType === 'sequence')
			script += '\tvar getMaxQuery = stringUtil.format("select max(%s) from %s", conditionColumn, getTableName(originalTableName));\n';
		return script;
	}, //getScript_getMaxQuery
	
	getScript_getBiggerConditionFromDb: function(){
		var script = '';
		if(this.bindingType ==='sequence'){
			script += '\tvar biggerCondition = dateUtil.format(dateUtil.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");\n';
		} else if(this.bindingType === 'date'){
			script += '\tvar biggerCondition = dbHandler.selectQuery(dbName, getMaxQuery);\n';
			script += '\tif(biggerCondition === null)\n';
			script += '\t\tbiggerCondition = "0";\n';
		} //if
		return script;
	}, //getScript_getBiggerConditionFromDb
	
	getScript_queryAndWriteFile: function(){
		var script = '';
		
		if(this.bindingType == 'simple'){
			script += '\tvar query = stringUtil.format("select %s from %s", selectColumn, getTableName(originalTableName));\n';
		} else if(this.bindingType === 'sequence'){
			script += '\tvar query = stringUtil.format("select %s from %s where %s > %s and %s <= %s", \n';
			script += '\t\t\tselectColumn, getTableName(originalTableName), conditionColumn, smallerCondition, conditionColumn, biggerCondition);\n';
		} else if(this.bindingType === 'date'){
			if(this.dbVendor === 'oracle' || this.dbVendor === 'db2' || this.dbVendor === 'tibero' || this.dbVendor === 'etc'){
				script += '\tvar query = stringUtil.format("select %s from %s where %s > to_date(\'%s\', \'YYYY-MM-DD HH24:MI:SS\') and %s <= to_date(\'%s\', \'YYYY-MM-DD HH24:MI:SS\')", \n';
				script += '\t\t\tselectColumn, getTableName(originalTableName), conditionColumn, smallerCondition, conditionColumn, biggerCondition);\n';
			} else if(this.dbVendor === 'mysql'){
				script += '\tvar query = stringUtil.format("select %s from %s where %s > str_to_date(\'%s\', \'%Y-%m-%d %H:%i:%s\') and %s <= str_to_date(\'%s\', \'%Y-%m-%d %H:%i:%s\')", \n';
				script += '\t\t\tselectColumn, getTableName(originalTableName), conditionColumn, smallerCondition, conditionColumn, biggerCondition);\n';
			} else if(this.dbVendor === 'mssql'){
				script += '\tvar query = stringUtil.format("select %s from %s where %s > \'%s\' and %s <= \'%s\'", \n';
				script += '\t\t\tselectColumn, getTableName(originalTableName), conditionColumn, smallerCondition, conditionColumn, biggerCondition);\n';
			} //if
		} //if
		
		script += '\tvar outputFilename = outputPath + getTableName(originalTableName) + "_" + dateUtil.format(dateUtil.currentTimeMillis(), "yyyyMMddHH") + ".txt";\n';
		script += '\tdbHandler.selectAndAppend(dbName, query, delimiter, outputFilename, charset);\n';
		return script;
	}, //getScript_queryAndWriteFile
	
	getScript_setBiggerConditionToSimpleRepo: function(){
		var script = '';
		if(this.bindingType != 'simple'){
			script += '\tsmallerCondition = biggerCondition;\n';
			script += '\tsimpleRepo.store(id, biggerCondition);\n';
		} //if
		return script;
	} //getScript_setBiggerConditionToSimpleRepo
}; //Db2FileScriptMaker
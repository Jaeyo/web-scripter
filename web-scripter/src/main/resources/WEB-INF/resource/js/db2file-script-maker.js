Db2FileScriptMaker= function(){
	this.id = new Date().getTime();
	this.model = null; //Db2FileModel
}; //INIT

Db2FileScriptMaker.prototype = {
	setModel: function(model){
		this.model = model;
		return this;
	}, //setModel
	
	script: function(){
		var helper = {
			step1_initConf: function(id, model){
				var script = '\n var conf = { ';
				script += '\n 	id: "{}", '.format(id);
				script += '\n 	period: {}, '.format(model.period);
				script += '\n 	selectColumn: "{}", '.format(model.selectColumn.toString());
				script += '\n 	tableName: "{}", '.format(model.tableName);
				script += '\n 	outputPath: "{}", '.format(model.outputPath);
				script += '\n 	delimiter: "{}", '.format(model.delimiter);
				script += '\n 	charset: "{}", '.format(model.charset);
				if(model.condition.type !== 'no-condition')
					script += '\n 	conditionColumn: "{}", '.format(model.condition.column);
				script += '\n ';
				script += '\n 	database: { ';
				script += '\n 		driver: "{}", '.format(model.database.driver);
				script += '\n 		connUrl: "{}", '.format(model.database.connUrl);
				script += '\n 		encryptedUsername: "{}", '.format(model.database.username);
				script += '\n 		encryptedPassword: "{}" '.format(model.database.password);
				script += '\n 	} //database ';
				script += '\n }; ';
				script += '\n ';
				return script;
			}, //step1_initConf	

			step2_initConditionVar: function(model){
				if(model.condition.type === 'no-condition')
					return '';
				var script = '\n var condition = { ';
				script += '\n 	smallValue: null, ';
				script += '\n 	bigValue: null ';
				script += '\n }; ';
				if(model.condition.type === 'date-condition'){
					script += '\n condition.smallValue = simpleRepo.load(conf.id); ';
					script += '\n if(condition.smallValue === null) ';
					script += '\n 	condition.smallValue = dateUtil.format(0, "yyyy-MM-dd HH:mm:ss"); ';
				} //if
				script += '\n ';
				return script;
			}, //step2_initConditionVar

			step3_mainFunction: function(model){
				var script = '\n function main(){ ';
				if(model.condition.type === 'date-condition'){
					script += '\n 	condition.bigValue = dateUtil.format(dateUtil.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"); ';
					script += '\n 	var query = " SELECT " + conf.selectColumn + ';
					script += '\n 				" FROM " + getTableName(conf.tableName) + ';
					if(model.database.vendor === 'mysql'){
						script += '\n 				" WHERE " + conf.conditionColumn + " > str_to_date(\'" + condition.smallValue + "\', \'%Y-%m-%d %H:%i:%s\') "';
						script += '\n 				" AND " + conf.conditionColumn + " <= str_to_date(\'" + condition.bigValue + "\', \'%Y-%m-%d %H:%i:%s\') ";';
					} else if(model.database.vendor === 'mssql'){
						script += '\n 				" WHERE " + conf.conditionColumn + " > \'" + condition.smallValue + "\'" ';
						script += '\n 				" AND " + conf.conditionColumn + " <= \'" + condition.smallValue + "\'"; ';
					} else{
						script += '\n 				" WHERE " + conf.conditionColumn + " > to_date(\'" + condition.smallValue + "\', \'YYYY-MM-DD HH24:MI:SS\') "';
						script += '\n 				" AND " + conf.conditionColumn + " <= to_date(\'" + condition.bigValue + "\', \'YYYY-MM-DD HH24:MI:SS\') ";';
					} //if
				} else if(model.condition.type === 'sequence-condition'){
					script += '\n 	var query = " SELECT " + conf.selectColumn + ';
					script += '\n 				" FROM " + getTableName(conf.tableName) + ';
					script += '\n 				" WHERE " + conf.conditionColumn + " > " + condition.smallValue + ';
					script += '\n 				" AND " + conf.conditionColumn + " <= " + condition.bigValue; ';
				} else {
					script += '\n 	var query = " SELECT " + conf.selectColumn + ';
					script += '\n 				" FROM " + getTableName(conf.tableName); ';
				} //if
				
				script += '\n 	var filename = outputPath + "output_" + dateUtil.format(dateUtil.currentTimeMillis(), "yyyyMMddHHmm") + ".txt"; ';
				script += '\n 	dbHandler.selectAndAppend(JSON.stringify(conf.database), query, conf.delimiter, filename, conf.charset); ';
				
				if(model.condition.type !== 'no-condition'){
					script += '\n 	condition.smallValue = condition.bigValue; ';
					script += '\n 	simpleRepo.store(conf.id, condition.bigValue); ';
				} //if

				script += '\n } //main ';
				script += '\n ';
				return script;
			}, //step3_mainFunction

			step4_getTableNameFunction: function(){
				var script = '\n function getTableName(originalTableName){ ';
				script += '\n 	var currentTime = dateUtil.currentTimeMillis(); ';
				script += '\n 	var yyyy = dateUtil.format(currentTime, "yyyy"); ';
				script += '\n 	var mm = dateUtil.format(currentTime, "MM"); ';
				script += '\n 	var dd = dateUtil.format(currentTime, "dd"); ';
				script += '\n 	var hh = dateUtil.format(currentTime, "HH"); ';
				script += '\n 	var mi = dateUtil.format(currentTime, "mm"); ';
				script += '\n 	return originalTableName ';
				script += '\n 		.replace("$yyyy", yyyy) ';
				script += '\n 		.replace("$mm", mm) ';
				script += '\n 		.replace("$dd", dd) ';
				script += '\n 		.replace("$hh", hh) ';
				script += '\n 		.replace("$mi", mi); ';
				script += '\n } //getTableName ';
				return script;
			}, //step4_getTablenameFunction
			
			step5_schedule: function(){
				var script = '\n scheduler.schedule(conf.period, new java.lang.Runnable(){ ';
				script += '\n 	run: function() { ';
				script += '\n 		try{ ';
				script += '\n 			main(); ';
				script += '\n 		} catch(e) {';
				script += '\n 		} //catch ';
				script += '\n 	} //run ';
				script += '\n }); ';
				return script;
			} //step5_schedule
		};

		var script = helper.step1_initConf(this.id, this.model);
		script += helper.step2_initConditionVar(this.model);
		script += helper.step3_mainFunction(this.model);
		script += helper.step4_getTableNameFunction();
		script += helper.step5_schedule();
		return script;
	} //script
}; //Db2FileScriptMaker
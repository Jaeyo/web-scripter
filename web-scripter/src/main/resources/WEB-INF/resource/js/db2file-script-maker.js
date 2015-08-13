Db2FileScriptMaker= function(){
	this.conf = {
		id: new Date().getTime(),
		period: null,
		selectColumn: null,
		tableName; null,
		outputPath: null,
		delimiter: null,
		charset: null,
		
		condition: {
			type: null,
			column: null
		},
		
		database: {
			vendor: null,
			driver: null,
			connUrl: null,
			encryptedUsername: null,
			encryptedPassword: null
		}
	};
}; //INIT

Db2FileScriptMaker.prototype = {
	script: function(){
		var helper = {
			step1_initConf: function(conf){
				var script = '\n var conf = { ';
				script += '\n 	id: "{id}", '.format(conf);
				script += '\n 	period: {period}, '.format(conf);
				script += '\n 	selectColumn: "{selectColumn}", '.format(conf);
				script += '\n 	tableName: "{tableName}", '.format(conf);
				script += '\n 	outputPath: "{outputPath}", '.format(conf);
				script += '\n 	delimiter: "{delimiter}", '.format(conf);
				script += '\n 	charset: "{charset}", '.format(conf);
				if(conf.condition.type !== 'no-condition')
					script += '\n 	conditionColumn: "{condition.column}", '.format(conf);
				script += '\n ';
				script += '\n 	database: { ';
				script += '\n 		driver: "{database.driver}", '.format(conf);
				script += '\n 		connUrl: {database.connUrl}, '.format(conf);
				script += '\n 		encryptedUsername: "{database.encryptedUsername}", '.format(conf);
				script += '\n 		encryptedPassword: "{database.encryptedPassword}" '.format(conf);
				script += '\n 	} //database ';
				script += '\n }; ';
				script += '\n ';
			}, //step1_initConf	

			step2_initConditionVar: function(conf){
				if(conf.condition.type === 'no-condition')
					return '';
				var script = '\n var condition = { ';
				script += '\n 	smallValue: null, ';
				script += '\n 	bigValue: null ';
				script += '\n }; ';
				if(conf.condition.type === 'date-condition'){
					script += '\n condition.smallValue = simpleRepo.load(conf.id); ';
					script += '\n if(condition.smallValue === null) ';
					script += '\n 	condition.smallValue = dateUtil.format(0, "yyyy-MM-dd HH:mm:ss"); ';
				} //if
				script += '\n ';
				return script;
			}, //step2_initConditionVar

			step3_mainFunction: function(conf){
				var script = '\n function main(){ ';
				if(conf.condition.type === 'date-condition'){
					script += '\n 	condition.bigValue = dateUtil.format(dateUtil.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"); ';
					script += '\n 	var query = " SELECT " + conf.selectColumn + ';
					script += '\n 				" FROM " + getTableName(conf.tableName) + ';
					if(conf.database.vendor === 'mysql'){
						script += '\n 				" WHERE " + conf.conditionColumn + " > str_to_date(\'" + condition.smallValue + "\', \'%Y-%m-%d %H:%i:%s\') "';
						script += '\n 				" AND " + conf.conditionColumn + " <= str_to_date(\'" + condition.bigValue + "\', \'%Y-%m-%d %H:%i:%s\') ";';
					} else if(conf.database.vendor === 'mssql'){
						script += '\n 				" WHERE " + conf.conditionColumn + " > \'" + condition.smallValue + "\'" ';
						script += '\n 				" AND " + conf.conditionColumn + " <= \'" + condition.smallValue + "\'"; ';
					} else{
						script += '\n 				" WHERE " + conf.conditionColumn + " > to_date(\'" + condition.smallValue + "\', \'YYYY-MM-DD HH24:MI:SS\') "';
						script += '\n 				" AND " + conf.conditionColumn + " <= to_date(\'" + condition.bigValue + "\', \'YYYY-MM-DD HH24:MI:SS\') ";';
					} //if
				} else if(conf.condition.type === 'sequence-condition'){
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
				
				if(conf.condition.type !== 'no-condition'){
					script += '\n 	condition.smallValue = condition.bigValue; ';
					script += '\n 	simpleRepo.store(conf.id, condition.bigValue); ';
				} //if

				script += '\n } //main ';
				script += '\n ';
				return script;
			}, //step3_mainFunction

			step4_getTableNameFunction: function(conf){
				var script = '\n function getTableName(originalTableName){ ';
				script += '\n 	var currentTime = dateUtil.currentTimeMillis(); ';
				script += '\n 	var yyyy = dateUtil.format(currentTime, "yyyy");
				script += '\n 	var mm = dateUtil.format(currentTime, "MM");
				script += '\n 	var dd = dateUtil.format(currentTime, "dd");
				script += '\n 	var hh = dateUtil.format(currentTime, "HH");
				script += '\n 	var mi = dateUtil.format(currentTime, "mm");
				script += '\n 	return originalTableName ';
				script += '\n 		.replace("$yyyy", yyyy) ';
				script += '\n 		.replace("$mm", mm) ';
				script += '\n 		.replace("$dd", dd) ';
				script += '\n 		.replace("$hh", hh) ';
				script += '\n 		.replace("$mi", mi); ';
				script += '\n } //getTableName ';
				return script;
			}, //step4_getTablenameFunction
			
			step5_schedule: function(conf){
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

		var script = helper.step1_initConf(this);
		script += helper.step2_initConditionVar(this);
		script += helper.step3_mainFunction(this);
		script += helper.step4_getTableNameFunction(this);
		script += helper.step5_schedule(this);
		return script;
	} //script
}; //Db2FileScriptMaker

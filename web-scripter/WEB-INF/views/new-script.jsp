<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="S" value="$" />
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>web scripter</title>

<!-- jquery -->
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>

<!-- bootstrap -->
<link href="http://bootswatch.com/superhero/bootstrap.min.css" rel="stylesheet">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<!-- bootstrap switch -->
<link href="http://www.bootstrap-switch.org/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
<script src="http://www.bootstrap-switch.org/dist/js/bootstrap-switch.js"></script>

<!-- bootbox -->
<script src="https://github.com/makeusabrew/bootbox/releases/download/v4.4.0/bootbox.min.js"></script>

<!-- jquery tmpl -->
<script src="/resource/js/lib/jquery.tmpl.min.js"></script>

<!-- bucket js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/buckets/1.90.0/buckets.js"></script>

<!-- string format -->
<script src="/resource/js/lib/string.format.js"></script>

<!-- code mirror -->
<script src="http://codemirror.net/lib/codemirror.js"></script>
<link href="http://codemirror.net/lib/codemirror.css" rel="stylesheet">

<script src="https://codemirror.net/mode/sql/sql.js"></script>
<script src="http://codemirror.net/mode/javascript/javascript.js"></script>

<script src="https://codemirror.net/addon/hint/show-hint.js"></script>
<link href="https://codemirror.net/addon/hint/show-hint.css" rel="stylesheet">
<script src="http://codemirror.net/addon/hint/javascript-hint.js"></script>

<link href="http://codemirror.net/theme/base16-dark.css" rel="stylesheet">

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/header.jsp" flush="false"/>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-4">
				<div><label>script name</label></div>
				<div>
					<input id="input-script-name" type="text" class="input form-control" />
				</div>
			</div>
			
			<div class="col-xs-4">
				<div><label>memo</label></div>
				<div>
					<textarea id="textarea-memo" class="form-control"></textarea>
				</div>
			</div>
			
			<div class="col-xs-4">
				<button id="btn-save" type="button" class="btn btn-primary pull-right" onclick="controller.save();">save</button>
			</div>
		</div>
		<hr />
		
		<div class="row">
			<div class="col-xs-7">
				<div class="row" id="div-initial-step" style="padding: 10px;">
					<div class="col-xs-6">
						<button type="button" class="btn btn-default" onclick="controller.stepByStep();">스텝 바이 스텝</button>
					</div>
					<div class="col-xs-6">
						<button type="button" class="btn btn-default" onclick="controller.scriptDirectly();">직접 스크립트 작성</button>
					</div>
				</div>
				
				<hr />
				
				<div class="row step" id="div-script" style="display: none; padding: 10px;">
					<div class="col-xs-12">
						<textarea id="textarea-script" class="form-control" rows="24"></textarea>
					</div>
				</div>
				
				<div class="row step" id="div-select-database" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>database</label>
					</div>
					<div class="col-xs-9">
						<div id="dropdown-database" class="dropdown">
							<button class="btn btn-default dropdown-toggle" type="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expand="true">select database <span class="caret"></span></button>
							<ui class="dropdown-menu" aria-labelledby="dropdown-btn-database">
							</ui>
						</div>
					</div>
				</div>
				
				<div class="row step" id="div-binding-type" style="display: none; padding: 10px">
					<div class="col-xs-3">
						<label>binding type</label>
					</div>
					<div class="col-xs-3">
						<button type="button" class="btn btn-default center" onclick="controller.setBindingType('simple');">simple</button>
					</div>
					<div class="col-xs-3">
						<button type="button" class="btn btn-default center" onclick="controller.setBindingType('date');">date</button>
					</div>
					<div class="col-xs-3">
						<button type="button" class="btn btn-default center" onclick="controller.setBindingType('sequence');">sequence</button>
					</div>
				</div>
				
				<div class="row step" id="div-date-column" style="display: none; padding: 10px">
					<div class="row">
						<div class="col-xs-3">
							<label>date column</label>
						</div>
						<div class="col-xs-9">
							<input type="text" id="input-date-column" class="input form-control" onkeyup="controller.makeQuery();" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<button type="button" class="btn btn-default pull-right" onclick="controller.setDateColumn();">next</button>
						</div>
					</div>
				</div>
				
				<div class="row step" id="div-sequence-column" style="display: none; padding: 10px">
					<div class="row">
						<div class="col-xs-3">
							<label>sequence column</label>
						</div>
						<div class="col-xs-9">
							<input type="text" id="input-sequence-column" class="input form-control" onkeyup="controller.makeQuery();" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<button type="button" class="btn btn-default pull-right" onclick="controller.setSequenceColumn();">next</button>
						</div>
					</div>
				</div>
				
				<div class="row step" id="div-make-query" style="display: none; padding: 10px;">
					<div class="row">
						<div class="col-xs-6">
							<input type="text" id="input-select-column" class="input form-control" placeholder="select column" onkeyup="controller.makeQuery();" />
						</div>
						<div class="col-xs-6">
							<input type="text" id="input-table-name" class="input form-control" placeholder="table name" onkeyup="controller.makeQuery();" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<textarea id="textarea-query-made"></textarea>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<button type="button" class="btn btn-default pull-right" onclick="controller.setQueryParams()">next</button>
						</div>
					</div>
				</div>
	
				<div class="row step" id="div-column-delimiter" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>column delimiter</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-column-delimiter" class="input form-control" value="|" />
					</div>
				</div>
				
				<div class="row step" id="div-column-delimiter" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>period</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-period" class="input form-control" value="60" />
					</div>
				</div>
				
				<div class="row step" id="div-expired-time-in-hour" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>expired time in hour</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-expired-time-in-hour" class="input form-control" />
					</div>
				</div>
				
				<div class="row step" id="div-output-path" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>output path</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-output-path" class="input form-control" value="/data/outputpath/" />
					</div>
				</div>
				
				<div class="row step" id="div-charset" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>output path</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-charset" class="input form-control" value="utf-8" />
					</div>
				</div>
				
				<hr />
				
				<div class="row step" id="div-make-script" style="display: none; padding: 10px;">
					<div class="col-xs-12">
						<button type="button" class="btn btn-primary" onclick="controller.makeScript();">make script</button>
					</div>
				</div>
			</div>
			
			<div class="col-xs-5" style="overflow: scroll; height: 700px; padding-left: 30px">
				<h3>DateUtil</h3>
				<h4>String format(long date, String format)</h4>
				<ul>
					<li>long 형으로 주어진 시간(date)을 포맷(format)에 맞춰서 출력한다. long 형의 시간 값은 DateUtil.parse(), DateUtil.currentTimeMillis() 를 통해 구할 수 있다.
					<li><b>Returns</b> 포맷팅된 날짜</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var formattedDate = dateUtil.format(1414460642364, "yyyyMMddHHmmss"); // => 20141028104502 
var formattedDate = dateUtil.format(1414460642364, "yyyyMMdd"); // => 20141028
var formattedDate = dateUtil.format(1414460642364, "yyyy-MM-dd"); // => 2014-10-28
				</textarea>
				<hr />
				
				<h4>String parse(String date, String format)</h4>
				<ul>
					<li>포맷(format)에 맞춰 포맷팅된 시간값(date)를 long 현태의 시간 값으로 변환한다.</li>
					<li><b>Returns</b> long 타입의 시간 값</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var dateValue = dateUtil.parse("20141028104502", "yyyyMMddHHmmss"); // => 1414460642364
var dateValue = dateUtil.parse("20141028", "yyyyMMdd"); // => 1414422000000
var dateValue = dateUtil.parse("2014 10-28", "yyyy MM-dd"); // => 1414422000000
				</textarea>
				<hr />
				
				<h4>long currentTimeMillis()</h4>
				<ul>
					<li>현재 시간을 long 형태의 시간 값으로 변환한다.</li>
					<li><b>Returns</b> long 타입의 시간값</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var currentTime = dateUtil.currentTimeMillis(); // => 1414460642364
				</textarea>
				<hr />
				
				<h3>DbHandler</h3>
				<h4>void executeQuery(String dbName, String query)</h4>
				<ul>
					<li>지정된 데이터베이스(dbName)에 대해서 insert, update, delete 쿼리(query)를 실행한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
dbHandler.executeQuery("sampleDb1", "insert into test_table(value1, value2) values("test1", "test2");
				</textarea>
				<hr />
				
				<h4>void executeBatch(String dbName, String[] quries)</h4>
				<ul>
					<li>지정된 데이터베이스(dbName)에 대해서 insert, update, delete 쿼리(query)를 batch로 실행한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var queries = [];
queries.push("insert into test_table (value1) values('test1')");
queries.push("insert into test_table (value1) values('test2')");
queries.push("insert into test_table (value1) values('test3')");
dbHandler.executeBatch("sampleDb1", queries);
				</textarea>
				<hr />
				
				<h4>String selectQuery(String dbName, String query)</h4>
				<ul>
					<li>지정된 데이터베이스(dbName)에 대해서 select 쿼리(query)를 실행한 결과를 String 형식으로 반환한다. 반환되는 데이터들의 row간 구분자는 '\n', column간 구분자는 공백(' ')으로 구성된다.</li>
					<li><b>Returns</b> select 쿼리의 결과</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var result = dbHandler.selectQuery("sampleDb1", "select value1, value2 from test_table"); // => "test1|test2\ntest3|test4"
				</textarea>
				<hr />
				
				<h4>String selectQueryIterator</h4>
				<ul>
					<li>지정된 데이터베이스(dbName)에 대해서 select 쿼리(query)를 실행한 결과를 DbRowIterator 형식으로 반환한다. DbRowIterator 클래스를 통해 건수가 많은 데이터를 한번에 불러와 메모리에 과다 적재되는 문제를 방지하고 한 행씩 받아 처리할 수 있다.</li>
					<li>DbRowIterator는 next()와 close() 두 개의 메소드를 가지는데, next()는 다음행 데이터를 문자열 배열 형태로 반환하며 마지막 행에 다다랐을 경우 null을 반환하고 close() 메소드는 데이터베이스와의 연결을 끊는다.</li>
					<li><b>Returns</b> DbRowIterator</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var rowIterator = dbHandler.selectQueryIterator("sampleDb1", "select value1, value2 from test_table");
var row = null;
while((row = rowIterator.next()) != null){
  logger.info("column count is " + row.length); // rowIterator.next()를 통해 문자열 배열 형태로 받았기 때문에 length 속성을 통해 컬럼 갯수를 확인할 수 있다.
  for(var i=0; i<row.length; i++){
    logger.info("column " + i + " : " + row[i]); //각 컬럼별로 값을 로그로 출력한다.
  } //for i
} //while
rowIterator.close(); //사용이 끝난 DbRowIterator는 항상 close 메소드를 호출하여 데이터베이스와의 연결을 종료시킨다.
				</textarea>
				<hr />
				
				<h4>String selectQuery(String dbName, String query, String delimiter)</h4>
				<ul>
					<li>지정된 데이터베이스(dbName)에 대해서 select 쿼리(query)를 실행한 결과를 String 형식을 변환하여 반환한다. 반환되는 데이터의 row간 구분자는 '\n', column간 구분자는 파라미터에서 지정된 delimiter로 구성된다.</li>
					<li><b>Returns</b> select 쿼리의 결과</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
String result = dbHandler.selectQuery("sampleDb1", "select value1, value2 from test_table", ","); // => "test1,test2\ntest3,test4"
				</textarea>
				<hr />
				
				<h4>void selectAndAppend(String dbName, String query, String delimiter, String filename, String charsetName)</h4>
				<ul>
					<li>지정된 데이터베이스(dbName)에 대해서 select 쿼리(query)를 실행한 결과를 곧바로 파일로 출력한다. 출력되는 데이터들의 row간 구분자는 '\n', column 간 구분자는 파라미터에서 지정된 delimiter로 구성된다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
dbHandler.selectAndAppend("sampleDb1", "select value1, value2 from test_table", ",", "/data/output.txt", "UTF-8");
				</textarea>
				<hr />
				
				<h3>FileExporter</h3>
				<h4>void write(String filename, String content)</h4>
				<ul>
					<li>파라미터 content에 담긴 내용을 파일로 출력한다. filename은 절대경로를 포함한 파일명으로 작성되어야 하며 해당 파일이 없을 경우 자동으로 생성한다. 기존에 파일 내 기록된 내용이 있을 경우 덮어써진다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
fileExporter.write("/data/output.txt", "this is output.txt"); // => /data/output.txt 파일이 생성되며 "this is output.txt" 파일이 기록된다.
				</textarea>
				<hr />
				
				<h4>void write(String filename, String content, String charsetName)</h4>
				<ul>
					<li>파라미터 content에 담긴 내용을 지정된 캐릭터셋(charsetName)으로 파일에 출력한다. filename은 절대경로를 포함한 파일명으로 작성되어야 하며 해당 파일이 없을 경우 자동으로 생성한다. 기존에 파일 내 기록된 내용이 있을 경우 덮어써진다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
fileExporter.write("/data/output.txt", "this is output.txt", "euc-kr");
fileExporter.write("/data/output.txt", "this is output.txt", "utf8");
fileExporter.write("/data/output.txt", "this is output.txt", "cp949");
				</textarea>
				<hr />
				
				<h4>void append(String filename, String content)</h4>
				<ul>
					<li>파라미터 content에 담긴 내용을 파일에 출력한다. filename은 절대경로를 포함한 파일명으로 작성되어야 하며 해당 파일이 없을 경우 자동으로 생성한다. 기존에 파일 내 기록된 내용이 있을 경우 뒤에 이어서 기록된다.</li>
					<li><b>Exmaple</b></li>
				</ul>
				<textarea class="textarea-sample-code">
fileExporter.write("/data/output.txt", "this is test.txt");
				</textarea>
				<hr />
				
				<h4>void append(String filename, String content, String charsetName)</h4>
				<ul>
					<li>파라미터 content에 담긴 내용을 지정된 캐릭터셋(charsetName)으로 파일에 출력한다. filename은 절대경로를 포함한 파일명으로 작성되어야 하며 해당 파일이 없을 경우 자동으로 생성한다. 기존에 파일 내 기록된 내용이 있을 경우 뒤에 이어서 기록된다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
fileExporter.write(“/data/output.txt”, “this is output.txt”, “euc-kr”);
fileExporter.write(“/data/output.txt”, “this is output.txt”, “utf8”);
fileExporter.write(“/data/output.txt”, “this is output.txt”, “cp949);
				</textarea>
				<hr />
				
				<h4>void createFile(String filename)</h4>
				<ul>
					<li>filename에 해당하는 파일을 생성한다. filename은 절대경로를 포함한 파일명으로 작성되어야 한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
fileExporter.createFile("/data/output.txt"); // => /tmp/output.txt 파일이 생성된다.
				</textarea>
				<hr />
				
				<h3>Scheduler</h3>
				<h4>void schedule(long period, Runnable task)</h4>
				<ul>
					<li>task에 담긴 로직을 period 주기에 따라 반복하여 실행한다. period는 밀리초를 기준으로 지정되어야 한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
scheduler.schedule(5*1000, new java.lang.Runable(){
	run:function(){
		logger.info(“test log”);
	}
});
//=> 5초마다 주기적으로 로그를 남긴다.
				</textarea>
				<hr />
				
				<h4>void schedule(long delay, long period, Runnable task)</h4>
				<ul>
					<li>task에 담긴 로직을 delay 만큼의 시간 이후에 period 주기에 따라 반복하여 실행한다. Delay와 period는 밀리초를 기준으로 지정되어야 한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
	scheduler.schedule(3*1000, 5*1000, new java.lang.Runable(){
		run:function(){
			logger.info(“test log”);
		}
	});
// => 3초후부터 5초마다 주기적으로 로그를 남긴다.
				</textarea>
				<hr />
				
				<h3>SimpleRepo</h3>
				<h4>void store(String key, String value)</h4>
				<ul>
					<li>간단한 key-value 형태의 데이터를 저장한다. 저장된 내용은 conf/simple_repo.properties 파일 내에 xml 형태로 저장되어 재기동 이후에도 유지된다. 같은 key를 가진 서로 다른 데이터 value가 저장될 경우 나중에 요청된 value가 저장된다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
simpleRepo.store(“lastExecutedTime”, “2014 1028 1139”);
				</textarea>
				<hr />
				
				<h4>String load(String key)</h4>
				<ul>
					<li>해당 key에 대한 value 데이터를 불러온다. 해당 key에 대한 데이터가 없을 경우 null을 반환한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
simpleRepo.load(“lastExecutedTime”);  // => “2014 1028 1139”
				</textarea>
				<hr />
				
				<h4>String load(String key, String defaultValue)</h4>
				<ul>
					<li>해당 key에 대한 value 데이터를 불러온다. 해당 key에 대한 데이터가 없을 경우 defaultValue를 반환한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
simpleRepo.load(“firstExecutedTime”, “2014 1028 1000”); //”firstExecutedTime” key에 대한 데이터가 없을 경우
// => “2014 1028 1000”
				</textarea>

				<h4>void clear()</h4>
				<ul>
					<li>key-value 저장소에 저장된 데이터들을 초기화한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
simpleRepo.clear();
				</textarea>
				<hr />
				
				<h3>RuntimeUtil</h3>
				<h4>void openShutdownPort()</h4>
				<ul>
					<li>프로세스 종료를 위한 포트를 개방한다. 기본 포트는 8021이다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
runtimeUtil.openShutdownPort();
				</textarea>
				<hr />
				
				<h4>void openShutdownPort(int port) </h4>
				<ul>
					<li>프로세스 종료를 위한 포트(port)를 지정하여 개방한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
runtimeUtil.openShutdownPort();
				</textarea>
				<hr />
				
				<h4>void sleep(long timeMillis)</h4>
				<ul>
					<li>지정된 밀리초(timeMillis)만큼 멈춘다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
runtimeUtil.sleep(1000); //1초 멈춤
				</textarea>
				<hr />
				
				<h4>void shutdown()</h4>
				<ul>
					<li>프로세스를 종료한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
runtimeUtil.shutdown();
				</textarea>
				<hr />
	
				<h3>Logger</h3>
				<h4>void info(String message)</h4>
				<ul>
					<li>INFO 로그를 남긴다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
logger.info(“this is info log”);
				</textarea>
				<hr />
				
				<h4>void debug(String message)</h4>
				<ul>
					<li>DEBUG 로그를 남긴다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
logger.debug(“this is debug log”);
				</textarea>
				<hr />
				
				<h4>void warn(String message)</h4>
				<ul>
					<li>WARN 로그를 남긴다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
logger.warn(“this is warn log”);
				</textarea>
				<hr />
				
				<h4>void error(String message)</h4>
				<ul>
					<li>ERROR 로그를 남긴다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
logger.error(“this is error log”);
				</textarea>
				<hr />
				
				<h4>void trace(String message)</h4>
				<ul>
					<li>TRACE 로그를 남긴다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
logger.trace(“this is trace log”);
				</textarea>
				<hr />
	
				<h3>FileReader</h3>
				<h4>void monitorFileNewLine(String filename, Function onLine, boolean deleteExpiredFile)</h4>
				<ul>
					<li>해당 파일(filename)을 감시하면서 새로운 line이 추가될 때마다 onLine Function을 실행한다. filename에는 $yyyy$mm$dd$hh$mi$ss 와 같이 date format 지정이 가능하며 이에 따른 파일 switching 시에 deleteExpiredFile 변수를 통해 지나간 파일을 지울지 여부를 선택할 수 있다. 캐릭터셋은 UTF-8 로 설정된다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var onLine = new com.igloosec.SpDbReader.common.Function(){
	execute:function(args){
		var line=args[0];
		logger.info("line is : " + line);
	} //execute
} //onLine

fileReader.monitorFileNewLine("/data/test_$yyyy$mm$dd$hh$mi.log", onLine, true);
				</textarea>
				<hr />
				
				<h4>void monitorFileNewLine(String filename, Function onLine, boolean deleteExpiredFile, String charset)</h4>
				<ul>
					<li>해당 파일(filename)을 감시하면서 새로운 line이 추가될 때마다 onLine Function을 실행한다. filename에는 $yyyy$mm$dd$hh$mi$ss 와 같이 date format 지정이 가능하며 이에 따른 파일 switching 시에 deleteExpiredFile 변수를 통해 지나간 파일을 지울지 여부를 선택할 수 있다.</li>
					<li>charset 항목에는 케릭터 셋을 지정한다. 지정 가능한 캐릭터셋은 Java Standard Charset(http://docs.oracle.com/javase/7/docs/api/java/nio/charset/Charset.html) 이외에 SpDbReader 가 사용하는 jvm 에서 지원하는 캐릭터 셋에 한한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var onLine = new com.igloosec.SpDbReader.common.Function(){
	execute:function(args){
		var line=args[0];
		logger.info("line is : " + line);
	} //execute
} //onLine
 
fileReader.monitorFileNewLine("/data/test_$yyyy$mm$dd$hh$mi.log", onLine, true, "euc-kr");
				</textarea>
				<hr />
				
				<h4>void monitorFileNewLine(String filename, Function onLine, boolean deleteExpiredFile, String charset, int timeAdjustSec)</h4>
				<ul>
					<li>해당 파일(filename)을 감시하면서 새로운 line이 추가될 때마다 onLine Function을 실행한다. filename에는 $yyyy$mm$dd$hh$mi$ss 와 같이 date format 지정이 가능하며 이에 따른 파일 switching 시에 deleteExpiredFile 변수를 통해 지나간 파일을 지울지 여부를 선택할 수 있다.</li>
					<li>파일 지정시 timeAdjustSec 파라미터를 통해 읽고자 하는 파일의 시간대를 조정할 수 있다.</li>
					<li>charset 항목에는 케릭터 셋을 지정한다. 지정 가능한 캐릭터셋은 Java Standard Charset(http://docs.oracle.com/javase/7/docs/api/java/nio/charset/Charset.html) 이외에 SpDbReader 가 사용하는 jvm 에서 지원하는 캐릭터 셋에 한한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var onLine = new com.igloosec.SpDbReader.common.Function(){
	execute:function(args){
		var line=args[0];
		logger.info("line is : " + line);
	} //execute
} //onLine
fileReader.monitorFileNewLine("/data/test_$yyyy$mm$dd$hh$mi.log", onLine, true, "euc-kr", -60);
				</textarea>
				<hr />
				
				<h4>String readAll(String filename)</h4>
				<ul>
					<li>해당 파일(filename)의 내용을 모두 읽어 반환한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var result = fileReader.readAll("/data/test.txt");
				</textarea>
				<hr />
				
				<h4>String readAll(String filename, boolean delete)</h4>
				<ul>
					<li>해당 파일(filename)의 내용을 모두 읽어 반환한다. 파라미터 delete가 true인 경우 읽은 파일을 삭제한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var result = fileReader.readAll("/data/test.txt", true); //파일 읽은 후에 삭제
				</textarea>
				<hr />
				
				<h4>String readAll(String filename, boolean delete, long maxBytes)</h4>
				<ul>
					<li>해당 파일(filename)의 내용을 최대 maxBytes 만큼만 읽어 반환한다. 파라미터 delete가 true인 경우 읽은 파일을 삭제한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var result = fileReader.readAll("/data/test.txt", false, 1*1024*1024); //최대 1MB 만큼만 읽는다.
				</textarea>
				<hr />
				
				<h3>OutputFileDeleteTask</h3>
				<h4>void startMonitoring(long period, long expiredTime)</h4>
				<ul>
					<li>출력 파일의 삭제를 위한 모니터링을 시작한다. period 주기마다 파일에 마지막으로 write된 시간을 감시하며 현재시간으로부터 expiredTime 만큼 경과했을 경우 해당 파일을 삭제한다. period와 expiredTime은 모두 밀리초 기준으로 작성되어야 한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
outputFileDeleteTask.startMonitoring(10*1000, 3*60*60*1000);
				</textarea>
				<hr />
				
				<h3>StringUtil</h3>
				<h4>String stringAt(String line, String delimiter, int index)</h4>
				<ul>
					<li>파라미터로 전달된 문자열(line)에서 구분자(delimiter) 기준으로 특정 부분에 있는 부분 문자열을 추출하여 반환한다.</li>
					<li><b>Example</b></li>
				</ul>
				<textarea class="textarea-sample-code">
var result = stringUtil.stringAt("aaa,bbb,ccc,ddd", ",", 2); // ccc 반환
				</textarea>
			</div>
		</div>
	</div>
	<!-- end of container -->
	
	<!-- server adpater js -->
	<script src="/resource/js/server-adapter.js"></script>

	<!-- new-script-generator js -->
	<script src="/resource/js/new-script-generator.js"></script>

	<!-- scripts js -->
	<script src="/resource/js/new-script.js"></script>
</body>
</html>
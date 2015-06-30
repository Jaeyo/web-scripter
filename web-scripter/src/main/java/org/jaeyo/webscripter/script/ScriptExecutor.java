package org.jaeyo.webscripter.script;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import org.jaeyo.webscripter.exception.AlreadyStartedException;
import org.jaeyo.webscripter.exception.ScriptNotRunningException;
import org.jaeyo.webscripter.script.bindings.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScriptExecutor {
	private static final Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);
	private Scheduler schedulerBinding = new Scheduler();
	private Map<Long, Thread> runningScripts = new HashMap<Long, Thread>();

	public void execute(final long sequence, final String script) throws AlreadyStartedException{
		logger.info("sequence: {}", sequence);
		
		if(runningScripts.containsKey(sequence))
			throw new AlreadyStartedException(sequence+"");
		
		Thread executorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					Thread.currentThread().setName(String.format("ScriptThread-%s", sequence));
					
					Bindings bindings = new SimpleBindings();
					bindings.put("logger", logger);
					bindings.put("scheduler", schedulerBinding);
					ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
					scriptEngine.eval(script, bindings);
				} catch(Exception e){
					if(e.getClass().equals(InterruptedException.class) == true)
						return;
					logger.error("", e);
				} finally{
					runningScripts.remove(sequence);
				} //finally
			} //run
		});
		executorThread.start();
		runningScripts.put(sequence, executorThread);
	} //execute
	
	public void stop(long sequence) {
		Thread executorThread = runningScripts.remove(sequence);
		if(executorThread != null)
			executorThread.interrupt();
		schedulerBinding.cancel(sequence);
	} //stop

	public Set<Long> getRunningScripts(){
		Set<Long> runningScriptSequences = new HashSet<Long>();
		runningScriptSequences.addAll(runningScripts.keySet());
		runningScriptSequences.addAll(schedulerBinding.getRunningScriptSequences());
		return runningScriptSequences;
	} //getRunningScripts
} //class
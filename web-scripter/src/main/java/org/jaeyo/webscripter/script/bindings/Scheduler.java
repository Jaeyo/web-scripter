package org.jaeyo.webscripter.script.bindings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scheduler {
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	private Map<Long, SchedulerTimer> timers = new HashMap<Long, SchedulerTimer>();
	
	public void schedule(long delay, long period, final Runnable task) {
		final long sequence = Long.parseLong(Thread.currentThread().getName().replace("ScriptThread-", ""));
		SchedulerTimer timer = timers.get(sequence);
		if(timer == null){
			timer = new SchedulerTimer();
			timers.put(sequence, timer);
		} //if
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Thread.currentThread().setName("ScriptThread-" + sequence);
				task.run();
			} // run
		}, delay, period);
	} // schedule

	public void schedule(long period, final Runnable task) {
		schedule(0, period, task);
	} // schedule
	
	public void scheduleAtFixedTime(String[] hhMMs, final Runnable task) {
		logger.info("hhMMs: {}", Arrays.toString(hhMMs));
		
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		SimpleDateFormat format4yyyyMMddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
		
		for(String hhMM : hhMMs){
			try {
				long targetTimestamp = format4yyyyMMddHHmm.parse(yyyyMMdd + hhMM).getTime();
				if(targetTimestamp < System.currentTimeMillis())
					targetTimestamp += 24 * 60 * 60 * 1000;
				
				schedule(targetTimestamp-System.currentTimeMillis(), 24 * 60 * 60 * 1000, task);
			} catch (ParseException e) {
				logger.error(String.format("%s, errmsg : %s, hhMMs : %s", e.getClass().getSimpleName(), e.getMessage(), Arrays.toString(hhMMs)), e);
			} //catch
		} //for hhMM
	} //scheduleAtFixedTime
	
	public void cancel(long sequence){
		SchedulerTimer timer = timers.remove(sequence);
		if(timer != null)
			timer.cancel();
	} //cancel
	
	public Set<Long> getRunningScriptSequences(){
		Set<Long> runningScriptSequences = new HashSet<Long>();
		for (Entry<Long, SchedulerTimer> entry : timers.entrySet()) {
			if(entry.getValue().isScheduled == true)
				runningScriptSequences.add(entry.getKey());
		} //for entry
		return runningScriptSequences;
	} //getRunningScriptSequences
	
	class SchedulerTimer extends Timer {
		private boolean isScheduled = false;
		
		@Override
		public void schedule(TimerTask task, long delay, long period) {
			isScheduled = true;
			super.schedule(task, delay, period);
		} //schedule

		@Override
		public void cancel() {
			isScheduled = false;
			super.cancel();
		} //cancel
	} //class
} // class
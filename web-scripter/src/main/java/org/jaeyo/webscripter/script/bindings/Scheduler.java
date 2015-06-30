package org.jaeyo.webscripter.script.bindings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	private Map<Long, SchedulerTimer> timers = new HashMap<Long, SchedulerTimer>();
	
	public void schedule(long delay, long period, final Runnable task) {
		long sequence = Long.parseLong(Thread.currentThread().getName().replace("ScriptThread-", ""));
		SchedulerTimer timer = timers.get(sequence);
		if(timer == null){
			timer = new SchedulerTimer();
			timers.put(sequence, timer);
		} //if
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				task.run();
			} // run
		}, delay, period);
	} // schedule

	public void schedule(long period, final Runnable task) {
		schedule(0, period, task);
	} // schedule
	
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
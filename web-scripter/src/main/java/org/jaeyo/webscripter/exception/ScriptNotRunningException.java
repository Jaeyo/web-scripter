package org.jaeyo.webscripter.exception;

public class ScriptNotRunningException extends Exception {

	public ScriptNotRunningException() {
		super();
	}

	public ScriptNotRunningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ScriptNotRunningException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScriptNotRunningException(String message) {
		super(message);
	}

	public ScriptNotRunningException(Throwable cause) {
		super(cause);
	}

}
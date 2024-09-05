package com.techlabs.app.exception;

public class AgentNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	   public AgentNotFoundException(String message) {
	      super(message);
	   }
}

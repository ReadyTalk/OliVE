package com.readytalk.olive.json;

public class SplitVideoRequest {

	// No-args constructor
	public SplitVideoRequest() {

	}

	public String command;
	public Arguments arguments;
	
	// Must be static for Gson to work.
	// See: http://sites.google.com/site/gson/gson-user-guide#TOC-Nested-Classes-including-Inner-Clas
	public static class Arguments {

		// No-args constructor
		public Arguments() {

		}
		
		public String video;
		public double splitTimeInSeconds;
	}
}

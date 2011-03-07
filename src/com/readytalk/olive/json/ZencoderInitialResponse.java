package com.readytalk.olive.json;

public class ZencoderInitialResponse {

	// No-args constructor
	public ZencoderInitialResponse() {

	}

	public Outputs[] outputs;
	public int id;
	
	// Must be static for Gson to work.
	// See: http://sites.google.com/site/gson/gson-user-guide#TOC-Nested-Classes-including-Inner-Clas
	public static class Outputs {

		// No-args constructor
		public Outputs() {

		}
		
		public String url;
		public String label;
		public int id;
	}
}

package com.readytalk.olive.json;

public class UpdateProjectsPositionRequest {

	// No-args constructor
	public UpdateProjectsPositionRequest() {

	}

	public String command;
	public Arguments arguments;
	
	// Must be static for Gson to work.
	// See: http://sites.google.com/site/gson/gson-user-guide#TOC-Nested-Classes-including-Inner-Clas
	public static class Arguments {

		// No-args constructor
		public Arguments() {

		}
		
		public Projects[] projects;
		
		// Must be static for Gson to work.
		// See: http://sites.google.com/site/gson/gson-user-guide#TOC-Nested-Classes-including-Inner-Clas
		public static class Projects {

			// No-args constructor
			public Projects() {

			}
			
			public String project;
			public int position;
		}
	}
}

package com.readytalk.olive.model;

public class Video {
	private String name;
	private String URL;
	private String icon;
	private Project project;
	public Video(String name, String uRL, String icon, Project p) {
		this.name = name;
		URL = uRL;
		this.icon = icon;
		this.project = p;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}

package com.readytalk.olive.model;

public class Video {

	private String name;
	private String url;
	private String icon;
	private int projectId;
	private int poolPosition;
	private int timelinePosition;
	private boolean isSelected;

	public Video(String name, String url, String icon, int projectId,
			int poolPosition, int timelinePosition, boolean isSelected) {
		this.name = name;
		this.url = url;
		this.icon = icon;
		this.projectId = projectId;
		this.poolPosition = poolPosition;
		this.timelinePosition = timelinePosition;
		this.isSelected = isSelected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int project) {
		this.projectId = project;
	}

	public void setPoolPosition(int poolPosition) {
		this.poolPosition = poolPosition;
	}

	public int getPoolPosition() {
		return poolPosition;
	}
	
	public void setTimelinePosition(int timelinePosition) {
		this.timelinePosition = timelinePosition;
	}

	public int getTimelinePosition() {
		return timelinePosition;
	}
	
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean getIsSelected() {
		return isSelected;
	}

}

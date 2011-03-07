package com.readytalk.olive.model;
/**
 * class Video holds information to show basic platform of editing page such as showing video icon after uploading a video, and moving the icons in time line and also in the editing box.
 *  
 * @author Team Olive
 *
 */
public class Video {

	private String name;
	private String url;
	private String icon;
	private int projectId;
	private int poolPosition;
	private int timelinePosition;
	private boolean isSelected;
/**
 * Video Constructor
 * @param name name of the project
 * @param url a location of a video which an user wants to upload
 * @param icon icon that represents a video clip
 * @param projectId unique identity key belonging to a project
 * @param poolPosition ordering of video clips in the editing box located top left of UI
 * @param timelinePosition ordering of video clips in the time line
 * @param isSelected check whether an icon is clicked or not
 */
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
/**
 * Brings the name of a video clip that has been set by an user
 * @return name of a video clip 
 */
	public String getName() {
		return name;
	}
/**
 * Registers a name of a video that is uploaded by an user
 * @param name
 */
	public void setName(String name) {
		this.name = name;
	}
/**
 * Brings an url address where a video user wants to up
 * @return url address
 */
	public String getUrl() {
		return url;
	}
	/**
	 * Registers an url address where a video located which an user wants to upload
	 * @param url passed in through the uploading interface
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * Brings an icon designated to a video clip  
	 * @return icon representing a video clip
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * Registers an icon to a uploaded video 
	 * @param icon an icon representing video on the user interface
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * Brings the key represents specific project page
	 * @return the key presents project 
	 */
	public int getProjectId() {
		return projectId;
	}
	/**
	 * Classifies project folder by designating specific project key
	 * @param project unique project key according to which project folder an user is in 
	 */
	public void setProjectId(int project) {
		this.projectId = project;
	}
	/**
	 * Registers the arrangement where a video clip icon has to be located in the editing box of the user interface
	 * @param poolPosition the order number of a video in the editing box
	 */
	public void setPoolPosition(int poolPosition) {
		this.poolPosition = poolPosition;
	}
	/**
	 * Brings the arrangement where a video clip icon has to be located in the editing box of user interface
	 * @return the order number of a video in the editing box
	 */
	public int getPoolPosition() {
		return poolPosition;
	}
	/**
	 * Registers a location of a video in the time line
	 * @param timelinePosition 
	 */
	public void setTimelinePosition(int timelinePosition) {
		this.timelinePosition = timelinePosition;
	}
	/**
	 * Brings the information regarding the arrangement of a video in the timeline
	 * @return a video clip ordering in the timeline
	 */
	public int getTimelinePosition() {
		return timelinePosition;
	}
	/**
	 * Changes video icon's status to be selected
	 * @param isSelected status saying whether a video clip is clicked or not
	 */
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	/**
	 * Returns its status whether a video clip is selected or not
	 * @return whether a video clip is clicked or not
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

}

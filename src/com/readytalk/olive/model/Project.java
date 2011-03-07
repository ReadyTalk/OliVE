package com.readytalk.olive.model;
/**
 * class Project request the relevant information to user
 * according to its name, accountId, and icon.
 * 
 * @author Team Olive
 *
 */

public class Project {
	
	private String name;
	private int accountId;
	private String icon;

/**
 * Project Constructor
 * @param name			a name of the Project folder
 * @param accountId		a unique number distinguishes the different user which enables to show specific user's projects list
 * @param icon 			an icon that is specifically designated to a project
 */
	public Project(String name, int accountId, String icon) {
		this.name = name;
		this.accountId = accountId;
		this.icon = icon;
	}
/**
 * In use of displaying the name of a project for developers for debugging purpose
 * 
 * @return name of a project
 */
	@Override
	public String toString() {
		return name;
	}
/**
 * calls the specific name of a project
 * 
 * @return name of a project
 */
	public String getName() {
		return name;
	}
/**
 * Sets the name of a project with the name that is typed by an user.
 * @param name name of a project
 */
	public void setName(String name) {
		this.name = name;
	}
/**
 * Calls specific an user account by their unique id registered in the Olive Database
 * 
 * @return unique user account id which enables the web to display correct project lists page.
 */
	public int getAccountId() {
		return accountId;
	}
/**
 * 
 * 
 * @param accountId
 */
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
/**
 * Calls the project icon that represents the project.
 * 
 * @return icon an image representing a project
 */
	public String getIcon() {
		return icon;
	}
/**
 * Sets the icon to the corresponding project
 * 
 * @param icon an image representing a project
 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
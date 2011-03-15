package com.readytalk.olive.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jets3t.service.security.AWSCredentials;

import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.User;
/**
 * class DatabaseApi provides tool to connect to Database
 * 
 * @author Team Olive
 *
 */
public class DatabaseApi {

	private static Logger log = Logger.getLogger(DatabaseApi.class.getName());

	// CAUTION: closeConnection() must be called sometime after this method.
	/**
	 * enables to connect to Olive database
	 */
	public static Connection getDBConnection() {
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx
					.lookup("java:comp/env/jdbc/OliveData");
			return ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Close the connection here on error.
		return null;
	}
	/**
	 * enables to disconnect from Live database
	 * @param c an object that is using Olive database
	 */
	public static void closeConnection(Connection c) {
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Check whether the user and password are registered in the Olive database
	 * @param username username that has been registered in the Olive database
	 * @param password password that has been registered in the Olive database
	 * @return return boolean expression saying whether the information ( username/pw ) is registered or not.
	 */
	public static Boolean isAuthorized(String username, String password) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT AccountID FROM Accounts WHERE Username = '" + username
					+ "' AND Password = Password('" + password + "');";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false; // Error!
	}
	/**
	 * Enables to register an account to the Olive database
	 * @param user user information typed in the registration form by an user
	 * @return passes true if the passed in values are valid to be registered, and passes false if the passed in values are invalid to register
	 */
	public static boolean AddAccount(User user) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "INSERT INTO Accounts (Username, Password, Name, Email) "
					+ "VALUES ('" + user.getUsername() + "', Password('"
					+ user.getPassword() + "') " + ", '" + user.getName()
					+ "', '" + user.getEmail() + "');";
			st.executeUpdate(s);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}

	// Used for the general query: "SELECT W FROM X WHERE Y = Z;"
	/**
	 * Get an information which requested by an user from encrypted table
	 */
	public static String getUnknownValueFromTable(String unknownLabel,
			String table, String knownLabel, String knownValue) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);

			s = "SELECT " + unknownLabel + " FROM " + table + " WHERE "
					+ knownLabel + " = '" + knownValue + "';";
			ResultSet r = st.executeQuery(s);
			String unknownValue = "";
			if (r.first()) {
				unknownValue = r.getString(unknownLabel);
			}
			return unknownValue;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return null;
	}
	/**
	 * Brings the specific account information according to user input id
	 * @param username specific username entered by an user
	 * @return return corresponding information of the user name entered by an user
	 */
	public static int getAccountId(String username) {
		return Integer.parseInt(getUnknownValueFromTable("AccountID",
				"Accounts", "Username", username));
	}
	/**
	 * Brings the Username according to its accountID
	 * @param accountId unique account number given to an user
	 * @return corresponding username and account to the accountID from the Olive database
	 */
	public static String getAccountUsername(int accountId) {
		return getUnknownValueFromTable("Username", "Accounts", "AccountID",
				Integer.toString(accountId));
	}
	/**
	 * Brings the Password information according to its accountID
	 * @param accountId unique account number given to an user
	 * @return corresponding password and account to the accountID from the Olive database
	 */
	public static String getAccountPassword(int accountId) {
		return getUnknownValueFromTable("Password", "Accounts", "AccountID",
				Integer.toString(accountId));
	}
	/**
	 * Brings the Account name according to its accountID
	 * @param accountId unique account number given to an user
	 * @return corresponding name and account to the accountID from the Olive database
	 */
	public static String getAccountName(int accountId) {
		return getUnknownValueFromTable("Name", "Accounts", "AccountID",
				Integer.toString(accountId));
	}
	/**
	 * Brings the email address according to its accountID
	 * @param accountId unique account number given to an user
	 * @return corresponding email and account to the accountID from the Olive database
	 */
	public static String getAccountEmail(int accountId) {
		return getUnknownValueFromTable("Email", "Accounts", "AccountID",
				Integer.toString(accountId));
	}
	/**
	 * Brings the security question according to its accountID
	 * @param accountId unique account number given to an user
	 * @return corresponding security question and account to the accountID from the Olive database
	 */
	public static String getAccountSecurityQuestion(int accountId) {
		return getUnknownValueFromTable("SecurityQuestion", "Accounts",
				"AccountID", Integer.toString(accountId));
	}
	/**
	 * Brings the answer of the security question according to its accountID
	 * @param accountId unique account number given to an user
	 * @return corresponding security question answer and account to the accountID from the Olive database
	 */
	public static String getAccountSecurityAnswer(int accountId) {
		return getUnknownValueFromTable("SecurityAnswer", "Accounts",
				"AccountID", Integer.toString(accountId));
	}
	/**
	 * sets the data for edit account section of the web page
	 * @param user brings the data of an user
	 * @return true if data is updated, false if not
	 */
	public static Boolean editAccount(User user) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "UPDATE Accounts SET Name = '" + user.getName()
					+ "' WHERE Username = '" + user.getUsername() + "';";
			st.executeUpdate(s);

			s = "UPDATE Accounts SET Password = Password('"
					+ user.getPassword() + "') WHERE Username = '"
					+ user.getUsername() + "';";
			st.executeUpdate(s);

			s = "UPDATE Accounts SET Email = '" + user.getEmail()
					+ "' WHERE Username = '" + user.getUsername() + "';";
			st.executeUpdate(s);

			s = "UPDATE Accounts SET SecurityQuestion = '"
					+ user.getSecurityQuestion() + "' WHERE Username = '"
					+ user.getUsername() + "';";
			st.executeUpdate(s);

			s = "UPDATE Accounts SET SecurityAnswer = '"
					+ user.getSecurityAnswer() + "' WHERE Username = '"
					+ user.getUsername() + "';";
			st.executeUpdate(s);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}
	/**
	 * enables to remove an account registered
	 * @param accountId unique account number given to an user
	 */
	public static void deleteAccount(int accountId) {
		// TODO implement

		// int projectId = getProjectId(name, accountId);
		// int videoId = getVideoId(name, projectId, accountId);

		// int projectId = -1;
		// int videoId = -1;

		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			ResultSet r;
			s = "SELECT ProjectID FROM Projects WHERE AccountID = " + accountId
					+ ";";
			r = st.executeQuery(s);
			if (r.first()) {
				do {
					deleteProject(r.getInt("ProjectID"));
				} while (r.next());
			}
			// TODO Broken
			// Delete all videos associated with all projects associated with the account.
			// s = "DELETE FROM Videos WHERE ProjectID = '" + projectId + "';"; // TODO Add error checking
			// st.executeUpdate(s);

			// Delete all projects associated with the account.
			// s = "DELETE FROM Projects WHERE AccountID = '" + accountId + "';"; // TODO Add error checking
			// st.executeUpdate(s);

			// Delete the account itself.
			s = "DELETE FROM Accounts WHERE AccountID = '" + accountId + "';"; // TODO Add error checking
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}
	/**
	 * Brings the project id registered in the Olive database
	 * @param projectName name of the project on the web
	 * @param accountId unique account number given to an user
	 * @return projectID if exists on the database, -1 if not there
	 */
	public static int getProjectId(String projectName, int accountId) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT ProjectID FROM Projects WHERE Name = '" + projectName
					+ "' AND AccountID = '" + accountId + "';";
			ResultSet r = st.executeQuery(s);
			int projectId = -1;
			if (r.first()) {
				projectId = r.getInt("ProjectID");
			}
			return projectId;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return -1;
	}
	/**
	 * Brings the project name according to its projectID
	 * @param projectId unique project number given to a project of an user
	 * @return name of the project in the projects of the accountID table of the Olive database
	 */
	public static String getProjectName(int projectId) {
		return getUnknownValueFromTable("Name", "Projects", "ProjectID",
				Integer.toString(projectId));
	}
	/**
	 * Brings the project accountID according to its projectID
	 * @param projectId unique project number given to a project of an user
	 * @return accountID of the project in the projects of the accountID table of the Olive database
	 */
	public static int getProjectAccountId(int projectId) {
		return Integer.parseInt(getUnknownValueFromTable("AccountID",
				"Projects", "ProjectID", Integer.toString(projectId)));
	}
	/**
	 * Brings the project icon according to its projectID
	 * @param projectId unique project number given to a project of an user
	 * @return project icon of the project in the projects of the accountID table of the Olive database
	 */
	public static String getProjectIcon(int projectId) {
		return getUnknownValueFromTable("Icon", "Projects", "ProjectID",
				Integer.toString(projectId));
	}
	/**
	 * Populates projects in the Olive database
	 * @param accountId unique account number given to an user
	 * @return projects properties
	 */
	public static String populateProjects(int accountId) {
		String projects = "";
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT * FROM Projects WHERE AccountID = '" + accountId + "';";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				int projectNum = 0;
				do {
					projectNum += 1; // TODO This is never used.
					String projectName = r.getString("Name");
					String projectIcon = "/olive/images/SPANISH OLIVES.jpg";
					projects += "<div id=\""
							+ projectName
							+ "\" class=\"project-icon-container\">"
							+ "\n"
							+ "<a href=\"OliveServlet?projectName="
							+ projectName
							+ "\"><img src=\""
							+ projectIcon
							+ "\" class=\"project-icon\" alt=\""
							+ projectName
							+ "\" /></a>"
							+ "\n"
							+ "<p><a href=\"OliveServlet?projectName="
							+ projectName
							+ "\">"
							+ projectName
							+ "</a></p>"
							+ "\n"
							+ "<p><small><a id=\"" // TODO Assign the videoName elsewhere for the JavaScript to access.
							+ projectName
							+ "\" class=\"warning delete-project\">Delete</a></small></p>"
							+ "\n" + "</div>" + "\n";
				} while (r.next());
			}
			return projects;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return projects;
		// TODO change db to have unique usernames for accounts and names for
		// both projects and videos in one project
	}
	/**
	 * Checks whether given project name exists already in the account
	 * @param projectName name of the project name
	 * @param accountId unique accountId given to an user
	 * @return true if it exists, false if not
	 */
	public static boolean projectExists(String projectName, int accountId) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);

			s = "SELECT Name FROM Projects WHERE Name = '" + projectName
					+ "' AND AccountID = '" + accountId + "';";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}
	/**
	 * Adds new project
	 * @param project project property such as name, accountID
	 * @return true if possible to make, false if not possible
	 */
	public static boolean AddProject(Project project) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			Boolean exists = projectExists(project.getName(),
					project.getAccountId());
			if (exists) {
				return false;
			}

			s = "INSERT INTO Projects (Name, AccountID, Icon) " + "VALUES ('"
					+ project.getName() + "', '" + project.getAccountId()
					+ "' , '" + project.getIcon() + "');";
			st.executeUpdate(s);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}
	/**
	 * Enables to delete a project requested by an user
	 * @param projectId unique projectId after created by an user
	 */
	public static void deleteProject(int projectId) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);

			// Delete all videos associated with the project.
			s = "DELETE FROM Videos WHERE ProjectID = '" + projectId + "';"; // TODO Add error checking
			st.executeUpdate(s);

			// Delete the project itself.
			s = "DELETE FROM Projects WHERE ProjectID = '" + projectId + "';"; // TODO Add error checking
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}
	/**
	 * Brings the unique videoId given video name and its unique projectId
	 * @param videoName Name of the video that has been typed by an user
	 * @param projectId unique ProjectId given after project's creation
	 * @return corresponding videoId in the Olive database given videoName and projectId
	 */
	// You don't need the accountId if you have the projectId. The projectId was
	// calculated using the accountId.
	public static int getVideoId(String videoName, int projectId) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT VideoID FROM Videos WHERE Name = '" + videoName
					+ "' AND ProjectID = '" + projectId + "';";
			ResultSet r = st.executeQuery(s);
			int videoId = -1;
			if (r.first()) {
				videoId = r.getInt("VideoID");
			}
			return videoId;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return -1;
	}
	/**
	 * Brings the name of the video name given videoId
	 * @param videoId unique videoId for each individual video
	 * @return Name of the video in the Olive database
	 */
	public static String getVideoName(int videoId) {
		return getUnknownValueFromTable("Name", "Videos", "VideoID",
				Integer.toString(videoId));
	}
	/**
	 * Brings the URL where video is located given videoId
	 * @param videoId unique videoId for each individual video
	 * @return URL of the video listed in the Olive database
	 */
	public static String getVideoUrl(int videoId) {
		return getUnknownValueFromTable("URL", "Videos", "VideoID",
				Integer.toString(videoId));
	}
	/**
	 * Brings the video icon given videoId
	 * @param videoId unique videoId for each individual video
	 * @return an icon corresponding to video and videoID
	 */
	public static String getVideoIcon(int videoId) {
		return getUnknownValueFromTable("Icon", "Videos", "VideoID",
				Integer.toString(videoId));
	}
	/**
	 * Brings the project given video Id
	 * @param videoId unique videoId for each individual video
	 * @return projectId where video belongs to listed in the Olive database
	 */
	public static int getVideoProjectId(int videoId) {
		return Integer.parseInt(getUnknownValueFromTable("ProjectID", "Videos",
				"VideoID", Integer.toString(videoId)));
	}
	/**
	 * Check whether there is any video listed in the editing box
	 * @param videoId unique videoId for each individual video
	 * @return true if there is nothing in the editing box, false if something exists
	 */
	public static boolean isVideoPoolPositionNotNull(int videoId) {
		int position = getVideoPoolPosition(videoId);
		if (position != -1) {
			return true;
		}
		return false;
	}
	/**
	 * Check whether there is any video listed in the time line
	 * @param videoId unique videoId for each individual video
	 * @return true if there is nothing in the time line, false if something exists
	 */
	public static boolean isVideoTimelinePositionNotNull(int videoId) {
		int position = getVideoTimelinePosition(videoId);
		if (position != -1) {
			return true;
		}
		return false;
	}
	/**
	 * check the arrangement of the video in the editing box
	 * @param videoId unique videoId for each individual video
	 * @return video's order in the editing box
	 */
	public static int getVideoPoolPosition(int videoId) {
		String videoPoolPosition = getUnknownValueFromTable("PoolPosition",
				"Videos", "VideoID", Integer.toString(videoId));
		if (videoPoolPosition == null) {
			return -1; // TODO Is this a good idea?
		}
		return Integer.parseInt(videoPoolPosition);
	}
	/**
	 * check the arrangement of the video in the time line
	 * @param videoId unique videoId for each individual video
	 * @return video's order in the time line
	 */
	public static int getVideoTimelinePosition(int videoId) {
		String videoTimelinePosition = getUnknownValueFromTable(
				"TimelinePosition", "Videos", "VideoID",
				Integer.toString(videoId));
		if (videoTimelinePosition == null) {
			return -1; // TODO Is this a good idea?
		}
		return Integer.parseInt(videoTimelinePosition);
	}
	/**
	 * check whether video is selected or not
	 * @param videoId unique videoId for each individual video
	 * @return true if selected, false if not
	 */
	public static boolean getVideoIsSelected(int videoId) {
		int isSelectedAsInt = Integer.parseInt(getUnknownValueFromTable(
				"IsSelected", "Videos", "VideoID", Integer.toString(videoId)));
		if (isSelectedAsInt == 0) {
			return false;
		}

		return true;
	}
	/**
	 * Enables to select or unselect
	 * @param videoId unique videoId for each individual video
	 * @param isSelected checker whether it is selected or not
	 * @return true if selected, false if unselected
	 */
	private static boolean setVideoAsSelectedOrUnselected(int videoId,
			boolean isSelected) {
		int isSelectedAsInt;
		if (isSelected) {
			isSelectedAsInt = 1;
		} else {
			isSelectedAsInt = 0;
		}
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "UPDATE Videos SET IsSelected = " + isSelectedAsInt
					+ " WHERE VideoID = '" + videoId + "';";
			st.executeUpdate(s);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}
	/**
	 * Sets status as selected
	 * @param videoId unique videoId for each individual video
	 * @return status of selected
	 */
	public static boolean setVideoAsSelected(int videoId) {
		return setVideoAsSelectedOrUnselected(videoId, true);
	}
	/**
	 * Sets status as unselected
	 * @param videoId unique videoId for each individual video
	 * @return status of unselected
	 */
	public static boolean setVideoAsUnselected(int videoId) {
		return setVideoAsSelectedOrUnselected(videoId, false);
	}
	/**
	 * Sets the position of an video in the time line
	 * @param videoId unique videoId for each individual video
	 * @param position the order in the editing box/time line
	 * @param positionType Pool or Timeline
	 * @return true if it sets, false if it did not
	 */
	private static boolean setPoolOrTimelinePosition(int videoId, int position,
			String positionType) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "UPDATE Videos SET " + positionType + " = " + position
					+ " WHERE VideoID = '" + videoId + "';";
			st.executeUpdate(s);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}
	/**
	 * Sets the position of an video in the editing box
	 * @param videoId unique videoId for each individual video
	 * @param position the order in the editing box/time line
	 * @return sets the order
	 */
	public static boolean setPoolPosition(int videoId, int position) {
		return setPoolOrTimelinePosition(videoId, position, "PoolPosition");
	}
	/**
	 * Sets the position of an video in the timeline
	 * @param videoId unique videoId for each individual video
	 * @param position the order in the editing box/time line
	 * @return sets the order
	 */
	public static boolean setTimelinePosition(int videoId, int position) {
		return setPoolOrTimelinePosition(videoId, position, "TimelinePosition");
	}
	/**
	 * Clears the position of video in the timeline
	 * @param projectId unique videoId for each individual video
	 * @param positionType editing box or timeline
	 * @return true if clears, false if not
	 */
	public static boolean setAllVideoPoolOrTimelinePositionsToNull(
			int projectId, String positionType) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT VideoID FROM Videos WHERE ProjectID = '" + projectId
					+ "';";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				do {
					setPoolOrTimelinePosition(r.getInt("VideoID"), -1,	// TODO Insert "NULL", not -1
							positionType);
				} while (r.next());
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}
	/**
	 * Clears the position in the editing box
	 * @param projectId unique videoId for each individual video
	 * @return clears editing box
	 */
	public static boolean setAllVideoPoolPositionsToNull(int projectId) {
		return setAllVideoPoolOrTimelinePositionsToNull(projectId,
				"PoolPosition");
	}
	/**
	 * Clears the position in the timeline
	 * @param projectId unique videoId for each individual video
	 * @return clears timeline
	 */
	public static boolean setAllVideoTimelinePositionsToNull(int projectId) {
		return setAllVideoPoolOrTimelinePositionsToNull(projectId,
				"TimelinePosition");
	}
	/**
	 * Populates video properties in the olive database
	 * @param projectId unique project number given to a project
	 * @return video properties
	 */
	public static String populateVideos(int projectId) {
		String videos = "";
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT * FROM Videos WHERE ProjectID = '" + projectId + "';";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				int videoNum = 0;
				do {
					videoNum += 1; // TODO This is inconsistent with projects.
					String videoName = r.getString("Name");
					String videoIcon = "/olive/images/olive.png";

					videos += "<span id=\""
							+ videoName
							+ "\" class=\"video-container\"><img id=\"olive"
							+ videoNum
							+ "\" class=\"video-icon\""
							+ "\n"
							+ "src=\""
							+ videoIcon
							+ "\" alt=\"olive"
							+ videoNum
							+ "\" />"
							+ "\n"
							+ videoName
							+ "<br />"
							+ "\n"
							+ "<small><a id\""
							+ videoName
							+ "\" class=\"link add-to-timeline\">Add to Timeline</a></small>"
							+ "<br />"
							+ "\n"
							+ "<small><a id=\"" // TODO Assign the videoName elsewhere for the JavaScript to access.
							+ videoName
							+ "\" class=\"warning delete-video\">Delete</a></small> </span>"
							+ "\n";
				} while (r.next());
			}
			return videos;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return videos;
		// TODO change db to have unique usernames for accounts and names for
		// both projects and videos in one project
	}
	/**
	 * brings the lists of videoIds in a given project
	 * @param projectId unique project number given to a project
	 * @return lists of video ids in an integer array form
	 */
	public static int[] getVideoIds(int projectId) {
		Connection conn = getDBConnection();
		List<Integer> videoIds = new LinkedList<Integer>();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			ResultSet r;
			s = "SELECT VideoID FROM Videos WHERE ProjectID = " + projectId
					+ ";";
			r = st.executeQuery(s);
			if (r.first()) {
				do {
					videoIds.add(r.getInt("VideoID"));
				} while (r.next());
			}

			// Convert the List to an int array.
			int[] videoIdsAsIntArray = new int[videoIds.size()];
			for (int i = 0; i < videoIds.size(); ++i) {
				videoIdsAsIntArray[i] = videoIds.get(i);
			}

			return videoIdsAsIntArray;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return null;
	}
	/**
	 * Adds video to the Olive database
	 * @param name name of the video
	 * @param url where video is located
	 * @param projectId unique project number 
	 * @param icon unique icon given to the video clip
	 */
	public static void AddVideo(String name, String url, int projectId,
			String icon) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "INSERT INTO Videos (Name, URL, ProjectID, Icon) VALUES ('"
					+ name + "', '" + url + "', '" + projectId + "' , '" + icon
					+ "');";
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}
	/**
	 * deletes an video
	 * @param videoId unique video id given a specific video clip
	 */
	public static void deleteVideo(int videoId) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "DELETE FROM Videos WHERE VideoID = '" + videoId + "';"; // TODO Add error checking
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}
	/**
	 * Brings the credential information to connect to database
	 * @return credential properties
	 */
	public static AWSCredentials getAwsCredentials() {
		String awsAccessKeyPropertyName = "";
		String awsSecretKeyPropertyName = "";
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT * FROM S3Credentials;";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				awsAccessKeyPropertyName = r.getString("AWSAccessKey");
				awsSecretKeyPropertyName = r.getString("AWSSecretKey");
			} else {
				log.severe("Cannot locate AWS credentials");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}

		return new AWSCredentials(awsAccessKeyPropertyName,
				awsSecretKeyPropertyName);
	}
	/**
	 * gets Zencoder access key
	 * @return Zencoder api access key information
	 */
	public static String getZencoderApiKey() {
		String zencoderApiKey = "";
		Connection conn = DatabaseApi.getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT * FROM ZencoderCredentials;";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				zencoderApiKey = r.getString("ZencoderAPIKey");
			} else {
				log.severe("Cannot locate Zencoder credentials");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}

		return zencoderApiKey;
	}
	/**
	 * Checks whether input value of security question and answer of an user is correct
	 * @param username id of an user
	 * @param securityQuestion security question made for recovery purpose
	 * @param securityAnswer answer for the security question made
	 * @return true if it matches, false if it does not
	 */
	public static Boolean isCorrectSecurityInfo(String username,
			String securityQuestion, String securityAnswer) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT AccountID FROM Accounts WHERE Username = '" + username
					+ "' AND SecurityQuestion = '" + securityQuestion
					+ "' AND SecurityAnswer = '" + securityAnswer + "';";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false; // Error!
	}
	/**
	 * Modifies a password
	 * @param username id of an user
	 * @param newPassword new password typed in
	 * @return true if it modified, false if not
	 */
	public static Boolean editPassword(String username, String newPassword) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "UPDATE Accounts SET Password = Password('" + newPassword
					+ "') WHERE Username = '" + username + "';";
			st.executeUpdate(s);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}
}

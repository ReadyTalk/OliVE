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

public class DatabaseApi {

	private static Logger log = Logger.getLogger(DatabaseApi.class
			.getName());

	// CAUTION: closeConnection() must be called sometime after this method.
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

	public static void closeConnection(Connection c) {
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Boolean isAuthorized(String username, String password) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT AccountId FROM Accounts WHERE Username = '" + username
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

	public static int getAccountId(String username) {
		return Integer.parseInt(getUnknownValueFromTable("AccountId",
				"Accounts", "Username", username));
	}

	public static String getAccountUsername(int accountId) {
		return getUnknownValueFromTable("Username", "Accounts", "AccountId",
				Integer.toString(accountId));
	}

	public static String getAccountPassword(int accountId) {
		return getUnknownValueFromTable("Password", "Accounts", "AccountId",
				Integer.toString(accountId));
	}

	public static String getAccountName(int accountId) {
		return getUnknownValueFromTable("Name", "Accounts", "AccountId",
				Integer.toString(accountId));
	}

	public static String getAccountEmail(int accountId) {
		return getUnknownValueFromTable("Email", "Accounts", "AccountId",
				Integer.toString(accountId));
	}

	public static String getAccountSecurityQuestion(int accountId) {
		return getUnknownValueFromTable("SecurityQuestion", "Accounts",
				"AccountId", Integer.toString(accountId));
	}

	public static String getAccountSecurityAnswer(int accountId) {
		return getUnknownValueFromTable("SecurityAnswer", "Accounts",
				"AccountId", Integer.toString(accountId));
	}

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
			s = "SELECT ProjectId FROM Projects WHERE AccountId = " + accountId
					+ ";";
			r = st.executeQuery(s);
			if (r.first()) {
				do {
					deleteProject(r.getInt("ProjectId"));
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
			s = "DELETE FROM Accounts WHERE AccountId = '" + accountId + "';"; // TODO Add error checking
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}

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

	public static String getProjectName(int projectId) {
		return getUnknownValueFromTable("Name", "Projects", "ProjectID",
				Integer.toString(projectId));
	}

	public static int getProjectAccountId(int projectId) {
		return Integer.parseInt(getUnknownValueFromTable("AccountID",
				"Projects", "ProjectID", Integer.toString(projectId)));
	}

	public static String getProjectIcon(int projectId) {
		return getUnknownValueFromTable("Icon", "Projects", "ProjectID",
				Integer.toString(projectId));
	}

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

	public static String getVideoName(int videoId) {
		return getUnknownValueFromTable("Name", "Videos", "VideoID",
				Integer.toString(videoId));
	}

	public static String getVideoUrl(int videoId) {
		return getUnknownValueFromTable("URL", "Videos", "VideoID",
				Integer.toString(videoId));
	}

	public static String getVideoIcon(int videoId) {
		return getUnknownValueFromTable("Icon", "Videos", "VideoID",
				Integer.toString(videoId));
	}

	public static int getVideoProjectId(int videoId) {
		return Integer.parseInt(getUnknownValueFromTable("ProjectID", "Videos",
				"VideoID", Integer.toString(videoId)));
	}

	public static int getVideoStartTimeStoryboard(int videoId) {
		String videoStartTimeStoryboard = getUnknownValueFromTable(
				"StartTimeStoryboard", "Videos", "VideoID",
				Integer.toString(videoId));
		if (videoStartTimeStoryboard == null) {
			return -1;	// TODO Is this a good idea?
		}
		return Integer.parseInt(videoStartTimeStoryboard);
	}

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
							+ "\" /><br />"
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

	public static Boolean isCorrectSecurityInfo(String username,
			String securityQuestion, String securityAnswer) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT AccountId FROM Accounts WHERE Username = '" + username
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

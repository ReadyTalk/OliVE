package com.readytalk.olive.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jets3t.service.security.AWSCredentials;

import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.User;
import com.readytalk.olive.servlet.OliveServlet;

public class OliveDatabaseApi {

	private static Logger log = Logger.getLogger(OliveServlet.class.getName());

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
		return null;
	}

	public static void closeConnection(Connection c) {
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Boolean isAuthorized(User user) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT Username FROM Accounts WHERE Username = '"
					+ user.getUsername() + "' AND Password = Password('"
					+ user.getPassword() + "');";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				return true;
			} else {
				return false;
			}
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
			s = "SELECT AccountID FROM Accounts WHERE Username = '"
					+ user.getUsername() + "';";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				int id = r.getInt("AccountID");
				user.setAccountId(id);
			} else {
				log.severe("Cannot locate AccountID with Username \""
						+ user.getUsername() + "\"");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}

	public static String getName(String username) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);

			s = "SELECT Name FROM Accounts WHERE Username = '" + username
					+ "';";
			ResultSet r = st.executeQuery(s);
			String name = "";
			if (r.first()) {
				name = r.getString("Name");
			}
			return name;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return null;
	}

	public static String getEmail(String username) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);

			s = "SELECT Email FROM Accounts WHERE Username = '" + username
					+ "';";
			ResultSet r = st.executeQuery(s);
			String email = "";
			if (r.first()) {
				email = r.getString("Email");
			}
			return email;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return null;
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
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}

	public static void deleteAccount(User user) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "DELETE FROM Accounts WHERE username = '" + user.getUsername()
					+ "';"; // Need to add error checking
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}

	public static String populateProjects(User user) {
		String projects = "";
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			if (user == null) {
				return projects;
			}
			if (user.getAccountId() == -1) {
				s = "SELECT AccountID FROM Accounts WHERE Username = '"
						+ user.getUsername() + "';";
				ResultSet r = st.executeQuery(s);
				int accountID = -1;
				if (r.first()) {
					accountID = r.getInt("AccountID");
				}
				user.setAccountId(accountID);
			}
			s = "SELECT * FROM Projects WHERE AccountID = "
					+ user.getAccountId() + ";";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				int projectNum = 0;
				do {
					projectNum += 1;
					String projectTitle = r.getString("Name");
					String projectIcon = "/olive/images/SPANISH OLIVES.jpg";
					projects += "<div id=\"project-"
							+ projectNum
							+ "\" class=\"project-icon-container\">"
							+ "\n"
							+ "<a href=\"OliveServlet?projectTitle="
							+ projectTitle
							+ "\"><img src=\""
							+ projectIcon
							+ "\" class=\"project-icon\" alt=\""
							+ projectTitle
							+ "\" /></a>"
							+ "\n"
							+ "<p><a href=\"OliveServlet?projectTitle="
							+ projectTitle
							+ "\">"
							+ projectTitle
							+ "</a></p>"
							+ "\n"
							+ "<p><small><a href=\"\" class=\"warning\">Delete</a></small></p>"
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

	public static boolean projectExists(String projectTitle, String username) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);

			s = "SELECT AccountID FROM Accounts WHERE Username = '" + username
					+ "';";
			ResultSet r = st.executeQuery(s);
			int accountID = -1;
			if (r.first()) {
				accountID = r.getInt("AccountID");
			} else {
				return false; // Username does not exist.
			}

			s = "SELECT Name FROM Projects WHERE Name = '" + projectTitle
					+ "' AND AccountID = '" + accountID + "';";
			r = st.executeQuery(s);
			if (r.first()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return false;
	}

	public static void AddProject(Project p, User u) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT AccountID FROM Accounts WHERE Username = '"
					+ u.getUsername() + "';";
			ResultSet r = st.executeQuery(s);
			int accountID = -1;
			if (r.first()) {
				accountID = r.getInt("AccountID");
			}
			if (accountID == -1) {
				throw new SQLException("There is no account for the username: "
						+ u.getUsername());
			}
			s = "INSERT INTO Projects (Name, AccountID, Icon) " + "VALUES ('"
					+ p.getName() + "', '" + accountID + "' , '" + "" + "');";
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}

	public static void deleteProject(String name, int AccountID) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "DELETE FROM Projects WHERE Name = '" + name
					+ "' AND AccountID = '" + AccountID + "';"; // Need to add error checking
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}
	
	public static void AddVideo(String name, String URL, int ProjectID,
			String icon) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "INSERT INTO Videos (Name, URL, ProjectID, Icon) VALUES ('"
					+ name + "', '" + URL + "', '" + ProjectID + "' , '" + icon
					+ "');";
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}

	public static void deleteVideo(String URL) {
		Connection conn = getDBConnection();
		try {
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "DELETE FROM Videos WHERE URL = '" + URL + "';"; // Need to add error checking
			st.executeUpdate(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}

	public static AWSCredentials loadAWSCredentials() throws IOException {
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

		AWSCredentials awsCredentials = new AWSCredentials(
				awsAccessKeyPropertyName, awsSecretKeyPropertyName);

		return awsCredentials;
	}

	public static String getZencoderApiKey() {
		String zencoderApiKey = "";
		Connection conn = OliveDatabaseApi.getDBConnection();
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
}

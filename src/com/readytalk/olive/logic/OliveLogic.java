package com.readytalk.olive.logic;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.User;

public class OliveLogic {

	public static Boolean isAuthorized(User user) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT Username FROM Accounts WHERE Username = '"
					+ user.getUsername() + "' AND Password = Password('" + user.getPassword() + "');";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				closeConnection(conn);

				return true;
			} else {
				closeConnection(conn);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false; // Error!
	}

	public static String populateProjects(User user) {

		String projects = "";
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			if(user==null){
				return projects;
			}
			if (user.getAccountID() == -1) {
				s = "SELECT AccountID FROM Accounts WHERE username ='" + user.getUsername() + "';";
				ResultSet r = st.executeQuery(s);
				int accountID = -1;
				if (r.first()) {
					accountID = r.getInt("AccountID");
				}
				user.setAccountID(accountID);
			}
			s = "SELECT * FROM Projects WHERE AccountID = " + user.getAccountID() + ";";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				int projectNum = 0;
				do {
					projectNum += 1;
					String projectTitle = r.getString("Name");
					String projectIcon = "/olive/images/SPANISH OLIVES.jpg";
					projects += "<div id=\"project-" + projectNum + "\" class=\"project-icon-container\">" + "\n" +
							"<img src=\"" + projectIcon + "\" class=\"project-icon\" alt=\"" + projectTitle + "\" />" + "\n" +
							"<p><a href=\"OliveServlet?page=editor&projectTitle=" + projectTitle + "\">" + projectTitle + "</a></p>" + "\n" +
							"<p><small><a href=\"\" class=\"warning\">Delete</a></small></p>" + "\n" +
							"</div>" +
							"\n";
				} while (r.next());
			}
			return projects;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projects;
		// TODO change db to have unique usernames for accounts and names for both projects and videos in one project

	}

	public static Connection getDBConnection() {
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/OliveData");
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

	public static boolean AddAccount(User user) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "INSERT INTO Accounts (Username, Password, Name, Email)" +
					"VALUES ('" + user.getUsername() + "', Password('" + user.getPassword() + "') " +
							", '" + user.getName() + "', '" + user.getEmail() + "');";
			st.executeUpdate(s);
			s = "SELECT AccountID FROM Accounts WHERE username ='"+user.getUsername()+"';";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				int id = r.getInt("AccountID");
				user.setAccountID(id);
			} else {
				// TODO Add error for rare case that it can't find the data
			}
			closeConnection(conn);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void AddProject(Project p, User u) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT AccountID FROM Accounts WHERE username ='" + u.getUsername() + "';";
			ResultSet r = st.executeQuery(s);
			int accountID = -1;
			if (r.first()) {
				accountID = r.getInt("AccountID");
			}
			if (accountID == -1) {
				throw new SQLException("There is no account for the username: " + u.getUsername());
			}
			s = "INSERT INTO Projects (Name, AccountID, Icon)" +
					"VALUES ('" + p.getTitle() + "', '" + accountID + "' , '');";
			st.executeUpdate(s);
			closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void AddVideo(String name, String URL, int ProjectID, String icon) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "INSERT INTO Videos (Name, URL, ProjectID, Icon)" +
					"VALUES ('" + name + "', '" + URL + "', '" + ProjectID + "' , '" + icon + "');";
			st.executeUpdate(s);
			closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteAccount(User user) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "DELETE FROM Accounts WHERE" +
					"username = '" + user.getUsername() + "';"; // Need to add error checking
			st.executeUpdate(s);
			closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteProject(String name, int AccountID) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "DELETE FROM Projects WHERE" +
					"Name = '" + name + "' AND AccountID = '" + AccountID + "';"; // Need to add error checking
			st.executeUpdate(s);
			closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteVideo(String URL) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "DELETE FROM Videos WHERE" +
					"URL = '" + URL + "';"; // Need to add error checking
			st.executeUpdate(s);
			closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Boolean editAccount(User user) {
		try {
			Connection conn = getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "UPDATE Accounts SET Name = '" + user.getName() + "' WHERE Username " +
					"= '" + user.getUsername() + "'";
			st.executeUpdate(s);

			s = "UPDATE Accounts SET Password = Password('" + user.getPassword() + "') WHERE Username " +
					"= '" + user.getUsername() + "'";
			st.executeUpdate(s);

			s = "UPDATE Accounts SET Email = '" + user.getEmail() + "' WHERE Username " +
					"= '" + user.getUsername() + "'";
			st.executeUpdate(s);
			closeConnection(conn);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String sanitize(String input) throws UnsupportedEncodingException {
		String output = input;

		// Remove the *really* bad stuff (which cause XSS attacks and SQL
		// injections).
		String[] illegalStrings = { "!", "#", "$", "^", "&", "*", "(", ")", "-", "=", "{", "}", "[", "]", "\\", "|", ";", "'", "\"", ":", ",", "<", ">", "/", "?", "`", "~" };
		for (int i = 0; i < illegalStrings.length; ++i) {
			output = output.replace(illegalStrings[i], "");
		}

		// Let encoding convert the rest to safe strings (but not remove).
		String encoding = "UTF-8"; // This is recommended by the The World Wide Web Consortium Recommendation. See : http://download.oracle.com/javase/1.4.2/docs/api/java/net/URLEncoder.html#encode(java.lang.String, java.lang.String)
									// Other encodings: http://download.oracle.com/javase/1.4.2/docs/api/java/nio/charset/Charset.html
		try {
			output = java.net.URLEncoder.encode(output, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("Choose a different encoding than \"" + encoding + "\". " + e.getMessage());
		}

		return output;
	}
}

package com.readytalk.olive.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.readytalk.olive.logic.OliveLogic;
import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.User;

public class OliveServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(OliveServlet.class.getName());

	// Don't store anything as a member variable in the Servlet.
	// private Object dontDoThis;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		/*
		 * The log will have .severe (very bad) .warning (uh oh) and .info
		 * 
		 * You probably want to always use the .info method to log.
		 */
		String id = request.getParameter("FormName");
		log
				.info("This is a servlet responding to an Http POST request from form: "
						+ id);

		Boolean isAuthorized;

		if (id.equals("LoginUser")) {

			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (OliveLogic.isSafe(username) && OliveLogic.isSafe(password)) {
				session.setAttribute("isSafe", true);
				User user = new User(username, password, OliveLogic.getEmail(username), OliveLogic.getName(username));
				isAuthorized = OliveLogic.isAuthorized(user);
				session.setAttribute("isAuthorized", isAuthorized); // Do not redisplay user name (XSS vulnerability).
				if (isAuthorized) { // Take the user to the projects page.
					session.setAttribute("username", user.getUsername());
					session.setAttribute("password", user.getPassword());
					session.setAttribute("email", user.getEmail());
					session.setAttribute("name", user.getName());
					session.removeAttribute("isSafe"); // Cleared so as to not interfere with any other form
					response.sendRedirect("projects.jsp");
				} else { // Keep the user on the same page.
					response.sendRedirect("index.jsp");
				}
			} else {
				session.setAttribute("isSafe", false);
				session.setAttribute("isAuthorized", false);
				response.sendRedirect("index.jsp");
			}

		} else if (id.equals("EditUser")) {
			String username = (String) session.getAttribute("username");
			String newName = request.getParameter("new-name");
			String newEmail = request.getParameter("new-email");
			String newPassword = request.getParameter("new-password");
			String confirmNewPassword = request
					.getParameter("confirm-new-password");
			if (OliveLogic.isSafe(newName) && OliveLogic.isSafe(newEmail)
					&& OliveLogic.isSafe(newPassword)
					&& OliveLogic.isSafe(confirmNewPassword)) {
				if (newPassword.equals(confirmNewPassword)) {
					User updateUser = new User(username, newPassword, newEmail,
							newName);
					Boolean editSuccessfully = OliveLogic
							.editAccount(updateUser);
					session.setAttribute("editSuccessfully", editSuccessfully);
					session.setAttribute("passwordsMatch", true);
					session.setAttribute("password", newPassword);
					session.setAttribute("email", newEmail);
					session.setAttribute("name", newName);
				} else {
					session.setAttribute("editSuccessfully", false);
					session.setAttribute("passwordsMatch", false);
				}
			} else {
				session.setAttribute("editSuccessfully", false);
			}
			response.sendRedirect("account.jsp");

		} else if (id.equals("AddUser")) {
			// The jQuery regex should catch malicious input, but sanitize just to be safe.
			String username = OliveLogic.sanitize(request.getParameter("name"));
			String password = OliveLogic.sanitize(request
					.getParameter("password"));
			String email = OliveLogic.sanitize(request.getParameter("email"));
			User newUser = new User(username, password, email, username);
			Boolean addSuccessfully = OliveLogic.AddAccount(newUser);
			if (addSuccessfully) {
				session.setAttribute("isAuthorized", true);
				session.setAttribute("username", username);
				session.setAttribute("password", password);
				session.setAttribute("email",email);
				response.sendRedirect("projects.jsp");
			} else {
				response.sendRedirect("index.jsp");
				// TODO Add error message here
			}
		}

		else if (id.equals("AddProject")) {
			/*
			 * PrintWriter out = response.getWriter();
			 * response.setContentType("text/plain");
			 * out.println("Project Created. Please close this window and refresh the projects page.");
			 */
			String projectName = request.getParameter("ProjectName");
			if (OliveLogic.isSafe(projectName)) {
				session.setAttribute("isSafe", true);
				// Adding the project information to the database
				User user = new User((String) session.getAttribute("username"),
						(String) session.getAttribute("password"));
				Project project = new Project(projectName, user);
				OliveLogic.AddProject(project, user);
			} else {
				session.setAttribute("isSafe", false);
			}
			response.sendRedirect("newProjectForm.jsp");

		}

	}

	// http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/Servlet-Tutorial-Form-Data.html
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		String projectTitle = request.getParameter("projectTitle");
		if (projectTitle != null
				&& OliveLogic.isSafe(projectTitle)
				&& OliveLogic.projectExists(projectTitle, (String) session
						.getAttribute("username"))) { // Short circuiting
			session.setAttribute("projectTitle", projectTitle);
			response.sendRedirect("editor.jsp");
		} else {
			response.sendRedirect("projects.jsp");
		}
	}
}

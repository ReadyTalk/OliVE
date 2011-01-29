package com.readytalk.olive.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.readytalk.olive.logic.HttpSenderReceiver;
import com.readytalk.olive.logic.OliveDataApi;
import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.User;

public class OliveServlet extends HttpServlet {
	// Don't store anything as a member variable in the Servlet.
	// private Object dontDoThis;

	// Private static variables are ok
	private static Logger log = Logger.getLogger(OliveServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String id = request.getParameter("FormName");
		log.info("This is a servlet responding to "
				+ "an Http POST request from form: " + id);
		if (id.equals("LoginUser")) {
			loginHandler(request, response, session);
		} else if (id.equals("EditUser")) {
			editUserHandler(request, response, session);
		} else if (id.equals("AddUser")) {
			addUserHandler(request, response, session);
		} else if (id.equals("AddProject")) {
			addProjectHandler(request, response, session);
		} else if (id.equals("SplitVideo")) {
			splitVideoHandler(request, response, session);
		} else {
			// TODO Add a condition here for unknown forms.
		}
	}

	private void loginHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		Boolean isAuthorized;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (OliveDataApi.isSafe(username) && OliveDataApi.isSafe(password)) {
			session.setAttribute("isSafe", true);
			User user = new User(username, password,
					OliveDataApi.getEmail(username),
					OliveDataApi.getName(username));
			isAuthorized = OliveDataApi.isAuthorized(user);
			session.setAttribute("isAuthorized", isAuthorized);
			if (isAuthorized) { // Take the user to the projects page.
				session.setAttribute("username", user.getUsername());
				session.setAttribute("password", user.getPassword());
				session.setAttribute("email", user.getEmail());
				session.setAttribute("name", user.getName());
				session.removeAttribute("isSafe"); // Cleared so as to not interfere with any other form.
				response.sendRedirect("projects.jsp");
			} else {
				response.sendRedirect("index.jsp"); // Keep the user on the same page.
			}
		} else {
			session.setAttribute("isSafe", false);
			session.setAttribute("isAuthorized", false);
			response.sendRedirect("index.jsp");
		}
	}

	private void editUserHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		String username = (String) session.getAttribute("username");
		String newName = request.getParameter("new-name");
		String newEmail = request.getParameter("new-email");
		String newPassword = request.getParameter("new-password");
		String confirmNewPassword = request
				.getParameter("confirm-new-password");
		if (OliveDataApi.isSafe(newName) && OliveDataApi.isSafe(newEmail)
				&& OliveDataApi.isSafe(newPassword)
				&& OliveDataApi.isSafe(confirmNewPassword)) {
			if (newPassword.equals(confirmNewPassword)) {
				User updateUser = new User(username, newPassword, newEmail,
						newName);
				Boolean editSuccessfully = OliveDataApi.editAccount(updateUser);
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
	}

	private void addUserHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		// The jQuery regex should catch malicious input, but sanitize just to
		// be safe.
		String username = OliveDataApi.sanitize(request.getParameter("name"));
		String password = OliveDataApi.sanitize(request
				.getParameter("password"));
		String email = OliveDataApi.sanitize(request.getParameter("email"));
		User newUser = new User(username, password, email, username);
		Boolean addSuccessfully = OliveDataApi.AddAccount(newUser);
		if (addSuccessfully) {
			session.setAttribute("isAuthorized", true);
			session.setAttribute("username", username);
			session.setAttribute("password", password);
			session.setAttribute("email", email);
			response.sendRedirect("projects.jsp");
		} else {
			response.sendRedirect("index.jsp");
			// TODO Add error message here
		}
	}

	private void addProjectHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		String projectName = request.getParameter("ProjectName");
		if (OliveDataApi.isSafe(projectName)) {
			session.setAttribute("isSafe", true);
			// Adding the project information to the database
			User user = new User((String) session.getAttribute("username"),
					(String) session.getAttribute("password"));
			Project project = new Project(projectName, user);
			OliveDataApi.AddProject(project, user);
		} else {
			session.setAttribute("isSafe", false);
		}
		response.sendRedirect("newProjectForm.jsp");
	}

	// http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/Servlet-Tutorial-Form-Data.html
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		String projectTitle = request.getParameter("projectTitle");
		if (projectTitle != null
				&& OliveDataApi.isSafe(projectTitle)
				&& OliveDataApi.projectExists(projectTitle,
						(String) session.getAttribute("username"))) { // Short-circuiting
			session.setAttribute("projectTitle", projectTitle);
			response.sendRedirect("editor.jsp");
		} else {
			response.sendRedirect("projects.jsp");
		}
	}

	private void splitVideoHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		HttpSenderReceiver.split();
		response.sendRedirect("editor.jsp");
	}
}

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
import com.readytalk.olive.logic.Security;
import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.User;
import com.readytalk.olive.util.Attribute;

public class OliveServlet extends HttpServlet {
	// Don't store anything as a member variable in the Servlet.
	// private Object dontDoThis;

	// Generated using Eclipse's "Add generated serial version ID" refactoring.
	private static final long serialVersionUID = -6820792513104430238L;
	// Private static variables are okay, though.
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
			log.severe("Unknown form performing POST request.");
		}
	}

	private void loginHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		Boolean isAuthorized;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (Security.isSafeUsername(username)
				&& Security.isSafePassword(password)) {
			session.setAttribute(Attribute.IS_SAFE.toString(), true);
			User user = new User(username, password,
					OliveDataApi.getEmail(username),
					OliveDataApi.getName(username));
			isAuthorized = OliveDataApi.isAuthorized(user);
			session.setAttribute(Attribute.IS_AUTHORIZED.toString(), isAuthorized);
			if (isAuthorized) { // Take the user to the projects page.
				session.setAttribute(Attribute.USERNAME.toString(), user.getUsername());
				session.setAttribute(Attribute.PASSWORD.toString(), user.getPassword());
				session.setAttribute(Attribute.EMAIL.toString(), user.getEmail());
				session.setAttribute(Attribute.NAME.toString(), user.getName());
				session.removeAttribute(Attribute.IS_SAFE.toString()); // Cleared so as to not interfere with any other form.
				response.sendRedirect("projects.jsp");
			} else {
				response.sendRedirect("index.jsp"); // Keep the user on the same page.
			}
		} else {
			session.setAttribute(Attribute.IS_SAFE.toString(), false);
			session.setAttribute(Attribute.IS_AUTHORIZED.toString(), false);
			response.sendRedirect("index.jsp");
		}
	}

	private void editUserHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		String username = (String) session.getAttribute(Attribute.USERNAME.toString());
		String newName = request.getParameter("new-name");
		String newEmail = request.getParameter("new-email");
		String newPassword = request.getParameter("new-password");
		String confirmNewPassword = request
				.getParameter("confirm-new-password");
		if (Security.isSafeName(newName) && Security.isSafeEmail(newEmail)
				&& Security.isSafePassword(newPassword)
				&& Security.isSafePassword(confirmNewPassword)) {
			if (newPassword.equals(confirmNewPassword)) {
				User updateUser = new User(username, newPassword, newEmail,
						newName);
				Boolean editSuccessfully = OliveDataApi.editAccount(updateUser);
				session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(), editSuccessfully);
				session.setAttribute(Attribute.PASSWORDS_MATCH.toString(), true);
				session.setAttribute(Attribute.PASSWORD.toString(), newPassword);
				session.setAttribute(Attribute.EMAIL.toString(), newEmail);
				session.setAttribute(Attribute.NAME.toString(), newName);
			} else {
				session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(), false);
				session.setAttribute(Attribute.PASSWORDS_MATCH.toString(), false);
			}
		} else {
			session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(), false);
		}
		response.sendRedirect("account.jsp");
	}

	private void addUserHandler(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		// The jQuery regex should catch malicious input, but sanitize just to
		// be safe.
		String username = Security.stripOutIllegalCharacters(request
				.getParameter("name"));
		String password = Security.stripOutIllegalCharacters(request
				.getParameter("password"));
		String email = Security.stripOutIllegalCharacters(request
				.getParameter("email"));
		User newUser = new User(username, password, email, username);
		Boolean addSuccessfully = OliveDataApi.AddAccount(newUser);
		if (addSuccessfully) {
			session.setAttribute(Attribute.IS_AUTHORIZED.toString(), true);
			session.setAttribute(Attribute.USERNAME.toString(), username);
			session.setAttribute(Attribute.PASSWORD.toString(), password);
			session.setAttribute(Attribute.EMAIL.toString(), email);
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
		if (Security.isSafeProjectName(projectName)) {
			session.setAttribute(Attribute.IS_SAFE.toString(), true);
			// Adding the project information to the database
			User user = new User((String) session.getAttribute(Attribute.USERNAME.toString()),
					(String) session.getAttribute(Attribute.PASSWORD.toString()));
			Project project = new Project(projectName, user);
			OliveDataApi.AddProject(project, user);
		} else {
			session.setAttribute(Attribute.IS_SAFE.toString(), false);
		}
		response.sendRedirect("new-project-form.jsp");
	}

	// http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/Servlet-Tutorial-Form-Data.html
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		String projectTitle = request.getParameter("projectTitle");
		if (projectTitle != null
				&& Security.isSafeProjectName(projectTitle)
				&& OliveDataApi.projectExists(projectTitle,
						(String) session.getAttribute(Attribute.USERNAME.toString()))) { // Short-circuiting
			session.setAttribute(Attribute.PROJECT_TITLE.toString(), projectTitle);
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

package com.readytalk.olive.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
		log.info("This is a servlet responding to an Http POST request from form: " + id);

		Boolean isAuthorized;

		if (id.equals("LoginUser")) {
			String username = OliveLogic.sanitize(request.getParameter("username"));
			String password = OliveLogic.sanitize(request.getParameter("password"));

			User user = new User(username, password);

			isAuthorized = OliveLogic.isAuthorized(user);

			session.setAttribute("isAuthorized", isAuthorized); // Do not redisplay user name (XSS vulnerability).
			if (isAuthorized) { // Take the user to the projects page.
				session.setAttribute("username", username);
				session.setAttribute("password", password);
				response.sendRedirect("projects.jsp");
			} else { // Keep the user on the same page.
				response.sendRedirect("index.jsp");
			}
		} else if (id.equals("EditUser")) {
			Object user = session.getAttribute("username");
			String username = (String) user;
			String newName = OliveLogic.sanitize(request.getParameter("new-name"));
			String newEmail = OliveLogic.sanitize(request.getParameter("new-email"));
			String newPassword = OliveLogic.sanitize(request.getParameter("new-password"));
			log.info("WTF?????????????");
			User updateUser = new User(username, newPassword, newEmail, newName);
			Boolean editSuccesfully = OliveLogic.editAccount(updateUser);
			session.setAttribute("editSuccesfully", editSuccesfully);
			response.sendRedirect("account.jsp");
		} else if (id.equals("AddUser")) {
			String username = OliveLogic.sanitize(request.getParameter("name"));
			String password = OliveLogic.sanitize(request.getParameter("password"));
			String email = OliveLogic.sanitize(request.getParameter("email"));
			User newUser = new User(username, password, email, username);
			Boolean addSuccesfully = OliveLogic.AddAccount(newUser);
			if(addSuccesfully){
				session.setAttribute("isAuthorized", true);
				session.setAttribute("username", username);
				session.setAttribute("password", password);
				response.sendRedirect("projects.jsp");
			}
			else{
				response.sendRedirect("index.jsp");
				//TODO Add error message here
			}
		}
			
		else if(id.equals("AddProject")){
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			out.println("Project Created. Please close this window and refresh the projects page.");
			String projectName = OliveLogic.sanitize(request.getParameter("ProjectName"));
			User user = new User((String) session.getAttribute("username"), (String) session.getAttribute("password"));
			Project project = new Project(projectName, user);
			OliveLogic.AddProject(project, user);
		}

	}

	// http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/Servlet-Tutorial-Form-Data.html
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		session.setAttribute("projectTitle", OliveLogic.sanitize(request.getParameter("projectTitle")));
		String page = OliveLogic.sanitize(request.getParameter("page")) + ".jsp";
		response.sendRedirect(page);

	}
}

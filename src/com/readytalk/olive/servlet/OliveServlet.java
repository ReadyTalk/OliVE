package com.readytalk.olive.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.readytalk.olive.json.DeleteProjectRequest;
import com.readytalk.olive.json.DeleteVideoRequest;
import com.readytalk.olive.json.GeneralRequest;
import com.readytalk.olive.json.SplitVideoRequest;
import com.readytalk.olive.logic.HttpSenderReceiver;
import com.readytalk.olive.logic.OliveDatabaseApi;
import com.readytalk.olive.logic.S3Api;
import com.readytalk.olive.logic.Security;
import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.User;
import com.readytalk.olive.model.Video;
import com.readytalk.olive.util.Attribute;
import com.readytalk.olive.util.InvalidFileSizeException;

public class OliveServlet extends HttpServlet {
	// Don't store anything as a member variable in the Servlet.
	// private Object dontDoThis;

	// Generated using Eclipse's "Add generated serial version ID" refactoring.
	private static final long serialVersionUID = -6820792513104430238L;
	// Static variables are okay, though, because they don't change across instances.
	private static Logger log = Logger.getLogger(OliveServlet.class.getName());
	public static final String TEMP_DIR_PATH = "/temp/"; // TODO Make a getter for this.
	public static File tempDir; // TODO Make a getter for this.
	public static final String DESTINATION_DIR_PATH = "/temp/"; // TODO Make a getter for this.
	public static File destinationDir; // TODO Make a getter for this.

	// Modified from: http://www.jsptube.com/servlet-tutorials/servlet-file-upload-example.html
	// Also see: http://stackoverflow.com/questions/4101960/storing-image-using-htm-input-type-file
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		createTempDirectories();
	}

	private void createTempDirectories() throws ServletException {
		String realPathTemp = getServletContext().getRealPath(TEMP_DIR_PATH);
		tempDir = new File(realPathTemp);
		if (!tempDir.isDirectory()) {
			throw new ServletException(TEMP_DIR_PATH + " is not a directory");
		}
		String realPathDest = getServletContext().getRealPath(
				DESTINATION_DIR_PATH);
		destinationDir = new File(realPathDest);
		if (!destinationDir.isDirectory()) {
			throw new ServletException(DESTINATION_DIR_PATH
					+ " is not a directory");
		}
	}

	private int getProjectIdFromSessionAttributes(HttpSession session) {
		String sessionUsername = (String) session
				.getAttribute(Attribute.USERNAME.toString());
		int accountId = OliveDatabaseApi.getAccountId(sessionUsername);
		String sessionProjectName = (String) session
				.getAttribute(Attribute.PROJECT_NAME.toString());
		int projectId = OliveDatabaseApi.getProjectId(sessionProjectName,
				accountId);
		return projectId;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (request.getContentType().contains(
				"application/x-www-form-urlencoded")) { // Full value: "application/x-www-form-urlencoded"
			// This is a regular text form.
			String id = request.getParameter("FormName");
			log.info("The servlet is responding to an "
					+ "HTTP POST request from form: " + id);
			if (id.equals("LoginUser")) {
				handleLogin(request, response, session);
			} else if (id.equals("EditUser")) {
				handleEditUser(request, response, session);
			} else if (id.equals("AddUser")) {
				handleAddUser(request, response, session);
			} else if (id.equals("AddProject")) {
				handleAddProject(request, response, session);
			} else if (id.equals("security-question-form")) {
				handleSecurityQuestion(request, response, session);
			} else if (id.equals("new_password")) {
				handleNewPassword(request, response, session);
			} else if (id.equals("DeleteAccount")) {
				handleDeleteAccount(request, response, session);
			} else {
				log.severe("HTTP POST request coming from unknown form: " + id);
			}
		} else if (request.getContentType().contains("multipart/form-data")) { // Full value: "multipart/form-data; boundary=----WebKitFormBoundaryjAGjLWGWeI3ltfBe"
			// This is a file upload form.
			log.info("The servlet is responding to an "
					+ "HTTP POST request from a file upload form");
			handleUploadVideo(request, response, session);
		} else if (request.getContentType().contains("application/json")) {
			// This is not a form, but a custom POST request with JSON in it.
			log.info("The servlet is responding to an "
					+ "HTTP POST request in JSON format");
			handleJsonPostRequest(request, response, session);
		} else {
			log.severe("Unknown content type");
		}
	}

	private void handleDeleteAccount(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		String username = (String) session.getAttribute(Attribute.USERNAME
				.toString());
		int accountId = OliveDatabaseApi.getAccountId(username);
		OliveDatabaseApi.deleteAccount(accountId);
		response.sendRedirect("logout.jsp");
	}

	private void handleNewPassword(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		String newPassword = request.getParameter("password");
		String confirmNewPassword = request.getParameter("confirm_password");
		Boolean newPasswordSet;
		if (Security.isSafePassword(newPassword)
				&& Security.isSafePassword(confirmNewPassword)) {
			session.setAttribute(Attribute.IS_SAFE.toString(), true);
			if (newPassword.equals(confirmNewPassword)) {
				session.setAttribute(Attribute.PASSWORDS_MATCH.toString(), true);
				String username = (String) session
						.getAttribute(Attribute.USERNAME.toString());
				newPasswordSet = OliveDatabaseApi.editPassword(username,
						newPassword);
				session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(),
						newPasswordSet);
			} else {
				session.setAttribute(Attribute.PASSWORDS_MATCH.toString(),
						false);
				session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(),
						false);
			}
		} else {
			session.setAttribute(Attribute.IS_SAFE.toString(), false);
			session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(), false);
		}
		response.sendRedirect("new-password-form.jsp");
		session.removeAttribute(Attribute.USERNAME.toString());
	}

	private void handleSecurityQuestion(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String securityQuestion = request.getParameter("security_question");
		String securityAnswer = request.getParameter("security_answer");
		Boolean isCorrect;
		if (Security.isSafeUsername(username)
				&& Security.isSafeSecurityQuestion(securityQuestion)
				&& Security.isSafeSecurityAnswer(securityAnswer)) {
			session.setAttribute(Attribute.IS_SAFE.toString(), true);
			isCorrect = OliveDatabaseApi.isCorrectSecurityInfo(username,
					securityQuestion, securityAnswer);
			session.setAttribute(Attribute.IS_CORRECT.toString(), isCorrect);
			if (isCorrect) {
				session.setAttribute(Attribute.USERNAME.toString(), username);
				response.sendRedirect("new-password-form.jsp");
				session.removeAttribute(Attribute.IS_SAFE.toString()); // Cleared so as to not interfere with any other form.
			} else {
				response.sendRedirect("forgot.jsp");
			}
		} else {
			session.setAttribute(Attribute.IS_SAFE.toString(), false);
			session.setAttribute(Attribute.IS_CORRECT.toString(), false);
			response.sendRedirect("forgot.jsp");
		}
	}

	// http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/Servlet-Tutorial-Form-Data.html
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("The servlet is responding to an HTTP GET request");
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		String projectName = request.getParameter("projectName");
		int accountId = OliveDatabaseApi.getAccountId((String) session
				.getAttribute(Attribute.USERNAME.toString()));
		if (projectName != null && Security.isSafeProjectName(projectName)
				&& OliveDatabaseApi.projectExists(projectName, accountId)) { // Short-circuiting
			session.setAttribute(Attribute.PROJECT_NAME.toString(), projectName);
			response.sendRedirect("editor.jsp");
		} else {
			response.sendRedirect("projects.jsp");
		}
		PrintWriter out = response.getWriter();
		out.println("File uploaded. Please close this window and refresh the editor page.");
		out.close();
	}

	private void handleLogin(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		Boolean isAuthorized;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (Security.isSafeUsername(username)
				&& Security.isSafePassword(password)) {
			session.setAttribute(Attribute.IS_SAFE.toString(), true);
			isAuthorized = OliveDatabaseApi.isAuthorized(username, password);
			session.setAttribute(Attribute.IS_AUTHORIZED.toString(),
					isAuthorized);
			if (isAuthorized) { // Take the user to the projects page.
				int accountId = OliveDatabaseApi.getAccountId(username);
				session.setAttribute(Attribute.USERNAME.toString(),
						OliveDatabaseApi.getAccountUsername(accountId));
				session.setAttribute(Attribute.PASSWORD.toString(), password);
				session.setAttribute(Attribute.EMAIL.toString(),
						OliveDatabaseApi.getAccountEmail(accountId));
				session.setAttribute(Attribute.NAME.toString(),
						OliveDatabaseApi.getAccountName(accountId));
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

	private void handleEditUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		String username = (String) session.getAttribute(Attribute.USERNAME
				.toString());
		String newName = request.getParameter("new-name");
		String newEmail = request.getParameter("new-email");
		String newPassword = request.getParameter("new-password");
		String confirmNewPassword = request
				.getParameter("confirm-new-password");
		String securityQuestion = request.getParameter("security_question");
		String securityAnswer = request.getParameter("security_answer");
		log.info("Security question: " + securityQuestion
				+ ". Security Answer: " + securityAnswer);
		if (Security.isSafeName(newName) && Security.isSafeEmail(newEmail)
				&& Security.isSafePassword(newPassword)
				&& Security.isSafePassword(confirmNewPassword)
				&& Security.isSafeSecurityQuestion(securityQuestion)
				&& Security.isSafeSecurityAnswer(securityAnswer)) {
			if (newPassword.equals(confirmNewPassword)) {
				User updateUser = new User(username, newPassword, newName,
						newEmail, securityQuestion, securityAnswer);
				Boolean editSuccessfully = OliveDatabaseApi
						.editAccount(updateUser);
				session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(),
						editSuccessfully);
				session.setAttribute(Attribute.PASSWORDS_MATCH.toString(), true);
				session.setAttribute(Attribute.PASSWORD.toString(), newPassword);
				session.setAttribute(Attribute.EMAIL.toString(), newEmail);
				session.setAttribute(Attribute.NAME.toString(), newName);
				session.setAttribute(Attribute.SECURITY_QUESTION.toString(),
						securityQuestion);
				session.setAttribute(Attribute.SECURITY_ANSWER.toString(),
						securityAnswer);
			} else {
				session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(),
						false);
				session.setAttribute(Attribute.PASSWORDS_MATCH.toString(),
						false);
			}
		} else {
			session.setAttribute(Attribute.EDIT_SUCCESSFULLY.toString(), false);
		}
		response.sendRedirect("account.jsp");
	}

	private void handleAddUser(HttpServletRequest request,
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
		User newUser = new User(username, password, "", email);
		Boolean addSuccessfully = OliveDatabaseApi.AddAccount(newUser);
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

	private void handleAddProject(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException, IOException {
		String projectName = request.getParameter("ProjectName");
		if (Security.isSafeProjectName(projectName)) {
			session.setAttribute(Attribute.IS_SAFE.toString(), true);

			String sessionUsername = (String) session
					.getAttribute(Attribute.USERNAME.toString());
			int accountId = OliveDatabaseApi.getAccountId(sessionUsername);
			String icon = ""; // TODO Get this from user input.
			Project project = new Project(projectName, accountId, icon);
			Boolean added = OliveDatabaseApi.AddProject(project);
			if (!added) {
				session.setAttribute(Attribute.ADD_SUCCESSFULLY.toString(),
						false);
			} else {
				session.setAttribute(Attribute.ADD_SUCCESSFULLY.toString(),
						true);
			}
		} else {
			session.setAttribute(Attribute.IS_SAFE.toString(), false);
		}
		response.sendRedirect("new-project-form.jsp");
	}

	private void handleUploadVideo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		PrintWriter out = response.getWriter();
		out.println("Uploading file...");

		response.setContentType("text/plain");

		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		// Set the size threshold, above which content will be stored on disk.
		fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB

		// Set the temporary directory to store the uploaded files of size above threshold.
		fileItemFactory.setRepository(tempDir);

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		File file = null;
		try {
			/*
			 * Parse the request
			 */
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();

			/*
			 * The two items in the form
			 */
			FileItem videoNameItem = null;
			FileItem fileItem = null;
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				/*
				 * Handle Form Fields.
				 */
				if (item.isFormField()
						&& item.getFieldName().equals("VideoName")) { // Short-circuitry
					// Handle text fields
					log.info("Form Name = \"" + item.getFieldName()
							+ "\", Value = \"" + item.getString() + "\"");
					videoNameItem = item;
				} else {
					// Handle Uploaded files.
					log.info("Field Name = \"" + item.getFieldName()
							+ "\", File Name = \"" + item.getName()
							+ "\", Content type = \""
							+ item.getContentType() // TODO Save this
							+ "\", File Size (bytes) = \"" + item.getSize()
							+ "\"");
					fileItem = item;
				}
			}

			if (videoNameItem == null) {
				log.severe("Video name field not found in video upload form");
				return;
			}
			if (fileItem == null) {
				log.severe("File field not found in video upload form");
				return;
			}

			/*
			 * Write file to the ultimate location.
			 */
			file = new File(destinationDir, fileItem.getName()); // Allocate the space
			fileItem.write(file); // Save the file to the allocated space
			int projectId = getProjectIdFromSessionAttributes(session);
			String videoName = videoNameItem.getString();
			if (Security.isSafeVideoName(videoName)) {
				String videoUrl = S3Api.uploadFile(file);
				if (videoUrl != null) {
					OliveDatabaseApi.AddVideo(videoName, videoUrl, projectId,
							"/olive/images/bbb480.jpg"); // TODO Get icon from Zencoder.
					// File downloadedFile = S3Api.downloadFile(videoUrl); // TODO Add to /temp/ folder so it can be played in the player.
				} else {
					out.println("Error uploading video to the cloud.");
					log.warning("Error uploading video to the cloud.");
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			} else {
				out.println("Video name is invalid.");
				log.warning("Video name is invalid.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			out.println("File uploaded. Please close this window and refresh the editor page.");
			out.println();
		} catch (FileUploadException e) {
			log.severe("Error encountered while parsing the request in the upload handler");
			out.println("Upload failed.");
			e.printStackTrace();
		} catch (InvalidFileSizeException e) {
			log.severe("Invalid file size");
			out.println("Upload failed (invalid file size)");
			e.printStackTrace();
		} catch (Exception e) {
			log.severe("Unknown error encountered while uploading file");
			out.println("Upload failed (unknown reason).");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			if (file != null) {
				file.delete();
			}
		}
	}

	// Gson help: http://code.google.com/p/google-gson/
	// http://stackoverflow.com/questions/338586/a-better-java-json-library
	// http://stackoverflow.com/questions/1688099/converting-json-to-java/1688182#1688182
	private void handleJsonPostRequest(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		String line;
		String json = "";
		while ((line = request.getReader().readLine()) != null) {
			json += line;
		}
		request.getReader().close();

		GeneralRequest generalRequest = new Gson().fromJson(json,
				GeneralRequest.class);

		if (generalRequest.command.equals("getProjects")) {
			handleGetProjects(request, response, session, json);
		} else if (generalRequest.command.equals("createProject")) {
			handleCreateProject(request, response, session, json);
		} else if (generalRequest.command.equals("deleteProject")) {
			handleDeleteProject(request, response, session, json);
		} else if (generalRequest.command.equals("renameProject")) {
			handleRenameProject(request, response, session, json);
		} else if (generalRequest.command.equals("getVideos")) {
			handleGetVideos(request, response, session, json);
		} else if (generalRequest.command.equals("createVideo")) {
			handleCreateVideo(request, response, session, json);
		} else if (generalRequest.command.equals("deleteVideo")) {
			handleDeleteVideo(request, response, session, json);
		} else if (generalRequest.command.equals("renameVideo")) {
			handleRenameVideo(request, response, session, json);
		} else if (generalRequest.command.equals("addToTimeline")) {
			handleAddToTimeline(request, response, session, json);
		} else if (generalRequest.command.equals("removeFromTimeline")) {
			handleRemoveFromTimeline(request, response, session, json);
		} else if (generalRequest.command.equals("addToSelected")) {
			handleAddToSelected(request, response, session, json);
		} else if (generalRequest.command.equals("removeFromSelected")) {
			handleRemoveFromSelected(request, response, session, json);
		} else if (generalRequest.command.equals("splitVideo")) {
			handleSplitVideo(request, response, session, json);
		} else if (generalRequest.command.equals("combineVideos")) {
			handleCombineVideos(request, response, session, json);
		} else if (generalRequest.command.equals("getVideoInformation")) {
			getVideoInformation(request, response, session, json);
		} else {
			log.warning("JSON request not recognized.");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	private void handleGetProjects(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleGetProjects has not yet been implemented.");
	}

	private void handleCreateProject(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleCreateProject has not yet been implemented.");
	}

	private void handleDeleteProject(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		DeleteProjectRequest deleteProjectRequest = new Gson().fromJson(json,
				DeleteProjectRequest.class);

		response.setContentType("text/plain");
		// response.setStatus(HttpServletResponse.SC_OK); // Unnecessary

		PrintWriter out = response.getWriter();

		String sessionUsername = (String) session
				.getAttribute(Attribute.USERNAME.toString());
		int accountId = OliveDatabaseApi.getAccountId(sessionUsername);
		String projectToDelete = deleteProjectRequest.arguments.project;
		int projectId = OliveDatabaseApi.getProjectId(projectToDelete,
				accountId);
		OliveDatabaseApi.deleteProject(projectId);

		out.println(deleteProjectRequest.arguments.project
				+ " deleted successfully.");
		out.close();
	}

	private void handleRenameProject(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleRenameProject has not yet been implemented.");
	}

	private void handleGetVideos(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleGetVideos has not yet been implemented.");
	}

	private void handleCreateVideo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleCreateVideo has not yet been implemented.");
	}

	private void handleDeleteVideo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		DeleteVideoRequest deleteVideoRequest = new Gson().fromJson(json,
				DeleteVideoRequest.class);

		response.setContentType("text/plain");

		PrintWriter out = response.getWriter();

		int projectId = getProjectIdFromSessionAttributes(session);
		int videoId = OliveDatabaseApi.getVideoId(
				deleteVideoRequest.arguments.video, projectId);
		OliveDatabaseApi.deleteVideo(videoId);

		out.println(deleteVideoRequest.arguments.video
				+ " deleted successfully.");
		out.close();
	}

	private void handleRenameVideo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleRenameVideo has not yet been implemented.");
	}

	private void handleAddToTimeline(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleAddToTimeline has not yet been implemented.");
	}

	private void handleRemoveFromTimeline(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleRemoveFromTimeline has not yet been implemented.");
	}

	private void handleAddToSelected(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleAddToSelected has not yet been implemented.");
	}

	private void handleRemoveFromSelected(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleRemoveFromSelected has not yet been implemented.");
	}

	private void handleSplitVideo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		SplitVideoRequest splitVideoRequest = new Gson().fromJson(json,
				SplitVideoRequest.class);

		response.setContentType("text/plain");

		PrintWriter out = response.getWriter();

		if (!Security.isSafeVideoName(splitVideoRequest.arguments.video)) {
			out.println("Name of video to split is invalid.");
			log.warning("Name of video to split is invalid.");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		if (!Security
				.isSafeSplitTimeInSeconds(splitVideoRequest.arguments.splitTimeInSeconds)) {
			out.println("Split time (in seconds) is invalid.");
			log.warning("Split time (in seconds) is invalid.");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		int projectId = getProjectIdFromSessionAttributes(session);
		int videoId = OliveDatabaseApi.getVideoId(
				splitVideoRequest.arguments.video, projectId);
		Video[] videoFragments = HttpSenderReceiver.split(videoId,
				splitVideoRequest.arguments.splitTimeInSeconds);

		for (Video videoFragment : videoFragments) { // foreach-loop
			OliveDatabaseApi.AddVideo(videoFragment.getName(),
					videoFragment.getUrl(), projectId, videoFragment.getIcon()); // projectId not computed by Zencoder
		}

		out.println(splitVideoRequest.arguments.video + " split at "
				+ splitVideoRequest.arguments.splitTimeInSeconds
				+ " seconds successfully.");
		out.close();
	}

	private void handleCombineVideos(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		log.severe("handleCombineVideos has not yet been implemented.");
	}

	private void getVideoInformation(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String json)
			throws IOException {
		int projectId = getProjectIdFromSessionAttributes(session);
		String videoString = S3Api.getVideoInformation(projectId);
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(videoString);
		out.close();
	}
}

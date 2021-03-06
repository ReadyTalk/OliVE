package com.readytalk.olive.logic;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;

/**
 * class Security checks whether the inputs are valid or invalid
 * 
 * @author Team Olive
 * 
 */
// The regular expressions were taken from the JavaScript, but with A-Z appended
// to all a-z's, to account for case sensitivity that the JavaScript usually
// accounts for using /i at the end of the regular expression.
public class Security {

	private static final int MIN_USERNAME_LENGTH = 3;
	private static final int MAX_USERNAME_LENGTH = 16;
	private static final String SAFE_USERNAME_REGEX = "^[a-zA-z]([0-9a-zA-Z_])+$";

	private static final int MIN_EMAIL_LENGTH = 6;
	private static final int MAX_EMAIL_LENGTH = 64;
	// From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
	private static final String SAFE_EMAIL_REGEX = "^((([a-zA-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-zA-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-zA-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-zA-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-zA-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-zA-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-zA-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-zA-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-zA-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-zA-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$";

	private static final int MIN_PASSWORD_LENGTH = 5;
	private static final int MAX_PASSWORD_LENGTH = 128;
	private static final String SAFE_PASSWORD_REGEX = "^[a-zA-Z0-9_]$|^[a-zA-Z0-9_]+[a-zA-Z0-9_ ]*[a-zA-Z0-9_]+$"; // Prevent spaces at the beginning or end, but don't require at least 2 characters.

	private static final int MIN_NAME_LENGTH = 1;
	private static final int MAX_NAME_LENGTH = 32;
	private static final String SAFE_NAME_REGEX = SAFE_PASSWORD_REGEX;

	private static final int MIN_PROJECT_NAME_LENGTH = 1;
	private static final int MAX_PROJECT_NAME_LENGTH = 32;
	private static final String SAFE_PROJECT_NAME_REGEX = SAFE_PASSWORD_REGEX;
	private static final int MAXIMUM_NUMBER_OF_PROJECTS = 25;

	private static final int MIN_VIDEO_NAME_LENGTH = 1;
	private static final int MAX_VIDEO_NAME_LENGTH = 32;
	private static final String SAFE_VIDEO_NAME_REGEX = SAFE_PASSWORD_REGEX;
	private static final int MAXIMUM_NUMBER_OF_VIDEOS = 25;

	public static final double MIN_SPLIT_TIME_IN_SECONDS = 0; // ASSUME: No video is shorter than 0 seconds.
	public static final double MAX_SPLIT_TIME_IN_SECONDS = 14400; // ASSUME: No video is longer than 4 hours.

	private static final int MIN_SECURITY_QUESTION_LENGTH = 1;
	private static final int MAX_SECURITY_QUESTION_LENGTH = 128;
	private static final String SAFE_SECURITY_QUESTION_REGEX = "^[0-9a-zA-Z .,]+[?.]*$";

	private static final int MIN_SECURITY_ANSWER_LENGTH = 1;
	private static final int MAX_SECURITY_ANSWER_LENGTH = 128;
	private static final String SAFE_SECURITY_ANSWER_REGEX = "^([0-9a-zA-Z .,])+$";

	private static final long MIN_VIDEO_SIZE_IN_BYTES = 1; // No empty files
	private static final long MAX_VIDEO_SIZE_IN_BYTES = 524288000; // 500 MB

	/**
	 * Checks to see if the length of an input meets database and JavaScript length requirement
	 * 
	 * @param input
	 *            user input
	 * @param minLength
	 *            minimum length allowed by database and Javascript
	 * @param maxLength
	 *            maximum length allowed by database and Javascript
	 * @return the length has to meet the requirements of database and Javascript
	 */
	// Input must match database and JavaScript length requirements!
	private static boolean isSafeLength(String input, int minLength,
			int maxLength) {
		return input != null && minLength <= input.length()
				&& input.length() <= maxLength;
	}

	/**
	 * Determines whether the input value is valid according to regular expression
	 * @param input
	 * @param validRegex
	 * @return
	 */
	// Input must match database and JavaScript value requirements!
	// For Java regular expression syntax, see:
	// http://download.oracle.com/javase/1.4.2/docs/api/java/util/regex/Pattern.html
	private static boolean isSafeValue(String input, String validRegex) {
		Pattern pattern = Pattern.compile(validRegex);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	public static boolean isSafeUsername(String username) {
		return isSafeLength(username, MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH)
				&& isSafeValue(username, SAFE_USERNAME_REGEX);
	}

	public static boolean isSafeEmail(String email) {
		// TODO Make case-insensitive with /i: http://www.regular-expressions.info/javascript.html
		return isSafeLength(email, MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH)
				&& isSafeValue(email, SAFE_EMAIL_REGEX);
	}

	public static boolean isSafePassword(String password) {
		return isSafeLength(password, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH)
				&& isSafeValue(password, SAFE_PASSWORD_REGEX);
	}

	public static boolean isSafeName(String name) {
		return isSafeLength(name, MIN_NAME_LENGTH, MAX_NAME_LENGTH)
				&& isSafeValue(name, SAFE_NAME_REGEX);
	}

	public static boolean isSafeProjectName(String projectName) {
		return isSafeLength(projectName, MIN_PROJECT_NAME_LENGTH,
				MAX_PROJECT_NAME_LENGTH)
				&& isSafeValue(projectName, SAFE_PROJECT_NAME_REGEX);
	}

	public static boolean isUniqueProjectName(String projectName, int accountId) {
		return !DatabaseApi.projectExists(projectName, accountId);
	}

	public static boolean projectFits(int numberOfProjects) {
		return numberOfProjects < MAXIMUM_NUMBER_OF_PROJECTS;
	}

	public static boolean isSafeVideoName(String videoName) {
		return isSafeLength(videoName, MIN_VIDEO_NAME_LENGTH,
				MAX_VIDEO_NAME_LENGTH)
				&& isSafeValue(videoName, SAFE_VIDEO_NAME_REGEX);
	}

	public static boolean isUniqueVideoName(String videoName, int projectId) {
		return !DatabaseApi.videoExists(videoName, projectId);
	}

	public static boolean videoFits(int numberOfVideos) {
		return numberOfVideos < MAXIMUM_NUMBER_OF_VIDEOS;
	}

	public static boolean isSafeSplitTimeInSeconds(double splitTimeInSeconds) {
		return splitTimeInSeconds > MIN_SPLIT_TIME_IN_SECONDS
				&& splitTimeInSeconds < MAX_SPLIT_TIME_IN_SECONDS;
	}

	public static boolean isSafeSecurityQuestion(String securityQuestion) {
		return isSafeLength(securityQuestion, MIN_SECURITY_QUESTION_LENGTH,
				MAX_SECURITY_QUESTION_LENGTH)
				&& isSafeValue(securityQuestion, SAFE_SECURITY_QUESTION_REGEX);
	}

	public static boolean isSafeSecurityAnswer(String securityAnswer) {
		return isSafeLength(securityAnswer, MIN_SECURITY_ANSWER_LENGTH,
				MAX_SECURITY_ANSWER_LENGTH)
				&& isSafeValue(securityAnswer, SAFE_SECURITY_ANSWER_REGEX);
	}

	public static boolean isSafeProjectIcon(File projectIcon) {
		return false; // TODO Implement this
	}

	public static boolean isSafeVideoIcon(File videoIcon) {
		return false; // TODO Implement this
	}

	public static boolean isSafeVideo(File video) {
		return video.isFile(); // TODO Improve this.

		/*
		 * FileItem video; // Passed as argument
		 * String contentType = video.getContentType();
		 * if (contentType == null) {
		 * return false;
		 * }
		 * String[] content = contentType.split("/");
		 * return content[0].equals("video")
		 * || (content[0].equals("audio") && content[1].equals("ogg"));
		 */
	}
	
	/**
	 * Used whenever a new video is created. Generates unique video name.
	 * @param videoName
	 * @param projectId
	 * @return
	 */
	public static String convertToSafeAndUniqueVideoName(String videoName,
			int projectId) {
		String safeVideo = stripOutIllegalCharacters(videoName, "_");
		safeVideo = safeVideo.trim(); // Trim off white space at beginning and end.

		while (safeVideo.length() < MIN_VIDEO_NAME_LENGTH) {
			safeVideo += "Video";
		}

		if (safeVideo.length() > MAX_VIDEO_NAME_LENGTH) {
			safeVideo.substring(0, MAX_VIDEO_NAME_LENGTH);
		}

		String divider = " ";
		int suffixInt = 1; // Normal human beings start counting at one, not zero.
		while (DatabaseApi.videoExists(safeVideo, projectId)) {
			String suffix = divider + suffixInt;
			int substringLength = MAX_VIDEO_NAME_LENGTH - suffix.length();

			if (safeVideo.length() > substringLength) { // Prevent IndexOutOfBoundsException
				safeVideo.substring(0, substringLength);
			}

			safeVideo += suffix;

			if (DatabaseApi.videoExists(safeVideo, projectId)) {
				// The loop will occur again and another number will be
				// appended, so undo the last appending before trying again. (so
				// "Video1" becomes "Video2" next, not "Video12")
				safeVideo = safeVideo.substring(0, safeVideo.length() - suffix.length());
			}

			suffixInt++;
		}

		return safeVideo;
	}

	private static final String[] ILLEGAL_STRINGS = { "!", "@", "#", "$", "%",
			"^", "&", "*", "(", ")", "-", "+", "=", "{", "}", "[", "]", "\\",
			"|", ";", ":", "'", "\"", ",", ".", "<", ">", "/", "?", "`", "~" };

	// The registration modal form does its own regular expression checking,
	// which should prevent any bad characters from getting in. This is just a
	// safety check for if the JavaScript got hacked and a bad character got in.
	public static String stripOutIllegalCharacters(String input,
			String replacement) {
		String output = input;

		// Remove the *really* bad stuff (which cause XSS attacks and SQL
		// injections).
		for (int i = 0; i < ILLEGAL_STRINGS.length; ++i) {
			output = output.replace(ILLEGAL_STRINGS[i], replacement);
		}

		return output;
	}
}

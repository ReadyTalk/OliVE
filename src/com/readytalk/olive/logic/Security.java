package com.readytalk.olive.logic;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Security {

	// Input must match database and JavaScript length requirements!
	private static boolean isSafeLength(String input, int minLength,
			int maxLength) {
		return input.length() < minLength || input.length() > maxLength;
	}

	// Input must match database and JavaScript value requirements!
	// For Java regular expression syntax, see:
	// http://download.oracle.com/javase/1.4.2/docs/api/java/util/regex/Pattern.html
	private static boolean isSafeValue(String input, String validRegex) {
		Pattern pattern = Pattern.compile(validRegex);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	public static boolean isSafeUsername(String input)
			throws UnsupportedEncodingException {
		return isSafeLength(input, 3, 16)
				&& isSafeValue(input, "^[a-z]([0-9a-z_])+$"); // TODO Make case-insensitive with /i: http://www.regular-expressions.info/javascript.html
	}

	public static boolean isSafeEmail(String input)
			throws UnsupportedEncodingException {
		// TODO Make case-insensitive with /i: http://www.regular-expressions.info/javascript.html
		return isSafeLength(input, 6, 64)
				&& isSafeValue(
						input,
						"^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$");
	}

	public static boolean isSafePassword(String input)
			throws UnsupportedEncodingException {
		return isSafeLength(input, 5, 128)
				&& isSafeValue(input, "^([0-9a-zA-Z])+$");
	}

	public static boolean isSafeName(String input)
			throws UnsupportedEncodingException {
		return isSafeLength(input, 1, 32)
				&& isSafeValue(input, "^([0-9a-zA-Z])+$"); // Same as password's regex
	}

	public static boolean isSafeProjectName(String input)
			throws UnsupportedEncodingException {
		return isSafeLength(input, 1, 32)
				&& isSafeValue(input, "^([0-9a-zA-Z])+$"); // Same as password's regex
	}

	public static boolean isSafeVideoName(String input)
			throws UnsupportedEncodingException {
		return isSafeLength(input, 1, 20)
				&& isSafeValue(input, "^([0-9a-zA-Z])+$"); // Same as password's regex
	}

	public static boolean isSafeSecurityQuestion(String input)
			throws UnsupportedEncodingException {
		return isSafeLength(input, 1, 20)
				&& isSafeValue(input, "^([0-9a-zA-Z])+$"); // Same as password's regex
	}

	public static boolean isSafeSecurityAnswer(String input)
			throws UnsupportedEncodingException {
		return isSafeLength(input, 1, 20)
				&& isSafeValue(input, "^([0-9a-zA-Z])+$"); // Same as password's regex
	}

	public static boolean isSafeProjectIcon(File icon)
			throws UnsupportedEncodingException {
		return false; // TODO Implement this
	}

	public static boolean isSafeVideoIcon(File icon)
			throws UnsupportedEncodingException {
		return false; // TODO Implement this
	}

	public static boolean isSafeVideo(File video)
			throws UnsupportedEncodingException {
		return false; // TODO Implement this
	}

	// The registration modal form does its own regular expression checking,
	// which should prevent any bad characters from getting in. This is just a
	// safety check for if the JavaScript got hacked and a bad character got in.
	public static String stripOutIllegalCharacters(String input) {
		// Why some characters are allowed:
		// http://stackoverflow.com/questions/2049502/what-characters-are-allowed-in-email-address
		String[] illegalStrings = { "!", "&", "*", "(", ")", "-", "=", "{",
				"}", "[", "]", "\\", "|", ";", "'", "\"", ":", ",", "<", ">",
				"/", "?", "`" };

		String output = input;

		// Remove the *really* bad stuff (which cause XSS attacks and SQL
		// injections).
		for (int i = 0; i < illegalStrings.length; ++i) {
			output = output.replace(illegalStrings[i], "");
		}

		return output;
	}
}

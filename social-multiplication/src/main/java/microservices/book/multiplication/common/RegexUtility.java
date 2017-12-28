package microservices.book.multiplication.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Utility class that helps for working with Regular Expressions
 * @author efrem
 *
 */
@Component
public class RegexUtility  {

	/**
	 * Search for first occurrence of {@code regex} pattern inside {@code input} and returns this substring or null
	 * if nothing matches the pattern 
	 * @param regex Regular Expression
	 * @param input String
	 * @return found substring or null
	 */
	public static String getFirstMatch(String regex, String input) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group() : null;
	}
}

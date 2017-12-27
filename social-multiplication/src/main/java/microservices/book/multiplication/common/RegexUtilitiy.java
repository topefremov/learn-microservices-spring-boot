package microservices.book.multiplication.common;

import java.util.regex.Pattern;

public interface RegexUtilitiy {
	public String getFirstMatcherFromInput(Pattern pattern, String input);
}

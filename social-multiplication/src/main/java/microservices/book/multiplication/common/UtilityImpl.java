package microservices.book.multiplication.common;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UtilityImpl implements Utility {
	
	private RegexUtilitiy regexUtility;
	private Pattern pattern;
	
	@Autowired
	public UtilityImpl(RegexUtilitiy regexUtility, @Qualifier("invalidProperyPattern") Pattern pattern) {
		this.regexUtility = regexUtility;
		this.pattern = pattern;
	}

	@Override
	public String getInvalidProperyFromReferencePath(String input) {
		return regexUtility.getFirstMatcherFromInput(pattern, input);
	}

}

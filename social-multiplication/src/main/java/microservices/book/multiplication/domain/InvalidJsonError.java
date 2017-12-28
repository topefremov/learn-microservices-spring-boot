package microservices.book.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class InvalidJsonError {
	private final long timestamp;
	private final int status;
	private final String error;
	private final String message;
	
	public InvalidJsonError(int status, String error, String message) {
		this(System.currentTimeMillis(), status, error, message);
	}
}

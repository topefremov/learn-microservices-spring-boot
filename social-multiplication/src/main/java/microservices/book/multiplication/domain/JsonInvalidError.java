package microservices.book.multiplication.domain;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class JsonInvalidError {
	private final long timestamp;
	private final int status;
	private final String error;
	private final String message;
}

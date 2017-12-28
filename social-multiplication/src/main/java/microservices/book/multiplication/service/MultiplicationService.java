package microservices.book.multiplication.service;

import java.util.List;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService {
	/**
	 * Creates a Multiplication object with two randomly-­ generated factors
	 * between 11 and 99.
	 *
	 * @return a Multiplication object with random factors
	 */
	Multiplication createRandomMultiplication();
	
	/**
	 * 
	 * @param resultAttempt 
	 * @return true if the attempt matches the result of the multiplication, false otherwise.
	 */
	boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);
	
	/**
	 * 
	 * @param userAlias User alias
	 * @return top five attempts given by user which is identified by User alias
	 */
	List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
	
	/**
	 * 
	 * @param resultId id of the multiplication attempt
	 * @return {@link MultiplicationResultAttempt} identified by resultId
	 */
	MultiplicationResultAttempt getResultById(Long resultId);
}

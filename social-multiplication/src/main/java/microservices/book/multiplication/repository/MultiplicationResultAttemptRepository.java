package microservices.book.multiplication.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;


/**
 * 
 * @author efrem
 * This interface allows us to store and retrieve attempts
 *
 */
public interface MultiplicationResultAttemptRepository 
	extends CrudRepository<MultiplicationResultAttempt, Long> {
	
	/**
	 * @return the last 5 attempts for a given user, identified by their alias.
	 */
	List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
	
}

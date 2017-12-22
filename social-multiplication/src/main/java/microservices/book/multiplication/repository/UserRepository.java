package microservices.book.multiplication.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import microservices.book.multiplication.domain.User;

/**
 * 
 * @author efrem
 * 
 * This interface allows us to save and retrieve Users
 */
public interface UserRepository extends CrudRepository<User, Long> {
	/**
	 * 
	 * @param alias User alias
	 * @return User identified by alias
	 */
	Optional<User> findByAlias(final String alias);
}

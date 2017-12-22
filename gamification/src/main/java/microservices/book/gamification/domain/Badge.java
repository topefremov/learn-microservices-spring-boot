package microservices.book.gamification.domain;


/**
 * 
 * @author efrem
 * Enumeration with the different types of badges that a user can win
 */
public enum Badge {
	
	// Badges depending on score
	BRONZE_MULTIPLICATOR,
	SILVER_MULTIPLICATOR,
	GOLD_MULTIPLICATOR,
	
	// Other badges won for different conditions
	FIRST_ATTEMPT,
	FIRST_WON
}

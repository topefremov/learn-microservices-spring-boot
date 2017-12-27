package microservices.book.gamification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
	
	private Map<Integer, Badge> badgesBasedOnScores;
	
	private ScoreCardRepository scoreCardRepository;
	private BadgeCardRepository badgeCardRepository;

	@Autowired
	GameServiceImpl(ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository) {
		this.scoreCardRepository = scoreCardRepository;
		this.badgeCardRepository = badgeCardRepository;
	}
	
	@PostConstruct
	private void init() {
		badgesBasedOnScores = new HashMap<>();
		badgesBasedOnScores.put(100, Badge.BRONZE_MULTIPLICATOR);
		badgesBasedOnScores.put(500, Badge.SILVER_MULTIPLICATOR);
		badgesBasedOnScores.put(999, Badge.GOLD_MULTIPLICATOR);
	}

	@Override
	public GameStats newAttemptForUser(Long userId, final Long attemptId, final boolean correct) {
		if (correct) {
			ScoreCard scoreCard = new ScoreCard(userId, attemptId);
			scoreCardRepository.save(scoreCard);
			log.info("User with id {} scored {} points for attempt id {}", userId, scoreCard.getScore(), attemptId);
			List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
			return new GameStats(userId, scoreCard.getScore(),
					badgeCards.stream()
					.map(BadgeCard::getBadge)
					.collect(Collectors.toList())); 
		}
		return GameStats.emptyStats(userId);
	}

	/**
    * Checks the total score and the different score cards obtained
    * to give new badges in case their conditions are met.
    */
	private List<BadgeCard> processForBadges(final Long userId, final Long attemptId) {
		List<BadgeCard> badgeCards = new ArrayList<>();
		
		int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
		log.info("New score for user {} is {}", userId, totalScore);
		List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
		List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
		
		// Badges depending on score
		for (Integer scoreThreshold: badgesBasedOnScores.keySet()) {
			checkAndGiveBadgeBasedOnScore(badgeCardList, badgesBasedOnScores.get(
					scoreThreshold), totalScore, scoreThreshold, userId)
			.ifPresent(badgeCards::add);	
		}
			
		if (scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
			BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
			badgeCards.add(firstWonBadge);
		}
		return badgeCards;
	}

    /**
     * Convenience method to check the current score against
     * the different thresholds to gain badges.
     * It also assigns badge to user if the conditions are met.
     */
	private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCards, 
			final Badge badge, final int score,
			final int scoreThreshold, final Long userId) {
		if(score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
			return Optional.of(giveBadgeToUser(badge, userId));
		}
		return Optional.empty();
	}

	/**
     * Assigns a new badge to the given user
     */
	private BadgeCard giveBadgeToUser(final Badge badge, final Long userId) {
		BadgeCard badgeCard = new BadgeCard(userId, badge);
		badgeCardRepository.save(badgeCard);
		log.info("User with id {} won a new badge: {}", userId, badge);
		return badgeCard;
	}

	/**
     * Checks if the passed list of badges includes the one being checked
     */
	private boolean containsBadge(final List<BadgeCard> badgeCards, final Badge badge) {
		return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
	}

	@Override
	@Transactional(readOnly = true)
	public GameStats retrieveStatsForUser(Long userId) {
		int score = scoreCardRepository.getTotalScoreForUser(userId);
		List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
		return new GameStats(userId, score, 
				badgeCards.stream().map(BadgeCard::getBadge)
				.collect(Collectors.toList()));
	}

}

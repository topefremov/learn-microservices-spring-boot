package microservices.book.gamification.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

	@Autowired
	private GameService gameService;

	@MockBean
	BadgeCardRepository badgeCardRepository;

	@MockBean
	ScoreCardRepository scoreCardRepository;

	@Test
	public void returnGameStatsWithFirstWonBadge() {
		// given
		Long userId = 1L;
		int totalScore = 10;

		List<BadgeCard> existingBadgeCards = Collections.emptyList();

		// then
		returnGameStatsWithBages(userId, totalScore, existingBadgeCards, Arrays.asList(Badge.FIRST_WON));
	}

	@Test
	public void returnGameStatsWithBronzeBage() {
		// given
		Long userId = 1L;
		int totalScore = 110;

		BadgeCard firstWonBadgeCard = new BadgeCard(userId, Badge.FIRST_WON);
		List<BadgeCard> existingBadgeCards = Arrays.asList(firstWonBadgeCard);

		// then
		returnGameStatsWithBages(userId, totalScore, existingBadgeCards, Arrays.asList(Badge.BRONZE_MULTIPLICATOR));
	}

	@Test
	public void returnGameStatsWithSilverBage() {
		// given
		Long userId = 1L;
		int totalScore = 510;

		BadgeCard firstWonBadgeCard = new BadgeCard(userId, Badge.FIRST_WON);
		BadgeCard bronzeBadgeCard = new BadgeCard(userId, Badge.BRONZE_MULTIPLICATOR);
		List<BadgeCard> existingBadgeCards = Arrays.asList(firstWonBadgeCard, bronzeBadgeCard);

		// then
		returnGameStatsWithBages(userId, totalScore, existingBadgeCards, Arrays.asList(Badge.SILVER_MULTIPLICATOR));
	}

	@Test
	public void returnGameStatsWithThreeWonBagesAtSameTime() {
		// given
		Long userId = 1L;
		int totalScore = 510;
		List<BadgeCard> existingBadgeCards = Collections.emptyList();
		
		//then
		returnGameStatsWithBages(userId, totalScore, existingBadgeCards, 
				Arrays.asList(Badge.BRONZE_MULTIPLICATOR, Badge.SILVER_MULTIPLICATOR, Badge.FIRST_WON));
	}
	
	@Test
	public void returnEmptyStats() {
		//given
		Long userId = 1L;
		Long attemptId = 25L;
		
		//when
		GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, false);
		
		//then
		assertThat(gameStats).isEqualTo(GameStats.emptyStats(userId));
	}
	
	@Test
	public void retriveStatsForUserTest() {
		//given
		Long userId = 25L;
		int exptectedScore = 250;
		BadgeCard firstWonBadgeCard = new BadgeCard(userId, Badge.FIRST_WON);
		BadgeCard bronzeBadgeCard = new BadgeCard(userId, Badge.BRONZE_MULTIPLICATOR);
		List<BadgeCard> expectedBadgeCards = Arrays.asList(firstWonBadgeCard, bronzeBadgeCard);	
		GameStats exptectedGameStats = new GameStats(userId, exptectedScore, 
				expectedBadgeCards.stream()
				.map(BadgeCard::getBadge)
				.collect(Collectors.toList()));
		given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(expectedBadgeCards);
		given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(exptectedScore);
		
		//when
		GameStats gameStats = gameService.retrieveStatsForUser(userId);
		
		//then
		assertThat(gameStats).isEqualTo(exptectedGameStats);
	}

	public void returnGameStatsWithBages(Long userId, int totalScore, List<BadgeCard> existingBadgeCards,
			List<Badge> badgesAfterProcessing) {
		// given
		Long attemptId = 45L;

		ScoreCard scoreCard = new ScoreCard(userId, attemptId);

		given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
		given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
				.willReturn(Collections.singletonList(scoreCard));
		given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(existingBadgeCards);

		// when
		GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, true);

		// then
		assertThat(gameStats.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
		assertThat(gameStats.getBadges()).isEqualTo(badgesAfterProcessing);
	}

}

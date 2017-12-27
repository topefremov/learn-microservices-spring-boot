package microservices.book.gamification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;

public class LeaderBoardServiceImplTest {
	
	private LeaderBoardServiceImpl leaderBoardServiceImpl;
	
	@Mock
	private ScoreCardRepository scoreCardRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.leaderBoardServiceImpl = new LeaderBoardServiceImpl(scoreCardRepository);
	}
	
	@Test
	public void getCurrentLeaderBoardTest() {
		//given
		
		LeaderBoardRow firstPlace = new LeaderBoardRow(1L, 2000L);
		LeaderBoardRow secondPlace = new LeaderBoardRow(1L, 1000L);
		LeaderBoardRow thirdPlace = new LeaderBoardRow(1L, 500L);
		LeaderBoardRow fourthPlace = new LeaderBoardRow(1L, 300L);
		List<LeaderBoardRow> expectedLeaderBoard = Arrays.asList(firstPlace, secondPlace, thirdPlace, fourthPlace);
		given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);
		
		//when
		List<LeaderBoardRow> currentLeaderBoard = leaderBoardServiceImpl.getCurrentLeaderBoard();
		
		//then
		assertThat(currentLeaderBoard).isEqualTo(expectedLeaderBoard);
	}

}

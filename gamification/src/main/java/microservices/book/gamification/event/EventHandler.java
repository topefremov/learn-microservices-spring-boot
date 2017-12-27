package microservices.book.gamification.event;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.service.GameService;

@Slf4j
@Component
public class EventHandler {
	
	private GameService gameService;
	
	@Autowired
	public EventHandler(final GameService gameService) {
		this.gameService = gameService;
	}
	
	@RabbitListener(queues = "${multiplication.queue}")
	void handleMultiplicationSolved(final MultiplicationSolvedEvent event) {
		log.info("Multiplication Solved Event recieved: {}", event.getMultiplicationResultAttemptId());
		try {
			gameService.newAttemptForUser(event.getUserId(), 
					event.getMultiplicationResultAttemptId(), 
					event.isCorrect());
		} catch(final Exception e) {
			log.error("Error when trying to process MultiplicationSolvedEvent", e);
			// Avoids the event to be re-queued and reprocessed 
			throw new AmqpRejectAndDontRequeueException(e);
		}
	}
}

package microservices.book.multiplication.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.MultiplicationSolvedEvent;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {
	
	private RandomGeneratorService randomGeneratorService;
	private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
	private UserRepository userRepository;
	private MultiplicationRepository multiplicationRepository;
	private EventDispatcher eventDispatcher;

	@Autowired
	public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService,
			MultiplicationResultAttemptRepository multiplicationResultAttemptRepository,
			UserRepository userRepository,
			MultiplicationRepository multiplicationRepository,
			EventDispatcher eventDispatcher) {
		this.randomGeneratorService = randomGeneratorService;
		this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
		this.userRepository = userRepository;
		this.multiplicationRepository = multiplicationRepository;
		this.eventDispatcher = eventDispatcher;
	}

	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		return new Multiplication(factorA, factorB);
	}

	@Override
	@Transactional
	public boolean checkAttempt(final MultiplicationResultAttempt attempt) {
		Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());
		Optional<Multiplication> multiplication = multiplicationRepository.findByFactorAAndFactorB(
				attempt.getMultiplication().getFactorA(), attempt.getMultiplication().getFactorB());
				
		
		// Avoids 'hack' attempts
		Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!");
		
		boolean isCorrect = attempt.getResultAttempt() == 
				(attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB());
		
		MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(user.orElse(attempt.getUser()),
				multiplication.orElse(attempt.getMultiplication()), attempt.getResultAttempt(), isCorrect);
		
		// Stores the attempt
		multiplicationResultAttemptRepository.save(checkedAttempt);
		
		// Communicates the result via Event
		eventDispatcher.send(
				new MultiplicationSolvedEvent(checkedAttempt.getId(),
						checkedAttempt.getUser().getId(),
						checkedAttempt.isCorrect())
				);
		
		return isCorrect;
	}

	@Override
	public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
		return multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}

}

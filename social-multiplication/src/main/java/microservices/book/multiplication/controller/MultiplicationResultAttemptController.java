package microservices.book.multiplication.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;

@Slf4j
@RestController
@RequestMapping("/results")
public final class MultiplicationResultAttemptController {
	private final MultiplicationService multiplicationService;

	private final int serverPort;
	
	@Autowired
	public MultiplicationResultAttemptController(MultiplicationService multiplicationService,
			@Value("${server.port}") int serverPort) {
		this.multiplicationService = multiplicationService;
		this.serverPort = serverPort;
	}
	
	@GetMapping
	ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(
			@RequestParam("alias") String alias) {
		return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
	}
	
	@GetMapping("/{resultId}")
	ResponseEntity<MultiplicationResultAttempt> getResultById(@PathVariable("resultId") final Long resultId) {
		log.info("Retriveing result {} from server @ {}", resultId, serverPort);
		return ResponseEntity.ok(multiplicationService.getResultById(resultId));
	}
	
	@PostMapping
	ResponseEntity<MultiplicationResultAttempt> 
	postResult(@Valid @RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
		boolean isCorrect = multiplicationService.checkAttempt(multiplicationResultAttempt);
		MultiplicationResultAttempt attemptCopy = new MultiplicationResultAttempt(
				multiplicationResultAttempt.getUser(), 
				multiplicationResultAttempt.getMultiplication(), 
				multiplicationResultAttempt.getResultAttempt(), 
				isCorrect);
				
		return ResponseEntity.ok(attemptCopy);
	}
}

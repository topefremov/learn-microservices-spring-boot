package microservices.book.multiplication.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;

@RestController
@RequestMapping("/results")
public final class MultiplicationResultAttemptController {
	private final MultiplicationService multiplicationService;

	@Autowired
	public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
		this.multiplicationService = multiplicationService;
	}
	
	@GetMapping
	ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(
			@RequestParam("alias") String alias) {
		return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
	}
	
	@GetMapping("/{resultId}")
	ResponseEntity<MultiplicationResultAttempt> getResultById(@PathVariable("resultId") final Long resultId) {
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

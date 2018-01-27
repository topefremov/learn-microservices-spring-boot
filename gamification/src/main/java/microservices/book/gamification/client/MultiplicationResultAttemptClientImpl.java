package microservices.book.gamification.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import microservices.book.gamification.client.dto.MultiplicationResultAttempt;

@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {
	
	private final RestTemplate restTemplate;
	private final String multiplicationHost;
	
	@Autowired
	public MultiplicationResultAttemptClientImpl(final RestTemplate restTemplate, 
			@Value("${multiplicationHost}") final String multiplicationHost) {
		this.restTemplate = restTemplate;
		this.multiplicationHost = multiplicationHost;
	}

	@HystrixCommand(fallbackMethod = "defaultResult")
	@Override
	public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(Long multiplicationResultAttemptId) {
		return restTemplate.getForObject(
				multiplicationHost + "/results/" + multiplicationResultAttemptId, 
				MultiplicationResultAttempt.class);
	}
	
	private MultiplicationResultAttempt defaultResult(final Long multiplicationResultAttemptId) {
        return new MultiplicationResultAttempt("fakeAlias",
                10, 10, 100, true);
    }
}

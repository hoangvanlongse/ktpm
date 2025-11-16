package service.study.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {

	@GetMapping("/api/study/health")
	public String check() {
		return "Service is up and running!";
	}
}

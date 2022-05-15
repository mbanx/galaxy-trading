package org.mbanx.challenge.galaxy.controller;

import org.mbanx.challenge.galaxy.service.TradingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trading")
public class TradingController {
	
	@PostMapping(value = "/queries")
	@ResponseBody
	public String processTrading(@RequestBody String body) {
		TradingService service = new TradingService();
		String output = service.processTrading(body);
		return output;
	}
}

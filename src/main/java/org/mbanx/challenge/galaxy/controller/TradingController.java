package org.mbanx.challenge.galaxy.controller;

import org.mbanx.challenge.galaxy.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trading")
public class TradingController {
	
	@Autowired
	private TradingService service;
	
	@PostMapping(value = "/queries")
	@ResponseBody
	public String processTrading(@RequestBody String body) {
		String output = service.processTrading(body);
		return output;
	}
}

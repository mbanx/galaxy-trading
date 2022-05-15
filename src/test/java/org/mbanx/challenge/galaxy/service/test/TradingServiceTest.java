package org.mbanx.challenge.galaxy.service.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingServiceTest {
	
	@Autowired(required = false)
	private TradingService tradingService;

//	@Ignore
	@Test
	public void test() {
		String text = "glob is I";
		QueryOutput qo = tradingService.firstTypeProcessor(text);
//		assertEquals(t, actual);
		assertTrue(qo.isValid());
	}
}

package org.mbanx.challenge.galaxy.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.service.TradingService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingServiceTest {

	private TradingService service;

	@Before
	public void init() {
		service = new TradingService();
	}

	//	@Ignore
	@Test
	public void test() {
		String text = "glob is I";
		QueryOutput qo = service.firstTypeProcessor(text);
		assertTrue(qo.isValid());
	}

	@Test
	public void test2() {
		QueryOutput qo1 = service.firstTypeProcessor("glob is I");
		QueryOutput qo2 = service.firstTypeProcessor("prok is V");
		QueryOutput qo3 = service.firstTypeProcessor("pish is X");
		QueryOutput qo4 = service.firstTypeProcessor("tegj is L");
		QueryOutput qo5 = service.secondTypeProcessor("glob glob Silver is 34 Credits");
		QueryOutput qo6 = service.secondTypeProcessor("glob prok Gold is 57800 Credits");
		QueryOutput qo7 = service.secondTypeProcessor("pish pish Iron is 3910 Credits");
		QueryOutput qo8 = service.thirdTypeProcessor("how much is pish tegj glob glob ?");
		QueryOutput qo9 = service.forthTypeProcessor("how many Credits is prok glob Silver ?");
		QueryOutput qo10 = service.forthTypeProcessor("how many Credits is glob prok Silver ?");
		QueryOutput qo11 = service.forthTypeProcessor("how many Credits is glob prok Gold ?");
		QueryOutput qo12 = service.forthTypeProcessor("how many Credits is glob prok Iron ?");
		QueryOutput qo13 = service.forthTypeProcessor("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");

		assertEquals("pish tegj glob glob is 42", qo8.getOutput());
		assertEquals("prok glob Silver is 102", qo9.getOutput());
		assertEquals("glob prok Silver is 68", qo10.getOutput());
		assertEquals("glob prok Gold is 57800", qo11.getOutput());
		assertEquals("glob prok Iron is 782", qo12.getOutput());
		assertEquals("I have no idea what you are talking about", qo13.getOutput());
	}
}

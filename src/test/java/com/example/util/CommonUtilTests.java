package com.example.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommonUtilTests {

	// user-agent 테스트 : DESKTOP, MOBILE, TABLET 을 구분
	@Test
	void getClientDeviceTest1() {
		// 아이폰일 때 (와이파이 or LTE)
		String mChrome = "Mozilla/5.0 (iPhone; CPU iPhone OS 18_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/133.0.6943.33 Mobile/15E148 Safari/604.1";
		String mFirefox = "Mozilla/5.0 (iPhone; CPU iPhone OS 18_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) FxiOS/135.0  Mobile/15E148 Safari/605.1.15";
		String mSafari = "Mozilla/5.0 (iPhone; CPU iPhone OS 18_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.1.1 Mobile/15E148 Safari/604.1";
		String mkakaotalk = "Mozilla/5.0 (iPhone; CPU iPhone OS 18_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 Safari/604.1 KAKAOTALK/11.4.1 (INAPP)";

		assertEquals("MOBILE", CommonUtil.getClientDevice(mChrome));
		assertEquals("MOBILE", CommonUtil.getClientDevice(mFirefox));
		assertEquals("MOBILE", CommonUtil.getClientDevice(mSafari));
		assertEquals("MOBILE", CommonUtil.getClientDevice(mkakaotalk));

		// 아이패드 미니일 때 (와이파이 or 테더링)
		String tChrome ="Mozilla/5.0 (iPad; CPU OS 18_3_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/133.0.6943.84 Mobile/15E148 Safari/604.1";
		String tFirefox ="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Safari/605.1.15";
		String tSafari ="Mozilla/5.0 (iPad; CPU OS 18_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.3 Mobile/15E148 Safari/604.1";
		String tEdge = "Mozilla/5.0 (iPad; CPU OS 18_3_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) EdgiOS/132.0.2957.122 Version/18.0 Mobile/15E148 Safari/604.1";
		String tkakaotalk = "Mozilla/5.0 (iPad; CPU OS 18_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 Safari/604.1 KAKAOTALK/11.4.0 (INAPP)";

		assertEquals("TABLET", CommonUtil.getClientDevice(tChrome));
		assertEquals("TABLET", CommonUtil.getClientDevice(tFirefox));
		assertEquals("TABLET", CommonUtil.getClientDevice(tSafari));
		assertEquals("TABLET", CommonUtil.getClientDevice(tEdge));
		assertEquals("TABLET", CommonUtil.getClientDevice(tkakaotalk));

		// acer 노트북일 때 (와이파이)
		String dChrome = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36";
		String dFirefox = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:135.0) Gecko/20100101 Firefox/135.0";
		String dEdge = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36 Edg/133.0.0.0";

		assertEquals("DESKTOP", CommonUtil.getClientDevice(dChrome));
		assertEquals("DESKTOP", CommonUtil.getClientDevice(dFirefox));
		assertEquals("DESKTOP", CommonUtil.getClientDevice(dEdge));
	}

}

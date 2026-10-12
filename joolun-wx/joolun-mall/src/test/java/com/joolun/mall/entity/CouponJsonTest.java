package com.joolun.mall.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CouponJsonTest {

	private final ObjectMapper objectMapper = JsonMapper.builder()
			.addModule(new JavaTimeModule())
			.build();

	@Test
	void shouldReadAdminDateTimeFormat() throws Exception {
		Coupon coupon = objectMapper.readValue("""
				{"startTime":"2026-09-21 00:00:00","endTime":"2026-09-30 23:59:59"}
				""", Coupon.class);

		assertEquals(LocalDateTime.of(2026, 9, 21, 0, 0), coupon.getStartTime());
		assertEquals(LocalDateTime.of(2026, 9, 30, 23, 59, 59), coupon.getEndTime());
	}

	@Test
	void shouldWriteAdminDateTimeFormat() throws Exception {
		Coupon coupon = new Coupon();
		coupon.setStartTime(LocalDateTime.of(2026, 9, 21, 0, 0));

		String json = objectMapper.writeValueAsString(coupon);

		org.junit.jupiter.api.Assertions.assertTrue(json.contains("\"startTime\":\"2026-09-21 00:00:00\""));
	}
}

package com.profiteer.core.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.profiteer.core.api.service.TrackingService;
import com.profiteer.core.api.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Component
@Slf4j
public class CORSHeaderInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private TrackingService trackingService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (request.getMethod().equalsIgnoreCase("options")) {

			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Headers", "*");
			response.setHeader("Access-Control-Expose-Headers", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
			log.info("Options Call....");
			return true;
		}

		log.info("URI ::: {} ", request.getRequestURI());
		log.info("Header Authorization::: {} ", request.getHeader("Authorization"));

		if (request.getHeader("Authorization") == null || (request.getHeader("Authorization") != null
				&& jwtUtil.extractUsername(request.getHeader("Authorization")) == null)) {
			log.info("Invalid Token");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			trackingService.saveUserSession(request, null, true);
			return false;
		}

		if (jwtUtil.isTokenExpired(request.getHeader("Authorization"))) {
			log.info("Token time Token");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		return true;
	}
}

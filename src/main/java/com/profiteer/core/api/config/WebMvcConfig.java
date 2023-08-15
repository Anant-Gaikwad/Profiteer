package com.profiteer.core.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.profiteer.core.api.interceptor.CORSHeaderInterceptor;

@SuppressWarnings("deprecation")
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private CORSHeaderInterceptor corsHeaderInterceptor;

	@Autowired
	public WebMvcConfig(CORSHeaderInterceptor corsHeaderInterceptor) {
		this.corsHeaderInterceptor = corsHeaderInterceptor;
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(corsHeaderInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/user/register",
						"/user/checkValidReferralCode",
						"/user/login",
						"/user/validateOtp",
						"/user/sendOtp",
						"/swagger-ui/**", 
						"/swagger-resources/**",
						"/v2/api-docs", 
						"/downloadTransactions");
	}
}
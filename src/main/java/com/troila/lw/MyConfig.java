package com.troila.lw;

import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.IRule;

/**
 * 這個類的作用，就是告訴spring容器，有一個叫做MyRule的規則類存在
 * @author liwei
 *
 */
public class MyConfig {

	@Bean
	public IRule getRule() {
		return new MyRule();
	}
}

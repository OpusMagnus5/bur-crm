package pl.bodzioch.damian.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@EnableAsync
@Configuration
class AsyncConfiguration {

	@Bean
	@Qualifier("asyncExecutor")
	public Executor asyncExecutor() {
		ThreadFactory factory = Thread.ofVirtual().name("async-executor-", 0L).factory();
		return Executors.newThreadPerTaskExecutor(factory);
	}
}

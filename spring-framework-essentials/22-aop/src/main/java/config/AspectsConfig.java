package config;

import org.springframework.context.annotation.*;

import rewards.internal.monitor.MonitorFactory;
import rewards.internal.monitor.jamon.JamonMonitorFactory;

@Configuration
@ComponentScan("rewards.internal.aspects")
@EnableAspectJAutoProxy
public class AspectsConfig {

	@Bean
	public MonitorFactory monitorFactory() {
		return new JamonMonitorFactory();
	}
}

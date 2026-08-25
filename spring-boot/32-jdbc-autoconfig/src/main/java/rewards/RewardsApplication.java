package rewards;

import config.RewardsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@Import(RewardsConfig.class)
@EnableConfigurationProperties(RewardsRecipientProperties.class)
public class RewardsApplication {
    static final String SQL = "SELECT count(*) FROM T_ACCOUNT";

    final Logger logger
            = LoggerFactory.getLogger(RewardsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }

    @Bean
    CommandLineRunner displayNumberOfAccounts(JdbcTemplate jdbcTemplate) {
        final String query = "SELECT COUNT(*) FROM T_ACCOUNT";
        final int numberOfAccounts = jdbcTemplate.queryForObject(query, Integer.class);
        return args -> {
            logger.info("Number of accounts: {}", numberOfAccounts);
        };
    }

    @Bean
    CommandLineRunner displayNameOfRewardsRecipient(RewardsRecipientProperties rewardsRecipientProperties) {
        return args -> {
            logger.info("Rewards recipient: {}", rewardsRecipientProperties.getName());
        };
    }
}

package rewards.internal.reward;

import common.datetime.SimpleDate;
import org.springframework.jdbc.core.JdbcTemplate;
import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;

import javax.sql.DataSource;
import java.sql.*;

/**
 * JDBC implementation of a reward repository that records the result
 * of a reward transaction by inserting a reward confirmation record.
 */

// TODO-08 (Optional) : Inject JdbcTemplate directly to this repository class
// - Refactor the constructor to get the JdbcTemplate injected directly
//   (instead of DataSource getting injected)
// - Refactor RewardsConfig accordingly
// - Refactor JdbcRewardRepositoryTests accordingly
// - Run JdbcRewardRepositoryTests and verity it passes

public class JdbcRewardRepository implements RewardRepository {

	private JdbcTemplate jdbcTemplate;

	public JdbcRewardRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public RewardConfirmation confirmReward(AccountContribution contribution, Dining dining) {
		String sql = "insert into T_REWARD (CONFIRMATION_NUMBER, REWARD_AMOUNT, REWARD_DATE, ACCOUNT_NUMBER, DINING_MERCHANT_NUMBER, DINING_DATE, DINING_AMOUNT) values (?, ?, ?, ?, ?, ?, ?)";
		String confirmationNumber = nextConfirmationNumber();

		jdbcTemplate.update(sql, confirmationNumber, contribution.getAmount().asBigDecimal(),
				new Date(SimpleDate.today().inMilliseconds()), contribution.getAccountNumber(),
				dining.getMerchantNumber(), new Date(dining.getDate().inMilliseconds()), dining.getAmount().asBigDecimal());

		return new RewardConfirmation(confirmationNumber, contribution);
	}

	private String nextConfirmationNumber() {
		String sql = "select next value for S_REWARD_CONFIRMATION_NUMBER from DUAL_REWARD_CONFIRMATION_NUMBER";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
}

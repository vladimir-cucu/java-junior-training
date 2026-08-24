package rewards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcBootApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testNumberOfAccounts() {
		final String query = "SELECT COUNT(*) FROM T_ACCOUNT";
		int numberOfAccounts = jdbcTemplate.queryForObject(query, Integer.class);
		assertThat(numberOfAccounts).isEqualTo(21);
	}
}

package accounts.web;

import accounts.AccountManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.money.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rewards.internal.account.Account;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerBootTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountManager accountManager;

	@Test
	public void accountDetails() throws Exception {
		given(accountManager.getAccount(0L))
				.willReturn(new Account("1234567890", "John Doe"));

		mockMvc.perform(get("/accounts/0"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name").value("John Doe"))
				.andExpect(jsonPath("number").value("1234567890"));

		verify(accountManager).getAccount(0L);
	}

	@Test
	public void accountDetailsFail() throws Exception {
		given(accountManager.getAccount(any(Long.class)))
				.willThrow(new IllegalArgumentException("No such account with id " + 0L));

		mockMvc.perform(get("/accounts/9999"))
				.andExpect(status().isNotFound());

		verify(accountManager).getAccount(any(Long.class));
	}

	@Test
	public void createAccount() throws Exception {
		Account testAccount = new Account("1234512345", "Mary Jones");
		testAccount.setEntityId(21L);

		given(accountManager.save(any(Account.class)))
				.willReturn(testAccount);

		mockMvc.perform(post("/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(testAccount)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/accounts/21"));

		verify(accountManager).save(any(Account.class));
	}

	@Test
	public void shouldReturnAccounts() throws Exception {
		given(accountManager.getAllAccounts())
				.willReturn(List.of(new Account("1234567890", "John Doe"), new Account("1234512345", "Mary Jones")));

		mockMvc.perform(get("/accounts"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name").value("John Doe"))
				.andExpect(jsonPath("$[0].number").value("1234567890"))
				.andExpect(jsonPath("$[1].name").value("Mary Jones"))
				.andExpect(jsonPath("$[1].number").value("1234512345"));

		verify(accountManager).getAllAccounts();
	}

	@Test
	public void shouldReturnValidBeneficiaryForAnAccount() throws Exception {
		Account testAccount = new Account("1234512345", "Mary Jones");
		testAccount.addBeneficiary("John Doe", new Percentage(0.5));

		given(accountManager.getAccount(0L))
				.willReturn(testAccount);

		mockMvc.perform(get("/accounts/0"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.beneficiaries", hasSize(1)))
				.andExpect(jsonPath("$.beneficiaries[0].name").value("John Doe"))
				.andExpect(jsonPath("$.beneficiaries[0].allocationPercentage").value(0.50));

		verify(accountManager).getAccount(0L);
	}

	// Utility class for converting an object into JSON string
	protected static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

package accounts;

import config.RestSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestSecurityConfig.class)
@EntityScan("rewards.internal")
public class RestWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestWsApplication.class, args);
    }

}

// TODO-19 (Optional): Verify that the newly added custom AuthenticationProvider works
// - Re-run this application
// - Using Chrome Incognito browser, access
//   http://localhost:8080/accounts/0
// - Enter "spring"/"spring" and verify accounts data
// - If you want to use "curl", use
//   curl -i -u spring:spring http://localhost:8080/accounts/0
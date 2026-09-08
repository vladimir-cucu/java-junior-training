package accounts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rewards.internal.restaurant.RestaurantRepository;

@Component
public class RestaurantHealthCheck implements HealthIndicator {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantHealthCheck(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Health health() {
        final long restaurantCount = restaurantRepository.getRestaurantCount();
        if (restaurantRepository.getRestaurantCount() > 0) {
            return Health.up().build();
        } else {
            return Health.status("NO_RESTAURANTS").withDetail("restaurantCount", 0).build();
        }
    }
}

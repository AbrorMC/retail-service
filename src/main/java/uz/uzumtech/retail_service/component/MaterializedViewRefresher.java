package uz.uzumtech.retail_service.component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterializedViewRefresher {

    @PersistenceContext
    EntityManager entityManager;

    @Scheduled(fixedRateString = "${app.scheduling.inventory-report-rate}")
    @Transactional
    public void refreshInventoryBalancesMaterializedView() {
        entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW CONCURRENTLY mv_inventory_balances").executeUpdate();
        entityManager.clear();
    }
}

package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findAllByUserId(String userId);

    int countItemsByBarcode(@Param("barcode") String barcode);

    Optional<Item> findItemByIdAndUserId(@Param("id") UUID id, @Param("userId") String userId);

    Optional<Item> findItemByBarcode(@Param("barcode") String barcode);

    @Query("SELECT DISTINCT t.item FROM Transaction t WHERE t.item.userId = :userId AND t.expirationDate < :date AND t.availableQuantity > 0")
    List<Item> findItemsInExpiration(@Param("date") LocalDate date, @Param("userId") String userId);

    @Query("SELECT DISTINCT t.item FROM Transaction t WHERE t.item.userId = :userid and t.availableQuantity < (t.quantity * 0.3) and t.availableQuantity > 0")
    List<Item> findItemsAlmostFinished(String userid, Pageable pageable);

}
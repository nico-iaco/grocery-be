package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    int countItemsByBarcode(@Param("barcode") String barcode);

    Optional<Item> findItemById(@Param("id") UUID id);

    Optional<Item> findItemByBarcode(@Param("barcode") String barcode);

    @Query("SELECT DISTINCT t.item FROM Transaction t WHERE t.expirationDate < :date and t.availableQuantity > 0")
    List<Item> findItemsInExpiration(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT t.item FROM Transaction t WHERE t.availableQuantity < (t.quantity * 0.3) and t.availableQuantity > 0")
    List<Item> findItemsAlmostFinished();

}
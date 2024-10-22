package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.Pantry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findAllByPantry(Pantry pantry);

    @Query("SELECT COUNT(i) FROM Item i WHERE i.barcode = :barcode AND i.pantry = :pantry")
    int countItemsByBarcodeAndPantry(@Param("barcode") String barcode, @Param("pantry") Pantry pantry);

    @Query("SELECT i FROM Item i WHERE i.id = :id AND i.pantry = :pantry")
    Optional<Item> findItemByIdAndPantry(@Param("id") UUID id, @Param("pantry") Pantry pantry);

    Optional<Item> findItemByBarcode(@Param("barcode") String barcode);

    @Query("SELECT DISTINCT t.item FROM Transaction t WHERE t.item.pantry = :pantry AND t.expirationDate < :date AND t.availableQuantity > 0")
    List<Item> findItemsInExpiration(@Param("date") LocalDate date, @Param("pantry") Pantry pantry);

    @Query("SELECT DISTINCT t.item FROM Transaction t WHERE t.item.pantry = :pantry and t.availableQuantity < (t.quantity * 0.3) and t.availableQuantity > 0")
    List<Item> findItemsAlmostFinished(Pantry pantry, Pageable pageable);

}
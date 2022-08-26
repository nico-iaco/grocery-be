package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    int countItemsByBarcode(@Param("barcode") String barcode);

    Optional<Item> findItemById(@Param("id") UUID id);

    Optional<Item> findItemByBarcode(@Param("barcode") String barcode);

}
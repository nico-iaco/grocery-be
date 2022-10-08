package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT SUM(t.quantity) from Transaction t where t.item = :item")
    Optional<Double> sumItemQuantityByItem(Item item);

    @Query("SELECT SUM(t.availableQuantity) from Transaction t where t.item = :item")
    Optional<Double> sumItemAvailableQuantityByItem(Item item);

    @Query("SELECT DISTINCT t.unit FROM Transaction t where t.item = :item")
    List<String> getUnitOfTransaction(Item item);

    List<Transaction> findTransactionsByItemOrderByExpirationDateAsc(Item item);

    Optional<Transaction> findTransactionByIdAndItem(UUID id, Item item);

    List<Transaction> findTransactionsByItemAndQuantityGreaterThan(Item item, double quantity);

}
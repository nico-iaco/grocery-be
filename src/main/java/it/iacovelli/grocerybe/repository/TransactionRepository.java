package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT SUM(t.quantity) from Transaction t where t.item = :item")
    double sumItemQuantityByItem(Item item);

    List<Transaction> findTransactionsByItem(Item item);

    Optional<Transaction> findTransactionByIdAndItem(UUID id, Item item);

    List<Transaction> findTransactionsByItemAndQuantityGreaterThan(Item item, double quantity);

}
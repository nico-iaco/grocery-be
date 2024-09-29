package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.FoodDetail;
import it.iacovelli.grocerybe.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FoodDetailRepository extends JpaRepository<FoodDetail, UUID> {

    Optional<FoodDetail> findFoodDetailByItem(Item item);

}

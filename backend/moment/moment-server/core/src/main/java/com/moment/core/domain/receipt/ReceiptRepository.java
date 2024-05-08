package com.moment.core.domain.receipt;

import com.moment.core.domain.cardView.CardView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface ReceiptRepository extends PagingAndSortingRepository<Receipt, Long>, JpaRepository<Receipt, Long>{

    List<Receipt> findAllByTrip_User_Id(Long userId);
    Page<Receipt> findAllByTrip_User_IdOrderByStDate(Long userId, Pageable pageable);
//    Optional<Receipt> findById(Long id);

//    void deleteAll(List<Receipt> receipts);

//    void save(Receipt receipt);
    Long countByTrip_User_Id(Long userId);
}

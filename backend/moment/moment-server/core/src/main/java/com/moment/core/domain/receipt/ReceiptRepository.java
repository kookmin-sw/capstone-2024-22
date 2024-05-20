package com.moment.core.domain.receipt;

import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReceiptRepository extends PagingAndSortingRepository<Receipt, Long>, JpaRepository<Receipt, Long>{

//    List<Receipt> findAllByTrip_User_Id(Long userId);
    Page<Receipt> findAllByUser_IdOrderByStDate(Long userId, Pageable pageable);
//    Optional<Receipt> findById(Long id);

//    void deleteAll(List<Receipt> receipts);

//    void save(Receipt receipt);
//    Long countByTrip_User_Id(Long userId);

//    List<Receipt> findAllByTrip(Trip trip);

    List<Receipt> findAllByUser_Id(Long userId);

    Number countByUser_Id(Long userId);

    List<Receipt> findAllByTrip(Trip trip);

    void deleteAllByUser(User user);
}

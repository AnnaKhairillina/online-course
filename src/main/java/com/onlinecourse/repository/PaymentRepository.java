package com.onlinecourse.repository;

import com.onlinecourse.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    interface RevenueProjection {
        Integer getMonthNum();
        String getSubject();
        BigDecimal getTotal();
    }

    @Query(value = """
            SELECT MONTH(p.payment_date) AS monthNum,
                   c.subject AS subject,
                   SUM(p.amount) AS total
            FROM payments p
            JOIN courses c ON c.id = p.course_id
            WHERE p.status = 'SUCCESS' AND YEAR(p.payment_date) = :year
            GROUP BY MONTH(p.payment_date), c.subject
            ORDER BY MONTH(p.payment_date), c.subject
            """, nativeQuery = true)
    List<RevenueProjection> aggregateRevenueByYear(@Param("year") int year);
}

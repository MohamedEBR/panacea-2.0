package com.example.panacea.repo;

import com.example.panacea.models.BillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillingRecordRepository extends JpaRepository<BillingRecord, Long> {
	@Query("SELECT b FROM BillingRecord b WHERE b.member.id = :memberId ORDER BY b.date DESC")
	List<BillingRecord> findByMemberIdOrderByDateDesc(@Param("memberId") Long memberId);
}

package com.example.panacea.repo;

import com.example.panacea.models.BillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRecordRepository extends JpaRepository<BillingRecord, Long> {
}

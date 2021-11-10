package com.liviavilaca.investimentsmanager.repository.acquisition;

import com.liviavilaca.investimentsmanager.model.acquisition.Acquisition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcquisitionRepository extends JpaRepository<Acquisition, Long> {
    Page<Acquisition> findByClientId(Long clientId, Pageable pageable);
}

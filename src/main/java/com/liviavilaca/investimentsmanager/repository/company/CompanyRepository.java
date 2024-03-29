package com.liviavilaca.investimentsmanager.repository.company;

import com.liviavilaca.investimentsmanager.model.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findByStatus(Boolean status, Pageable pageable);
    List<Company> findByStatus(Boolean status, Sort sort);
}

package com.liviavilaca.investimentsmanager.repository.action;

import com.liviavilaca.investimentsmanager.model.action.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {
}

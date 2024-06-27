package com.PagePerformanceTracker.backend.repositories;

import com.PagePerformanceTracker.backend.models.Website;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository  extends JpaRepository<Website, Long> {
}

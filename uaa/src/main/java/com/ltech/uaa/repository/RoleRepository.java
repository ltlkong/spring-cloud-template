package com.ltech.uaa.repository;

import com.ltech.uaa.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, Long> {

    Set<AppRole> findByNameIn(Set<String> names);
}

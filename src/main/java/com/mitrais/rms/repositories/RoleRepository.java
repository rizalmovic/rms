package com.mitrais.rms.repositories;

import com.mitrais.rms.domains.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    public Optional<Role> findByCanonical(String name);

}
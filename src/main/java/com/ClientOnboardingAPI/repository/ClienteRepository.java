package com.ClientOnboardingAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ClientOnboardingAPI.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
}


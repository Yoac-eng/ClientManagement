package com.yoaceng.clientmanagement.repositories;

import com.yoaceng.clientmanagement.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

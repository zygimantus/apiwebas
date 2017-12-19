package com.zygimantus.apiwebas.vaadin.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zygimantus.apiwebas.vaadin.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

        User findByUserNameAndPassword(String userName, String password);

}

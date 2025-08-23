package com.co.btg.api.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.co.btg.api.models.User;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {
    // Puedes agregar queries personalizadas aquí si lo necesitas
}

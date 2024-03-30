package com.irojas.demojwt.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username); 
    
    @Modifying
    @Query("update User u set u.firstname=:firstname, u.lastname=:lastname, u.country=:country, u.role=:role where u.id = :id")
    void updateUser(@Param("id") Integer id, @Param("firstname") String firstname, @Param("lastname") String lastname, @Param("country") String country, @Param("role") Role role);
}

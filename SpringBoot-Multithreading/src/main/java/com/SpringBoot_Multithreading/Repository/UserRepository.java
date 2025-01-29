package com.SpringBoot_Multithreading.Repository;

import com.SpringBoot_Multithreading.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}

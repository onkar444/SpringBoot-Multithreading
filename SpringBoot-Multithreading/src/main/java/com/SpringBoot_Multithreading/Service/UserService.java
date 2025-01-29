package com.SpringBoot_Multithreading.Service;

import com.SpringBoot_Multithreading.Entity.User;
import com.SpringBoot_Multithreading.Repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    public void saveUser(MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();
        List<User> users = parseCSVFile(file);
        LOGGER.info("Saving list of users");

        users = userRepository.saveAll(users);

        long endTime = System.currentTimeMillis();
            LOGGER.info("Total time::{}",(endTime-startTime));
        CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        List<User> users = userRepository.findAll();
        LOGGER.info("Get List of Users by {}",Thread.currentThread().getName());
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCSVFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    User user = new User();
                    user.setName(data[0]);
                    user.setEmailId(data[1]);
                    user.setGender(data[2]);
                    users.add(user);
                }
            }
            return users;
        } catch (final IOException e) {
            LOGGER.error("Failed to parse CSV");
            throw new RuntimeException(e);
        }
    }
}

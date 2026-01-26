package com.brainluim.repository;

import com.brainluim.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    // Find all users (for login screen)
    List<UserProfile> findAll();
}
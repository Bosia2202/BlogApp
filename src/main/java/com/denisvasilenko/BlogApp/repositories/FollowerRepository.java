package com.denisvasilenko.BlogApp.repositories;

import com.denisvasilenko.BlogApp.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends JpaRepository<Follower,Long> {
}

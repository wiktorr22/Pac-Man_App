package com.example.server.GameResult;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<GameResult,Long> {

    @Query("SELECT MAX(g.result) FROM GameResult g")
    Optional<Integer> getTheBestResult();




}

package com.fwd.assesment.test.alvin.repository;

import com.fwd.assesment.test.alvin.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {
}

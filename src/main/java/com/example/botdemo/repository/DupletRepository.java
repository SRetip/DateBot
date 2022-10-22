package com.example.botdemo.repository;

import com.example.botdemo.domain.Duplet;
import com.example.botdemo.domain.DupletStatus;
import com.example.botdemo.domain.Usser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DupletRepository extends JpaRepository<Duplet, Long> {
    @Query("select d from Duplet d join fetch d.firstUsser where d.status = ?1 and d.secondUsser = ?2 ")
    Optional<Duplet> findFirstByStatusAndFirstUserReactAndSecondUsser(DupletStatus status, Usser user);

    Optional<Duplet> findFirstBySecondUsserAndStatus(Usser usser, DupletStatus status);
}

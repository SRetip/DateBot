package com.example.botdemo.repository;

import com.example.botdemo.domain.Usser;
import com.example.botdemo.repository.custom.CustomUsserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsserRepository extends JpaRepository<Usser, Long>, CustomUsserRepository {


}

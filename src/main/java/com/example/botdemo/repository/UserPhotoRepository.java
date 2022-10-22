package com.example.botdemo.repository;

import com.example.botdemo.domain.UserPhoto;
import com.example.botdemo.domain.Usser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    void deleteAllByUser(Usser usser);
}

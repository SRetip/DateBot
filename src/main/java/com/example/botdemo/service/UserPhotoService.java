package com.example.botdemo.service;

import com.example.botdemo.domain.UserPhoto;
import com.example.botdemo.domain.Usser;
import com.example.botdemo.repository.UserPhotoRepository;
import com.example.botdemo.repository.UsserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UsserRepository usserRepository;

    public void updatePhotos(List<String> photos, Long userId) {
        Usser usser = usserRepository.getReferenceById(userId);
        userPhotoRepository.deleteAllByUser(usser);
        photos.forEach(p -> {
            UserPhoto userPhoto = new UserPhoto();
            userPhoto.setPhotoId(p);
            userPhoto.setUser(usser);
            userPhotoRepository.save(userPhoto);
        });
    }
}

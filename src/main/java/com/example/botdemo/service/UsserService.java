package com.example.botdemo.service;

import com.example.botdemo.domain.Usser;
import com.example.botdemo.repository.UsserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsserService {
    @Autowired
    private UsserRepository usserRepository;

    public Usser getSelf(Long id) throws Exception {
        return usserRepository.findById(id).orElseThrow(Exception::new);//TODO exception
    }

    public void addUsser(Usser usser) {
        usserRepository.save(usser);
    }
}

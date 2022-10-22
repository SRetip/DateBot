package com.example.botdemo.repository.custom;

import com.example.botdemo.domain.Usser;

import java.util.List;
import java.util.Optional;

public interface CustomUsserRepository {
    Optional<Usser> getPairForUsser(int lowAgeLimit, int highAgeLimit, String city, String interestedGender, int selfAge, String selfGender, Long selfId);

    List<Usser> getMatchedUsersForUser(Long selfId);
}

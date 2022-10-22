package com.example.botdemo.repository.custom.implementation;

import com.example.botdemo.domain.Usser;
import com.example.botdemo.repository.custom.CustomUsserRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class UsserRepositoryImpl implements CustomUsserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Usser> getPairForUsser(int lowAgeLimit, int highAgeLimit, String city, String interestedGender, int selfAge, String selfGender, Long selfId) {
        Query query = entityManager.createNativeQuery("select *\n" +
                "from usser u\n" +
                "where (select d.id" +
                "       from duplet d" +
                "       where (d.first_user_id = ? and d.second_user_id = u.id)" +
                "          or (d.first_user_id = u.id and d.second_user_id = ?)) IS NULL" +
                "  and (u.age between ? and ?" +
                "    and u.city = ?" +
                "    and u.gender = ?" +
                "    and ? between u.desired_lower_age_limit and u.desired_upper_age_limit" +
                "    and u.preferred_gender = ?)" +
                "limit 1;", Usser.class);
        int k = 1;
        query.setParameter(k++, selfId);
        query.setParameter(k++, selfId);
        query.setParameter(k++, lowAgeLimit);
        query.setParameter(k++, highAgeLimit);
        query.setParameter(k++, city);
        query.setParameter(k++, interestedGender);
        query.setParameter(k++, selfAge);
        query.setParameter(k++, selfGender);
        Object obj = null;
        try {
            obj = query.getSingleResult();
        } catch (NoResultException exception) {
            return Optional.empty();
        }
        return Optional.of((Usser) obj);
    }

    @Override
    public List<Usser> getMatchedUsersForUser(Long selfId) {
        Query query = entityManager.createNativeQuery("select * from usser u join duplet d on" +
                "    ((u.id=d.first_user_id and ? =d.second_user_id) or ( ? =d.first_user_id and u.id=d.second_user_id))" +
                "    where d.status='MATCH';", Usser.class);
        int k = 1;
        query.setParameter(k++, selfId);
        query.setParameter(k++, selfId);
        return (List<Usser>) query.getResultList();
    }
}

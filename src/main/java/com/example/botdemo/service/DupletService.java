package com.example.botdemo.service;

import com.example.botdemo.domain.Duplet;
import com.example.botdemo.domain.DupletStatus;
import com.example.botdemo.domain.UserReact;
import com.example.botdemo.domain.Usser;
import com.example.botdemo.repository.DupletRepository;
import com.example.botdemo.repository.UsserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DupletService {

    @Autowired
    private DupletRepository dupletRepository;

    @Autowired
    private UsserRepository userRepository;


    public Optional<Duplet> getDupletForUserFromDuplet(Usser self) {
        return dupletRepository.findFirstByStatusAndFirstUserReactAndSecondUsser
                (DupletStatus.IN_PROCCES, self);
    }

    private Duplet addDuplet(Duplet duplet) {
        return dupletRepository.save(duplet);
    }

    private Optional<Duplet> getById(Long id) {
        return dupletRepository.findById(id);
    }

    @SneakyThrows
    public Duplet getDupletForUser(Usser usser) {

        return getDupletForUserFromDuplet(usser).orElseGet(() -> {
            Usser u = userRepository.getPairForUsser
                    (usser.getDesiredLowerAgeLimit(), usser.getDesiredUpperAgeLimit(), usser.getCity(),
                            usser.getPreferredGender(), usser.getAge(), usser.getGender(), usser.getId()).orElse(null);
            if (u != null) {
                Duplet duplet = new Duplet();
                duplet.setFirstUsser(userRepository.getReferenceById(usser.getId()));
                duplet.setSecondUsser(u);
                duplet.setStatus(DupletStatus.PREPARED);
                return addDuplet(duplet);
            }
            return null;
        });
    }

    @SneakyThrows
    public boolean evaluateDuplet(Duplet duplet, UserReact react) throws Exception {
        boolean result = false;
        Duplet duplet1 = getById(duplet.getId()).orElseThrow(Exception::new);//TODO make Exception
        if (duplet1.getFirstUserReact() == null) {
            duplet1.setFirstUserReact(react);
            duplet1.setStatus(DupletStatus.IN_PROCCES);
        } else if (duplet1.getSecondUserReact() == null) {
            duplet1.setSecondUserReact(react);
            if (duplet1.getFirstUserReact().equals(UserReact.LIKED)
                    && duplet1.getSecondUserReact().equals(UserReact.LIKED)) {
                duplet1.setStatus(DupletStatus.MATCH);
                result = true;
            } else {
                duplet1.setStatus(DupletStatus.MIS_MATCH);
            }
        }
        addDuplet(duplet1);
        return result;
    }

    public List<Usser> getMatchedPairForUser(Usser self) {
        return userRepository.getMatchedUsersForUser(self.getId());
    }


    public Duplet getDupletById(Long id) {
        try {
            return getById(id).orElseThrow(Exception::new);//TODO exception
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

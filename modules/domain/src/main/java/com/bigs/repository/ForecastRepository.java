package com.bigs.repository;

import com.bigs.entity.Forecast;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ForecastRepository {

    private final EntityManager em;

    public void save(Forecast forecast){
        em.persist(forecast);
    }

    public List<Forecast> find(){
        return em.createQuery("select f from Forecast f", Forecast.class)
                .getResultList();
    }
}

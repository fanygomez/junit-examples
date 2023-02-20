package org.example.repositories.impl;

import org.example.Datos;
import org.example.models.Examen;
import org.example.repositories.ExamenRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository {
    @Override
    public Examen save(Examen examen) {
        System.out.println("ExamenRepositoryImpl.save");
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        System.out.println("ExamenRepositoryImpl.findAll");
        try {
            System.out.println("");
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Datos.EXAMEN_LIST;
    }
}

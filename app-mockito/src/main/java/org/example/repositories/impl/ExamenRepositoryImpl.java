package org.example.repositories.impl;

import org.example.models.Examen;
import org.example.repositories.ExamenRepository;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository {
    @Override
    public Examen save(Examen examen) {
        return examen;
    }

    @Override
    public List<Examen> findAll() {
        return Arrays.asList(
                new Examen(5L,"Math"),
                new Examen(6L,"History"),
                new Examen(7L,"Tech")
        );
    }
}

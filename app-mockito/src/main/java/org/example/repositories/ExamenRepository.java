package org.example.repositories;

import org.example.models.Examen;

import java.util.List;

public interface ExamenRepository {
    Examen save(Examen examen);
    List<Examen> findAll();
}

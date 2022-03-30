package org.example.repositories;

import org.example.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}

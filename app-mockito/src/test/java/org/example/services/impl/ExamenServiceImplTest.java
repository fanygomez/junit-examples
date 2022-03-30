package org.example.services.impl;

import org.example.models.Examen;
import org.example.repositories.ExamenRepository;
import org.example.repositories.impl.ExamenRepositoryImpl;
import org.example.services.IExamenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {
    ExamenRepository repository;
    IExamenService service;
    @BeforeEach
    void setUp() {
        //preparar escenario
        repository = mock(ExamenRepository.class); // ( clase a simular )
        service = new ExamenServiceImpl(repository);

    }

    @Test
    void findExamenPorNombre() {
//        ExamenRepository examenRepository = new ExamenRepositoryImpl();

        List<Examen> datos = Arrays.asList(
                new Examen(5L,"Math"),
                new Examen(6L,"History"),
                new Examen(7L,"Tech")
        );

        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Math");
        // Junit
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());// recomendo orElseThrow() -> get()
        assertEquals("Math", examen.orElseThrow().getNombre());

    }
    @Test
    void findExamenPorNombrelistaVacia() {
//        ExamenRepository examenRepository = new ExamenRepositoryImpl();
       repository = mock(ExamenRepository.class); // ( clase a simular )
       service = new ExamenServiceImpl(repository);

        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Math");
        // Junit
        assertFalse(examen.isPresent());

    }
}
package org.example.services.impl;

import org.example.models.Examen;
import org.example.repositories.ExamenRepository;
import org.example.repositories.PreguntasRepository;
import org.example.repositories.impl.ExamenRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) //Habilitar
class ExamenServiceImplTest {
    @Mock
    ExamenRepository repository;
    @Mock
    PreguntasRepository  preguntasRepository;
    @InjectMocks
    ExamenServiceImpl service;

    @BeforeEach
    void setUp() {
        //preparar escenario
        //MockitoAnnotations.openMocks(this); //activar

//        repository = mock(ExamenRepository.class); // ( clase a simular )
//        preguntasRepository = mock(PreguntasRepository.class);
//        service = new ExamenServiceImpl(repository,preguntasRepository);

    }

    @Test
    void findExamenPorNombre() {
//        ExamenRepository examenRepository = new ExamenRepositoryImpl();

//        List<Examen> datos = Arrays.asList(
//                new Examen(5L,"Math"),
//                new Examen(6L,"History"),
//                new Examen(7L,"Tech")
//        );

        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
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

        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Math");
        // Junit
        assertFalse(examen.isPresent());

    }

    @Test
    void testPreguntasExamen() {
    when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
//    when(preguntasRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.Preguntas_LIST);
    when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.Preguntas_LIST);

    Examen examen = service.findExamenPorNombreConPreguntas("Math");
    assertEquals(5,examen.getPreguntas().size());
    assertTrue(examen.getPreguntas().contains("integrales"));
    }

    @Test
    void testPreguntasExamenVerifyMethod() {
        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
//    when(preguntasRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.Preguntas_LIST);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.Preguntas_LIST);

        Examen examen = service.findExamenPorNombreConPreguntas("Math");
        assertEquals(5,examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));

        //verificar si se llama los  metodos dentro del service impl
        //verificar interaccion con el mock
        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
//    when(preguntasRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.Preguntas_LIST);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.Preguntas_LIST);

        Examen examen = service.findExamenPorNombreConPreguntas("Math");
        assertNull(examen);
        //verificar si se llama los  metodos dentro del service impl
        //verificar interaccion con el mock
        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    void testGuardarExamen() {
        when(repository.save(any(Examen.class))).thenReturn(Datos.EXAMEN);
        Examen examen = service.guardarExamen(Datos.EXAMEN);
    }
}
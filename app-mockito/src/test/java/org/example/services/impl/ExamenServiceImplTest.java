package org.example.services.impl;

import org.example.models.Examen;
import org.example.repositories.ExamenRepository;
import org.example.repositories.PreguntasRepository;
import org.example.repositories.impl.ExamenRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) //Habilitar
class ExamenServiceImplTest {
    //Given
    @Mock
    ExamenRepository repository;
    @Mock
    PreguntasRepository  preguntasRepository;
    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long>  captor;
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
        //Given
        when(repository.findAll()).thenReturn(Collections.emptyList());
//    when(preguntasRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.Preguntas_LIST);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.Preguntas_LIST);
        // When
        Examen examen = service.findExamenPorNombreConPreguntas("Math");
        //Then
        assertNull(examen);
        //verificar si se llama los  metodos dentro del service impl
        //verificar interaccion con el mock
        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    void testGuardarExamen() {
        //Given
        Examen newExam =  Datos.EXAMEN;
        newExam.setPreguntas(Datos.Preguntas_LIST);
        when(repository.save(any(Examen.class))).then(new Answer<Examen>(){
            Long sequence = 1L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(sequence++);
                return examen;
            }
        });
        // When
//        Examen examen = service.guardarExamen(Datos.EXAMEN);
        Examen examen = service.guardarExamen(newExam);
        // Then
        assertNotNull(examen.getId());
        assertEquals(1L, examen.getId());
        assertEquals("Math", examen.getNombre());
        verify(repository).save(any(Examen.class));
        verify(preguntasRepository).saveList(anyList());
    }
    @Test
    void testManejoException(){
//        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST_ID_NULL);
        when(preguntasRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Math");
        });
        assertEquals(IllegalArgumentException.class,exception.getClass());
        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(isNull());
    }

    @Test
    void testArgumentsMatchers(){
        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
//        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST_ID_NULL);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.Preguntas_LIST);
        service.findExamenPorNombreConPreguntas("Math");

        verify(repository).findAll();//validar la invovacion del metodo findAll()
        verify(preguntasRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(5L))); //validar si el Id = 5
//        verify(preguntasRepository).findPreguntasPorExamenId(eq(5L));
    }
    @Test
    void testArgumentsMatchers2(){
        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.Preguntas_LIST);
        service.findExamenPorNombreConPreguntas("Math");

        verify(repository).findAll();//validar la invovacion del metodo findAll()
        verify(preguntasRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers())); //validar si el Id = 5
    }
    public static class MiArgsMatchers implements ArgumentMatcher<Long>{
        private Long argument;
        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "MiArgsMatchers{}, El numero: " +
                    + argument + " debe ser un entero positivo";
        }
    }
    @Test
    void testArgumentsCaptor(){
        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.Preguntas_LIST);
        service.findExamenPorNombreConPreguntas("Math");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        verify(preguntasRepository).findPreguntasPorExamenId(captor.capture());
        assertEquals(5L,captor.getValue());
    }
    @Test
    void testDoThrow(){
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.Preguntas_LIST);
        doThrow(IllegalArgumentException.class).when((preguntasRepository)).saveList(anyList());//doThrow() para metodos void()
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.guardarExamen(examen);
        });
    }
    @Test
    void testDoAnswer(){
        when(repository.findAll()).thenReturn(Datos.EXAMEN_LIST);
        service.findExamenPorNombreConPreguntas("Math");
        doAnswer(invocationOnMock -> {
            Long id  = invocationOnMock.getArgument(0);
            return id == 5L? Datos.Preguntas_LIST: Collections.emptyList();
        }).when(preguntasRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("History");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("History"));
        assertEquals(5L, examen.getId());
        assertEquals("Math", examen.getNombre());

        verify(preguntasRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testDoAnswerGuardarExamen() {
        //Given
        Examen newExam =  Datos.EXAMEN;
        newExam.setPreguntas(Datos.Preguntas_LIST);
//        when(repository.save(any(Examen.class))).then(new Answer<Examen>(){
//            Long sequence = 1L;
//            @Override
//            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
//                Examen examen = invocationOnMock.getArgument(0);
//                examen.setId(sequence++);
//                return examen;
//            }
//        });
        // doAnswer
        doAnswer(new Answer<Examen>(){
            Long sequence = 1L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(sequence++);
                return examen;
            }
        }).when(repository).save(any(Examen.class));
        //When
//        Examen examen = service.guardarExamen(Datos.EXAMEN);
        Examen examen = service.guardarExamen(newExam);
        // Then
        assertNotNull(examen.getId());
        assertEquals(1L, examen.getId());
        assertEquals("Math", examen.getNombre());
        verify(repository).save(any(Examen.class));
        verify(preguntasRepository).saveList(anyList());
    }
}
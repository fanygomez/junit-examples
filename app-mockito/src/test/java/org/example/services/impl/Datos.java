package org.example.services.impl;

import org.example.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMEN_LIST = Arrays.asList(
            new Examen(5L,"Math"),
            new Examen(6L,"History"),
            new Examen(7L,"Tech")
    );
    public final static List<Examen> EXAMEN_LIST_ID_NULL = Arrays.asList(
            new Examen(null,"Math"),
            new Examen(null,"History"),
            new Examen(null,"Tech")
    );
    public final static List<Examen> EXAMEN_LIST_ID_NEGATIVOS = Arrays.asList(
//            new Examen(-6L,"Math"),
            new Examen(null,"History"),
            new Examen(null,"Tech")
    );

    public final static List<String> Preguntas_LIST = Arrays.asList(
            "aritmetica",
            "integrales",
            "derivadas",
            "trigonometria",
            "geometria"
    );

    public final static Examen EXAMEN = new Examen(8L,"Math");
}

package org.example.services.impl;

import org.example.models.Examen;
import org.example.repositories.ExamenRepository;
import org.example.repositories.PreguntasRepository;
import org.example.services.IExamenService;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements IExamenService {

    private ExamenRepository examenRepository;
    private PreguntasRepository preguntasRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntasRepository preguntasRepository) {
        this.examenRepository = examenRepository;
        this.preguntasRepository = preguntasRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
       return this.examenRepository.findAll().stream().filter(examen -> examen.getNombre().contains(nombre))
                .findFirst();
//        Examen examen = null;
//
//        if (examenOptional.isPresent()){
//            examen = examenOptional.orElseThrow();
//        }

//        return examen;
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if (examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntasRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardarExamen(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntasRepository.saveList(examen.getPreguntas());
        }
        return examenRepository.save(examen);
    }


}

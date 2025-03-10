package com.forohub.controller;

import com.forohub.domain.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DataRegistroDelTopic dataRegistroDelTopic,
                                                                UriComponentsBuilder uriComponentsBuilder) {

        if (topicoRepository.existsByTituloAndMensaje(dataRegistroDelTopic.titulo(), dataRegistroDelTopic.mensaje())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERROR: Ya existe un tópico con el mismo título y mensaje");
        }

        Topico topico = topicoRepository.save(new Topico(dataRegistroDelTopic));

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.isEstadoActivo(), topico.getAutor(),
                topico.getNombreCurso());

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DataListadoDelTopic>> listarTopicos(@PageableDefault(size = 10,
            sort = "creacion", direction = Sort.Direction.DESC) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findByEstadoActivoTrue(paginacion).map(DataListadoDelTopic::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity listarUnSoloTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.isEstadoActivo(),
                topico.getAutor(), topico.getNombreCurso());
        return ResponseEntity.ok(datosTopico);
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarTopico(@RequestBody @Valid DataActualizarDelTopic dataActualizarDelTopic) {
        Topico topico = topicoRepository.getReferenceById(dataActualizarDelTopic.id());
        topico.actualizarDatos(dataActualizarDelTopic);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.isEstadoActivo(), topico.getAutor(), topico.getNombreCurso()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        topico.desactivarTopico();
        return ResponseEntity.noContent().build();
    }
}
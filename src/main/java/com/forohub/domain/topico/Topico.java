package com.forohub.domain.topico;

import com.forohub.domain.curso.NombreCurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private String fechaCreacion;
    private boolean estadoActivo;
    private String autor;
    @Enumerated(EnumType.STRING)
    private NombreCurso nombreCurso;

    public Topico(DataRegistroDelTopic dataRegistroDelTopic) {
        this.estadoActivo = true;
        this.titulo = dataRegistroDelTopic.titulo();
        this.mensaje = dataRegistroDelTopic.mensaje();
        this.autor = dataRegistroDelTopic.autor();
        this.nombreCurso = dataRegistroDelTopic.nombreCurso();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.fechaCreacion = LocalDateTime.now().format(formatter);
    }

    public void actualizarDatos(DataActualizarDelTopic dataActualizarDelTopic) {

        if (dataActualizarDelTopic.titulo() != null) {
            this.titulo = dataActualizarDelTopic.titulo();
        }
        if (dataActualizarDelTopic.mensaje() != null) {
            this.mensaje = dataActualizarDelTopic.mensaje();
        }
        if (dataActualizarDelTopic.nombreCurso() != null) {
            this.nombreCurso = dataActualizarDelTopic.nombreCurso();
        }
    }

    public void desactivarTopico() {
        this.estadoActivo = false;
    }
}
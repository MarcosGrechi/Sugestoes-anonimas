package br.com.fullstack.sugestao.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sugestao_id", nullable = false)
    private Sugestao sugestao;

    private String texto;
    private LocalDateTime dataEnvio;
}

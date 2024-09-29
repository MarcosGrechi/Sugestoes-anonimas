package br.com.fullstack.sugestao.controllers;

import br.com.fullstack.sugestao.entities.Comentario;
import br.com.fullstack.sugestao.services.ComentarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping("/{sugestaoId}")
    public List<Comentario> listarComentariosPorSugestao(@PathVariable Long sugestaoId) {
        log.info("Requisição para listar comentários da sugestão ID: {}", sugestaoId);
        return comentarioService.listarComentariosPorSugestao(sugestaoId);
    }

    @PostMapping
    public Comentario criarComentario(@RequestBody Comentario comentario) {
        log.info("Requisição para criar novo comentário.");
        return comentarioService.criarComentario(comentario);
    }
}



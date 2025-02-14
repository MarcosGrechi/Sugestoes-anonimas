package br.com.fullstack.sugestao.controllers;

import br.com.fullstack.sugestao.dtos.ComentarioDTO;
import br.com.fullstack.sugestao.entities.Comentario;
import br.com.fullstack.sugestao.entities.Sugestao;
import br.com.fullstack.sugestao.services.SugestaoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sugestoes")
@RequiredArgsConstructor
public class SugestaoController {

    private final SugestaoService sugestaoService;

    @Operation(summary = "Listar todas as sugestões", description = "Retorna todas as sugestões, com a opção de filtrar por título")
    @GetMapping
    public List<Sugestao> listarSugestoes(
            @Parameter(description = "Filtro opcional para buscar sugestões pelo título")
            @RequestParam(required = false) String titulo) {
        log.info("Requisição para listar sugestões, filtro por título: {}", titulo != null ? titulo : "Nenhum filtro");
        return sugestaoService.listarSugestoesComFiltro(titulo);
    }


    @Operation(summary = "Consultar sugestão por ID", description = "Retorna uma sugestão e seus comentários, ordenados por data de envio mais recente")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obterSugestaoComComentarios(
            @Parameter(description = "ID da sugestão a ser consultada", required = true)
            @PathVariable Long id) {
        log.info("Requisição para obter sugestão com ID: {}", id);
        return sugestaoService.obterSugestaoPorId(id)
                .map(sugestao -> {
                    List<Comentario> comentarios = sugestaoService.listarComentariosPorSugestao(id);
                    Map<String, Object> resposta = new HashMap<>();
                    resposta.put("sugestao", sugestao);
                    resposta.put("comentarios", comentarios);
                    return ResponseEntity.ok(resposta);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Sugestao criarSugestao(@RequestBody Sugestao sugestao) {
        log.info("Requisição para criar nova sugestão.");
        return sugestaoService.criarSugestao(sugestao);
    }

    @Operation(summary = "Adicionar um comentário", description = "Adiciona um comentário a uma sugestão e atualiza o campo 'dataAtualizacao'",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Texto do comentário a ser adicionado", required = true,
                    content = @Content(schema = @Schema(implementation = ComentarioDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comentário adicionado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Sugestão não encontrada")
            }
    )
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<Comentario> adicionarComentario(
            @Parameter(description = "ID da sugestão", required = true)
            @PathVariable Long id,
            @RequestBody ComentarioDTO comentarioDTO) {
        log.info("Requisição para adicionar comentário na sugestão com ID: {}", id);
        Comentario novoComentario = new Comentario();
        novoComentario.setTexto(comentarioDTO.getTexto());
        Comentario comentarioSalvo = sugestaoService.adicionarComentario(id, novoComentario);
        return ResponseEntity
                .created(URI.create("/sugestoes/" + id + "/comentarios/" + comentarioSalvo.getId()))
                .body(comentarioSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSugestao(@PathVariable Long id) {
        log.info("Requisição para deletar sugestão com ID: {}", id);
        try {
            sugestaoService.deletarSugestao(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error("Sugestão não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        } catch (Exception e) {
            log.error("Erro ao deletar sugestão com ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
}
}

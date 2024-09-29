package br.com.fullstack.sugestao.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para criação de um comentário")
public class ComentarioDTO {

    @Schema(description = "Texto do comentário", example = "Concordo com a sugestão", required = true)
    private String texto;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

package br.com.trilha.codechella.domain;

import br.com.trilha.codechella.Evento;
import br.com.trilha.codechella.enumeration.TipoEvento;

import java.time.LocalDate;

public record EventoDTO(Long id,
        TipoEvento tipo,
        String nome,
        LocalDate data,
        String descricao) {

    public static EventoDTO toDto(Evento evento){
        return new EventoDTO(evento.getId(), evento.getTipo(), evento.getNome(), evento.getData(), evento.getDescricao());
    }

    public Evento toEntity(){
        Evento evento = new Evento();
        evento.setNome(this.nome);
        evento.setTipo(this.tipo);
        evento.setData(this.data);
        evento.setDescricao(this.descricao);

        return evento;
    }
}

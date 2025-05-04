package br.com.trilha.codechella.domain;

import java.util.List;

public record TraducaoDTO(List<Texto> translations) {

    public String getText(){
       return translations.get(0).text();
    }
}

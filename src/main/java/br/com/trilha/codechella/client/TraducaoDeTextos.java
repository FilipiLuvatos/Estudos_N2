package br.com.trilha.codechella.client;

import br.com.trilha.codechella.domain.TraducaoDTO;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TraducaoDeTextos {

    public static Mono<String> obterTraducao(String texto, String idioma){

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api-free.deepl.com/v2/translate") // Consumo da API
                .build();

        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("text", texto); // Texto que será traduzido
        req.add("target_lang", idioma); // Para qual idioma será traduzido

        return webClient.post() // Requisição
                .header("Authorization", System.getenv("T_TRADUZ")) //Header
                .body(BodyInserters.fromFormData(req))//Corpo da requisição
                .retrieve()// Resposta
                .bodyToMono(TraducaoDTO.class) //Transforma para a classe
                .map(TraducaoDTO::getText);

    }

}

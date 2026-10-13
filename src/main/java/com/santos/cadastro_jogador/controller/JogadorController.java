
package com.santos.cadastro_jogador.controller;

import com.santos.cadastro_jogador.business.JogadorService;
import com.santos.cadastro_jogador.business.dto.JogadorPatchRequest;
import com.santos.cadastro_jogador.business.dto.JogadorRequest;
import com.santos.cadastro_jogador.business.dto.JogadorResponse;
import com.santos.cadastro_jogador.infraestructure.entitys.Posicao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/jogador")
@RequiredArgsConstructor
public class JogadorController {

    private final JogadorService jogadorService;

    // CREATE: POST /jogador
    @PostMapping
    public ResponseEntity<JogadorResponse> salvarJogador(
            @RequestBody @Valid JogadorRequest dados,
            UriComponentsBuilder uriBuilder) {

        JogadorResponse criado = jogadorService.salvarJogador(dados);

        URI local = uriBuilder
                .path("/jogador/{id}")
                .buildAndExpand(criado.id())
                .toUri();

        return ResponseEntity.created(local).body(criado);
    }

    // READ: lista com filtros opcionais
    // GET /jogador?id=1&nome=Pel&posicao=ATACANTE&numeroCamisa=10
    @GetMapping
    public ResponseEntity<List<JogadorResponse>> listarJogadores(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Posicao posicao,
            @RequestParam(required = false) Integer numeroCamisa) {

        return ResponseEntity.ok(
                jogadorService.listarJogadores(
                        id, nome, posicao, numeroCamisa
                )
        );
    }

    // READ: por ID
    // GET /jogador/1
    @GetMapping("/{id}")
    public ResponseEntity<JogadorResponse> buscarJogadorPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                jogadorService.buscarJogadorPorId(id)
        );
    }

    // READ: por número da camisa
    // GET /jogador/camisa/10
    @GetMapping("/camisa/{numero}")
    public ResponseEntity<JogadorResponse> buscarJogadorPorCamisa(
            @PathVariable Integer numero) {

        return ResponseEntity.ok(
                jogadorService.buscarJogadorPorCamisa(numero)
        );
    }

    // UPDATE completo: PUT /jogador/1
    @PutMapping("/{id}")
    public ResponseEntity<JogadorResponse> atualizarJogador(
            @PathVariable Integer id,
            @RequestBody @Valid JogadorRequest dados) {

        return ResponseEntity.ok(
                jogadorService.atualizarJogador(id, dados)
        );
    }

    // UPDATE parcial: PATCH /jogador/1
    @PatchMapping("/{id}")
    public ResponseEntity<JogadorResponse> atualizarParcialmente(
            @PathVariable Integer id,
            @RequestBody @Valid JogadorPatchRequest dados) {

        return ResponseEntity.ok(
                jogadorService.atualizarParcialmente(id, dados)
        );
    }

    // DELETE: por ID
    // DELETE /jogador/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarJogador(
            @PathVariable Integer id) {

        jogadorService.deletarJogador(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE: por número da camisa
    // DELETE /jogador/camisa/10
    @DeleteMapping("/camisa/{numero}")
    public ResponseEntity<Void> deletarJogadorPorCamisa(
            @PathVariable Integer numero) {

        jogadorService.deletarJogadorPorCamisa(numero);
        return ResponseEntity.noContent().build();
    }
}

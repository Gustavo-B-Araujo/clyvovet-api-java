package br.com.fiap.clyvovet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.clyvovet.dto.request.AlertaSaudeRequest;
import br.com.fiap.clyvovet.dto.response.AlertaSaudeResponse;
import br.com.fiap.clyvovet.enums.TipoAlerta;
import br.com.fiap.clyvovet.service.AlertaSaudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alertas")
@Tag(name = "Alertas de Saúde", description = "Endpoints para gerenciamento de alertas de saúde dos pets")
public class AlertaSaudeController {

    @Autowired
    private AlertaSaudeService alertaSaudeService;

    @PostMapping
    @Operation(summary = "Gerar um novo alerta de saúde")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alerta criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<AlertaSaudeResponse> create(@Valid @RequestBody AlertaSaudeRequest request) {
        AlertaSaudeResponse response = alertaSaudeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar alertas com filtros e paginação")
    public ResponseEntity<Page<AlertaSaudeResponse>> findAll(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) TipoAlerta tipo,
            @RequestParam(required = false) Boolean lido,
            @PageableDefault(size = 10, sort = "dataGeracao", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AlertaSaudeResponse> page = alertaSaudeService.findAll(petId, tipo, lido, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/nao-lidos")
    @Operation(summary = "Listar alertas não lidos")
    public ResponseEntity<Page<AlertaSaudeResponse>> findNaoLidos(
            @PageableDefault(size = 10, sort = "dataGeracao", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AlertaSaudeResponse> page = alertaSaudeService.findNaoLidos(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta encontrado"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    public ResponseEntity<AlertaSaudeResponse> findById(@PathVariable Long id) {
        AlertaSaudeResponse response = alertaSaudeService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/lido")
    @Operation(summary = "Marcar alerta como lido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Marcado como lido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    public ResponseEntity<Void> marcarComoLido(@PathVariable Long id) {
        alertaSaudeService.marcarComoLido(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar alerta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alerta deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alertaSaudeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
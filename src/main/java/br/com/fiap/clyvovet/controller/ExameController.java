package br.com.fiap.clyvovet.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.clyvovet.dto.request.ExameRequest;
import br.com.fiap.clyvovet.dto.response.ExameResponse;
import br.com.fiap.clyvovet.service.ExameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/exames")
@Tag(name = "Exames", description = "Endpoints para gerenciamento de exames dos pets")
public class ExameController {

    @Autowired
    private ExameService exameService;

    @PostMapping
    @Operation(summary = "Registrar um novo exame")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exame registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ExameResponse> create(@Valid @RequestBody ExameRequest request) {
        ExameResponse response = exameService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar exames com filtros e paginação")
    public ResponseEntity<Page<ExameResponse>> findAll(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long consultaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(size = 10, sort = "dataRealizacao", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ExameResponse> page = exameService.findAll(petId, tipo, consultaId, dataInicio, dataFim, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar exame por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exame encontrado"),
            @ApiResponse(responseCode = "404", description = "Exame não encontrado")
    })
    public ResponseEntity<ExameResponse> findById(@PathVariable Long id) {
        ExameResponse response = exameService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar exame existente")
    public ResponseEntity<ExameResponse> update(@PathVariable Long id, @Valid @RequestBody ExameRequest request) {
        ExameResponse response = exameService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar exame")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exame deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exame não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
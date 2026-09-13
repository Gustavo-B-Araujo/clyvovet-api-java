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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.clyvovet.dto.request.ClinicaRequest;
import br.com.fiap.clyvovet.dto.response.ClinicaResponse;
import br.com.fiap.clyvovet.service.ClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clinicas")
@Tag(name = "Clínicas", description = "Endpoints para gerenciamento de clínicas veterinárias")
public class ClinicaController {

    @Autowired
    private ClinicaService clinicaService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova clínica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clínica criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ClinicaResponse> create(@Valid @RequestBody ClinicaRequest request) {
        ClinicaResponse response = clinicaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as clínicas com paginação")
    public ResponseEntity<Page<ClinicaResponse>> findAll(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClinicaResponse> page = clinicaService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar clínica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clínica encontrada"),
            @ApiResponse(responseCode = "404", description = "Clínica não encontrada")
    })
    public ResponseEntity<ClinicaResponse> findById(@PathVariable Long id) {
        ClinicaResponse response = clinicaService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar clínica existente")
    public ResponseEntity<ClinicaResponse> update(@PathVariable Long id, @Valid @RequestBody ClinicaRequest request) {
        ClinicaResponse response = clinicaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar clínica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Clínica não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clinicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
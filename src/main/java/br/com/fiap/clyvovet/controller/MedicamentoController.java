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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.clyvovet.dto.request.MedicamentoRequest;
import br.com.fiap.clyvovet.dto.response.MedicamentoResponse;
import br.com.fiap.clyvovet.enums.StatusMedicamento;
import br.com.fiap.clyvovet.service.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicamentos")
@Tag(name = "Medicamentos", description = "Endpoints para gerenciamento de medicamentos dos pets")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @PostMapping
    @Operation(summary = "Registrar um novo medicamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicamento registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MedicamentoResponse> create(@Valid @RequestBody MedicamentoRequest request) {
        MedicamentoResponse response = medicamentoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar medicamentos com filtros e paginação")
    public ResponseEntity<Page<MedicamentoResponse>> findAll(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) StatusMedicamento status,
            @RequestParam(required = false) Long consultaId,
            @PageableDefault(size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MedicamentoResponse> page = medicamentoService.findAll(petId, status, consultaId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar medicamentos ativos (status ATIVO)")
    public ResponseEntity<Page<MedicamentoResponse>> findAtivos(
            @PageableDefault(size = 10, sort = "dataInicio") Pageable pageable) {
        Page<MedicamentoResponse> page = medicamentoService.findMedicamentosAtivos(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar medicamento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Medicamento não encontrado")
    })
    public ResponseEntity<MedicamentoResponse> findById(@PathVariable Long id) {
        MedicamentoResponse response = medicamentoService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar medicamento existente")
    public ResponseEntity<MedicamentoResponse> update(@PathVariable Long id, @Valid @RequestBody MedicamentoRequest request) {
        MedicamentoResponse response = medicamentoService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar medicamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medicamento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Medicamento não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
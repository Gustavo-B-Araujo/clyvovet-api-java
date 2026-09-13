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

import br.com.fiap.clyvovet.dto.request.VacinaRequest;
import br.com.fiap.clyvovet.dto.response.VacinaResponse;
import br.com.fiap.clyvovet.service.VacinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/vacinas")
@Tag(name = "Vacinas", description = "Endpoints para gerenciamento de vacinas dos pets")
public class VacinaController {

    @Autowired
    private VacinaService vacinaService;

    @PostMapping
    @Operation(summary = "Registrar uma nova vacina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacina registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<VacinaResponse> create(@Valid @RequestBody VacinaRequest request) {
        VacinaResponse response = vacinaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar vacinas com filtros e paginação")
    public ResponseEntity<Page<VacinaResponse>> findAll(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "dataAplicacao", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VacinaResponse> page = vacinaService.findAll(petId, nome, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vacina por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina encontrada"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    public ResponseEntity<VacinaResponse> findById(@PathVariable Long id) {
        VacinaResponse response = vacinaService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vencidas")
    @Operation(summary = "Listar vacinas vencidas (dataProximaDose < hoje)")
    public ResponseEntity<Page<VacinaResponse>> findVencidas(
            @PageableDefault(size = 10, sort = "dataProximaDose") Pageable pageable) {
        Page<VacinaResponse> page = vacinaService.findVacinasVencidas(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/proximas")
    @Operation(summary = "Listar vacinas próximas (próxima dose entre hoje e 30 dias)")
    public ResponseEntity<Page<VacinaResponse>> findProximas(
            @PageableDefault(size = 10, sort = "dataProximaDose") Pageable pageable) {
        Page<VacinaResponse> page = vacinaService.findVacinasProximas(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vacina existente")
    public ResponseEntity<VacinaResponse> update(@PathVariable Long id, @Valid @RequestBody VacinaRequest request) {
        VacinaResponse response = vacinaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar vacina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vacina deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vacinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
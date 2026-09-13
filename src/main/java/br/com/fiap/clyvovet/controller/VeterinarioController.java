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

import br.com.fiap.clyvovet.dto.request.VeterinarioRequest;
import br.com.fiap.clyvovet.dto.response.VeterinarioResponse;
import br.com.fiap.clyvovet.service.VeterinarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/veterinarios")
@Tag(name = "Veterinários", description = "Endpoints para gerenciamento de veterinários")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @PostMapping
    @Operation(summary = "Cadastrar um novo veterinário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veterinário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<VeterinarioResponse> create(@Valid @RequestBody VeterinarioRequest request) {
        VeterinarioResponse response = veterinarioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os veterinários com paginação e filtros opcionais")
    public ResponseEntity<Page<VeterinarioResponse>> findAll(
            @RequestParam(required = false) String especialidade,
            @RequestParam(required = false) Long clinicaId,
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<VeterinarioResponse> page = veterinarioService.findAll(especialidade, clinicaId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veterinário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinário encontrado"),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado")
    })
    public ResponseEntity<VeterinarioResponse> findById(@PathVariable Long id) {
        VeterinarioResponse response = veterinarioService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veterinário existente")
    public ResponseEntity<VeterinarioResponse> update(@PathVariable Long id, @Valid @RequestBody VeterinarioRequest request) {
        VeterinarioResponse response = veterinarioService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar veterinário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veterinarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
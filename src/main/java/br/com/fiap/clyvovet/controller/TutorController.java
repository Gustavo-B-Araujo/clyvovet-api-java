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

import br.com.fiap.clyvovet.dto.request.TutorRequest;
import br.com.fiap.clyvovet.dto.response.TutorResponse;
import br.com.fiap.clyvovet.service.TutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tutores")
@Tag(name = "Tutores", description = "Endpoints para gerenciamento de tutores")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping
    @Operation(summary = "Cadastrar um novo tutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<TutorResponse> create(@Valid @RequestBody TutorRequest request) {
        TutorResponse response = tutorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os tutores com paginação")
    public ResponseEntity<Page<TutorResponse>> findAll(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<TutorResponse> page = tutorService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/by-email")
    @Operation(summary = "Buscar tutor por email (usado pelo app mobile após login/cadastro)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor encontrado"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado")
    })
    public ResponseEntity<TutorResponse> findByEmail(@RequestParam String email) {
        TutorResponse response = tutorService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tutor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor encontrado"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado")
    })
    public ResponseEntity<TutorResponse> findById(@PathVariable Long id) {
        TutorResponse response = tutorService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tutor existente")
    public ResponseEntity<TutorResponse> update(@PathVariable Long id, @Valid @RequestBody TutorRequest request) {
        TutorResponse response = tutorService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tutorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
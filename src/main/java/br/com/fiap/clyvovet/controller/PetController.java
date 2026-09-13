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

import br.com.fiap.clyvovet.dto.request.PetRequest;
import br.com.fiap.clyvovet.dto.response.PetResponse;
import br.com.fiap.clyvovet.dto.response.ConsultaResponse;
import br.com.fiap.clyvovet.dto.response.VacinaResponse;
import br.com.fiap.clyvovet.dto.response.MedicamentoResponse;
import br.com.fiap.clyvovet.dto.response.ExameResponse;
import br.com.fiap.clyvovet.dto.response.AlertaSaudeResponse;
import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.service.PetService;
import br.com.fiap.clyvovet.service.ConsultaService;
import br.com.fiap.clyvovet.service.VacinaService;
import br.com.fiap.clyvovet.service.MedicamentoService;
import br.com.fiap.clyvovet.service.ExameService;
import br.com.fiap.clyvovet.service.AlertaSaudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pets")
@Tag(name = "Pets", description = "Endpoints para gerenciamento de pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private VacinaService vacinaService;

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private ExameService exameService;

    @Autowired
    private AlertaSaudeService alertaSaudeService;



    @PostMapping
    @Operation(summary = "Cadastrar um novo pet")
    public ResponseEntity<PetResponse> create(@Valid @RequestBody PetRequest request) {
        PetResponse response = petService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar pets com filtros e paginação")
    public ResponseEntity<Page<PetResponse>> findAll(
            @RequestParam(required = false) Especie especie,
            @RequestParam(required = false) String raca,
            @RequestParam(required = false) Long tutorId,
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PetResponse> page = petService.findAll(especie, raca, tutorId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pet por ID")
    public ResponseEntity<PetResponse> findById(@PathVariable Long id) {
        PetResponse response = petService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pet existente")
    public ResponseEntity<PetResponse> update(@PathVariable Long id, @Valid @RequestBody PetRequest request) {
        PetResponse response = petService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pet")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //endpoints de relacionamentos

    @GetMapping("/{id}/consultas")
    @Operation(summary = "Listar todas as consultas do pet")
    public ResponseEntity<Page<ConsultaResponse>> getConsultasByPetId(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ConsultaResponse> page = consultaService.findByPetId(id, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/vacinas")
    @Operation(summary = "Listar todas as vacinas do pet")
    public ResponseEntity<Page<VacinaResponse>> getVacinasByPetId(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "dataAplicacao", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VacinaResponse> page = vacinaService.findByPetId(id, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/medicamentos")
    @Operation(summary = "Listar todos os medicamentos do pet")
    public ResponseEntity<Page<MedicamentoResponse>> getMedicamentosByPetId(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MedicamentoResponse> page = medicamentoService.findByPetId(id, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/exames")
    @Operation(summary = "Listar todos os exames do pet")
    public ResponseEntity<Page<ExameResponse>> getExamesByPetId(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "dataRealizacao", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ExameResponse> page = exameService.findByPetId(id, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/alertas")
    @Operation(summary = "Listar todos os alertas de saúde do pet")
    public ResponseEntity<Page<AlertaSaudeResponse>> getAlertasByPetId(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "dataGeracao", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AlertaSaudeResponse> page = alertaSaudeService.findByPetId(id, pageable);
        return ResponseEntity.ok(page);
    }
}
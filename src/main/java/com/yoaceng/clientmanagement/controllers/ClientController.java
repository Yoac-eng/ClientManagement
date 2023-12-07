package com.yoaceng.clientmanagement.controllers;


import com.yoaceng.clientmanagement.dto.ClientDTO;
import com.yoaceng.clientmanagement.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


/**
 * Controller padrão para lidar com requisições relacionadas com Clientes.
 *
 * @author Cayo Cutrim
 * @since 05/12/2023
 */
@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    /**
     * Busca todos os clientes de forma paginada.
     *
     * @param pageable Define critérios de paginação (tamanho, número da página, ordenação).
     * @return ResponseEntity com Page<ClientDTO> dos clientes e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAll (Pageable pageable){
        Page<ClientDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id ID do cliente a ser buscado.
     * @return ResponseEntity com ClientDTO do cliente encontrado e status HTTP 200 (OK).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id){
        ClientDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Insere um novo cliente.
     *
     * Este método recebe um objeto ClientDTO com os dados do cliente, valida esses dados e, se estiverem corretos,
     * insere um novo cliente no sistema. Após a inserção, é gerada uma URI de localização do recurso criado.
     *
     * @param newClientDTO O objeto ClientDTO contendo os dados do novo cliente. Deve ser válido.
     * @return ResponseEntity com status HTTP 201 (Created) e a URI do recurso criado no header 'Location'.
     */
    @PostMapping
    public ResponseEntity<ClientDTO> insert(@Valid @RequestBody ClientDTO newClientDTO){
        newClientDTO = service.insert(newClientDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newClientDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(newClientDTO);
    }

    /**
     * Atualiza um cliente existente com base no ID fornecido.
     *
     * @param id O ID do cliente a ser atualizado.
     * @param dto O DTO do cliente contendo os dados a serem atualizados. Deve ser um corpo de solicitação válido.
     * @return ResponseEntity<ClientDTO> com o DTO do cliente atualizado e o status HTTP OK.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> update(@PathVariable Long id, @Valid @RequestBody ClientDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    /**
     * Exclui um cliente com base no ID fornecido.
     *
     * @param id O ID do cliente a ser excluído.
     * @return ResponseEntity<Void> com status HTTP No Content após a exclusão bem-sucedida do cliente.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

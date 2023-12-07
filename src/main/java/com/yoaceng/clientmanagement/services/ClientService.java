package com.yoaceng.clientmanagement.services;

import com.yoaceng.clientmanagement.dto.ClientDTO;
import com.yoaceng.clientmanagement.entities.Client;
import com.yoaceng.clientmanagement.repositories.ClientRepository;
import com.yoaceng.clientmanagement.services.exceptions.DatabaseException;
import com.yoaceng.clientmanagement.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca uma página de clientes.
     *
     * @param pageable Objeto `Pageable` que define a paginação.
     * @return Page<ClientDTO> contendo uma lista paginada de clientes convertidos em DTOs.
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
        //Page is a stream in java
        Page<Client> result = repository.findAll(pageable);
        return result.map(this::convertToDto);
    }


    /**
     * Busca um cliente pelo seu ID.
     *
     * @param id O ID do cliente a ser buscado.
     * @return ClientDTO correspondente ao cliente encontrado.
     * @throws ResourceNotFoundException se o cliente não for encontrado.
     */
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Client client = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado pelo id fornecido."));
        return convertToDto(client);
    }

    /**
     * Insere um novo cliente na base de dados.
     *
     * @param dto O DTO de cliente que representa os dados a serem inseridos.
     * @return ClientDTO correspondente ao cliente inserido.
     */
    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client client = convertToEntity(dto);
        return convertToDto(repository.save(client));
    }

    /**
     * Atualiza os dados de um cliente existente.
     *
     * @param id O ID do cliente a ser atualizado.
     * @param dto O DTO de cliente com os dados atualizados.
     * @return ClientDTO com os dados atualizados do cliente.
     * @throws ResourceNotFoundException se o cliente especificado não for encontrado.
     */
    @Transactional
    public ClientDTO update(Long id, ClientDTO dto){
        try{
            Client clientById = repository.getReferenceById(id);
            copyDtoDataToEntity(dto, clientById);
            Client result = repository.save(clientById);
            return convertToDto(result);

        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }


    /**
     * Exclui um cliente com base em seu ID.
     *
     * Usa a propagação SUPPORTS, o que significa que a operação pode ocorrer
     * dentro de uma transação existente, mas não iniciará uma nova se não houver nenhuma.
     *
     * @param id O ID do cliente a ser excluído.
     * @throws ResourceNotFoundException se o cliente com o ID especificado não for encontrado.
     * @throws DatabaseException se ocorrer uma falha de integridade referencial durante a exclusão.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }


    private void copyDtoDataToEntity(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }

    private ClientDTO convertToDto(Client client){
        return modelMapper.map(client, ClientDTO.class);
    }

    private Client convertToEntity(ClientDTO clientDTO){
        return modelMapper.map(clientDTO, Client.class);
    }

}

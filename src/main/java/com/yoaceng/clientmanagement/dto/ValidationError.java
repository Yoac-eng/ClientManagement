package com.yoaceng.clientmanagement.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa especificamente um erro de validação nos campos, além
 * da informação padrão de erro.
 */
public class ValidationError extends CustomError{

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName, message));
    }
}

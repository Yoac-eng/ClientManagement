package com.yoaceng.clientmanagement.dto;


/**
 * Classe que representa o formato da mensagem de erro retornado quando
 * algum campo falha no processo de validação.
 */
public class FieldMessage {

    private String fieldName;
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}

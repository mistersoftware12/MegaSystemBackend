package com.Biblioteca.DTO.Extra;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdResponse implements Serializable {
    Long id;

    public IdResponse() {
    }

    public IdResponse(Long id) {
        this.id = id;
    }
}

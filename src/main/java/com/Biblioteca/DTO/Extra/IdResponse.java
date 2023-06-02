package com.Biblioteca.DTO.Extra;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class IdResponse implements Serializable {
    Long id;

    BigInteger count;

    Boolean estado;
    public IdResponse() {
    }

    public IdResponse(Long id) {
        this.id = id;
    }
}

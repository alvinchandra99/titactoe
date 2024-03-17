package com.fwd.assesment.test.alvin.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BadRequestException extends RuntimeException {

  public BadRequestException(String meesage) {
    super(meesage);
  }
}

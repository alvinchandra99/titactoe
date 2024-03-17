package com.fwd.assesment.test.alvin.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ServerErrorException extends RuntimeException {
  public ServerErrorException(String messsage) {
    super(messsage);
  }
}

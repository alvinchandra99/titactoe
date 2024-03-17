package com.fwd.assesment.test.alvin.controller;

import com.fwd.assesment.test.alvin.model.common.CommonResponse;
import com.fwd.assesment.test.alvin.model.exception.BadRequestException;
import com.fwd.assesment.test.alvin.model.exception.ServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorController {
  @ExceptionHandler(value = {BadRequestException.class})
  public CommonResponse<String> handleBadRequest(BadRequestException ex) {
    log.error("Exception occured with ex : {} ", ex.getMessage(), ex);
    CommonResponse<String> commonResponse = new CommonResponse<>();
    commonResponse.setCode(400);
    commonResponse.setStatus(HttpStatus.BAD_REQUEST.toString());
    commonResponse.setData(ex.getMessage());
    return commonResponse;
  }

  @ExceptionHandler(value = {ServerErrorException.class})
  public CommonResponse<String> handleServerError(BadRequestException ex) {
    log.error("Exception occured with ex : {} ", ex.getMessage(), ex);
    CommonResponse<String> commonResponse = new CommonResponse<>();
    commonResponse.setCode(500);
    commonResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    commonResponse.setData(ex.getMessage());
    return commonResponse;
  }
}

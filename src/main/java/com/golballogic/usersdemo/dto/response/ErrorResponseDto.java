package com.golballogic.usersdemo.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponseDto {
    private Date timestamp;
    private Integer code;
    private String detail;
}

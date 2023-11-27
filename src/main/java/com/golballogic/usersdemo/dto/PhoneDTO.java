package com.golballogic.usersdemo.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDTO implements Serializable {
    private Long number;
    private Integer cityCode;
    private String countryCode;
}

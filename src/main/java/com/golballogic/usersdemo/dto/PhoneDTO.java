package com.golballogic.usersdemo.dto;

import com.golballogic.usersdemo.domain.Phone;
import lombok.Data;

@Data
public class PhoneDTO {
    private Long number;
    private Integer cityCode;
    private String countryCode;

}

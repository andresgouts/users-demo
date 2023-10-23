package com.golballogic.usersdemo.dto.request;

import com.golballogic.usersdemo.domain.Phone;
import lombok.Data;

@Data
public class PhoneDTO {
    private Long number;
    private Integer cityCode;
    private String countryCode;

    public Phone toPhoneEntity() {
        Phone phone = new Phone();
        phone.setNumber(this.number);
        phone.setCityCode(this.cityCode);
        phone.setCountryCode(this.countryCode);
        return phone;
    }
}

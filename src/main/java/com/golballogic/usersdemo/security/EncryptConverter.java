package com.golballogic.usersdemo.security;

import com.golballogic.usersdemo.service.EncryptionDecryptionService;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;

@AllArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

    private final EncryptionDecryptionService encryptionDecryptionUtils;
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptionDecryptionUtils.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptionDecryptionUtils.decrypt(dbData);
    }
}

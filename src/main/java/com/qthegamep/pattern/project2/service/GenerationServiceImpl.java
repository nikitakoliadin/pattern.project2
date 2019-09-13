package com.qthegamep.pattern.project2.service;

import java.security.SecureRandom;

public class GenerationServiceImpl implements GenerationService {

    @Override
    public String generateUniqueId(Long length) {
        StringBuilder uniqueId = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; ++i) {
            boolean upperCase = secureRandom.nextBoolean();
            char symbol = (char) (secureRandom.nextInt(26) + 97);
            if (upperCase) {
                symbol = Character.toUpperCase(symbol);
            }
            uniqueId.append(symbol);
        }
        return uniqueId.toString();
    }
}

package com.qthegamep.pattern.project2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class GenerationServiceImpl implements GenerationService {

    private static final Logger LOG = LoggerFactory.getLogger(GenerationServiceImpl.class);

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
        LOG.debug("Generated unique ID: {}", uniqueId);
        return uniqueId.toString();
    }
}

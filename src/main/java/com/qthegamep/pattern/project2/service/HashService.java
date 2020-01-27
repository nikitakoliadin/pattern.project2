package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.compile.HashServiceException;
import com.qthegamep.pattern.project2.model.container.HashAlgorithm;

public interface HashService {

    String encode(String data, HashAlgorithm hashAlgorithm) throws HashServiceException;

    String encode(String data, HashAlgorithm hashAlgorithm, String requestId) throws HashServiceException;
}

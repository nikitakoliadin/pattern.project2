package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.compile.CryptoServiceException;
import com.qthegamep.pattern.project2.model.container.HashAlgorithm;

public interface CryptoService {

    String encode(String data, HashAlgorithm hashAlgorithm) throws CryptoServiceException;

    String encode(String data, HashAlgorithm hashAlgorithm, String requestId) throws CryptoServiceException;
}

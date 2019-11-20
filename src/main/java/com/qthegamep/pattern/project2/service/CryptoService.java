package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.CryptoServiceException;
import com.qthegamep.pattern.project2.model.HashAlgorithm;

public interface CryptoService {

    String encodeTo(String data, HashAlgorithm hashAlgorithm) throws CryptoServiceException;

    String encodeTo(String data, HashAlgorithm hashAlgorithm, String requestId) throws CryptoServiceException;
}

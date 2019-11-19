package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.CryptoServiceException;

public interface CryptoService {

    String encodeToMD5Hash(String data) throws CryptoServiceException;

    String encodeToMD5Hash(String data, String requestId) throws CryptoServiceException;
}

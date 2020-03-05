package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.compile.HashServiceException;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.model.container.HashAlgorithm;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class HashServiceImpl implements HashService {

    private static final Logger LOG = LoggerFactory.getLogger(HashServiceImpl.class);

    @Override
    public String encode(String data, HashAlgorithm hashAlgorithm) throws HashServiceException {
        return encode(data, hashAlgorithm, null);
    }

    @Override
    public String encode(String data, HashAlgorithm hashAlgorithm, String requestId) throws HashServiceException {
        LOG.debug("Encode: {} Algorithm: {} RequestId: {}", data, hashAlgorithm, requestId);
        if (HashAlgorithm.MD5_HASH_ALGORITHM.equals(hashAlgorithm)) {
            return encode(data, hashAlgorithm.getAlgorithmName());
        } else {
            throw new HashServiceException(Error.ENCODE_HASH_ALGORITHM_NOT_EXISTS_ERROR);
        }
    }

    private String encode(String data, String hashAlgorithmName) throws HashServiceException {
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance(hashAlgorithmName);
            md5MessageDigest.update(data.getBytes(), 0, data.length());
            return Hex.encodeHexString(md5MessageDigest.digest());
        } catch (Exception e) {
            throw new HashServiceException(e, Error.ENCODE_HASH_ERROR);
        }
    }
}

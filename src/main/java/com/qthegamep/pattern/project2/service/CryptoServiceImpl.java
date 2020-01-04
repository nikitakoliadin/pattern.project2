package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.compile.CryptoServiceException;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.model.container.HashAlgorithm;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class CryptoServiceImpl implements CryptoService {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoServiceImpl.class);

    @Override
    public String encodeTo(String data, HashAlgorithm hashAlgorithm) throws CryptoServiceException {
        return encodeTo(data, hashAlgorithm, null);
    }

    @Override
    public String encodeTo(String data, HashAlgorithm hashAlgorithm, String requestId) throws CryptoServiceException {
        LOG.debug("Encode: {} Algorithm: {} RequestId: {}", data, hashAlgorithm.getAlgorithmName(), requestId);
        if (HashAlgorithm.MD5_HASH_ALGORITHM.equals(hashAlgorithm)) {
            return encode(data, hashAlgorithm.getAlgorithmName());
        } else {
            throw new CryptoServiceException(Error.ENCODE_HASH_ALGORITHM_NOT_EXISTS_ERROR);
        }
    }

    private String encode(String data, String hashAlgorithmName) throws CryptoServiceException {
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance(hashAlgorithmName);
            md5MessageDigest.update(data.getBytes(), 0, data.length());
            return Hex.encodeHexString(md5MessageDigest.digest());
        } catch (Exception e) {
            throw new CryptoServiceException(e, Error.ENCODE_HASH_ERROR);
        }
    }
}

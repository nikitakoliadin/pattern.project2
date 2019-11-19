package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.CryptoServiceException;
import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.util.Constants;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class CryptoServiceImpl implements CryptoService {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoServiceImpl.class);

    @Override
    public String encodeToMD5Hash(String data) throws CryptoServiceException {
        return encodeToMD5Hash(data, null);
    }

    @Override
    public String encodeToMD5Hash(String data, String requestId) throws CryptoServiceException {
        LOG.debug("Encode to MD5 hash: {} RequestId: {}", data, requestId);
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance(Constants.MD5_HASH.getValue());
            md5MessageDigest.update(data.getBytes(), 0, data.length());
            return Hex.encodeHexString(md5MessageDigest.digest());
        } catch (Exception e) {
            throw new CryptoServiceException(e, ErrorType.ENCODE_TO_MD5_HASH_ERROR);
        }
    }
}

package com.qthegamep.pattern.project2.model;

public enum HashAlgorithm {

    MD5("MD5");

    private String algorithm;

    HashAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}

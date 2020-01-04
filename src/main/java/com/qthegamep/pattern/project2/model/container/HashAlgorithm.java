package com.qthegamep.pattern.project2.model.container;

public enum HashAlgorithm {

    MD5_HASH_ALGORITHM("MD5");

    private String algorithmName;

    HashAlgorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }
}

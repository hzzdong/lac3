package com.linkallcloud.sh.sm;

public class Signature {

    private String identity;// 签名者身份标识
    private String algorithm;// 签名算法
    private String value;// 签名内容

    public Signature() {
        this.algorithm = "SHA1";
    }

    /**
     * Signature构造器
     * 
     * @param identity
     *            签名者身份标识
     * @throws BaseException
     */
    public Signature(String identity) {
        this(identity, "SHA1");
    }

    /**
     * Signature构造器
     * 
     * @param identity
     *            签名者身份标识
     * @param algorithm
     *            签名算法
     * @param value
     *            签名内容
     * @throws BaseException
     */
    public Signature(String identity, String algorithm) {
        this.setIdentity(identity);
        this.setAlgorithm(algorithm);
    }

    /**
     * @return the algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * @param algorithm
     *            the algorithm to set
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = ((algorithm == null || algorithm.length() <= 0) || !"MD5".equals(algorithm)) ? "SHA1" : "MD5";
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the identity
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * @param identity
     *            the identity to set
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

}

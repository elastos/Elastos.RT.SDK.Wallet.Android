package org.elastos.elastoswallet;

/**
 * IIdChainSubWallet jni
 */
public class IIdChainSubWallet extends ISidechainSubWallet{
    private long mIDchainProxy;

    /**
     * Create a id transaction and return the content of transaction in json format, this is a special transaction to register id related information on id chain.
     * @param fromAddress specify which address we want to spend, or just input empty string to let wallet choose UTXOs automatically.
     * @param payloadJson is payload for register id related information in json format, the content of payload should have Id, Path, DataHash, Proof, and Sign.
     * @param programJson is program data in json format, which contains redeem script of id (Code) and sign of the \p payloadJson with id related private key (Parameter).
     * @param memo input memo attribute for describing.
     * @param remark is used to record message of local wallet.
     * @return If success return the content of transaction in json format.
     */
    public String CreateIdTransaction(String fromAddress, String payloadJson, String programJson
                , String memo, String remark) throws WalletException {
        return nativeCreateIdTransaction(mIDchainProxy, fromAddress, payloadJson, programJson, memo, remark);
    }

    public IIdChainSubWallet(long proxy) {
        super(proxy);
        mIDchainProxy = proxy;
    }

    private native String nativeCreateIdTransaction(long proxy, String fromAddress, String payloadJson
            , String programJson, String memo, String remark);
}

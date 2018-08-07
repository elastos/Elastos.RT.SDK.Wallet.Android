
package org.elastos.elastoswallet;

/**
 * ISubWallet
 */
public class ISubWallet {
    private long mSubProxy;

    /**
     * Get the sub wallet chain id.
     * @return sub wallet chain id.
     */
    public String GetChainId() {
        return nativeGetChainId(mSubProxy);
    }

    /**
     * Get balances of all addresses in json format.
     * @return balances of all addresses in json format.
     */
    public String GetBalanceInfo() {
        return nativeGetBalanceInfo(mSubProxy);
    }

    /**
     * Get sum of balances of all addresses.
     * @return sum of balances.
     */
    public long GetBalance() {
        return nativeGetBalance(mSubProxy);
    }

    /**
     * Create a new address or return existing unused address. Note that if create the sub wallet by setting the singleAddress to true, will always return the single address.
     * @return a new address or existing unused address.
     */
    public String CreateAddress() {
        return nativeCreateAddress(mSubProxy);
    }

    /**
     * Get all created addresses in json format. The parameters of start and count are used for purpose of paging.
     * @param start specify start index of all addresses list.
     * @param count specify count of addresses we need.
     * @return addresses in json format.
     */
    public String GetAllAddress(int start, int count) {
        return nativeGetAllAddress(mSubProxy, start, count);
    }

    /**
     * Get balance of only the specified address.
     * @param address is one of addresses created by current sub wallet.
     * @return balance of specified address.
     */
    public long GetBalanceWithAddress(String address) {
        return nativeGetBalanceWithAddress(mSubProxy, address);
    }

    /**
     * Add a sub wallet callback object listened to current sub wallet.
     * @param subCallback is a pointer who want to listen events of current sub wallet.
     */
    public void AddCallback(ISubWalletCallback subCallback) {
        nativeAddCallback(mSubProxy, subCallback);
    }

    /**
     * Remove a sub wallet callback object listened to current sub wallet.
     * @param subCallback is a pointer who want to listen events of current sub wallet.
     */
    public void RemoveCallback(ISubWalletCallback subCallback) {
        nativeRemoveCallback(mSubProxy, subCallback);
    }

    /**
     * Create a normal transaction and return the content of transaction in json format.
     * @param fromAddress specify which address we want to spend, or just input empty string to let wallet choose UTXOs automatically.
     * @param toAddress specify which address we want to send.
     * @param amount specify amount we want to send.
     * @param memo input memo attribute for describing.
     * @param remark is used to record message of local wallet.
     * @return If success return the content of transaction in json format.
     */
    public String CreateTransaction(String fromAddress, String toAddress, long amount, String memo, String remark) throws WalletException {
        return nativeCreateTransaction(mSubProxy, fromAddress, toAddress, amount, memo, remark);
    }

    /**
     * Create a multi-sign address used to create multi-sign transaction.
     * @param multiPublicKeyJson is a list of public keys in json format.
     * @param totalSignNum specify total sign number (n).
     * @param requiredSignNum specify required sign number (m).
     * @return multi-sign address.
     */
    public String CreateMultiSignAddress(String multiPublicKeyJson, int totalSignNum, int requiredSignNum) {
        return nativeCreateMultiSignAddress(mSubProxy, multiPublicKeyJson, totalSignNum, requiredSignNum);
    }

    /**
     * Create a multi-sign transaction and return the content of transaction in json format.
     * @param fromAddress specify which address we want to spend, or just input empty string to let wallet choose UTXOs automatically.
     * @param toAddress specify which address we want to send.
     * @param amount specify amount we want to send.
     * @param memo input memo attribute for describing.
     * @return If success return the content of transaction in json format.
     */
    public String CreateMultiSignTransaction(String fromAddress, String toAddress, long amount, String memo) throws WalletException {
        return nativeCreateMultiSignTransaction(mSubProxy, fromAddress, toAddress, amount, memo);
    }

    /**
     * Send raw transaction by p2p network.
     * @param transactionJson content of transaction in json format.
     * @param fee specify fee for miners, fee must greater or equal than 1000 (sela).
     * @param payPassword use to decrypt the root private key temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @return Sent result in json format.
     */
    public String SendRawTransaction(String transactionJson, long fee, String payPassword) throws WalletException {
        return nativeSendRawTransaction(mSubProxy, transactionJson, fee, payPassword);
    }

    /**
     * Get all qualified transactions sorted by descent (newest first).
     * @param start specify start index of all transactions list.
     * @param count specify count of transactions we need.
     * @param addressOrTxid filter word which can be an address or a transaction id, if empty all transactions shall be qualified.
     * @return All qualified transactions in json format.
     */
    public String GetAllTransaction(int start, int count, String addressOrTxId) {
        return nativeGetAllTransaction(mSubProxy, start, count, addressOrTxId);
    }

    /**
     * Sign message through root private key of the master wallet.
     * @param message need to signed, it should not be empty.
     * @param payPassword use to decrypt the root private key temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @return signed data of the message.
     */
    public String Sign(String message, String payPassword) throws WalletException {
        return nativeSign(mSubProxy, message, payPassword);
    }

    /**
     * Verify signature by public key and raw message. This method can check signatures signed by any private keys not just the root private key of the master wallet.
     * @param publicKey belong to the private key signed the signature.
     * @param message raw data.
     * @param signature signed data by a private key that correspond to the public key.
     * @return the result wrapper by a json.
     */
    public boolean CheckSign(String address, String message, String signature) throws WalletException {
        return nativeCheckSign(mSubProxy, address, message, signature);
    }

    /**
     * Calculate transaction fee by content of transaction.
     * @param rawTransaction content of transaction in json format.
     * @param feePerKb specify the factor to calculate fee (transaction size * feePerKb).
     * @return Calculate result of final fee.
     */
    public long CalculateTransactionFee(String rawTransaction, long feePerKb) throws WalletException {
        return nativeCalculateTransactionFee(mSubProxy, rawTransaction, feePerKb);
    }

    public ISubWallet(long proxy) {
        mSubProxy = proxy;
    }

    protected long getProxy() {
        return mSubProxy;
    }

    private native String nativeGetChainId(long subProxy);
    private native String nativeGetBalanceInfo(long subProxy);
    private native long nativeGetBalance(long subProxy);
    private native String nativeCreateAddress(long subProxy);
    private native String nativeGetAllAddress(long subProxy, int start,int count);
    private native long nativeGetBalanceWithAddress(long subProxy, String address);
    private native void nativeAddCallback(long subProxy, ISubWalletCallback subCallback);
    private native void nativeRemoveCallback(long subProxy, ISubWalletCallback subCallback);
    private native String nativeCreateTransaction(long subProxy, String fromAddress, String toAddress, long amount, String memo, String remark);
    private native String nativeCreateMultiSignTransaction(long subProxy, String fromAddress, String toAddress, long amount, String memo);
    private native String nativeCreateMultiSignAddress(long subProxy, String multiPublicKeyJson, int totalSignNum, int requiredSignNum);
    private native String nativeSendRawTransaction(long subProxy, String transactionJson, long fee, String payPassword);
    private native String nativeGetAllTransaction(long subProxy, int start, int count, String addressOrTxId);
    private native String nativeSign(long subProxy, String message, String payPassword);
    private native boolean nativeCheckSign(long subProxy, String address, String message, String signature);
    private native long nativeCalculateTransactionFee(long subProxy, String rawTransaction, long feePerKb);
}

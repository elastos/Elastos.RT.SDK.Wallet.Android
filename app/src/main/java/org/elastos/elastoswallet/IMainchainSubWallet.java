package org.elastos.elastoswallet;

/**
 * IMainchainSubWallet jni
 */
public class IMainchainSubWallet extends ISubWallet {
    private long mMainchainProxy;

    /**
     * Create a deposit transaction and return the content of transaction in json format. Note that \p amount should greater than sum of \p so that we will leave enough fee for sidechain.
     * @param fromAddress specify which address we want to spend, or just input empty string to let wallet choose UTXOs automatically.
     * @param toAddress specify which address we want to send, in this method to address shall be genesis address of the side chain
     * @param amount specify amount we want to send.
     * @param sidechainAccounts a list of sidechain accounts in json format.
     * @param sidechainAmounts a list of sidechain amounts in json format, each amount should correspond to \p sidechainAccounts by order.
     * @param sidechainIndices a list of sidechain indices in json format, each index should correspond to \p sidechainAccounts by order.
     * @param memo input memo attribute for describing.
     * @param remark is used to record message of local wallet.
     * @return　If success return the content of transaction in json format.
     */
    public String CreateDepositTransaction(String fromAddress, String toAddress, long amount, String sidechainAccounts,
            String sidechainAmounts, String sidechainIndexs, String memo, String remark) throws WalletException {
        return nativeCreateDepositTransaction(mMainchainProxy, fromAddress, toAddress, amount, sidechainAccounts,
                    sidechainAmounts, sidechainIndexs, memo, remark);
    }

    public IMainchainSubWallet(long proxy) {
        super(proxy);
        mMainchainProxy = proxy;
    }

    private native String nativeCreateDepositTransaction(long proxy, String fromAddress, String toAddress, long amount
            , String sidechainAccounts, String sidechainAmounts, String sidechainIndexs, String memo, String remark);
}

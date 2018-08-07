package org.elastos.elastoswallet;

/**
 * ISidechainSubWallet jni
 */
public class ISidechainSubWallet extends ISubWallet {
    private long mSidechainProxy;

    /**
     * Create a withdraw transaction and return the content of transaction in json format. Note that \p amount should greater than sum of \p so that we will leave enough fee for mainchain.
     * @param fromAddress specify which address we want to spend, or just input empty string to let wallet choose UTXOs automatically.
     * @param toAddress specify which address we want to send, in this method to address shall be destroy address of the side chain. Note that destroy address is a special address generate by a program hash that all bytes set to zero.
     * @param amount specify amount we want to send.
     * @param mainchainAccounts a list of mainchain accounts in json format.
     * @param mainchainAmounts a list of mainchain amounts in json format, each amount should correspond to \p mainchainAccounts by order.
     * @param mainchainIndexs a list of mainchain indices in json format, each index should correspond to \p mainchainAccounts by order.
     * @param memo input memo attribute for describing.
     * @param remark is used to record message of local wallet.
     * @return If success return the content of transaction in json format.
     */
    public String CreateWithdrawTransaction(String fromAddress, String toAddress, long amount, String mainchainAccounts,
                String mainchainAmounts, String mainchainIndexs, String memo, String remark) throws WalletException {

        return nativeCreateWithdrawTransaction(mSidechainProxy, fromAddress, toAddress, amount, mainchainAccounts, mainchainAmounts,
                    mainchainIndexs, memo, remark);
    }

    /**
     * Get genesis address of the side chain, the address is a special address will be set to toAddress in CreateDepositTransaction.
     * @return genesis address of the side chain.
     */
    public String GetGenesisAddress() throws WalletException {
        return nativeGetGenesisAddress(mSidechainProxy);
    }

    public ISidechainSubWallet(long proxy) {
        super(proxy);
        mSidechainProxy = proxy;
    }

    private native String nativeCreateWithdrawTransaction(long proxy, String fromAddress, String toAddress, long amount, String mainchainAccounts,
                String mainchainAmounts, String mainchainIndexs, String memo, String remark);

    private native String nativeGetGenesisAddress(long proxy);
}

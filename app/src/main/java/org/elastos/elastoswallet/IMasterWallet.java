
package org.elastos.elastoswallet;

import java.util.ArrayList;

/**
 * IMasterWallet
 */
public class IMasterWallet {
    static public class CHAINID {
        public static String MAIN = "ELA";
        public static String ID = "IdChain";
    }

    private long mMasterProxy;

    /**
     * Get the master wallet id.
     * @return master wallet id.
     */
    public String GetId() {
        return nativeGetId(mMasterProxy);
    }

    /**
     * Get wallet existing sub wallets.
     * @return existing sub wallets by array.
     */
    public ArrayList<ISubWallet> GetAllSubWallets() {
        long[] subWalletProxies = nativeGetAllSubWallets(mMasterProxy);
        ArrayList<ISubWallet> list = new ArrayList<ISubWallet>();
        if (subWalletProxies != null) {
            for (int i = 0; i < subWalletProxies.length; i++) {
                if (i == 0) {
                    list.add(new IMainchainSubWallet(subWalletProxies[i]));
                }
                else {
                    list.add(new IIdChainSubWallet(subWalletProxies[i]));
                }
            }
        }

        return list;
    }

    /**
     * Create a sub wallet by specifying wallet type.
     * @param chainID unique identity of a sub wallet. Chain id should not be empty.
     * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @param singleAddress if true created wallet will have only one address inside, otherwise sub wallet will manager a chain of addresses for security.
     * @param feePerKb specify fee per kb to calculate fee by size of transaction. Fee per key default set to zero so that sub wallet will calculate by default "fee rate".
     * @return If success will return a pointer of sub wallet interface.
     */
    public ISubWallet CreateSubWallet(String chainID, String payPassword, boolean singleAddress, long feePerKb) throws WalletException {
        if ((!CHAINID.MAIN.equals(chainID)) && (!CHAINID.ID.equals(chainID))) {
            throw new WalletException("Not support the other sidechain now.");
        }

        long subProxy = nativeCreateSubWallet(mMasterProxy, chainID, payPassword, singleAddress, feePerKb);
        if (CHAINID.MAIN.equals(chainID)) {
            return new IMainchainSubWallet(subProxy);
        }
        else if (CHAINID.ID.equals(chainID)) {
            return new IIdChainSubWallet(subProxy);
        }

        throw new WalletException("Not support the other sidechain now..");
    }

    /**
     * Recover a sub wallet from scratch.
     * @param chainID unique identity of a sub wallet. Chain id should not be empty.
     * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @param singleAddress singleAddress if true created wallet will have only one address inside, otherwise sub wallet will manager a chain of addresses for security.
     * @param limitGap specify the max gap length for recover addresses and transactions.
     * @param feePerKb specify fee per kb to calculate fee by size of transaction. Fee per key default set to zero so that sub wallet will calculate by default "fee rate".
     * @return If success will return a pointer of sub wallet interface.
     */
    public ISubWallet RecoverSubWallet(String chainID, String payPassword, boolean singleAddress, int limitGap, long feePerKb) throws WalletException {
        long subProxy = nativeRecoverSubWallet(mMasterProxy, chainID, payPassword, singleAddress, limitGap, feePerKb);
        if (CHAINID.MAIN.equals(chainID)) {
            return new IMainchainSubWallet(subProxy);
        }
        else if (CHAINID.ID.equals(chainID)) {
            return new IIdChainSubWallet(subProxy);
        }
        throw new WalletException("Not support the other sidechain now..");
    }

    /**
     * Destroy a sub wallet created by the master wallet.
     * @param wallet sub wallet object, should created by the master wallet.
     */
    public void DestroyWallet(ISubWallet wallet)
    {
        nativeDestroyWallet(mMasterProxy, wallet.getProxy());
    }

    /**
     * Get public key of the root private key belongs to the master wallet.
     * @return public key of the root private key
     */
    public String GetPublicKey()
    {
        return nativeGetPublicKey(mMasterProxy);
    }

    /**
     * Sign message through root private key of the master wallet.
     * @param message need to signed, it should not be empty.
     * @param payPassword use to decrypt the root private key temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @return signed data of the message.
     */
    public String Sign(String message, String payPassword) throws WalletException {
        return nativeSign(mMasterProxy, message, payPassword);
    }

    /**
     * Verify signature by public key and raw message. This method can check signatures signed by any private keys not just the root private key of the master wallet.
     * @param publicKey belong to the private key signed the signature.
     * @param message raw data.
     * @param signature signed data by a private key that correspond to the public key.
     * @return the result wrapper by a json.
     */
    public String CheckSign(String publicKey, String message, String signature) throws WalletException {
        return nativeCheckSign(mMasterProxy, publicKey, message, signature);
    }

    /**
     * Verify an address which can be normal, multi-sign, cross chain, or id address.
     * @param address to be verified.
     * @return True if valid, otherwise return false.
     */
    public boolean IsAddressValid(String address) {
        return nativeIsAddressValid(mMasterProxy, address);
    }

    /**
     * Get all chain ids of supported chains.
     * @return a list of chain id.
     */
    public String[] GetSupportedChains() {
        return nativeGetSupportedChains(mMasterProxy);
    }

    /**
     * Change pay password which encrypted private key and other important data in memory.
     * @param oldPassword the old pay password.
     * @param newPassword new pay password.
     */
    public void ChangePassword(String oldPassword, String newPassword) throws WalletException {
        nativeChangePassword(mMasterProxy, oldPassword, newPassword);
    }

    public IMasterWallet(long proxy) {
        mMasterProxy = proxy;
    }

    public long GetProxy() {
        return mMasterProxy;
    }

    private native String nativeGetId(long masterProxy);
    private native long[] nativeGetAllSubWallets(long masterProxy);
    private native long nativeCreateSubWallet(long masterProxy, String chainID, String payPassword, boolean singleAddress, long feePerKb);
    private native long nativeRecoverSubWallet(long masterProxy, String chainID, String payPassword, boolean singleAddress, int limitGap, long feePerKb);
    private native String nativeGetPublicKey(long masterProxy);
    private native void nativeDestroyWallet(long masterProxy, long subWalletProxy);
    private native String nativeSign(long masterProxy, String message, String payPassword);
    private native String nativeCheckSign(long masterProxy, String publicKey, String message, String signature);
    private native boolean nativeIsAddressValid(long masterProxy, String address);
    private native String[] nativeGetSupportedChains(long masterProxy);
    private native void nativeChangePassword(long proxy, String oldPassword, String newPassword);
}

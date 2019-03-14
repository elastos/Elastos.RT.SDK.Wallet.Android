
package org.elastos.spvcore;

/**
 * ISubWallet
 */
public class ISubWallet {
    private long mSubProxy;

	/**
	 * Get the sub wallet chain id.
	 * @return sub wallet chain id.
	 */
    public String GetChainId() throws WalletException {
        return nativeGetChainId(mSubProxy);
    }

	/**
	 * Here is a example of hd account wallet basic info:
	 * {
	 * 	"Type": "Normal" //Type can be Normal, Mainchain, Sidechain and Idchain
	 * 	"Account":
	 * 		{
	 * 			"Type": "HD Account"
	 * 			"Details":
	 * 				{
	 * 					"CoinIndex": 1
	 * 				}
	 * 		}
	 * }
	 *
	 * and an example of multi-sign account wallet basic info:
	 * {
	 * 	"Type": "Mainchain" //Type can be Normal, Mainchain, Sidechain and Idchain
	 * 	"Account":
	 * 		{
	 * 			"Type": "Multi-Sign Account"
	 * 		}
	 * }
	 * @return basic information of current master wallet.
	 */
	public String GetBasicInfo() throws WalletException {
		return nativeGetBasicInfo(mSubProxy);
	}

	/**
	 * Get balances of all addresses in json format.
	 * @return balances of all addresses in json format.
	 */
    public String GetBalanceInfo() throws WalletException {
        return nativeGetBalanceInfo(mSubProxy);
    }

    public long GetBalance(int balanceType) throws WalletException {
        return nativeGetBalance(mSubProxy, balanceType);
    }

	/**
	 * Create a new address or return existing unused address. Note that if create the sub wallet by setting the singleAddress to true, will always return the single address.
	 * @return a new address or existing unused address.
	 */
    public String CreateAddress() throws WalletException {
        return nativeCreateAddress(mSubProxy);
    }

	/**
	 * Get all created addresses in json format. The parameters of start and count are used for purpose of paging.
	 * @param start specify start index of all addresses list.
	 * @param count specify count of addresses we need.
	 * @return addresses in json format.
	 */
    public String GetAllAddress(int start, int count) throws WalletException {
        return nativeGetAllAddress(mSubProxy, start, count);
    }

    public long GetBalanceWithAddress(String address, int balanceType) throws WalletException {
        return nativeGetBalanceWithAddress(mSubProxy, address, balanceType);
    }

	/**
	 * Add a sub wallet callback object listened to current sub wallet.
	 * @param subCallback is a callback who want to listen events of current sub wallet.
	 */
    public void AddCallback(ISubWalletCallback subCallback) throws WalletException {
        nativeAddCallback(mSubProxy, subCallback);
    }

	/**
	 * Remove a sub wallet callback object listened to current sub wallet.
	 */
    public void RemoveCallback() throws WalletException {
        nativeRemoveCallback(mSubProxy);
    }

    public String CreateTransaction(String fromAddress, String toAddress, long amount, String memo, String remark, boolean useVotedUTXO) throws WalletException {
        return nativeCreateTransaction(mSubProxy, fromAddress, toAddress, amount, memo, remark, useVotedUTXO);
    }

    public String CreateMultiSignTransaction(String fromAddress, String toAddress, long amount, String memo, String remark, boolean useVotedUTXO) throws WalletException {
        return nativeCreateMultiSignTransaction(mSubProxy, fromAddress, toAddress, amount, memo, remark, useVotedUTXO);
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

	public String UpdateTransactionFee(String rawTransaction, long fee, String fromAddress) throws WalletException {
		return nativeUpdateTransactionFee(mSubProxy, rawTransaction, fee, fromAddress);
	}

	/**
	 * Sign a transaction or append sign to a multi-sign transaction and return the content of transaction in json format.
	 * @param rawTransaction content of transaction in json format.
	 * @param payPassword use to decrypt the root private key temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @return If success return the content of transaction in json format.
	 */
	public String SignTransaction(String rawTransaction, String payPassword) throws WalletException {
		return nativeSignTransaction(mSubProxy, rawTransaction, payPassword);
	}

	/**
	 * Get signers already signed specified transaction.
	 * @param rawTransaction a multi-sign transaction to find signed signers.
	 * @return Signed signers in json format. An example of result will be displayed as follows:
	 * {
	 * 	[
	 * 		"03b73a64f50c142c1f08710e04b928553508c3028e045dfdfdc5489434df13275e",
	 * 		"02f925e82f4482a9aa853a35203ab8965439c9db6aee8ef1783d2e1a491c28a482"
	 * 	]
	 * }
	 */
	public String GetTransactionSignedSigners(String rawTransaction) throws WalletException {
		return nativeGetTransactionSignedSigners(mSubProxy, rawTransaction);
	}

	/**
	 * Send a transaction by p2p network.
	 * @param rawTransaction content of transaction in json format.
	 * @return Sent result in json format.
	 */
	public String PublishTransaction(String rawTransaction) throws WalletException {
		return nativePublishTransaction(mSubProxy, rawTransaction);
	}

	/**
	 * Get all qualified transactions sorted by descent (newest first).
	 * @param start specify start index of all transactions list.
	 * @param count specify count of transactions we need.
	 * @param addressOrTxId filter word which can be an address or a transaction id, if empty all transactions shall be qualified.
	 * @return All qualified transactions in json format.
	 */
    public String GetAllTransaction(int start, int count, String addressOrTxId) throws WalletException {
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

    public boolean CheckSign(String publicKey, String message, String signature) throws WalletException {
        return nativeCheckSign(mSubProxy, publicKey, message, signature);
    }

	/**
	 * Get root public key of current sub wallet.
	 * @return root public key with hex string format.
	 */
	public String GetPublicKey() throws WalletException {
		return nativeGetPublicKey(mSubProxy);
	}

    public ISubWallet(long proxy) {
        mSubProxy = proxy;
    }

    protected long getProxy() {
        return mSubProxy;
    }

    private native String nativeGetChainId(long subProxy);
    private native String nativeGetBasicInfo(long subProxy);
    private native String nativeGetBalanceInfo(long subProxy);
    private native long nativeGetBalance(long subProxy, int balanceType);
    private native String nativeCreateAddress(long subProxy);
    private native String nativeGetAllAddress(long subProxy, int start, int count);
    private native long nativeGetBalanceWithAddress(long subProxy, String address, int balanceType);
    private native void nativeAddCallback(long subProxy, ISubWalletCallback subCallback);
    private native void nativeRemoveCallback(long subProxy);
    private native String nativeCreateTransaction(long subProxy, String fromAddress, String toAddress, long amount, String memo, String remark, boolean useVotedUTXO);
    private native String nativeCreateMultiSignTransaction(long subProxy, String fromAddress, String toAddress, long amount, String memo, String remark, boolean useVotedUTXO);
    private native long nativeCalculateTransactionFee(long subProxy, String rawTransaction, long feePerKb);
	private native String nativeUpdateTransactionFee(long subProxy, String rawTransaction, long fee, String fromAddress);
	private native String nativeSignTransaction(long subProxy, String rawTransaction, String payPassword);
	private native String nativeGetTransactionSignedSigners(long subProxy, String rawTransaction);
	private native String nativePublishTransaction(long subProxy, String rawTransaction);
    private native String nativeGetAllTransaction(long subProxy, int start, int count, String addressOrTxId);
    private native String nativeSign(long subProxy, String message, String payPassword);
    private native boolean nativeCheckSign(long subProxy, String publicKey, String message, String signature);
	private native String nativeGetPublicKey(long jSubProxy);
}

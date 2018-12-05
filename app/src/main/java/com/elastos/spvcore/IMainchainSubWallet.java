package com.elastos.spvcore;

/**
 * IMainchainSubWallet jni
 */
public class IMainchainSubWallet extends ISubWallet {
	private long mMainchainProxy;

	public IMainchainSubWallet(long proxy) {
		super(proxy);
		mMainchainProxy = proxy;
	}



	public String CreateDepositTransaction(String fromAddress, String toAddress, long amount, String sidechainAccounts,
			String sidechainAmounts, String sidechainIndexs, String memo, String remark) throws WalletException {
		return nativeCreateDepositTransaction(mMainchainProxy, fromAddress, toAddress, amount, sidechainAccounts,
				sidechainAmounts, sidechainIndexs, memo, remark);
	}

	public String CreateRegisterProducerTransaction(String fromAddress, String toAddress,
			String publicKey, String nickName, String url, long location) throws WalletException {
		return nativeCreateRegisterProducerTransaction(mMainchainProxy, fromAddress, toAddress, publicKey, nickName, url, location);
	}

	public String CreateCancelProducerTransaction(String publicKey) throws WalletException {
		return nativeCreateCancelProducerTransaction(mMainchainProxy, publicKey);
	}

	public String CreateVoteProducerTransaction(long stake, String publicKeys) throws WalletException {
		return nativeCreateVoteProducerTransaction(mMainchainProxy, stake, publicKeys);
	}



	private native String nativeCreateDepositTransaction(long proxy, String fromAddress, String toAddress, long amount,
			String sidechainAccounts, String sidechainAmounts, String sidechainIndexs, String memo, String remark);

	private native String nativeCreateRegisterProducerTransaction(long proxy, String fromAddress, String toAddress,
			String publicKey, String nickName, String url, long location);

	private native String nativeCreateCancelProducerTransaction(long proxy, String publicKey);

	private native String nativeCreateVoteProducerTransaction(long proxy, long stake, String publicKeys);
}

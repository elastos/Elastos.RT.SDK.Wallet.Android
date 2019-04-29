
package com.elastos.spvcore;

import java.util.ArrayList;
import android.util.Log;

public class MasterWalletManager {
	private String TAG = "MasterWalletManager";
	private long mManagerProxy = 0;
	private String mRootPath = null;

	public MasterWalletManager(String rootPath) {
		mRootPath = rootPath;
		mManagerProxy = nativeInitMasterWalletManager(mRootPath);
	}

	public void DisposeNative() {
		Log.i(TAG, "DisposeNative");
		nativeDisposeNative(mManagerProxy);
	}

	public IMasterWallet CreateMasterWallet(String masterWalletId, String mnemonic,
			String phrasePassword, String payPassword, boolean singleAddress) throws WalletException {

		long masterProxy = nativeCreateMasterWallet(mManagerProxy, masterWalletId, mnemonic,
				phrasePassword, payPassword, singleAddress);

		if (masterProxy == 0) {
			Log.e(TAG, "Create master wallet fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	public ArrayList<IMasterWallet> GetAllMasterWallets() throws WalletException {

		ArrayList<IMasterWallet> list = new ArrayList<IMasterWallet>();
		long[] masterWalletProxies = nativeGetAllMasterWallets(mManagerProxy);

		for (int i = 0; i < masterWalletProxies.length; i++) {
			list.add(new IMasterWallet(masterWalletProxies[i]));
		}

		return list;
	}

	public String[] GetAllMasterWalletIds() throws WalletException {
		return nativeGetAllMasterWalletIds(mManagerProxy);
	}

	public IMasterWallet GetWallet(String masterWalletId) throws WalletException {
		long masterWalletProxy = nativeGetWallet(mManagerProxy, masterWalletId);

		if (masterWalletProxy == 0) {
			Log.e(TAG, "Get master wallet fail");
			return null;
		}

		return new IMasterWallet(masterWalletProxy);
	}

	public void DestroyWallet(String masterWalletId) throws WalletException {
		nativeDestroyWallet(mManagerProxy, masterWalletId);
	}

	public IMasterWallet ImportWalletWithOldKeystore(String masterWalletId, String keystoreContent,
			String backupPassWord, String payPassWord, String phrasePassword) throws WalletException {

		long masterProxy = nativeImportWalletWithOldKeystore(mManagerProxy, masterWalletId,
				keystoreContent, backupPassWord, payPassWord, phrasePassword);

		if (masterProxy == 0) {
			Log.e(TAG, "Import master wallet with key store fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	public IMasterWallet ImportWalletWithKeystore(String masterWalletId, String keystoreContent,
			String backupPassWord, String payPassWord) throws WalletException {

		long masterProxy = nativeImportWalletWithKeystore(mManagerProxy, masterWalletId,
				keystoreContent, backupPassWord, payPassWord);

		if (masterProxy == 0) {
			Log.e(TAG, "Import master wallet with key store fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	public IMasterWallet ImportWalletWithMnemonic(String masterWalletId, String mnemonic,
			String phrasePassword, String payPassWord, boolean singleAddress) throws WalletException {

		long masterProxy = nativeImportWalletWithMnemonic(mManagerProxy, masterWalletId,
				mnemonic, phrasePassword, payPassWord, singleAddress);

		if (masterProxy == 0) {
			Log.e(TAG, "Import master wallet with mnemonic fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	public String ExportWalletWithKeystore(IMasterWallet masterWallet,
			String backupPassWord, String payPassword) throws WalletException {

		return nativeExportWalletWithKeystore(mManagerProxy, masterWallet, backupPassWord, payPassword);
	}

	public String ExportWalletWithMnemonic(IMasterWallet masterWallet,
			String payPassWord) throws WalletException {

		return nativeExportWalletWithMnemonic(mManagerProxy, masterWallet, payPassWord);
	}

	public String GenerateMnemonic(String language) throws WalletException {
		return nativeGenerateMnemonic(mManagerProxy, language);
	}

	public String GetMultiSignPubKeyWithMnemonic(String phrase, String phrasePassword) throws WalletException {
		return nativeGetMultiSignPubKeyWithMnemonic(mManagerProxy, phrase, phrasePassword);
	}

	public String GetMultiSignPubKeyWithPrivKey(String privKey) throws WalletException {
		return nativeGetMultiSignPubKeyWithPrivKey(mManagerProxy, privKey);
	}

	public void SaveConfigs() {
		nativeSaveConfigs(mManagerProxy);
	}

	public IMasterWallet CreateMultiSignMasterWallet(String masterWallet,
			String coSigners, int requiredSignCount) throws WalletException {

		long masterProxy = nativeCreateMultiSignMasterWallet(mManagerProxy, masterWallet,
				coSigners, requiredSignCount);

		if (masterProxy == 0) {
			Log.e(TAG, "Create multi sign master wallet fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	public IMasterWallet CreateMultiSignMasterWallet(String masterWallet, String privKey, String payPassword,
			String coSigners, int requiredSignCount) throws WalletException {

		long masterProxy = nativeCreateMultiSignMasterWalletWithPrivKey(mManagerProxy, masterWallet,
				privKey, payPassword, coSigners, requiredSignCount);

		if (masterProxy == 0) {
			Log.e(TAG, "Create multi sign master wallet with private key fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	public IMasterWallet CreateMultiSignMasterWallet(String masterWalletId, String mnemonic, String phrasePassword,
			String payPassword, String coSigners, int requiredSignCount) throws WalletException {

		long masterProxy = nativeCreateMultiSignMasterWalletWithMnemonic(mManagerProxy, masterWalletId, mnemonic,
				phrasePassword, payPassword, coSigners, requiredSignCount);

		if (masterProxy == 0) {
			Log.e(TAG, "Create multi sign master wallet with mnemonic fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	public String EncodeTransactionToString(String txJson) {
		return nativeEncodeTransactionToString(mManagerProxy, txJson);
	}

	public String DecodeTransactionFromString(String cipher) {
		return nativeDecodeTransactionFromString(mManagerProxy, cipher);
	}

	public String GetVersion() {
		return nativeGetVersion(mManagerProxy);
	}

	private native void nativeSaveConfigs(long proxy);

	private native String nativeGenerateMnemonic(long proxy, String language);

	private native String nativeGetMultiSignPubKeyWithMnemonic(long proxy, String phrase, String phrasePassword);

	private native String nativeGetMultiSignPubKeyWithPrivKey(long proxy, String privKey);

	private native long nativeCreateMasterWallet(long proxy, String masterWalletId, String mnemonic,
			String phrasePassword, String payPassword, boolean singleAddress);

	private native long nativeCreateMultiSignMasterWallet(long proxy, String masterWalletId,
			String coSigners, int requiredSignCount);

	private native long nativeCreateMultiSignMasterWalletWithPrivKey(long proxy, String masterWalletId, String privKey,
			String payPassword, String coSigners, int requiredSignCount);

	private native long nativeCreateMultiSignMasterWalletWithMnemonic(long proxy, String masterWalletId, String mnemonic,
			String phrasePassword, String payPassword, String coSigners, int requiredSignCount);

	private native long nativeImportWalletWithOldKeystore(long proxy, String masterWalletId,
			String keystoreContent, String backupPassword, String payPassword, String phrasePassword);

	private native long nativeImportWalletWithKeystore(long proxy, String masterWalletId,
			String keystoreContent, String backupPassWord ,String payPassWord);

	private native long nativeImportWalletWithMnemonic(long proxy, String masterWalletId, String mnemonic,
			String phrasePassword, String payPassWord, boolean singleAddress);

	private native String nativeExportWalletWithKeystore(long proxy, IMasterWallet masterWallet,
			String backupPassWord,String payPassword);

	private native String nativeExportWalletWithMnemonic(long proxy, IMasterWallet masterWallet, String backupPassWord);

	private native void nativeDestroyWallet(long proxy, String masterWalletId);

	private native long[] nativeGetAllMasterWallets(long proxy);

	private native String[] nativeGetAllMasterWalletIds(long proxy);

	private native long nativeGetWallet(long proxy, String masterWalletId);

	private native String nativeEncodeTransactionToString(long proxy, String txJson);

	private native String nativeDecodeTransactionFromString(long proxy, String cipher);

	private native long nativeInitMasterWalletManager(String rootPath);

	private native void nativeDisposeNative(long proxy);

	private native String nativeGetVersion(long proxy);
}

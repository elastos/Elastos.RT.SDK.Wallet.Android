
package org.elastos.spvcore;

import java.util.ArrayList;
import android.util.Log;

public class MasterWalletManager {
	private String TAG = "MasterWalletManager";
	private long mManagerProxy = 0;
	private String mRootPath = null;

	/**
	 * Constructor
	 * @param rootPath specify directory for all config files, including mnemonic config files and peer connection config files. Root should not be empty, otherwise will throw invalid argument exception.
	 */
	public MasterWalletManager(String rootPath) {
		mRootPath = rootPath;
		mManagerProxy = nativeInitMasterWalletManager(mRootPath);
	}

	public void DisposeNative() {
		Log.i(TAG, "DisposeNative");
		nativeDisposeNative(mManagerProxy);
	}

	/**
	 * Create a new master wallet by mnemonic and phrase password, or return existing master wallet if current master wallet manager has the master wallet id.
	 * @param masterWalletId is the unique identification of a master wallet object.
	 * @param mnemonic use to generate seed which deriving the master private key and chain code.
	 * @param phrasePassword combine with random seed to generate root key and chain code. Phrase password can be empty or between 8 and 128, otherwise will throw invalid argument exception.
	 * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @param singleAddress singleAddress if true created wallet will have only one address inside, otherwise sub wallet will manager a chain of addresses for security.
	 * @return If success will return a pointer of master wallet interface.
	 */
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

	/**
	 * Get manager existing master wallets.
	 * @return existing master wallet array.
	 */
	public ArrayList<IMasterWallet> GetAllMasterWallets() throws WalletException {

		ArrayList<IMasterWallet> list = new ArrayList<IMasterWallet>();
		long[] masterWalletProxies = nativeGetAllMasterWallets(mManagerProxy);

		for (int i = 0; i < masterWalletProxies.length; i++) {
			list.add(new IMasterWallet(masterWalletProxies[i]));
		}

		return list;
	}

	/**
	 * Get manager available master wallet ids.
	 * @return available ids array.
	 */
	public String[] GetAllMasterWalletIds() throws WalletException {
		return nativeGetAllMasterWalletIds(mManagerProxy);
	}

	/**
	 * Get a master wallet object by id.
	 * @param masterWalletId master wallet id.
	 * @return master wallet object.
	 */
	public IMasterWallet GetWallet(String masterWalletId) throws WalletException {
		long masterWalletProxy = nativeGetWallet(mManagerProxy, masterWalletId);

		if (masterWalletProxy == 0) {
			Log.e(TAG, "Get master wallet fail");
			return null;
		}

		return new IMasterWallet(masterWalletProxy);
	}

	/**
	 * Destroy a master wallet.
	 * @param masterWalletId A pointer of master wallet interface create or imported by wallet factory object.
	 */
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

	/**
	 * Import master wallet by key store file.
	 * @param masterWalletId is the unique identification of a master wallet object.
	 * @param keystoreContent specify key store content in json format.
	 * @param backupPassword use to encrypt key store file. Backup password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @return If success will return a pointer of master wallet interface.
	 */
	public IMasterWallet ImportWalletWithKeystore(String masterWalletId, String keystoreContent,
			String backupPassword, String payPassword) throws WalletException {

		long masterProxy = nativeImportWalletWithKeystore(mManagerProxy, masterWalletId,
				keystoreContent, backupPassword, payPassword);

		if (masterProxy == 0) {
			Log.e(TAG, "Import master wallet with key store fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	/**
	 * Import master wallet by mnemonic.
	 * @param masterWalletId is the unique identification of a master wallet object.
	 * @param mnemonic for importing the master wallet.
	 * @param phrasePassword combine with mnemonic to generate root key and chain code. Phrase password can be empty or between 8 and 128, otherwise will throw invalid argument exception.
	 * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @param singleAddress singleAddress if true created wallet will have only one address inside, otherwise sub wallet will manager a chain of addresses for security.
	 * @return If success will return a pointer of master wallet interface.
	 */
	public IMasterWallet ImportWalletWithMnemonic(String masterWalletId, String mnemonic,
			String phrasePassword, String payPassword, boolean singleAddress) throws WalletException {

		long masterProxy = nativeImportWalletWithMnemonic(mManagerProxy, masterWalletId,
				mnemonic, phrasePassword, payPassword, singleAddress);

		if (masterProxy == 0) {
			Log.e(TAG, "Import master wallet with mnemonic fail");
			return null;
		}

		return new IMasterWallet(masterProxy);
	}

	/**
	 * Export key store content of the master wallet in json format.
	 * @param masterWallet A pointer of master wallet interface create or imported by wallet factory object.
	 * @param backupPassword use to decrypt key store file. Backup password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @param payPassword use to decrypt and generate mnemonic temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @return If success will return key store content in json format.
	 */
	public String ExportWalletWithKeystore(IMasterWallet masterWallet,
			String backupPassword, String payPassword) throws WalletException {

		return nativeExportWalletWithKeystore(mManagerProxy, masterWallet, backupPassword, payPassword);
	}

	/**
	 * Export mnemonic of the master wallet.
	 * @param masterWallet A pointer of master wallet interface create or imported by wallet factory object.
	 * @param payPassword use to decrypt and generate mnemonic temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @return If success will return the mnemonic of master wallet.
	 */
	public String ExportWalletWithMnemonic(IMasterWallet masterWallet,
			String payPassword) throws WalletException {

		return nativeExportWalletWithMnemonic(mManagerProxy, masterWallet, payPassword);
	}

	/**
	 * Generate a mnemonic by random 128 entropy. We support English, Chinese, French, Italian, Japanese, and
	 * 	Spanish 6 types of mnemonic currently.
	 * @param language specify mnemonic language.
	 * @return a random mnemonic.
	 */
	public String GenerateMnemonic(String language) throws WalletException {
		return nativeGenerateMnemonic(mManagerProxy, language);
	}

	/**
	 * Get public key for creating multi sign wallet with phrase.
	 * @param phrase is something like mnemonic generated from GenerateMnemonic().
	 * @param phrasePassword combine with random seed to generate root key and chain code. Phrase password can be empty or between 8 and 128, otherwise will throw invalid argument exception.
	 * @return public key as expected.
	 */
	public String GetMultiSignPubKeyWithMnemonic(String phrase, String phrasePassword) throws WalletException {
		return nativeGetMultiSignPubKeyWithMnemonic(mManagerProxy, phrase, phrasePassword);
	}

	/**
	 * Get public key for creating multi sign wallet with private key.
	 * @param privKey private key to do the sign job of related multi-sign accounts.
	 * @return public key as expected.
	 */
	public String GetMultiSignPubKeyWithPrivKey(String privKey) throws WalletException {
		return nativeGetMultiSignPubKeyWithPrivKey(mManagerProxy, privKey);
	}

	/**
	 * Save local storage specifically. Note that the storage saving will be called automatically in destructor.
	 */
	public void SaveConfigs() {
		nativeSaveConfigs(mManagerProxy);
	}

	/**
	 * Create a multi-sign master wallet by mnemonic phrase password and related co-signers, or return existing master wallet if current master wallet manager has the master wallet id.
	 * @param masterWallet is the unique identification of a master wallet object.
	 * @param coSigners is an array of signers' public key
	 * @param requiredSignCount specify minimum count to accomplish related transactions.
	 * @return If success will return a pointer of master wallet interface.
	 */
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

	/**
	 * Create a multi-sign master wallet by mnemonic phrase password and related co-signers, or return existing master wallet if current master wallet manager has the master wallet id.
	 * @param masterWallet is the unique identification of a master wallet object.
	 * @param privKey private key to do the sign job of related multi-sign accounts.
	 * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @param coSigners is an array of signers' public key
	 * @param requiredSignCount specify minimum count to accomplish related transactions.
	 * @return If success will return a pointer of master wallet interface.
	 */
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

	/**
	 * Create a multi-sign master wallet by private key and related co-signers, or return existing master wallet if current master wallet manager has the master wallet id.
	 * @param masterWalletId is the unique identification of a master wallet object.
	 * @param mnemonic use to generate seed which deriving the master private key and chain code.
	 * @param phrasePassword combine with random seed to generate root key and chain code. Phrase password can be empty or between 8 and 128, otherwise will throw invalid argument exception.
	 * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
	 * @param coSigners is an array of signers' public key.
	 * @param requiredSignCount specify minimum count to accomplish related transactions.
	 * @return If success will return a pointer of master wallet interface.
	 */
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

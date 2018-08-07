
package org.elastos.elastoswallet;

import java.util.ArrayList;

/**
 * MasterWalletManager
 */
public class MasterWalletManager {
    static {
        System.loadLibrary("spvsdk");
        System.loadLibrary("idchain");
        System.loadLibrary("Elastos.Runtime");
        System.loadLibrary("Elastos.CoreLibrary");
        System.loadLibrary("Elastos.Wallet");
        System.loadLibrary("Elastos.DID");
        System.loadLibrary("elastoswallet");
    }

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

    /**
     * Create a new master wallet by mnemonic and phrase password, or return existing master wallet if current master wallet manager has the master wallet id.
     * @param masterWalletId is the unique identification of a master wallet object.
     * @param mnemonic use to generate seed which deriving the master private key and chain code.
     * @param phrasePassword combine with random seed to generate root key and chain code. Phrase password can be empty or between 8 and 128, otherwise will throw invalid argument exception.
     * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @param language specify language of mnemonic, value of language should correspond to the language of \p mnemonic.
     * @return If success will return a pointer of master wallet interface.
     */
    public IMasterWallet CreateMasterWallet(String masterWalletId, String mnemonic, String phrasePassword, String payPassword
            , String language) throws WalletException
    {
        long masterProxy = nativeCreateMasterWallet(mManagerProxy, masterWalletId, mnemonic, phrasePassword, payPassword, language);
        return new IMasterWallet(masterProxy);
    }

    /**
     * Get manager existing master wallets.
     * @return existing master wallet array.
     */
    public ArrayList<IMasterWallet> GetAllMasterWallets() throws WalletException {
        long[] masterWalletProxies = nativeGetAllMasterWallets(mManagerProxy);
        if (masterWalletProxies != null) {
            ArrayList<IMasterWallet> list = new ArrayList<IMasterWallet>();
            for (int i = 0; i < masterWalletProxies.length; i++) {
                list.add(new IMasterWallet(masterWalletProxies[i]));
            }
            return list;
        }
        return null;
    }

    /**
     * Destroy a master wallet.
     * @param masterWallet A pointer of master wallet interface create or imported by wallet factory object.
     */
    public void DestroyWallet(String masterWalletId) throws WalletException {
        nativeDestroyWallet(mManagerProxy, masterWalletId);
    }

    /**
     * Import master wallet by key store file.
     * @param masterWalletId is the unique identification of a master wallet object.
     * @param keystoreContent specify key store content in json format.
     * @param backupPassword use to encrypt key store file. Backup password should between 8 and 128, otherwise will throw invalid argument exception.
     * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @param phrasePassword combine with random seed to generate root key and chain code. Phrase password can be empty or between 8 and 128, otherwise will throw invalid argument exception.
     * @return If success will return a pointer of master wallet interface.
     */
    public IMasterWallet ImportWalletWithKeystore(String masterWalletId, String keystoreContent,String backupPassWord
                    ,String payPassWord, String phrasePassword) throws WalletException {
        long masterProxy = nativeImportWalletWithKeystore(mManagerProxy, masterWalletId, keystoreContent, backupPassWord, payPassWord, phrasePassword);
        return new IMasterWallet(masterProxy);
    }

    /**
     * Import master wallet by mnemonic.
     * @param masterWalletId is the unique identification of a master wallet object.
     * @param mnemonic for importing the master wallet.
     * @param phrasePassword combine with mnemonic to generate root key and chain code. Phrase password can be empty or between 8 and 128, otherwise will throw invalid argument exception.
     * @param payPassword use to encrypt important things(such as private key) in memory. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @param language specify language of mnemonic. Language should not be empty, and exit corresponding language config file under the root path. The config begin with fixed prefix "mnemonic_" and end with ".txt" extension, for example mnemonic of Chinese config will be "mnemonic_chinese.txt".
     * @return If success will return a pointer of master wallet interface.
     */
    public IMasterWallet ImportWalletWithMnemonic(String masterWalletId, String mnemonic, String phrasePassword
                    ,String payPassWord, String language) throws WalletException {
        long masterProxy = nativeImportWalletWithMnemonic(mManagerProxy, masterWalletId, mnemonic, phrasePassword, payPassWord, language);
        return new IMasterWallet(masterProxy);
    }

    /**
     * Export key store content of the master wallet in json format.
     * @param masterWallet A pointer of master wallet interface create or imported by wallet factory object.
     * @param backupPassword use to decrypt key store file. Backup password should between 8 and 128, otherwise will throw invalid argument exception.
     * @param payPassword use to decrypt and generate mnemonic temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @return If success will return key store content in json format.
     */
    public String ExportWalletWithKeystore(IMasterWallet masterWallet, String backupPassWord, String payPassword) throws WalletException {
        return nativeExportWalletWithKeystore(mManagerProxy, masterWallet, backupPassWord, payPassword);
    }

    /**
     * Export mnemonic of the master wallet.
     * @param masterWallet A pointer of master wallet interface create or imported by wallet factory object.
     * @param payPassword use to decrypt and generate mnemonic temporarily. Pay password should between 8 and 128, otherwise will throw invalid argument exception.
     * @return If success will return the mnemonic of master wallet.
     */
    public String ExportWalletWithMnemonic(IMasterWallet masterWallet,String backupPassWord) throws WalletException {
        return nativeExportWalletWithMnemonic(mManagerProxy, masterWallet, backupPassWord);
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
     * Save local storage specifically. Note that the storage saving will be called automatically in destructor.
     */
    public void SaveConfigs() {
        nativeSaveConfigs(mManagerProxy);
    }

    public void finalize() {
        nativeDisposeNative(mManagerProxy);
    }

    private native long nativeInitMasterWalletManager(String rootPath);

    private native long nativeCreateMasterWallet(long proxy, String masterWalletId, String mnemonic, String phrasePassword
                    , String payPassword, String language);

    private native long nativeImportWalletWithKeystore(long proxy, String masterWalletId,
                    String keystoreContent,String backupPassWord ,String payPassWord, String phrasePassword);

    private native long nativeImportWalletWithMnemonic(long proxy, String masterWalletId, String mnemonic,
                    String phrasePassword,String payPassWord,String language);

    private native String nativeExportWalletWithKeystore(long proxy, IMasterWallet masterWallet,
                    String backupPassWord,String payPassword);

    private native String nativeExportWalletWithMnemonic(long proxy, IMasterWallet masterWallet,String backupPassWord);
    private native void nativeDestroyWallet(long proxy, String masterWalletId);
    private native void nativeDisposeNative(long proxy);
    private native long[] nativeGetAllMasterWallets(long proxy);
    private native String nativeGenerateMnemonic(long proxy, String language);
    private native void nativeSaveConfigs(long proxy);
}

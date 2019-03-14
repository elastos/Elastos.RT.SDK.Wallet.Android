
package org.elastos.spvcore;

import java.util.ArrayList;
import android.util.Log;

/**
 * IMasterWallet
 */
public class IMasterWallet {
    static public class CHAINID {
        public static String MAIN = "ELA";
        public static String ID = "IdChain";
    }
	static public String TAG = "IMasterWallet";

    private long mMasterProxy;

    public String GetId() {
        return nativeGetId(mMasterProxy);
    }

	public String GetBasicInfo() {
		return nativeGetBasicInfo(mMasterProxy);
	}

	public ArrayList<ISubWallet> GetAllSubWallets() {
		Object[] allSubWallets = nativeGetAllSubWallets(mMasterProxy);

		ArrayList<ISubWallet> list = new ArrayList<ISubWallet>();
		for (int i = 0; i < allSubWallets.length; i++) {
			list.add((ISubWallet)allSubWallets[i]);
		}

		return list;
	}

	public ISubWallet GetSubWallet(String chainID) {
		ArrayList<ISubWallet> subWalletList = GetAllSubWallets();
		for (int i = 0; i < subWalletList.size(); i++) {
			if (chainID.equals(subWalletList.get(i).GetChainId())) {
				return subWalletList.get(i);
			}
		}

		return null;
	}

	public ISubWallet CreateSubWallet(String chainID, long feePerKb) throws WalletException {
		if ((!CHAINID.MAIN.equals(chainID)) && (!CHAINID.ID.equals(chainID))) {
			throw new WalletException("Not support the other sidechain now.");
		}

		long subProxy = nativeCreateSubWallet(mMasterProxy, chainID, feePerKb);
		if (subProxy == 0) {
			Log.e(TAG, "Native create subwallet fail: subProxy is 0");
			throw new WalletException("Native create subwallet fail");
		}

		if (CHAINID.MAIN.equals(chainID)) {
			return new IMainchainSubWallet(subProxy);
		} else if (CHAINID.ID.equals(chainID)) {
			return new IIdChainSubWallet(subProxy);
		}

		Log.e(TAG, "CreateSubWallet error: unsupport chainID = " + chainID);
		throw new WalletException("Not support the other sidechain now");
	}

    public void DestroyWallet(ISubWallet wallet) {
        nativeDestroyWallet(mMasterProxy, wallet.getProxy());
    }

    public String GetPublicKey() {
        return nativeGetPublicKey(mMasterProxy);
    }

    public String Sign(String message, String payPassword) throws WalletException {
        return nativeSign(mMasterProxy, message, payPassword);
    }

    public boolean CheckSign(String publicKey, String message, String signature) throws WalletException {
        return nativeCheckSign(mMasterProxy, publicKey, message, signature);
    }

    public IMasterWallet(long proxy) {
        mMasterProxy = proxy;
    }

    public boolean IsAddressValid(String address) {
        return nativeIsAddressValid(mMasterProxy, address);
    }

    public String[] GetSupportedChains() {
        return nativeGetSupportedChains(mMasterProxy);
    }

    public long GetProxy() {
        return mMasterProxy;
    }

    public void ChangePassword(String oldPassword, String newPassword) throws WalletException {
        nativeChangePassword(mMasterProxy, oldPassword, newPassword);
    }

    private native String nativeGetId(long masterProxy);
	private native String nativeGetBasicInfo(long masterProxy);
    private native Object[] nativeGetAllSubWallets(long masterProxy);
    private native long nativeCreateSubWallet(long masterProxy, String chainID, long feePerKb);
    private native String nativeGetPublicKey(long masterProxy);
    private native void nativeDestroyWallet(long masterProxy, long subWalletProxy);
    private native String nativeSign(long masterProxy, String message, String payPassword);
    private native boolean nativeCheckSign(long masterProxy, String publicKey, String message, String signature);
    private native boolean nativeIsAddressValid(long masterProxy, String address);
    private native String[] nativeGetSupportedChains(long masterProxy);
    private native void nativeChangePassword(long proxy, String oldPassword, String newPassword);
}

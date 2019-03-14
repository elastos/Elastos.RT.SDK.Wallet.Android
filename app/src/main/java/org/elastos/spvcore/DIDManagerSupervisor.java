
package org.elastos.spvcore;


public class DIDManagerSupervisor {
	private long mSuervisor = 0;

	public DIDManagerSupervisor(String rootPath) {
		mSuervisor = nativeNewDIDManagerSupervisor(rootPath);
	}

	public void finalize() {
		nativeDisposeNative(mSuervisor);
	}

	public IDidManager CreateDIDManager(IMasterWallet masterWallet, String rootPath) throws WalletException {
		long DIDManagerProxy = nativeCreateDIDManager(mSuervisor, masterWallet.GetProxy(), rootPath);
		return new IDidManager(DIDManagerProxy);
	}

	public void DestroyDIDManager(IDidManager manager) throws WalletException {
		nativeDestroyDIDManager(mSuervisor, manager.getProxy());
	}

	private native long nativeNewDIDManagerSupervisor(String rootPath);
	private native void nativeDisposeNative(long supervisor);

	private native long nativeCreateDIDManager(long supervisor, long masterWallet, String rootPath);
	private native void nativeDestroyDIDManager(long supervisor, long IDManagerProxy);
}


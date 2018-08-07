
package org.elastos.elastoswallet;


public interface IIdManagerCallback {
    public void OnIdStatusChanged(String id, String path, /*const nlohmann::json*/ String value);
}

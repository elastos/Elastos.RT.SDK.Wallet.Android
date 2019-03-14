package org.elastos.spvcore;

import android.content.Context;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ElastosWalletUtils {
	static {
		System.loadLibrary("spvsdk");
//		System.loadLibrary("idchain");
		System.loadLibrary("elastoswallet");
	}

    public static void InitConfig(Context context, String rootPath) {
        String[] names={"CoinConfig.json",
                "mnemonic_chinese.txt",
                "mnemonic_french.txt",
                "mnemonic_italian.txt",
                "mnemonic_japanese.txt",
                "mnemonic_spanish.txt"};

        for (int i = 0; i < names.length; i++) {
            InputStream is = context.getClass().getClassLoader().getResourceAsStream("assets/config/" + names[i]);
            try {
                OutputStream fosto = new FileOutputStream(rootPath+"/"+names[i]);
                byte bt[] = new byte[1024];
                int c = 0;
                while ((c = is.read(bt)) > 0) {
                    fosto.write(bt, 0, c);
                }
                is.close();
                fosto.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

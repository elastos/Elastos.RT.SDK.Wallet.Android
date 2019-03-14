// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#include "ElaUtils.h"
#include "MasterWalletManager.h"

using namespace Elastos::ElaWallet;

#define  CLASS_MASTERWALLET   "org/elastos/spvcore/IMasterWallet"
#define  FIELD_MASTERWALLET   "mMasterProxy"

#define SIG_nativeGenerateMnemonic "(JLjava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeGenerateMnemonic(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jlanguage)
{
	bool exception = false;
	std::string msgException;

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	const char* language = env->GetStringUTFChars(jlanguage, NULL);
	jstring mnemonic = NULL;

	try {
		std::string str = walletManager->GenerateMnemonic(language);
		mnemonic = env->NewStringUTF(str.c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jlanguage, language);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return mnemonic;
}

#define SIG_nativeGetMultiSignPubKeyWithMnemonic "(JLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeGetMultiSignPubKeyWithMnemonic(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jPhrase,
		jstring jPhrasePassword)
{
	bool exception = false;
	std::string msgException;

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	const char *phrase = env->GetStringUTFChars(jPhrase, NULL);
	const char *phrasePassword = env->GetStringUTFChars(jPhrasePassword, NULL);
	jstring pubkey = NULL;

	try {
		std::string str = walletManager->GetMultiSignPubKey(phrase, phrasePassword);
		pubkey = env->NewStringUTF(str.c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jPhrase, phrase);
	env->ReleaseStringUTFChars(jPhrasePassword, phrasePassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return pubkey;
}

#define SIG_nativeGetMultiSignPubKeyWithPrivKey "(JLjava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeGetMultiSignPubKeyWithPrivKey(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jPrivKey)
{
	bool exception = false;
	std::string msgException;

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	const char *privKey = env->GetStringUTFChars(jPrivKey, NULL);
	jstring pubkey = NULL;

	try {
		std::string str = walletManager->GetMultiSignPubKey(privKey);
		pubkey = env->NewStringUTF(str.c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jPrivKey, privKey);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return pubkey;
}

#define SIG_nativeCreateMasterWallet "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)J"
static jlong JNICALL nativeCreateMasterWallet(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jmasterWalletId,
		jstring jmnemonic,
		jstring jphrasePassword,
		jstring jpayPassword,
		jboolean jSingleAddress)
{
	bool exception = false;
	std::string msgException;

	const char* masterWalletId = env->GetStringUTFChars(jmasterWalletId, NULL);
	const char* mnemonic = env->GetStringUTFChars(jmnemonic, NULL);
	const char* phrasePassword = env->GetStringUTFChars(jphrasePassword, NULL);
	const char* payPassword = env->GetStringUTFChars(jpayPassword, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	IMasterWallet* masterWallet = NULL;

	try {
		masterWallet = walletManager->CreateMasterWallet(masterWalletId, mnemonic,
				phrasePassword, payPassword, jSingleAddress);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jmasterWalletId, masterWalletId);
	env->ReleaseStringUTFChars(jmnemonic, mnemonic);
	env->ReleaseStringUTFChars(jphrasePassword, phrasePassword);
	env->ReleaseStringUTFChars(jpayPassword, payPassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeCreateMultiSignMasterWallet "(JLjava/lang/String;Ljava/lang/String;I)J"
static jlong JNICALL nativeCreateMultiSignMasterWallet(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jMasterWalletId,
		jstring jCoSigners,
		jint jRequiredSignCount)
{
	bool exception = false;
	std::string msgException;

	const char* masterWalletId = env->GetStringUTFChars(jMasterWalletId, NULL);
	const char* coSigners = env->GetStringUTFChars(jCoSigners, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	IMasterWallet* masterWallet = NULL;
	nlohmann::json coSignersJson = nlohmann::json::parse(coSigners);

	try {
		masterWallet = walletManager->CreateMultiSignMasterWallet(masterWalletId, coSignersJson, jRequiredSignCount);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jMasterWalletId, masterWalletId);
	env->ReleaseStringUTFChars(jCoSigners, coSigners);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeCreateMultiSignMasterWalletWithPrivKey "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)J"
static jlong JNICALL nativeCreateMultiSignMasterWalletWithPrivKey(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jMasterWalletId,
		jstring jPrivKey,
		jstring jPayPassword,
		jstring jCoSigners,
		jint jRequiredSignCount)
{
	bool exception = false;
	std::string msgException;

	const char* masterWalletId = env->GetStringUTFChars(jMasterWalletId, NULL);
	const char* privKey = env->GetStringUTFChars(jPrivKey, NULL);
	const char* payPassword = env->GetStringUTFChars(jPayPassword, NULL);
	const char* coSigners = env->GetStringUTFChars(jCoSigners, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	IMasterWallet* masterWallet = NULL;

	try {
		nlohmann::json coSignersJson = nlohmann::json::parse(coSigners);
		masterWallet = walletManager->CreateMultiSignMasterWallet(masterWalletId, privKey, payPassword, coSignersJson, jRequiredSignCount);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jMasterWalletId, masterWalletId);
	env->ReleaseStringUTFChars(jPrivKey, privKey);
	env->ReleaseStringUTFChars(jPayPassword, payPassword);
	env->ReleaseStringUTFChars(jCoSigners, coSigners);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeCreateMultiSignMasterWalletWithMnemonic "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)J"
static jlong JNICALL nativeCreateMultiSignMasterWalletWithMnemonic(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jMasterWalletId,
		jstring jMnemonic,
		jstring jPhrasePassword,
		jstring jPayPassword,
		jstring jCoSigners,
		jint jRequiredSignCount)
{
	bool exception = false;
	std::string msgException;

	const char *masterWalletId = env->GetStringUTFChars(jMasterWalletId, NULL);
	const char *mnemonic       = env->GetStringUTFChars(jMnemonic, NULL);
	const char *phrasePassword = env->GetStringUTFChars(jPhrasePassword, NULL);
	const char *payPassword    = env->GetStringUTFChars(jPayPassword, NULL);
	const char *coSigners      = env->GetStringUTFChars(jCoSigners, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	IMasterWallet* masterWallet = NULL;

	try {
		nlohmann::json coSignersJson = nlohmann::json::parse(coSigners);
		masterWallet = walletManager->CreateMultiSignMasterWallet(masterWalletId, mnemonic,
				phrasePassword, payPassword, coSignersJson, jRequiredSignCount);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jMasterWalletId, masterWalletId);
	env->ReleaseStringUTFChars(jMnemonic, mnemonic);
	env->ReleaseStringUTFChars(jPhrasePassword, phrasePassword);
	env->ReleaseStringUTFChars(jPayPassword, payPassword);
	env->ReleaseStringUTFChars(jCoSigners, coSigners);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeDestroyWallet "(JLjava/lang/String;)V"
static void JNICALL nativeDestroyWallet(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jmasterWalletId)
{
	bool exception = false;
	std::string msgException;

	const char* masterWalletId = env->GetStringUTFChars(jmasterWalletId, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;

	try {
		walletManager->DestroyWallet(masterWalletId);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jmasterWalletId, masterWalletId);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}
}

#define SIG_nativeImportWalletWithOldKeystore "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J"
static jlong JNICALL nativeImportWalletWithOldKeystore(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jmasterWalletId,
		jstring jkeystoreContent,
		jstring jbackupPassword,
		jstring jpayPassword,
		jstring jphrasePassword)
{
	bool exception = false;
	std::string msgException;

	const char* masterWalletId = env->GetStringUTFChars(jmasterWalletId, NULL);
	const char* keystoreContent = env->GetStringUTFChars(jkeystoreContent, NULL);
	const char* backupPassword = env->GetStringUTFChars(jbackupPassword, NULL);
	const char* payPassword = env->GetStringUTFChars(jpayPassword, NULL);
	const char* phrasePassword = env->GetStringUTFChars(jphrasePassword, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	IMasterWallet* masterWallet = NULL;

	try {
		masterWallet = walletManager->ImportWalletWithKeystore(masterWalletId,
				nlohmann::json::parse(keystoreContent), backupPassword, payPassword, phrasePassword);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jmasterWalletId, masterWalletId);
	env->ReleaseStringUTFChars(jkeystoreContent, keystoreContent);
	env->ReleaseStringUTFChars(jbackupPassword, backupPassword);
	env->ReleaseStringUTFChars(jpayPassword, payPassword);
	env->ReleaseStringUTFChars(jphrasePassword, phrasePassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeImportWalletWithKeystore "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J"
static jlong JNICALL nativeImportWalletWithKeystore(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jmasterWalletId,
		jstring jkeystoreContent,
		jstring jbackupPassword,
		jstring jpayPassword)
{
	bool exception = false;
	std::string msgException;

	const char* masterWalletId = env->GetStringUTFChars(jmasterWalletId, NULL);
	const char* keystoreContent = env->GetStringUTFChars(jkeystoreContent, NULL);
	const char* backupPassword = env->GetStringUTFChars(jbackupPassword, NULL);
	const char* payPassword = env->GetStringUTFChars(jpayPassword, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	IMasterWallet* masterWallet = NULL;

	try {
		masterWallet = walletManager->ImportWalletWithKeystore(masterWalletId,
				nlohmann::json::parse(keystoreContent), backupPassword, payPassword);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jmasterWalletId, masterWalletId);
	env->ReleaseStringUTFChars(jkeystoreContent, keystoreContent);
	env->ReleaseStringUTFChars(jbackupPassword, backupPassword);
	env->ReleaseStringUTFChars(jpayPassword, payPassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeImportWalletWithMnemonic "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)J"
static jlong JNICALL nativeImportWalletWithMnemonic(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jmasterWalletId,
		jstring jmnemonic,
		jstring jphrasePassword,
		jstring jpayPassword,
		jboolean jSingleAddress)
{
	bool exception = false;
	std::string msgException;

	const char* masterWalletId = env->GetStringUTFChars(jmasterWalletId, NULL);
	const char* mnemonic = env->GetStringUTFChars(jmnemonic, NULL);
	const char* phrasePassword = env->GetStringUTFChars(jphrasePassword, NULL);
	const char* payPassword = env->GetStringUTFChars(jpayPassword, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	IMasterWallet* masterWallet = NULL;

	try {
		masterWallet = walletManager->ImportWalletWithMnemonic(masterWalletId, mnemonic, phrasePassword, payPassword, jSingleAddress);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jmasterWalletId, masterWalletId);
	env->ReleaseStringUTFChars(jmnemonic, mnemonic);
	env->ReleaseStringUTFChars(jphrasePassword, phrasePassword);
	env->ReleaseStringUTFChars(jpayPassword, payPassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeExportWalletWithKeystore "(JLorg/elastos/spvcore/IMasterWallet;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeExportWalletWithKeystore(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jobject jmasterWallet,
		jstring jbackupPassword,
		jstring jpayPassword)
{
	bool exception = false;
	std::string msgException;

	const char* backupPassword = env->GetStringUTFChars(jbackupPassword, NULL);
	const char* payPassword = env->GetStringUTFChars(jpayPassword, NULL);

	jclass cls = env->FindClass(CLASS_MASTERWALLET);
	long masterProxy = GetJavaLongField(env, cls, jmasterWallet, FIELD_MASTERWALLET);
	CheckErrorAndLog(env, "nativeExportWalletWithKeystore", __LINE__);
	IMasterWallet *masterWallet = (IMasterWallet*)masterProxy;
	MasterWalletManager *walletManager = (MasterWalletManager*)jWalletMgr;

	jstring result = NULL;

	try {
		nlohmann::json r= walletManager->ExportWalletWithKeystore(masterWallet, backupPassword, payPassword);
		result = env->NewStringUTF(r.dump().c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jbackupPassword, backupPassword);
	env->ReleaseStringUTFChars(jpayPassword, payPassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeExportWalletWithMnemonic "(JLorg/elastos/spvcore/IMasterWallet;Ljava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeExportWalletWithMnemonic(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jobject jmasterWallet,
		jstring jpayPassword)
{
	bool exception = false;
	std::string msgException;

	jclass cls = env->FindClass(CLASS_MASTERWALLET);
	long masterProxy = GetJavaLongField(env, cls, jmasterWallet, FIELD_MASTERWALLET);
	CheckErrorAndLog(env, "nativeExportWalletWithMnemonic", __LINE__);
	IMasterWallet* masterWallet = (IMasterWallet*)masterProxy;

	const char* payPassword = env->GetStringUTFChars(jpayPassword, NULL);

	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;

	jstring result = NULL;

	try {
		std::string str = walletManager->ExportWalletWithMnemonic(masterWallet, payPassword);
		result = env->NewStringUTF(str.c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jpayPassword, payPassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeGetVersion "(J)Ljava/lang/String;"
static jstring JNICALL nativeGetVersion(JNIEnv *env, jobject clazz, jlong jWalletMgr)
{
	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;

	std::string version = walletManager->GetVersion();

	return env->NewStringUTF(version.c_str());
}

#define SIG_nativeGetAllMasterWallets "(J)[J"
static jlongArray JNICALL nativeGetAllMasterWallets(JNIEnv *env, jobject clazz, jlong jWalletMgr)
{
	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	std::vector<IMasterWallet *> array;

	try {
		array = walletManager->GetAllMasterWallets();
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
		return NULL;
	}

	const int length = array.size();
	jlongArray jarray = env->NewLongArray(length);

	if (length > 0) {
		jlong masterWallets[length];
		for (int i = 0; i < length; i++) {
			masterWallets[i] = (jlong)array[i];
		}
		env->SetLongArrayRegion(jarray, 0, length, masterWallets);
	}

	return jarray;
}

#define SIG_nativeGetAllMasterWalletIds "(J)[Ljava/lang/String;"
static jobjectArray JNICALL nativeGetAllMasterWalletIds(JNIEnv *env, jobject clazz, jlong jWalletMgr)
{
	try {
		MasterWalletManager *walletManager = (MasterWalletManager *)jWalletMgr;
		std::vector<std::string> allIds = walletManager->GetAllMasterWalletIds();

		jclass objClass = env->FindClass("java/lang/String");
		jobjectArray objArray = env->NewObjectArray(allIds.size(), objClass, 0);
		for (int i = 0; i < allIds.size(); i++) {
			env->SetObjectArrayElement(objArray, i, env->NewStringUTF(allIds[i].c_str()));
		}

		return objArray;
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
		return NULL;
	}
}

#define SIG_nativeGetWallet "(JLjava/lang/String;)J"
static jlong JNICALL nativeGetWallet(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jMasterWalletId)
{
	bool exception = false;
	std::string msgException;

	const char *masterWalletId = env->GetStringUTFChars(jMasterWalletId, NULL);
	IMasterWallet *masterWallet = NULL;

	try {
		MasterWalletManager *walletManager = (MasterWalletManager *)jWalletMgr;
		masterWallet = walletManager->GetWallet(masterWalletId);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jMasterWalletId, masterWalletId);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)masterWallet;
}

#define SIG_nativeSaveConfigs "(J)V"
static void JNICALL nativeSaveConfigs(JNIEnv *env, jobject clazz, jlong jWalletMgr)
{
	MasterWalletManager *walletManager = (MasterWalletManager *)jWalletMgr;
	try {
		walletManager->SaveConfigs();
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
	}
}

#define SIG_nativeEncodeTransactionToString "(JLjava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeEncodeTransactionToString(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jtxJson)
{
	bool exception = false;
	std::string msgException;

	jstring result = NULL;
	const char *txJson = env->GetStringUTFChars(jtxJson, NULL);

	try {
		MasterWalletManager *walletManager = (MasterWalletManager *)jWalletMgr;

		nlohmann::json stringJson = walletManager->EncodeTransactionToString(nlohmann::json::parse(txJson));
		result = env->NewStringUTF(stringJson.dump().c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jtxJson, txJson);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeDecodeTransactionFromString "(JLjava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeDecodeTransactionFromString(JNIEnv *env, jobject clazz, jlong jWalletMgr,
		jstring jCipher)
{
	bool exception = false;
	std::string msgException;

	jstring result = NULL;
	const char *cipher = env->GetStringUTFChars(jCipher, NULL);

	try {
		MasterWalletManager *walletManager = (MasterWalletManager *) jWalletMgr;

		nlohmann::json txJson = walletManager->DecodeTransactionFromString(nlohmann::json::parse(cipher));
		result = env->NewStringUTF(txJson.dump().c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jCipher, cipher);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeInitMasterWalletManager "(Ljava/lang/String;)J"
static jlong JNICALL nativeInitMasterWalletManager(JNIEnv *env, jobject clazz, jstring jrootPath)
{
	bool exception = false;
	std::string msgException;

	MasterWalletManager* walletManager = NULL;
	const char* rootPath = env->GetStringUTFChars(jrootPath, NULL);

	try {
		walletManager = new MasterWalletManager(rootPath);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jrootPath, rootPath);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)walletManager;
}

#define SIG_nativeDisposeNative "(J)V"
static void JNICALL nativeDisposeNative(JNIEnv *env, jobject clazz, jlong jWalletMgr)
{
	MasterWalletManager* walletManager = (MasterWalletManager*)jWalletMgr;
	delete walletManager;
}

static const JNINativeMethod gMethods[] = {
	REGISTER_METHOD(nativeSaveConfigs),
	REGISTER_METHOD(nativeGenerateMnemonic),
	REGISTER_METHOD(nativeGetMultiSignPubKeyWithMnemonic),
	REGISTER_METHOD(nativeGetMultiSignPubKeyWithPrivKey),
	REGISTER_METHOD(nativeCreateMasterWallet),
	REGISTER_METHOD(nativeCreateMultiSignMasterWallet),
	REGISTER_METHOD(nativeCreateMultiSignMasterWalletWithPrivKey),
	REGISTER_METHOD(nativeCreateMultiSignMasterWalletWithMnemonic),
	REGISTER_METHOD(nativeGetAllMasterWallets),
	REGISTER_METHOD(nativeGetAllMasterWalletIds),
	REGISTER_METHOD(nativeGetWallet),
	REGISTER_METHOD(nativeDestroyWallet),
	REGISTER_METHOD(nativeImportWalletWithKeystore),
	REGISTER_METHOD(nativeImportWalletWithOldKeystore),
	REGISTER_METHOD(nativeImportWalletWithMnemonic),
	REGISTER_METHOD(nativeExportWalletWithKeystore),
	REGISTER_METHOD(nativeExportWalletWithMnemonic),
	REGISTER_METHOD(nativeGetVersion),
	REGISTER_METHOD(nativeEncodeTransactionToString),
	REGISTER_METHOD(nativeDecodeTransactionFromString),
	REGISTER_METHOD(nativeInitMasterWalletManager),
	REGISTER_METHOD(nativeDisposeNative),
};

int register_elastos_spv_IMasterWalletManager(JNIEnv *env)
{
	return jniRegisterNativeMethods(env,
			"org/elastos/spvcore/MasterWalletManager",
			gMethods, NELEM(gMethods));
}


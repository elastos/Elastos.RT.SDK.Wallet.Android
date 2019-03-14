// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#include "ElaUtils.h"
#include "IMasterWallet.h"
#include "nlohmann/json.hpp"

using namespace Elastos::ElaWallet;
#define CLASS_MCSUBWALLET   "org/elastos/spvcore/IMainchainSubWallet"
#define CLASS_IDSUBWALLET   "org/elastos/spvcore/IIdChainSubWallet"
#define CHAINID_MAINCHAIN "ELA"
#define CHAINID_IDCHAIN "IdChain"

#define SIG_nativeGetId "(J)Ljava/lang/String;"
static jstring JNICALL nativeGetId(JNIEnv *env, jobject clazz, jlong jMasterProxy)
{
	jstring id = NULL;
	try {
		IMasterWallet *masterWallet = (IMasterWallet *)jMasterProxy;
		std::string key = masterWallet->GetId();
		id = env->NewStringUTF(key.c_str());
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
	}

	return id;
}

#define SIG_nativeGetBasicInfo "(J)Ljava/lang/String;"
static jstring JNICALL nativeGetBasicInfo(JNIEnv *env, jobject clazz, jlong jMasterProxy)
{
	IMasterWallet *masterWallet = (IMasterWallet *)jMasterProxy;
	jstring info = NULL;

	try {
		nlohmann::json basicInfo = masterWallet->GetBasicInfo();
		info = env->NewStringUTF(basicInfo.dump().c_str());
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
	}

	return info;
}

#define SIG_nativeGetAllSubWallets "(J)[Ljava/lang/Object;"
static jobjectArray JNICALL nativeGetAllSubWallets(JNIEnv *env, jobject clazz, jlong jMasterProxy)
{
	try {
		IMasterWallet* masterWallet = (IMasterWallet*)jMasterProxy;
		std::vector<ISubWallet *> allSubWallets = masterWallet->GetAllSubWallets();

		jclass clazzSubWallet;
		jmethodID subWalletConstructor;
		jobject subWallet;

		jclass clazzObject = env->FindClass("java/lang/Object");
		jobjectArray subWalletArray = env->NewObjectArray(allSubWallets.size(), clazzObject, NULL);

		for (int i = 0; i < allSubWallets.size(); i++) {
			std::string id = allSubWallets[i]->GetChainId();
			if (id == CHAINID_MAINCHAIN) {
				clazzSubWallet = env->FindClass(CLASS_MCSUBWALLET);
				subWalletConstructor = env->GetMethodID(clazzSubWallet, "<init>", "(J)V");
				subWallet = env->NewObject(clazzSubWallet, subWalletConstructor, (jlong) allSubWallets[i]);
				env->SetObjectArrayElement(subWalletArray, i, subWallet);
			} else if (id == CHAINID_IDCHAIN) {
				clazzSubWallet = env->FindClass(CLASS_IDSUBWALLET);
				subWalletConstructor = env->GetMethodID(clazzSubWallet, "<init>", "(J)V");
				subWallet = env->NewObject(clazzSubWallet, subWalletConstructor, (jlong) allSubWallets[i]);
				env->SetObjectArrayElement(subWalletArray, i, subWallet);
			} else {
				ThrowWalletException(env, "Unknow chain id");
				return NULL;
			}
		}

		return subWalletArray;
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
		return NULL;
	}
}

#define SIG_nativeCreateSubWallet "(JLjava/lang/String;J)J"
static jlong JNICALL nativeCreateSubWallet(JNIEnv *env, jobject clazz, jlong jMasterProxy,
		jstring jChainID,
		jlong jFeePerKb)
{
	bool exception = false;
	std::string msgException;

	const char* chainID = env->GetStringUTFChars(jChainID, NULL);

	IMasterWallet *masterWallet = (IMasterWallet *)jMasterProxy;
	ISubWallet *subWallet = NULL;

	try {
		subWallet = masterWallet->CreateSubWallet(chainID, jFeePerKb);
	} catch (std::exception& e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jChainID, chainID);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jlong)subWallet;
}

#define SIG_nativeDestroyWallet "(JJ)V"
static void JNICALL nativeDestroyWallet(JNIEnv *env, jobject clazz, jlong jMasterProxy,
		jlong jsubWalletProxy)
{
	try {
		IMasterWallet *masterWallet = (IMasterWallet *)jMasterProxy;
		ISubWallet *subWallet = (ISubWallet *)jsubWalletProxy;
		masterWallet->DestroyWallet(subWallet);
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
	}
}

#define SIG_nativeGetPublicKey "(J)Ljava/lang/String;"
static jstring JNICALL nativeGetPublicKey(JNIEnv *env, jobject clazz, jlong jMasterProxy)
{
	jstring key = NULL;

	try {
		IMasterWallet *masterWallet = (IMasterWallet *)jMasterProxy;
		std::string k = masterWallet->GetPublicKey();
		key = env->NewStringUTF(k.c_str());
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
	}

	return key;
}

#define SIG_nativeSign "(JLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeSign(JNIEnv *env, jobject clazz, jlong jMasterProxy,
		jstring jmessage,
		jstring jpayPassword)
{
	bool exception = false;
	std::string msgException;

	const char* message = env->GetStringUTFChars(jmessage, NULL);
	const char* payPassword = env->GetStringUTFChars(jpayPassword, NULL);

	IMasterWallet* masterWallet = (IMasterWallet*)jMasterProxy;
	jstring result = NULL;

	try {
		std::string r = masterWallet->Sign(message, payPassword);
		result = env->NewStringUTF(r.c_str());
	} catch (std::exception& e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jmessage, message);
	env->ReleaseStringUTFChars(jpayPassword, payPassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeCheckSign "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z"
static jboolean JNICALL nativeCheckSign(JNIEnv *env, jobject clazz, jlong jMasterProxy,
		jstring jaddress,
		jstring jmessage,
		jstring jsignature)
{
	bool exception = false;
	std::string msgException;

	const char* address = env->GetStringUTFChars(jaddress, NULL);
	const char* message = env->GetStringUTFChars(jmessage, NULL);
	const char* signature = env->GetStringUTFChars(jsignature, NULL);

	IMasterWallet *masterWallet = (IMasterWallet *)jMasterProxy;
	jboolean result = false;

	try {
		result = masterWallet->CheckSign(address, message, signature);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jaddress, address);
	env->ReleaseStringUTFChars(jmessage, message);
	env->ReleaseStringUTFChars(jsignature, signature);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeIsAddressValid "(JLjava/lang/String;)Z"
static jboolean JNICALL nativeIsAddressValid(JNIEnv *env, jobject clazz, jlong jMasterProxy, jstring jaddress)
{
	bool exception = false;
	std::string msgException;

	const char* address = env->GetStringUTFChars(jaddress, NULL);

	IMasterWallet *masterWallet = (IMasterWallet*)jMasterProxy;
	bool valid = false;

	try {
		valid = masterWallet->IsAddressValid(address);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jaddress, address);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return (jboolean)valid;
}

#define SIG_nativeGetSupportedChains "(J)[Ljava/lang/String;"
static jobjectArray JNICALL nativeGetSupportedChains(JNIEnv *env, jobject clazz, jlong jMasterProxy)
{
	IMasterWallet* masterWallet = (IMasterWallet*)jMasterProxy;
	std::vector<std::string> chains;

	try {
		chains = masterWallet->GetSupportedChains();
	} catch (std::exception &e) {
		ThrowWalletException(env, e.what());
		return NULL;
	}

	jclass objClass = env->FindClass("java/lang/String");
	jobjectArray objArray = env->NewObjectArray(chains.size(), objClass, 0);
	for (int i = 0; i < chains.size(); ++i) {
		env->SetObjectArrayElement(objArray, i, env->NewStringUTF(chains[i].c_str()));
	}

	return objArray;
}

#define SIG_nativeChangePassword "(JLjava/lang/String;Ljava/lang/String;)V"
static void JNICALL nativeChangePassword(JNIEnv *env, jobject clazz, jlong jMasterProxy, jstring joldPassword, jstring jnewPassword)
{
	bool exception = false;
	std::string msgException;

	const char *oldPassword = env->GetStringUTFChars(joldPassword, NULL);
	const char *newPassword = env->GetStringUTFChars(jnewPassword, NULL);

	IMasterWallet *masterWallet = (IMasterWallet*)jMasterProxy;

	try {
		masterWallet->ChangePassword(oldPassword, newPassword);
	} catch (std::exception& e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(joldPassword, oldPassword);
	env->ReleaseStringUTFChars(jnewPassword, newPassword);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}
}

static const JNINativeMethod gMethods[] = {
	REGISTER_METHOD(nativeGetId),
	REGISTER_METHOD(nativeGetBasicInfo),
	REGISTER_METHOD(nativeGetAllSubWallets),
	REGISTER_METHOD(nativeCreateSubWallet),
	REGISTER_METHOD(nativeDestroyWallet),
	REGISTER_METHOD(nativeGetPublicKey),
	REGISTER_METHOD(nativeSign),
	REGISTER_METHOD(nativeCheckSign),
	REGISTER_METHOD(nativeIsAddressValid),
	REGISTER_METHOD(nativeGetSupportedChains),
	REGISTER_METHOD(nativeChangePassword),
};

jint register_elastos_spv_IMasterWallet(JNIEnv *env)
{
	return jniRegisterNativeMethods(env,
			"org/elastos/spvcore/IMasterWallet",
			gMethods, NELEM(gMethods));
}


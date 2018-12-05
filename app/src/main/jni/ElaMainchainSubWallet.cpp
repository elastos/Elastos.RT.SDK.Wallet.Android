// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#include "ElaUtils.h"
#include "IMainchainSubWallet.h"
#include "nlohmann/json.hpp"

using namespace Elastos::ElaWallet;

#define SIG_nativeCreateDepositTransaction "(JLjava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeCreateDepositTransaction(JNIEnv *env, jobject clazz, jlong jMainSubWalletProxy,
		jstring jfromAddress,
		jstring jtoAddress,
		jlong amount,
		jstring jsidechainAccounts,
		jstring jsidechainAmounts,
		jstring jsidechainIndexs,
		jstring jmemo,
		jstring jremark)
{
	bool exception = false;
	std::string msgException;

	const char* fromAddress = env->GetStringUTFChars(jfromAddress, NULL);
	const char* toAddress = env->GetStringUTFChars(jtoAddress, NULL);
	const char* sidechainAccounts = env->GetStringUTFChars(jsidechainAccounts, NULL);
	const char* sidechainAmounts = env->GetStringUTFChars(jsidechainAmounts, NULL);
	const char* sidechainIndexs = env->GetStringUTFChars(jsidechainIndexs, NULL);
	const char* memo = env->GetStringUTFChars(jmemo, NULL);
	const char* remark = env->GetStringUTFChars(jremark, NULL);

	IMainchainSubWallet* wallet = (IMainchainSubWallet*)jMainSubWalletProxy;
	jstring tx = NULL;

	try {
		nlohmann::json txJson = wallet->CreateDepositTransaction(fromAddress, toAddress, amount, nlohmann::json::parse(sidechainAccounts),
				nlohmann::json::parse(sidechainAmounts), nlohmann::json::parse(sidechainIndexs), memo, remark);
		tx = env->NewStringUTF(txJson.dump().c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jfromAddress, fromAddress);
	env->ReleaseStringUTFChars(jtoAddress, toAddress);
	env->ReleaseStringUTFChars(jsidechainAccounts, sidechainAccounts);
	env->ReleaseStringUTFChars(jsidechainAmounts, sidechainAmounts);
	env->ReleaseStringUTFChars(jsidechainIndexs, sidechainIndexs);
	env->ReleaseStringUTFChars(jmemo, memo);
	env->ReleaseStringUTFChars(jremark, remark);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return tx;
}

#define SIG_nativeCreateRegisterProducerTransaction "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)Ljava/lang/String;"
static jstring JNICALL nativeCreateRegisterProducerTransaction(JNIEnv *env, jobject clazz, jlong jMainSubWalletProxy,
	jstring jfromAddress,
	jstring jtoAddress,
	jstring jpublicKey,
	jstring jnickName,
	jstring jurl,
	jlong location)
{
	bool exception = false;
	std::string msgException;

	const char* fromAddress = env->GetStringUTFChars(jfromAddress, NULL);
	const char* toAddress = env->GetStringUTFChars(jtoAddress, NULL);
	const char* publicKey = env->GetStringUTFChars(jpublicKey, NULL);
	const char* nickName = env->GetStringUTFChars(jnickName, NULL);
	const char* url = env->GetStringUTFChars(jurl, NULL);

	IMainchainSubWallet* wallet = (IMainchainSubWallet*)jMainSubWalletProxy;
	jstring tx = NULL;

	try {
		nlohmann::json txJson = wallet->CreateRegisterProducerTransaction(fromAddress, toAddress, publicKey, nickName, url, location);
		tx = env->NewStringUTF(txJson.dump().c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jfromAddress, fromAddress);
	env->ReleaseStringUTFChars(jtoAddress, toAddress);
	env->ReleaseStringUTFChars(jpublicKey, publicKey);
	env->ReleaseStringUTFChars(jnickName, nickName);
	env->ReleaseStringUTFChars(jurl, url);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return tx;
}

#define SIG_nativeCreateCancelProducerTransaction "(JLjava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeCreateCancelProducerTransaction(JNIEnv *env, jobject clazz, jlong jMainSubWalletProxy,
	jstring jpublicKey)
{

	bool exception = false;
	std::string msgException;

	const char* publicKey = env->GetStringUTFChars(jpublicKey, NULL);

	IMainchainSubWallet* wallet = (IMainchainSubWallet*)jMainSubWalletProxy;
	jstring tx = NULL;

	try {
		nlohmann::json txJson = wallet->CreateCancelProducerTransaction(publicKey);
		tx = env->NewStringUTF(txJson.dump().c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jpublicKey, publicKey);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return tx;
}

#define SIG_nativeCreateVoteProducerTransaction "(JJLjava/lang/String;)Ljava/lang/String;"
static jstring JNICALL nativeCreateVoteProducerTransaction(JNIEnv *env, jobject clazz, jlong jMainSubWalletProxy,
		jlong stake,
		jstring jpublicKeys)
{

	bool exception = false;
	std::string msgException;

	const char* publicKeys = env->GetStringUTFChars(jpublicKeys, NULL);

	IMainchainSubWallet* wallet = (IMainchainSubWallet*)jMainSubWalletProxy;
	jstring tx = NULL;

	try {
		nlohmann::json txJson = wallet->CreateVoteProducerTransaction(stake, nlohmann::json::parse(publicKeys));
		tx = env->NewStringUTF(txJson.dump().c_str());
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}

	env->ReleaseStringUTFChars(jpublicKeys, publicKeys);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return tx;
}


static const JNINativeMethod gMethods[] = {
	REGISTER_METHOD(nativeCreateDepositTransaction),
	REGISTER_METHOD(nativeCreateRegisterProducerTransaction),
	REGISTER_METHOD(nativeCreateCancelProducerTransaction),
	REGISTER_METHOD(nativeCreateVoteProducerTransaction),
};

jint register_elastos_spv_IMainchainSubWallet(JNIEnv *env)
{
	return jniRegisterNativeMethods(env,
			"com/elastos/spvcore/IMainchainSubWallet",
			gMethods, NELEM(gMethods));
}


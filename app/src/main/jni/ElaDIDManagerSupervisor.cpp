// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#include "ElaUtils.h"
#include "DIDManagerSupervisor.h"

using namespace Elastos::ElaWallet;
using namespace Elastos::DID;

#define SIG_nativeNewDIDManagerSupervisor "(Ljava/lang/String;)J"
static jlong JNICALL nativeNewDIDManagerSupervisor(JNIEnv *env, jobject clazz, jstring jrootPath)
{
	bool exception = false;
	std::string msgException;
	jlong result = 0;

	const char* rootPath = env->GetStringUTFChars(jrootPath, NULL);

#ifdef ENABLE_DID
	DIDManagerSupervisor *supervisor = NULL;

	try {
		supervisor = new DIDManagerSupervisor(rootPath);
		result = (jlong) supervisor;
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}
#endif

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeDisposeNative "(J)V"
static void JNICALL nativeDisposeNative(JNIEnv *env, jobject clazz, jlong jSupervisor)
{
	bool exception = false;
	std::string msgException;

#ifdef ENABLE_DID
	try {
		DIDManagerSupervisor *supervisor = (DIDManagerSupervisor *) jSupervisor;
		delete supervisor;
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}
#endif

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}
}

#define SIG_nativeCreateDIDManager "(JJLjava/lang/String;)J"
static jlong JNICALL nativeCreateDIDManager(JNIEnv *env, jobject clazz, jlong jSupervisor,
		jlong jMasterWallet, jstring jrootPath)
{
	bool exception = false;
	std::string msgException;
	jlong result = 0;

	const char *rootPath = env->GetStringUTFChars(jrootPath, NULL);
	IDIDManager *manager = NULL;

#ifdef ENABLE_DID
	try {
		DIDManagerSupervisor *supervisor = (DIDManagerSupervisor *) jSupervisor;
		IMasterWallet *masterWallet = (IMasterWallet *) jMasterWallet;

		manager = supervisor->CreateDIDManager(masterWallet, rootPath);
		result = (jlong)manager;
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}
#endif

	env->ReleaseStringUTFChars(jrootPath, rootPath);

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}

	return result;
}

#define SIG_nativeDestroyDIDManager "(JJ)V"
static void JNICALL nativeDestroyDIDManager(JNIEnv *env, jobject clazz, jlong jSupervisor,
		jlong jIDManager)
{
	bool exception = false;
	std::string msgException;

#ifdef ENABLE_DID
	try {
		DIDManagerSupervisor *supervisor = (DIDManagerSupervisor *) jSupervisor;
		IDIDManager *manager = (IDIDManager *) jIDManager;

		supervisor->DestroyDIDManager(manager);
	} catch (std::exception &e) {
		exception = true;
		msgException = e.what();
	}
#endif

	if (exception) {
		ThrowWalletException(env, msgException.c_str());
	}
}

static const JNINativeMethod gMethods[] = {
	REGISTER_METHOD(nativeNewDIDManagerSupervisor),
	REGISTER_METHOD(nativeDisposeNative),
	REGISTER_METHOD(nativeCreateDIDManager),
	REGISTER_METHOD(nativeDestroyDIDManager),
};

jint register_elastos_spv_DIDManagerSupervisor(JNIEnv *env)
{
	return jniRegisterNativeMethods(env,
			"org/elastos/spvcore/DIDManagerSupervisor",
			gMethods, NELEM(gMethods));
}


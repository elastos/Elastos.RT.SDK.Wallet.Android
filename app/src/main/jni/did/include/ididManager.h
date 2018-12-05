// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#ifndef IDCHAIN_IDIDMANAGER_H
#define IDCHAIN_IDIDMANAGER_H
#include <map>
#include "nlohmann/json.hpp"

#include "idid.h"
#include "IIdManagerCallback.h"


namespace Elastos {
	namespace DID {
		//class IIdManagerCallback;
		class IDIDManager {
		public:

			virtual IDID * CreateDID(const std::string &password) = 0;

			virtual IDID * GetDID(const std::string &didName) = 0;

			virtual nlohmann::json GetDIDList() const = 0;

			virtual void  DestoryDID(const std::string &didName) = 0;

			virtual void ClearLocal() = 0;

			virtual bool  RegisterCallback(const std::string &id, IIdManagerCallback *callback)= 0;

			virtual bool  UnregisterCallback(const std::string &id)= 0;
		};

	}
}

#endif //IDCHAIN_IDIDMANAGER_H


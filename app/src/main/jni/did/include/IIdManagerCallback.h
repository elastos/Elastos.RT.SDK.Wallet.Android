// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#ifndef __ELASTOS_SDK_IIDMANAGERCALLBACK_H__
#define __ELASTOS_SDK_IIDMANAGERCALLBACK_H__
#include <map>
#include "nlohmann/json.hpp"

namespace Elastos {
	namespace DID {

		class IIdManagerCallback {
		public:

			virtual void OnIdStatusChanged(
					const std::string &id,
					const std::string &path,
					const nlohmann::json &value) = 0;
		};

	}
}

#endif //__ELASTOS_SDK_IIDMANAGERCALLBACK_H__

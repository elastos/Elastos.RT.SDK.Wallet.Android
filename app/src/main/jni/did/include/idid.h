// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.
#include <map>
#include "nlohmann/json.hpp"

#ifndef IDCHAIN_IDID_H
#define IDCHAIN_IDID_H

namespace Elastos {
	namespace DID {

		class IDID {
		public:

			virtual std::string GetDIDName() = 0;

			virtual void SetValue(
				const std::string &keyPath,
				const nlohmann::json &valueJson) = 0;

			virtual nlohmann::json GetValue(
				const std::string &path) const = 0;

			virtual nlohmann::json GetHistoryValue(
				const std::string &keyPath) const = 0;

			virtual nlohmann::json GetAllKeys(
				uint32_t start,
				uint32_t count) const = 0;

			virtual std::string Sign(
				const std::string &message, const std::string &password) = 0;

			virtual nlohmann::json GenerateProgram(
				const std::string &message,
				const std::string &password) = 0;

			virtual nlohmann::json CheckSign(
				const std::string &message,
				const std::string &signature) = 0;

			virtual std::string GetPublicKey(
				) = 0;

		};

	}
}

#endif //IDCHAIN_IDID_H



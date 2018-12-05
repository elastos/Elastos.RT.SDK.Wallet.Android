// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#ifndef __ELASTOS_DID_IDIDMANAGERSUPERVISOR_H__
#define __ELASTOS_DID_IDIDMANAGERSUPERVISOR_H__

#include "ididManager.h"
#include "IMasterWallet.h"

namespace Elastos {
	namespace DID {

		class IDIDManagerSupervisor {
		public:
			virtual ~IDIDManagerSupervisor() noexcept {}

			virtual IDIDManager *CreateDIDManager(ElaWallet::IMasterWallet* masterWallet, const std::string &rootPath) = 0;

			virtual void DestroyDIDManager(IDIDManager *manager) = 0;

		};

	}
}
#endif //__ELASTOS_DID_IIDMANAGERSUPERVISOR_H__

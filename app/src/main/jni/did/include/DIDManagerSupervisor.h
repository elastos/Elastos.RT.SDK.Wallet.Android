// Copyright (c) 2012-2018 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

#ifndef __ELASTOS_DID_DIDMANAGERSUPERVISOR_H__
#define __ELASTOS_DID_DIDMANAGERSUPERVISOR_H__

#include "IDIDManagerSupervisor.h"
#include "ididManager.h"
#include "IMasterWallet.h"

namespace Elastos {
	namespace DID {

		class DIDManagerSupervisor : public IDIDManagerSupervisor {
		public:
			DIDManagerSupervisor(const std::string &rootPath);

			IDIDManager *CreateDIDManager(ElaWallet::IMasterWallet* masterWallet, const std::string &rootPath);

			void DestroyDIDManager(IDIDManager *manager);

		protected:
			std::string _rootPath;
		};

	}
}

#endif //__ELASTOS_SDK_IDMANAGERFACTORY_H__

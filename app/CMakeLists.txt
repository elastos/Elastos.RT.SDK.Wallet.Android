# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

include_directories(src/main/cpp/elastos/include)
link_directories(src/main/cpp/elastos/libs)

set(SOURCES
    src/main/cpp/ElasotsWalletDemo.cpp
    src/main/cpp/ElaMasterWalletManager.cpp
    src/main/cpp/ElaMasterWallet.cpp
    src/main/cpp/ElaSubWallet.cpp
    src/main/cpp/ElaMainchainSubWallet.cpp
    src/main/cpp/ElaSidechainSubWallet.cpp
    src/main/cpp/ElaIdChainSubWallet.cpp
    src/main/cpp/ElaIdManagerFactory.cpp
    src/main/cpp/ElaDidManager.cpp
    src/main/cpp/ElaDid.cpp
    src/main/cpp/ElaUtils.cpp
)

add_library(elastoswallet SHARED ${SOURCES})

target_link_libraries(elastoswallet
    spvsdk
    Elastos.Runtime
    Elastos.Runtime_static
    Elastos.CoreLibrary
    Elastos.Wallet
    Elastos.DID
    idchain
    log
)

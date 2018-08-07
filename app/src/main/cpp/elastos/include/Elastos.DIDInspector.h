#ifndef __CAR_ELASTOS_DIDINSPECTOR_H__
#define __CAR_ELASTOS_DIDINSPECTOR_H__

#ifndef _NO_INCLIST
#include "elastos.h"
using namespace Elastos;
#include "Elastos.CoreLibrary.h"

#endif // !_NO_INCLIST

#ifndef __UUNM_Elastos_DIDInspector_DEFINED__
#define __UUNM_Elastos_DIDInspector_DEFINED__
#define c_pElastos_DIDInspectorUunm "libElastos.DIDInspector.so"
#endif // __UUNM_Elastos_DIDInspector_DEFINED__

#ifndef __ECLSID_CDIDInspector_DEFINED__
#define __ECLSID_CDIDInspector_DEFINED__
static const _ELASTOS ClassID ECLSID_CDIDInspector = {
    {0xC6A12218,0x234C,0x5235,{0x6C,0xCF,0xA8,0x79,0xCB,0xF5,0xEF,0xBC}},
    (char *)c_pElastos_DIDInspectorUunm,
    0xd069c1d8 };
#endif // __CLSID_CDIDInspector_DEFINED__

#ifndef __ECLSID_CDIDInspectorClassObject_DEFINED__
#define __ECLSID_CDIDInspectorClassObject_DEFINED__
static const _ELASTOS ClassID ECLSID_CDIDInspectorClassObject = {
    {0x36E02D19,0x234C,0x5235,{0x6C,0xCF,0xA8,0x79,0xCB,0x35,0xF3,0x92}},
    (char *)c_pElastos_DIDInspectorUunm,
    0xd069c1d8 };
#endif // __CLSID_CDIDInspectorClassObject_DEFINED__

#ifndef __EIID_IDIDInspector_DEFINED__
#define __EIID_IDIDInspector_DEFINED__
static const _ELASTOS InterfaceID EIID_IDIDInspector = \
    {0x4D891F19,0x2352,0x5235,{0x6C,0xCF,0xA8,0x79,0xCB,0xF5,0xCF,0xAC}};
#endif // __IID_IDIDInspector_DEFINED__

#ifndef __EIID_ICDIDInspectorClassObject_DEFINED__
#define __EIID_ICDIDInspectorClassObject_DEFINED__
static const _ELASTOS InterfaceID EIID_ICDIDInspectorClassObject = \
    {0x8FCA5A16,0xD312,0x8D48,{0x14,0xDB,0x33,0x6A,0xDE,0x72,0xCD,0xBC}};
#endif // __IID_ICDIDInspectorClassObject_DEFINED__

interface IDIDInspector;
interface ICDIDInspectorClassObject;





#ifdef __ELASTOS_DIDINSPECTOR_USER_TYPE_H__
#include "Elastos.DIDInspector_user_type.h"
#endif
ELAPI _Impl_AcquireCallbackHandler(PInterface pServerObj, _ELASTOS REIID iid, PInterface *ppHandler);
ELAPI _Impl_CheckClsId(PInterface pServerObj, const _ELASTOS ClassID* pClassid, PInterface *ppServerObj);

CAR_INTERFACE("4D891F19-2352-5235-6CCF-A879CBF5CFAC")
IDIDInspector : public IInterface
{
    virtual CARAPI_(PInterface) Probe(
        /* [in] */ _ELASTOS REIID riid) = 0;

    static CARAPI_(IDIDInspector*) Probe(PInterface pObj)
    {
        if (pObj == NULL) return NULL;
        return (IDIDInspector*)pObj->Probe(EIID_IDIDInspector);
    }

    static CARAPI_(IDIDInspector*) Probe(IObject* pObj)
    {
        if (pObj == NULL) return NULL;
        return (IDIDInspector*)pObj->Probe(EIID_IDIDInspector);
    }

    static CARAPI_(IDIDInspector*) QueryInterface(const Elastos::String& uid)
    {
        return NULL;
    }

    virtual CARAPI CheckDID(
        /* [in] */ const _ELASTOS String& publicKey,
        /* [in] */ const _ELASTOS String& did,
        /* [out] */ _ELASTOS Boolean * matched) = 0;

    virtual CARAPI CheckSign(
        /* [in] */ const _ELASTOS String& publicKey,
        /* [in] */ const _ELASTOS String& message,
        /* [in] */ const _ELASTOS String& signature,
        /* [out] */ _ELASTOS Boolean * matched) = 0;

};
CAR_INTERFACE("8FCA5A16-D312-8D48-14DB-336ADE72CDBC")
ICDIDInspectorClassObject : public IClassObject
{
    virtual CARAPI_(PInterface) Probe(
        /* [in] */ _ELASTOS REIID riid) = 0;

    static CARAPI_(ICDIDInspectorClassObject*) Probe(PInterface pObj)
    {
        if (pObj == NULL) return NULL;
        return (ICDIDInspectorClassObject*)pObj->Probe(EIID_ICDIDInspectorClassObject);
    }

    static CARAPI_(ICDIDInspectorClassObject*) Probe(IObject* pObj)
    {
        if (pObj == NULL) return NULL;
        return (ICDIDInspectorClassObject*)pObj->Probe(EIID_ICDIDInspectorClassObject);
    }

    static CARAPI_(ICDIDInspectorClassObject*) QueryInterface(const Elastos::String& uid)
    {
        return NULL;
    }

    virtual CARAPI CreateObjectWithDefaultCtor(
        /* [out] */ IInterface ** newObj) = 0;

};
#ifndef _INSIDE_ELASTOS_DIDINSPECTOR_
class CDIDInspector
{
public:
    static _ELASTOS ECode New(
        /* [out] */ IDIDInspector** __object)
    {
        return _CObject_CreateInstance(ECLSID_CDIDInspector, RGM_SAME_DOMAIN, EIID_IDIDInspector, (PInterface*)__object);
    }

    static _ELASTOS ECode New(
        /* [out] */ IObject** __object)
    {
        return _CObject_CreateInstance(ECLSID_CDIDInspector, RGM_SAME_DOMAIN, EIID_IObject, (PInterface*)__object);
    }

    static _ELASTOS ECode New(
        /* [out] */ Elastos::Core::ISynchronize** __object)
    {
        return _CObject_CreateInstance(ECLSID_CDIDInspector, RGM_SAME_DOMAIN, Elastos::Core::EIID_ISynchronize, (PInterface*)__object);
    }

    static _ELASTOS ECode New(
        /* [out] */ IWeakReferenceSource** __object)
    {
        return _CObject_CreateInstance(ECLSID_CDIDInspector, RGM_SAME_DOMAIN, EIID_IWeakReferenceSource, (PInterface*)__object);
    }

};
#endif // _INSIDE_ELASTOS_DIDINSPECTOR_


#endif // __CAR_ELASTOS_DIDINSPECTOR_H__

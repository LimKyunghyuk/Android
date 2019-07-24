/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\work3\\WhoAreYou_20100804\\src\\com\\skt\\arm\\aidl\\IArmService.aidl
 */
package com.skt.arm.aidl;
public interface IArmService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.skt.arm.aidl.IArmService
{
private static final java.lang.String DESCRIPTOR = "com.skt.arm.aidl.IArmService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.skt.arm.aidl.IArmService interface,
 * generating a proxy if needed.
 */
public static com.skt.arm.aidl.IArmService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.skt.arm.aidl.IArmService))) {
return ((com.skt.arm.aidl.IArmService)iin);
}
return new com.skt.arm.aidl.IArmService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_executeArm:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.executeArm(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.skt.arm.aidl.IArmService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public int executeArm(java.lang.String aid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(aid);
mRemote.transact(Stub.TRANSACTION_executeArm, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_executeArm = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public int executeArm(java.lang.String aid) throws android.os.RemoteException;
}

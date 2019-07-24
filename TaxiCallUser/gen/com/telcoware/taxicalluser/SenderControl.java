/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\work3\\TaxiCallUser\\src\\com\\telcoware\\taxicalluser\\SenderControl.aidl
 */
package com.telcoware.taxicalluser;
public interface SenderControl extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.telcoware.taxicalluser.SenderControl
{
private static final java.lang.String DESCRIPTOR = "com.telcoware.taxicalluser.SenderControl";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.telcoware.taxicalluser.SenderControl interface,
 * generating a proxy if needed.
 */
public static com.telcoware.taxicalluser.SenderControl asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.telcoware.taxicalluser.SenderControl))) {
return ((com.telcoware.taxicalluser.SenderControl)iin);
}
return new com.telcoware.taxicalluser.SenderControl.Stub.Proxy(obj);
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
case TRANSACTION_startSend:
{
data.enforceInterface(DESCRIPTOR);
this.startSend();
reply.writeNoException();
return true;
}
case TRANSACTION_endSend:
{
data.enforceInterface(DESCRIPTOR);
this.endSend();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.telcoware.taxicalluser.SenderControl
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
public void startSend() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startSend, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void endSend() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_endSend, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startSend = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_endSend = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void startSend() throws android.os.RemoteException;
public void endSend() throws android.os.RemoteException;
}

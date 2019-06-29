/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package jp.co.sharp.android.fotarecovery;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFotaRecoveryService
extends IInterface {
    public void rebootFotaInstall(String var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IFotaRecoveryService {
        private static final String DESCRIPTOR = "jp.co.sharp.android.fotarecovery.IFotaRecoveryService";
        static final int TRANSACTION_rebootFotaInstall = 1;

        public Stub() {
            this.attachInterface((IInterface)this, "jp.co.sharp.android.fotarecovery.IFotaRecoveryService");
        }

        public static IFotaRecoveryService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("jp.co.sharp.android.fotarecovery.IFotaRecoveryService");
            if (iInterface != null && iInterface instanceof IFotaRecoveryService) {
                return (IFotaRecoveryService)iInterface;
            }
            return new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("jp.co.sharp.android.fotarecovery.IFotaRecoveryService");
                    return true;
                }
                case 1: 
            }
            parcel.enforceInterface("jp.co.sharp.android.fotarecovery.IFotaRecoveryService");
            this.rebootFotaInstall(parcel.readString());
            parcel2.writeNoException();
            return true;
        }

        private static class Proxy
        implements IFotaRecoveryService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "jp.co.sharp.android.fotarecovery.IFotaRecoveryService";
            }

            @Override
            public void rebootFotaInstall(String string2) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("jp.co.sharp.android.fotarecovery.IFotaRecoveryService");
                    parcel.writeString(string2);
                    this.mRemote.transact(1, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }
        }

    }

}


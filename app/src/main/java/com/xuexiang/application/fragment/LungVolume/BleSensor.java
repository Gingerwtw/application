package com.xuexiang.application.fragment.LungVolume;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
//import com.intellchildcare.cc.CCApplication;			只是蓝牙方面的
import com.xuexiang.application.MyApp;

import java.util.UUID;

import static java.util.UUID.fromString;

import androidx.annotation.RequiresApi;

public class BleSensor {

	//spp service
	public static UUID SPP_SERVICE_UUID = fromString("0000abf0-0000-1000-8000-00805f9b34fb");
//	public static UUID SPP_SERVICE_UUID = fromString("0000abf2-0000-1000-8000-00805f9b34fb");
	public static UUID SPP_W_UUID = fromString("0000abf1-0000-1000-8000-00805f9b34fb");
	public static UUID SPP_R_EN_UUID = fromString("0000abf2-0000-1000-8000-00805f9b34fb");
	public static UUID SPP_CMD_UUID = fromString("0000abf3-0000-1000-8000-00805f9b34fb");
	public static UUID SPP_CMD_R_EN_UUID = fromString("0000abf4-0000-1000-8000-00805f9b34fb");

	private static final int GATT_TIMEOUT = 2000; // milliseconds
	private static final String TAG = "EcgSensor";
	
	private int battLevel = 100; //电池电量信息
	private boolean connected = false;
	private BleDevice bleDevice= null;
	
	public BleSensor() {
		
	}

	public void setBleDevice(BleDevice device)
	{
		bleDevice = device;
	}

	private boolean mBusy = false;
	public boolean waitIdle(int i) {
		mBusy = false;
		i /= 10;
		while (--i > 0) {
			if (mBusy)
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			else
				break;
		}
		return i > 0;
	}
	
	public void setConnected(boolean state) {
		connected = state;
	}
	
	public void setConnectionParameters() {

	    if(!connected) {
			return;
		}
	}


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void enableSPPReceivedNotify(boolean enable) {
		Log.d("enableSPPReceivedNotify","enableSPPReceivedNotify");

		if(!connected) {
			return;
		}
		BluetoothGatt btGatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
		if(null == btGatt)
		{
			Log.e(TAG, "get BluetoothGatt error");
			return;
		}
		BluetoothGattService serv = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			serv = btGatt.getService(SPP_SERVICE_UUID);
			Log.d("voll", String.valueOf(serv));
		}
		if(null == serv ) {
			Log.e(TAG, "enable spp notify error");
			return;
		}
		BluetoothGattCharacteristic characteristic = serv
				.getCharacteristic(SPP_R_EN_UUID);
		Log.d("voll", String.valueOf(characteristic));
		//		CCApplication.getInstance().setBel(bleDevice);
//		CCApplication.getInstance().setUuid_service(characteristic.getService().getUuid().toString());
//		CCApplication.getInstance().setUuid_notify(characteristic.getUuid().toString());
		BleManager.getInstance().notify(
				bleDevice,
				characteristic.getService().getUuid().toString(),
				characteristic.getUuid().toString(),
//				"",
//				"",
				new BleNotifyCallback() {

					@Override
					public void onNotifySuccess() {
						Log.d("voll","onNotifySuccess");
						mBusy = false;
					}

					@Override
					public void onNotifyFailure(final BleException exception) {
						Log.d("voll","onNotifyFailure"+exception);
						mBusy = false;
					}

					@Override
					public void onCharacteristicChanged(byte[] data) {
						Log.d("voll","Changed "+data.length);

						mBusy = false;
//						Log.d(TAG,"Notify:"+ Utils.bytesToHex(data));
						Bundle dataB = new Bundle();
						dataB.clear();
						dataB.putString(BleDataProcess.EXTRA_UUID, SPP_R_EN_UUID.toString());
						String name = "";
						if(bleDevice != null) {
							name = bleDevice.getName();
						}
						dataB.putString(BleDataProcess.EXTRA_NAME,name);
						byte[] values = new byte[data.length];
						System.arraycopy(data, 0, values, 0, data.length);
						dataB.putByteArray(BleDataProcess.EXTRA_DATA, data);

						// 通过重写的Application类，在工具类中获得context
						// 通过BleDataProcess中的handler传递音频数据
						Handler handler = BleDataProcess.getInstance(MyApp.getContext()).mHandler;
						if(handler != null) {
							Message msg = handler.obtainMessage();
							msg.setData(dataB);
							handler.sendMessage(msg);
						}
					}
				});
		boolean rst = waitIdle(GATT_TIMEOUT);
		if(!rst) {
			Log.e(TAG, "enableSPPReceivedNotify fail");
		}
	}



	public boolean checkIdle() {
		return waitIdle(50);
	}
}

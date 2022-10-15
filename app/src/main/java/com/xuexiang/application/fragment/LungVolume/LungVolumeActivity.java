package com.xuexiang.application.fragment.LungVolume;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.xuexiang.application.R;
import com.xuexiang.application.activity.FaceActivity;
import com.xuexiang.application.activity.LungVolumeCalibrateActivity;
import com.xuexiang.application.activity.MainActivity;
import com.xuexiang.application.adapter.DeviceAdapter;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xrouter.utils.TextUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static java.util.UUID.fromString;

/**
 * 肺音fragment
 */
@Page(anim = CoreAnim.none)
public class LungVolumeActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "LungVolume";

    private Button lung_volume_scan_btn;
    private TextView lung_volume_text_hint;

    //    private ImageView img_loading;
//    private Animation operatingAnim;

//    private LinearLayout layout_setting;
//    private TextView txt_setting;
//    private EditText et_name, et_mac, et_uuid;
//    private Switch sw_auto;

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    private int whetherScanSucceed = 0;

    private DeviceAdapter mDeviceAdapter;

    private ProgressDialog progressDialog;
    private MaterialDialog.Builder warningDialog;

    private static Context mContext;

    private BleSensor sensorDevice = new BleSensor();

    public Context getContext(){return mContext;}

    public static UUID SPP_SERVICE_UUID = fromString("0000abf0-0000-1000-8000-00805f9b34fb");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung_volume);
        initView();

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

        mContext = getContext();
//        new Thread(new AudioRecordThread()).start();
        BleDataProcess.getInstance(LungVolumeActivity.this).setSendHandler(mhandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        showConnectedDevice();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        lung_volume_scan_btn = (Button) findViewById(R.id.lung_volume_scan);
        ImageButton btn_back = findViewById(R.id.lung_volume_toolbar_back);
        lung_volume_text_hint = findViewById(R.id.lung_volume_text_hint);

        lung_volume_scan_btn.setText(getString(R.string.start_scan));
        lung_volume_scan_btn.setOnClickListener(this);
        btn_back.setOnClickListener(this);

//        et_name = (EditText) findViewById(R.id.et_name);
//        et_mac = (EditText) findViewById(R.id.et_mac);
//        et_uuid = (EditText) findViewById(R.id.et_uuid);
//        sw_auto = (Switch) findViewById(R.id.sw_auto);
//
//        layout_setting = (LinearLayout) findViewById(R.id.layout_setting);
//        txt_setting = (TextView) findViewById(R.id.txt_setting);
//        txt_setting.setOnClickListener(this);
//        layout_setting.setVisibility(View.GONE);
//        txt_setting.setText(getString(R.string.expand_search_settings));
//
//        img_loading = (ImageView) findViewById(R.id.img_loading);
//        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        operatingAnim.setInterpolator(new LinearInterpolator());

        progressDialog = new ProgressDialog(this);

        mDeviceAdapter = new DeviceAdapter(this);
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
//                if (BleManager.getInstance().isConnected(bleDevice)) {
//                    Intent intent = new Intent(MainActivity.this, OperationActivity.class);
//                    intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
//                    startActivity(intent);
//                }
            }
        });

        ListView listView_device = (ListView) findViewById(R.id.list_device);
        listView_device.setAdapter(mDeviceAdapter);

        warningDialog = new MaterialDialog.Builder(LungVolumeActivity.this)
                .positiveText("确认");
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if(BleDataProcess.MSG_WAVE_DATAS == message.what) {
                Log.d(TAG, "handleMessage");

                lung_volume_text_hint.setText("音频传输成功");
            }
        }
    };

//    class AudioRecordThread implements Runnable {
//        public final static int MSG_RECORD_TIMEOUT =1;
//        @SuppressLint("HandlerLeak")
//        @Override
//        public void run() {
//            Looper.prepare();
//            Looper mLooper = Looper.myLooper();
//            Handler audioRecordHandler = new Handler(mLooper) {
//                @Override
//                public void handleMessage(Message msg) {
//                    Log.d(TAG,"audioRecordHandler received msg "+msg.what);
//                    if(BleDataProcess.MSG_WAVE_DATAS == msg.what) {
//                        Log.d(TAG, "handleMessage");
//                        lung_volume_text_hint.setText("音频传输成功");
//                    }
//                }
//            };
//            BleDataProcess.getInstance(LungVolumeActivity.this).setSendHandler(audioRecordHandler);
//            // 启动子线程消息循环队列
//            Looper.loop();
//
//        }
//    }



    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lung_volume_scan:
                if (lung_volume_scan_btn.getText().equals(getString(R.string.start_scan))) {
                    checkPermissions();
                } else if (lung_volume_scan_btn.getText().equals(getString(R.string.stop_scan))) {
                    BleManager.getInstance().cancelScan();
                }
                break;
            case R.id.lung_volume_toolbar_back:
                finish();
                break;
//            case R.id.txt_setting:
//                if (layout_setting.getVisibility() == View.VISIBLE) {
//                    layout_setting.setVisibility(View.GONE);
//                    txt_setting.setText(getString(R.string.expand_search_settings));
//                } else {
//                    layout_setting.setVisibility(View.VISIBLE);
//                    txt_setting.setText(getString(R.string.retrieve_search_settings));
//                }
//                break;
        }
    }

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            warningDialog.title("未打开蓝牙").content("请打开手机蓝牙").show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    warningDialog.title("未打开定位服务").content("请打开手机定位服务").show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void setScanRule() {
//        String[] uuids;
//        String str_uuid = et_uuid.getText().toString();
//        if (TextUtils.isEmpty(str_uuid)) {
//            uuids = null;
//        } else {
//            uuids = str_uuid.split(",");
//        }
//        UUID[] serviceUuids = null;
//        if (uuids != null && uuids.length > 0) {
//            serviceUuids = new UUID[uuids.length];
//            for (int i = 0; i < uuids.length; i++) {
//                String name = uuids[i];
//                String[] components = name.split("-");
//                if (components.length != 5) {
//                    serviceUuids[i] = null;
//                } else {
//                    serviceUuids[i] = UUID.fromString(uuids[i]);
//                }
//            }
//        }
//
//        String[] names;
//        String str_name = et_name.getText().toString();
//        if (TextUtils.isEmpty(str_name)) {
//            names = null;
//        } else {
//            names = str_name.split(",");
//        }
//
//        String mac = et_mac.getText().toString();
//
//        boolean isAutoConnect = sw_auto.isChecked();

//        UUID [] uuids = new UUID[1];
//        uuids[0] = SPP_SERVICE_UUID;
//        String mac = null;
        String[] names = new String[1];
        names[0] = "ESP_SPP_SER001";

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(null)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setDeviceMac(null)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(true)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(5000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        Log.d("Lung volume","setScan");
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
//                img_loading.startAnimation(operatingAnim);
//                img_loading.setVisibility(View.VISIBLE);
                lung_volume_scan_btn.setText(getString(R.string.scanning));
                whetherScanSucceed = 0;
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
//                img_loading.clearAnimation();
//                img_loading.setVisibility(View.INVISIBLE);


                Log.d(TAG, "onScanFinished, number of device"+mDeviceAdapter.getCount());
                if(mDeviceAdapter.getCount() == 0){
                    warningDialog.title("未扫描到听诊器设备").content("请打开听诊器后重新扫描").show();
                    lung_volume_scan_btn.setText(getString(R.string.start_scan));
                }else {
                    lung_volume_scan_btn.setText("蓝牙已连接");
                    lung_volume_text_hint.setText("按下听诊器的蓝牙按钮，开始传输音频");
                }
            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        Log.d("connect","connect");
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
//                img_loading.clearAnimation();
//                img_loading.setVisibility(View.INVISIBLE);
                lung_volume_scan_btn.setText(getString(R.string.start_scan));
                progressDialog.dismiss();

//                XToastUtils.info("连接失败");
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                whetherScanSucceed = 1;
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                readRssi(bleDevice);//获取设备的实时信号强度Rssi
                setMtu(bleDevice, 263);//设置最大传输单元MTU
                onConfigFirmware();

//                changeBleState(BleControl.BLE_CONNECTED);
//                setBleState();
//                onConfigFirmware();
//                mBleDeviceInfo = bleDevice;
//                sensorDevice.setBleDevice(mBleDeviceInfo);
//                sensorDevice.setConnected(true);

                Log.d("onConnectSuccess","onConnectSuccess, number of device"+mDeviceAdapter.getCount());
//                sensorDevice.enableSPPReceivedNotify(true);
                sensorDevice.setBleDevice(bleDevice);
                sensorDevice.setConnected(true);

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
//                    Toast.makeText(MainActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
//                    XToastUtils.info("连接断开了");
                } else {
//                    Toast.makeText(MainActivity.this, getString(R.string.disconnected), Toast.LENGTH_LONG).show();
//                    XToastUtils.info("断开连接");
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }
                lung_volume_text_hint.setText("打开听诊器后，点击按钮连接蓝牙");
            }
        });
    }

    private void readRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.i(TAG, "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i(TAG, "onRssiSuccess: " + rssi);
            }
        });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Log.i(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Log.i(TAG, "onMtuChanged: " + mtu);
            }
        });
    }

    private void onConfigFirmware() {
        Thread newThread =  new Thread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                configFirmware();
            }
        });
        newThread.start(); //启动线程
    }

    /**
     * 对传感器进行配置
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void configFirmware() {
        int timeOutCnt = 0;
        int step = 1;
        while((timeOutCnt <10)&&(step < 5)) {
            if(3== step) {
                sensorDevice.setConnectionParameters();
                step += 1;
            }else if(1== step) {
                //ecgDevice.readBattLevel();
                step += 1;
            }else if(4== step) {
                sensorDevice.enableSPPReceivedNotify(true);
                step += 1;
            }else if(2== step) {
                //ecgDevice.setTime();
                step += 1;
            }
            timeOutCnt = 0;
            while(!sensorDevice.checkIdle()) {
                timeOutCnt ++;
                if(timeOutCnt >= 10) {
                    break;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

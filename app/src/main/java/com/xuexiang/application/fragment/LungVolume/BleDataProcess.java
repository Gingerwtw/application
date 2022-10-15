package com.xuexiang.application.fragment.LungVolume;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;

public class BleDataProcess extends Thread {

    public final static String EXTRA_DATA = "BleDataProcess.EXTRA_DATA";
    public final static String EXTRA_UUID = "BleDataProcess.EXTRA_UUID";
    public final static String EXTRA_NAME = "BleDataProcess.EXTRA_NAME";
    public final static int MSG_WAVE_DATAS =2;

    protected static final String TAG = "BleDataProcess";
    private static BleDataProcess instance = null;
    public Handler mHandler = null;
    private Looper mLooper;

    private static Context mContext = null;

    private Handler sendHandler = null;

    public BleDataProcess() {

    }

    public void setSendHandler(Handler handler){
        sendHandler = handler;
    }

    public static BleDataProcess getInstance(Context context) {
        if (null == instance) {
            instance = new BleDataProcess();
            instance.start();
        }
        mContext = context;
        return instance;
    }

    public void exit() {
        if(mLooper != null) {
            mLooper.quit();
            mLooper = null;
            instance = null;
        }
    }

    // 接收从BleSensor过来的数据
    public void run() {
        Looper.prepare();
        mLooper = Looper.myLooper();
        mHandler = new Handler(mLooper) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                String typeStr = data.getString(EXTRA_UUID);
                if (typeStr.equals(BleSensor.SPP_R_EN_UUID.toString())) {
                    byte[] value = data.getByteArray(EXTRA_DATA);
                    String name = data.getString(EXTRA_NAME);
                    receivedBleDatas(value,name);
                }
                else {
                    Log.d(TAG, "Unknow data");
                }

            }
        };

        // 启动子线程消息循环队列
        Looper.loop();
    }

    private void receivedBleDatas(byte[] value,String deviceName){
        if(null == value) {
            Log.d(TAG, "receivedBleDatas : value is null");
            return;
        }
        if(value.length > 4) {
            if ((value[0] == '#') && (value[1] == '#')) {
                int totalNum = value.length - 4;
                byte[] rawData = new byte[totalNum];
                for (int i = 0; i < totalNum; i++) {
                    rawData[i] = value[i + 4];
                }
                parseWaveDatas(rawData);
            }
        }
    }

    //	private void parseWaveDatas(byte[] value){
//		byte[] rawData = new byte[value.length];
//		for(int i=0;i<value.length/2;i++){
//			int tmp = (value[i*2]&0xff)|((value[i*2+1]&0xff)<<8);
//			byte[] rst = new byte[2];
//			rst[0] =  (byte)(tmp & 0xFF);
//			rst[1] =  (byte)((tmp>>8) & 0xFF);
//
//			rawData[i*2]=rst[0];
//			rawData[i*2+1]=rst[1];
//		}
//		if(sendHandler != null){
//			Bundle dataB = new Bundle();
//			dataB.clear();
//			dataB.putByteArray(BleDataProcess.EXTRA_DATA, rawData);
//			Message msg = sendHandler.obtainMessage();
//			msg.what = MSG_WAVE_DATAS;
//			msg.setData(dataB);
//			sendHandler.sendMessage(msg);
//		}
//		Log.d(TAG,"wave datas cnt:"+ rawData.length);
//	}
/*
	double[] BH = {
			0.0003081210415822177, 	0.0, -0.0009243631247466532, 0.0,
			0.0009243631247466532, 	0.0, -0.0003081210415822177
			};
	double[] AH  = {
			1.0, -5.7104243302048765, 13.599014827618793, -17.28792918070625,
			12.374047597409104, -4.7282437717319965, 0.7535348707123358
			};

	double[] BL = {
			0.0028981946337214293,	0.0, -0.008694583901164288,	0.0,
			0.008694583901164288,	0.0, -0.0028981946337214293
			};

	double[] AL = {
			1.0, -5.290745940163181,11.753782557078337, -14.041612379844732,
			9.517424399916711,	-3.4709022351128955,0.5320753683120917
			};
*/
    // 心音滤波参数
    double[]BH = {
            // 20-200
/*               		0.0003081210415822177, 	0.0, -0.0009243631247466532, 0.0,
                		0.0009243631247466532, 	0.0, -0.0003081210415822177
                		*/
            // 40-150
            0.000074068986821906, 0.0, -0.000222206960465719, 0.0,
            0.000222206960465719, 0.0, -0.000074068986821906

    };
    double[]AH  = {
            // 20-200
            /*               			1.0, -5.7104243302048765, 13.599014827618793, -17.28792918070625,
                       12.374047597409104, -4.7282437717319965, 0.7535348707123358
                       */
            // 40-150
            1.000000000000000, -5.816478759797879, 14.109042366221395, -18.269433765222434,
            13.318928739933627, -5.183329536833532, 0.841271002243478
    };
    //肺音滤波参数
    double[] BL = {
            // 100-500
            /*               0.0028981946337214293,	0.0, -0.008694583901164288,	0.0,
                           0.008694583901164288,	0.0, -0.0028981946337214293
           */
//			// 120-450
//			0.001706723119875, 0.0, -0.005120169359625, 0.0,
//			0.005120169359625, 0.0, -0.001706723119875

//			// 80-250
//			0.000261477746934203 , 0.0 , -0.000784433240802609 ,0.0,
//			0.000784433240802609,0.0,  -0.000261477746934203
            //20-250
//			0.000619945850000, 0.0,  -0.001859837550000 , 0.0,   0.001859837550000, 0.0,  -0.000619945850000
            //40-250
            //		0.000478723046218 , 0.0,  -0.001436169138653, 0.0,   0.001436169138653,  0.0,  -0.000478723046218

            //120-300
            0.000308121041616449 , 0.0,  -0.000924363124849346 ,0.0 , 0.000924363124849346 ,0.0,-0.000308121041616449
    };
    double[] AL = {
            // 100-500
/*                1.0, -5.290745940163181,11.753782557078337, -14.041612379844732,
                9.517424399916711,	-3.4709022351128955,0.5320753683120917
                */
//			// 120-450
//			1.000000000000000, -5.391439783193984, 12.207190008693054, -14.860764788149357,
//			10.260840957938246, -3.810413892483608, 0.594616240753234

            //120-300
            1.000000000000000,  -5.654016984862635,  13.385102007086131, -16.982490992730334,
            12.179418967603519,  -4.681538367041898,   0.753534870712335
//			//80-250
//			1.000000000000000,  -5.697778859386098 , 13.565280526490177, -17.273820727615448,
//			12.408447698781757,  -4.767620443004152,   0.765493449853753
//			//20-250
//			1.000000000000000 , -5.630477844981993,  13.225371769979724, -16.589487272549526 ,
//			11.721210194767890,  -4.423053462786468 ,  0.696436640236720
            //40-250
            //		1.000000000000000,  -5.652978682072008,  13.338345916510864, -16.815514514607880,
            //		11.946665659482742,  -4.535266727872783,   0.718748548598679
    };




    double[] x = new double[7];
    double[] yH = new double[7];
    double[] yL = new double[7];

    double lastdd;

    private void parseWaveDatas(byte[] value){
        byte[] rawData = new byte[value.length];
        for(int i=0;i<value.length/2;i++) {
/*
			int tmp = (value[i*2]&0xff)|((value[i*2+1]&0xff)<<8);
			byte[] rst = new byte[2];
			rst[0] =  (byte)(tmp & 0xFF);
			rst[1] =  (byte)((tmp>>8) & 0xFF);

			rawData[i*2]=rst[0];
			rawData[i*2+1]=rst[1];

 */
            for (int j = 6; j>0; j--) {
                x[j] = x[j-1];
                yH[j] = yH[j-1];
                yL[j] = yL[j-1];
            }
//			x[0] = (value[i*2]&0xff)|((value[i*2+1]&0xff)<<8);
            short s = (short) (value[i*2] & 0x00FF |((value[i*2+1] & 0x00FF) << 8) );

            // wgb增加，音频放大，且消去破音
            int gainValue =1;
//            if(CCApplication.getInstance().getIsMeasure()==0){
//                gainValue = 4;
//            }
//            else
            {
                gainValue = 4;
            }
	/*		int sInt = s;
			sInt *= gainValue;
			if (sInt > 32767){
				s = 32767;
			}else if (sInt < -32768){
				s = -32768;
			}else{
				s = (short) sInt;
			}
*/
            x[0] = s / 32768f; // 32768 = 2^15

            //  心音滤波
//			yH[0] = x[0]*BH[0] + x[1]*BH[1] + x[2]*BH[2] + x[3]*BH[3] + x[4]*BH[4] + x[5]*BH[5] + x[6]*BH[6]
//					- yH[1]*AH[1] - yH[2]*AH[2] - yH[3]*AH[3] - yH[4]*AH[4] - yH[5]*AH[5] - yH[6]*AH[6];

            //  肺音滤波
//			yL[0] = x[0]*BL[0] + x[1]*BL[1] + x[2]*BL[2] + x[3]*BL[3] + x[4]*BL[4] + x[5]*BL[5] + x[6]*BL[6]
//					- yL[1]*AL[1] - yL[2]*AL[2] - yL[3]*AL[3] - yL[4]*AL[4] - yL[5]*AL[5] - yL[6]*AL[6];

            //数据双精度浮点数转换为16位整数
//            // 从yH[0] or yL[0]中选一个，往后面传
//            if(CCApplication.getInstance().getIsMeasure()==0){

                //  心音滤波
                yH[0] = x[0]*BH[0] + x[1]*BH[1] + x[2]*BH[2] + x[3]*BH[3] + x[4]*BH[4] + x[5]*BH[5] + x[6]*BH[6]
                        - yH[1]*AH[1] - yH[2]*AH[2] - yH[3]*AH[3] - yH[4]*AH[4] - yH[5]*AH[5] - yH[6]*AH[6];
                lastdd=yH[0];
//            }else {
//                //  肺音滤波
//                yL[0] = x[0]*BL[0] + x[1]*BL[1] + x[2]*BL[2] + x[3]*BL[3] + x[4]*BL[4] + x[5]*BL[5] + x[6]*BL[6]
//                        - yL[1]*AL[1] - yL[2]*AL[2] - yL[3]*AL[3] - yL[4]*AL[4] - yL[5]*AL[5] - yL[6]*AL[6];
//                lastdd=yL[0];
//            }
            //	int tmp = (int)(lastdd*32768f + 0.5);

            //220721 new code
            int tmp = (int) (lastdd * gainValue * 32768f + 0.5);

            // wgb增加，去除滤波后的破音
            if (tmp > 32767){
                tmp = 32767;
            }else if (tmp < -32768){
                tmp = -32768;
            }

            byte[] rst = new byte[2];
            rst[0] =  (byte)(tmp & 0xFF);
            rst[1] =  (byte)((tmp>>8) & 0xFF);
            rawData[i*2]=rst[0];
            rawData[i*2+1]=rst[1];
        }
        if(sendHandler != null){
            Bundle dataB = new Bundle();
            dataB.clear();
            dataB.putByteArray(BleDataProcess.EXTRA_DATA, rawData);
            Message msg = sendHandler.obtainMessage();
            msg.what = MSG_WAVE_DATAS;
            msg.setData(dataB);
            sendHandler.sendMessage(msg);
        }
        Log.d(TAG,"wave datas cnt:"+ rawData.length+"data:"+ Arrays.toString(rawData));
    }

}


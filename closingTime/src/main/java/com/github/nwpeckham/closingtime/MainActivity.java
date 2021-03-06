package com.github.nwpeckham.closingtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    ConsumerIrManager irManager;
    Method irWrite;
    SparseArray<String> irData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        irData = new SparseArray<String>();
        irData.put(
                R.id.buttonVizio,
                "0000 006D 0022 0002 0157 00AC 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0689 0157 0056 0015 0E94");
        irData.put(
                R.id.buttonFixVizio,
                "0000 006D 0022 0002 0157 00AC 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0689 0157 0056 0015 0E94");
        irData.put(
                R.id.buttonSamsung,
                "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e");
        irData.put(
                R.id.buttonSanyo,
               "0000 006c 0022 0002 0155 00aa 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 05f9 0155 0057 0015 0e30");
        irData.put(
                R.id.buttonPanasonic,
                "0000 0070 0000 0032 0081 0040 0012 0012 0012 0030 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0030 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0030 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0030 0012 0012 0012 0030 0012 0030 0012 0030 0012 0030 0012 0012 0012 0012 0012 0030 0012 0012 0012 0030 0012 0030 0012 0030 0012 0030 0012 0012 0012 0030 0012 0aba");
        irData.put(
                R.id.buttonDynex,
                "0000 006D 0000 0022 0154 00A9 0015 0015 0015 003F 0015 003F 0015 0015 0015 0015 0015 0015 0015 0015 0015 003F 0015 003F 0015 0015 0015 003F 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003F 0015 003F 0015 003F 0015 003F 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003F 0015 003F 0015 003F 0015 003F 0015 02F8");
        irManager = (ConsumerIrManager)this.getSystemService(CONSUMER_IR_SERVICE);
    }

    public void irSend(View view) {
        String data =  irData.get(view.getId());
        if (data != null) {
            try {
                irManager.transmit(getFrequency(data),getCodes(data));
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this,"You do not have the rights.",5);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,e.toString(),5);
            }
        }
    }

    public void spamThatShit(View view){
        for(int i = 0; i < irData.size(); i++){
            int key = irData.keyAt(i);
            String data = irData.valueAt(i);
                Toast.makeText(this, data,3);
                irManager.transmit(getFrequency(data),getCodes(data));
            }
            Toast.makeText(this,"PEWPEWPEWPEW",2);
        }

    protected int getFrequency(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency

        frequency = (int) (1000000 / (frequency * 0.241246));
        return frequency;
    }

    protected int[] getCodes(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        list.remove(0); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        int[] irCodes = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            irCodes[i] = Integer.parseInt(list.get(i), 16);
        }

        return irCodes;
    }

}

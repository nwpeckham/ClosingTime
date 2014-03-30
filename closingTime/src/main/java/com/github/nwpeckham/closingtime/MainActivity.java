package com.github.nwpeckham.closingtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
    Object irdaService;
    Method irWrite;
    SparseArray<String> irData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        irData = new SparseArray<String>();
        irData.put(
                R.id.buttonVizio,
                hex2dec("0000 006D 0022 0002 0157 00AC 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0041 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0041 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0689 0157 0056 0015 0E94"));
        irData.put(
                R.id.buttonSamsung,
                hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e"));
        irData.put(
                R.id.buttonSanyo,
                hex2dec("0000 006c 0022 0002 0155 00aa 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 05f9 0155 0057 0015 0e30"));
        irData.put(
                R.id.buttonPanasonic,
                hex2dec("0000 0070 0000 0032 0081 0040 0012 0012 0012 0030 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0030 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0030 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0012 0030 0012 0012 0012 0030 0012 0030 0012 0030 0012 0030 0012 0012 0012 0012 0012 0030 0012 0012 0012 0030 0012 0030 0012 0030 0012 0030 0012 0012 0012 0030 0012 0aba"));
        irData.put(
                //TODO: Test the Dynex code
                //TODO: Learn how IR hex codes work. More specifically, how they are encoded across formats (or rather, what formats are they encoded?) And, most importantly, how do I get these fucking Dynex TVs to shut off.
                R.id.buttonDynex,
                hex2dec("0000 006D 0022 0002 0155 00AA 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0040 0015 05ED 0155 0055 0015 0E47"));
        irInit();
    }

    public void irInit() {
        irdaService = this.getSystemService("irda");
        Class c = irdaService.getClass();
        Class p[] = { String.class };
        try {
            irWrite = c.getMethod("write_irsend", p);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void irSend(View view) {
        String data = "";
        if (view.getId() == R.id.buttonSend) {
            EditText codeBox = (EditText)findViewById(R.id.textCustomCode);
            data = hex2dec(codeBox.getText().toString());
        } else {
            data = irData.get(view.getId());
        }
        if (data != null) {
            try {
                irWrite.invoke(irdaService, data);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    protected String hex2dec(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }

}

package com.example.simplemanagerapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Add the SimpleManager import
import com.example.simplemanager.SimpleManager;

public class SimpleManagerActivity extends Activity
{
    private static final String TAG = "simplemanager-app";
    EditText int1, int2, str1;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    
    // Add SimpleManager instance
    SimpleManager mSimpleManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Initialize SimpleManager
        mSimpleManager = new SimpleManager();
        
        int1 = (EditText) findViewById(R.id.integer1);
        int2 = (EditText) findViewById(R.id.integer2);
        str1 = (EditText) findViewById(R.id.string1);
        tv1 = (TextView) findViewById(R.id.textview1);
        tv2 = (TextView) findViewById(R.id.textview2);
        tv3 = (TextView) findViewById(R.id.textview3);

        writeIrrelevantPrivateFile(this);
    }

    public void onButtonAddints(View view)
    {
        int a = 0;
        int b = 0;
        int sum = 0;
        try {
            a = Integer.parseInt(int1.getText().toString());
        } catch (NumberFormatException e) { }

        try {
            b = Integer.parseInt(int2.getText().toString());
        } catch (NumberFormatException e) { }

        // Call SimpleManager addInts method
        if (mSimpleManager != null) {
            sum = mSimpleManager.addInts(a, b);
            Log.i(TAG, "Added " + a + " + " + b + " = " + sum);
        } else {
            Log.e(TAG, "SimpleManager is null!");
            sum = a + b; // fallback
        }

        tv1.setText(Integer.toString(sum));
    }

    public void onButtonEchostring(View view)
    {
        String str = str1.getText().toString();
        String result = "TBD";
        
        // Call SimpleManager echoString method
        if (mSimpleManager != null) {
            result = mSimpleManager.echoString(str);
            Log.i(TAG, "Echoed string: " + result);
        } else {
            Log.e(TAG, "SimpleManager is null!");
            result = "Error: SimpleManager not available";
        }
        
        tv2.setText(result);
    }

    public void onButton3(View view)
    {
        // TBD: call the 3rd function in ISimpleManager and write the string
        // to tv3
        // Example: if there's a third method in SimpleManager
        // String result = mSimpleManager.someThirdMethod();
        // tv3.setText(result);
        
        tv3.setText("Third function - Not implemented yet");
    }

    private void writeIrrelevantPrivateFile(Context context)
    {
        String filename = "myfile.txt";
        String fileContents = "Hello world!\n";
        /** Note: getFilesDir returns path to the directory where files
          created with openFileOutput will be placed */
        Log.i(TAG, "private files directory is " + context.getFilesDir());

        try {
            /** From API level 24 (N/7) Context.MODE_PRIVATE is required */
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            fos.close();
            Log.i(TAG, "File written successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error while writing file " + e);
        }
    }
}
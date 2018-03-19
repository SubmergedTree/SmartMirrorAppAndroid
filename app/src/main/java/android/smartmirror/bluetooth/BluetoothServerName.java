package android.smartmirror.bluetooth;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public class BluetoothServerName {
    private String name = "raspberrypi";
    private final String filename = "deviceBluetoothName.dbn";
    private boolean isLoaded = false;
    private Context context = null;

    public void setApplicationContext(Context context) {
        this.context = context;
    }

    public void setName(String name) {
        if(context == null) {
            Log.e("BluetoothServerName", "No context is set. Unable to save new name to file");
        } else {
            Log.i("BSN saves:", name);
            this.save();
        }
        this.name = name;
    }

    public String getName() {
        if (!isLoaded) {
            if(context == null) {
                Log.e("BluetoothServerName", "No context is set. Unable to load name to file");
            } else {
                this.load();
                isLoaded = true;
            }
        }
        return name;
    }

    private void save() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(name);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void load() {
        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                name = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}

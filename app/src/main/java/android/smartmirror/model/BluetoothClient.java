package android.smartmirror.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.smartmirror.exceptions.NoBluetoothSupportedException;
import android.smartmirror.model.BluetoothName;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by jannik on 06.03.18.
 */

public class BluetoothClient {
    private BluetoothAdapter bluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private final UUID MY_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
    private ClientThread clientThread;

    public BluetoothClient(AppCompatActivity activity) throws NoBluetoothSupportedException {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new NoBluetoothSupportedException();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            activity.startActivity(intent);
        }
    }

    public void searchMirror(BluetoothName bluetoothName) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                //Log.e("deviceName",deviceName);
                //Log.e("deviceHardwareAdress",deviceHardwareAddress);
                if (deviceName.equals(bluetoothName.toString())) {
                    bluetoothAdapter.cancelDiscovery();
                    clientThread = new ClientThread(device);
                }
            }
        }
        if(!(clientThread == null)) {
            Log.e(":","start connectTread");
            clientThread.start();
            //connectThread.write((loremIpsum).getBytes());
        }
    }

    private class ClientThread extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        ClientThread(BluetoothDevice device) {
            this.device = device;
        }

        @Override
        public void run() {
        }

        public void write() {
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!socket.isConnected()) {
                try {
                    socket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

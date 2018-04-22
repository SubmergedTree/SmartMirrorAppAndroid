package android.smartmirror.model.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.smartmirror.model.bluetooth.exception.NoBluetoothSupportedException;
import android.smartmirror.model.bluetooth.exception.NoFittingUUIDException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public class BluetoothClient {
    private static BluetoothAdapter bluetoothAdapter;
    private static ClientThread clientThread;
    private static BluetoothSocket socket;
    private static OutputStream out;
    private static InputStream in;

    public boolean activateBluetooth() throws NoBluetoothSupportedException {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new NoBluetoothSupportedException();
        }
        return bluetoothAdapter.isEnabled();
    }

    public boolean searchDevice(String name) {
        try {
            activateBluetooth();
        } catch (NoBluetoothSupportedException e) {
            e.printStackTrace();
        }
        if (bluetoothAdapter == null) {
            return false;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            String deviceName = device.getName();
            if(deviceName.equals(name)) {
                Log.e("search Mirror", "no fitting uuid");
                try {
                    Connector connector = new Connector(device);
                    connector.start();
                } catch (NoFittingUUIDException e) {
                    Log.e("search Mirror", "no fitting uuid");
                    return false;
                }
            }
        }
        return true;
    }


    public void send(String msg) {
        clientThread.write(msg.getBytes());
    }

    public void cancel() {
        Log.e("BLuetooth client", "cancel in BluetoothClient");
        if(clientThread != null)
            clientThread.cancel();
    }

    public boolean isConnected() {
        return clientThread != null && clientThread.isConnected();
    }


    private static class Connector extends Thread {


        private final UUID MY_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

        Connector(BluetoothDevice device) throws NoFittingUUIDException{
            createRfcommSocket(device);
        }

        private void createRfcommSocket(BluetoothDevice device) throws NoFittingUUIDException{
            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                throw new NoFittingUUIDException();
            }
        }

        @Override
        public void run() {
            if(!socket.isConnected()) {
                try {
                    socket.connect();
                    out = socket.getOutputStream();
                    in = socket.getInputStream();
                } catch (IOException e) {
                    return;
                }
                Log.i("Connector", "Start clientThread");
                clientThread =  new ClientThread();
                clientThread.start();
                Connection.use().invokeCallbacks(Connection.Callbacks.ON_CONNECTED);
            }
        }
    }

    private static class ClientThread extends Thread {
        private AtomicBoolean isCanceled;

        ClientThread() {
            this.isCanceled = new AtomicBoolean(false);
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();
            byte[] buffer = new byte[1024];

            Log.i("ClientThread", "isRunning");
            while(!isCanceled.get()) {
                if(!isConnected()) {
                    break;
                }
                read(buffer);
            }
            Log.e("Client:", Boolean.toString(isCanceled.get()) + " dead");

            try {
                Log.e("client thread", "closed");
                if(!this.isCanceled.get()) {
                    out.close();
                    in.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Does not run in the same thread as read.
        // for large data paralyse this task. e.g. ASynchTask ?
        private void write(byte[] bytes) {
            if(isConnected())
                try {
                    out.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        private class ReadVariables {
             long toRead = 0;
             long hasRead = 0;
             String wholeReceived = "";
             boolean isHeader = true;
             final char headerChar = 'H';
             void reset() {
                toRead = 0;
                hasRead = 0;
                isHeader = true;
            }
        }
        private ReadVariables readVars = new ReadVariables();

        private void read(byte[] buffer) {
            int hasReceivedOnThisBuffer = 0;
            try {
                hasReceivedOnThisBuffer = in.read(buffer);
                String receivedFromSender = new String(buffer);
                receivedFromSender = receivedFromSender.substring(0, hasReceivedOnThisBuffer);
                readVars.wholeReceived = readVars.wholeReceived.concat(receivedFromSender);
               // Log.e("whole", readVars.wholeReceived);

                if(readVars.isHeader) {
                    for(int i = (int)readVars.hasRead; i < readVars.wholeReceived.length(); i++) {
                        if(readVars.wholeReceived.charAt(i) == readVars.headerChar) {
                            String bufToRead = readVars.wholeReceived.substring(0, i);
                            try {
                                readVars.toRead = Long.parseLong(bufToRead);
                                readVars.isHeader = false;
                                //Log.e("toRead", Long.toString(readVars.toRead));
                            } catch (NumberFormatException e) {
                                Log.e("Bluetooth Client:",e.getMessage());
                            }
                            readVars.wholeReceived = readVars.wholeReceived.substring(i + 1);
                            hasReceivedOnThisBuffer = readVars.wholeReceived.length();
                            //readVars.hasRead = hasReceivedOnThisBuffer;
                            break;
                        }
                    }
                }
                readVars.hasRead += hasReceivedOnThisBuffer;
                if(!readVars.isHeader) {
                    if(readVars.hasRead == readVars.toRead) {
                        Log.e("client read final",readVars.wholeReceived);
                        Connection.use().invokeCallbacks(Connection.Callbacks.RECEIVE,readVars.wholeReceived);
                        readVars.wholeReceived = readVars.wholeReceived.substring((int)readVars.toRead);
                        readVars.reset();
                    }
                }
            } catch (IOException e) {
                isCanceled.set(true);
                Log.e("read", "exception");
                Log.e("isConnected", Boolean.toString(this.isConnected()));
                Connection.use().invokeDisconnectCallback();
                //e.printStackTrace();
            }
        }

        synchronized boolean isConnected() {
            return socket.isConnected();
        }

        synchronized void cancel() {
            if(!this.isCanceled.get()) {
                this.isCanceled.compareAndSet(false,true);
                try {
                    out.close();
                    in.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

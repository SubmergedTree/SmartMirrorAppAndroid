package android.smartmirror.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.smartmirror.bluetooth.exception.NoBluetoothSupportedException;
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
    private BluetoothAdapter bluetoothAdapter;
    private final UUID MY_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
    private ClientThread clientThread;

    public boolean activateBluetooth() throws NoBluetoothSupportedException {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new NoBluetoothSupportedException();
        }
        return bluetoothAdapter.isEnabled();
    }

    public boolean searchMirror(String name) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            String deviceName = device.getName();
            if(deviceName.equals(name)) {
                this.clientThread = new ClientThread(device);
                return true;
            }
        }
        return false;
    }

    public void start() {
        if(!(clientThread == null)) {
            clientThread.start();
        }
    }

    public void send(String msg) {
        //Log.e("Status: ","send now");
        clientThread.write(msg.getBytes());
        //Log.e("Status: ","dataSend");
    }

    public void cancel() {
        Log.e("BLuetooth client", "cancel in BluetoothClient");
        if(clientThread != null)
            clientThread.cancel();
    }

    public boolean isConnected() {
        if (clientThread == null) {
            return false;
        }
        return clientThread.isConnected();
    }

    private class ClientThread extends Thread {
        private BluetoothSocket socket;
        private OutputStream out;
        private InputStream in;
        //  boolean isCanceled;
        private AtomicBoolean isCanceled;

        public ClientThread(BluetoothDevice device) {
            this.isCanceled = new AtomicBoolean(false);
            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!socket.isConnected()) {
                try {
                    socket.connect();
                    out = socket.getOutputStream();
                    in = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();
            byte[] buffer = new byte[1024];

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
        synchronized private void write(byte[] bytes) {
            if(isConnected())
                try {
                    out.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        private class ReadVariables {
            public long toRead = 0;
            public long hasRead = 0;
            public String wholeReceived = "";
            public boolean isHeader = true;
            public final char headerChar = 'H';
            public void reset() {
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
                        // Log.e("client read final",readVars.wholeReceived);
                        Connection.use().receive(readVars.wholeReceived);
                        readVars.wholeReceived = readVars.wholeReceived.substring((int)readVars.toRead);
                        readVars.reset();
                    }
                }
            } catch (IOException e) {
                Log.e("read", "exception");
                e.printStackTrace();
            }
        }

        synchronized public boolean isConnected() {
            return socket.isConnected();
        }

        synchronized public void cancel() {
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

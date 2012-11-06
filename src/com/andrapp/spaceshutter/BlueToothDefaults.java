package com.andrapp.spaceshutter;

public class BlueToothDefaults {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 11;
    public static final int MESSAGE_READ = 22;
    public static final int MESSAGE_WRITE = 33;
    public static final int MESSAGE_DEVICE_NAME = 44;
    public static final int MESSAGE_TOAST = 55;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
}

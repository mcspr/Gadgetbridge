package nodomain.freeyourgadget.gadgetbridge.service.devices.miband;

import java.util.Calendar;
import java.util.GregorianCalendar;

import nodomain.freeyourgadget.gadgetbridge.model.BatteryState;

public class BatteryInfo extends AbstractInfo {
    public static final byte DEVICE_BATTERY_NORMAL = 0;
    public static final byte DEVICE_BATTERY_LOW = 1;
    public static final byte DEVICE_BATTERY_CHARGING = 2;
    public static final byte DEVICE_BATTERY_CHARGING_FULL = 3;
    public static final byte DEVICE_BATTERY_CHARGE_OFF = 4;
    
    public BatteryInfo(byte[] data) {
        super(data);
    }

    public int getLevelInPercent() {
        if (mData.length >= 1) {
            return mData[0];
        }
        return 50; // actually unknown
    }

    public BatteryState getState() {
        if (mData.length >= 10) {
            int value = mData[9];
            switch (value) {
                case DEVICE_BATTERY_NORMAL:
                    return BatteryState.BATTERY_NORMAL;
                case DEVICE_BATTERY_LOW:
                    return BatteryState.BATTERY_LOW;
                case DEVICE_BATTERY_CHARGING:
                    return BatteryState.BATTERY_CHARGING;
                case DEVICE_BATTERY_CHARGING_FULL:
                    return BatteryState.BATTERY_CHARGING_FULL;
                case DEVICE_BATTERY_CHARGE_OFF:
                    return BatteryState.BATTERY_NOT_CHARGING_FULL;
            }
        }
        return BatteryState.UNKNOWN;
    }

    public GregorianCalendar getLastChargeTime() {
        GregorianCalendar lastCharge = new GregorianCalendar();

        if (mData.length >= 10) {
            lastCharge.set(Calendar.YEAR, (2000 + mData[1]));
            lastCharge.set(Calendar.MONTH, mData[2]);
            lastCharge.set(Calendar.DATE, mData[3]);
            lastCharge.set(Calendar.HOUR_OF_DAY, mData[4]);
            lastCharge.set(Calendar.MINUTE, mData[5]);
            lastCharge.set(Calendar.SECOND, mData[6]);
        }

        return lastCharge;
    }

    public int getNumCharges() {
        if (mData.length >= 10) {
            return ((0xff & mData[7]) | ((0xff & mData[8]) << 8));

        }
        return -1;
    }
}

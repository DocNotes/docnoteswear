package www.wearable.com.docnoteswear.model;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;

/**
 * Created by arvind on 6/4/16.
 */
public class Prescription {

    public static String STATUS_STARTED = "pending";
    public static String STATUS_COMPLETED = "completed";
    public static String STATUS_DELAYED = "delayed";


    public String medication_type;
    public String quantity;
    public int time_interval;
    public String days;
    public String status = STATUS_STARTED;

    public enum  intervalValues{
      ONE_TIME_DAY , TWO_TIME_DAY , THREE_TIME_DAY
    };


    private static final String TYPE = "type";
    private static final String QUANTITY = "quantity";
    private static final String TIME = "time";

    public Prescription getFromDataMap(DataMap map) {
        Prescription obj = new Prescription();
        obj.medication_type = map.getString(TYPE);
        obj.quantity = map.getString(QUANTITY);
        obj.time_interval = map.getInt(TIME);
        return obj;
    }

    public DataMap writeToDataMap() {
        DataMap map = new DataMap();
        map.putString(TYPE, this.medication_type);
        map.putString(QUANTITY, this.quantity);
        map.putInt(TIME, this.time_interval);
        return map;
    }

    public ArrayList<Prescription> getAllPrescriptions(ArrayList<Prescription> prescriptions)
    {
        ArrayList<Prescription> allPrescriptions = new ArrayList<>();
        for(Prescription prescription : prescriptions){
            if(prescription.time_interval > 0  ){
                allPrescriptions.add(prescription);
            }
        }
        return allPrescriptions;
    }

    @Override
    public String toString() {
        return "["+medication_type+","+quantity+","+time_interval+"]";
    }
}

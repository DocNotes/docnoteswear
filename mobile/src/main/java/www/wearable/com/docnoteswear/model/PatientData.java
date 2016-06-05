package www.wearable.com.docnoteswear.model;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;

/**
 * Created by arvind on 6/4/16.
 */
public class PatientData {

    private static final String NAME = "name";
    private static final String UUID = "uuid";
    private static final String REMARKS = "remarks";
    private static final String PRESCRIPTIONS = "prescriptions";
    private static final String SCHEDULE = "schedule";
    public String name;
    public String uuid;
    public String remark;
    public String admissionDateDisplay;
    public long admissionDate;
    public String schedule;
    public ArrayList<DataMap> prescriptionsMap;
    public ArrayList<Prescription> prescriptions;


    public PatientData getFromDataMap(DataMap map) {
        PatientData data = new PatientData();
        data.name = map.getString(NAME);
        data.uuid = map.getString(UUID);
        data.remark = map.getString(REMARKS);
        data.schedule = map.getString(SCHEDULE);
        data.prescriptionsMap = map.getDataMapArrayList(PRESCRIPTIONS);
        data.prescriptions = getPrescriptionsFromMap(this.prescriptionsMap);
        return data;

    }

    public DataMap getFromDataMap() {
        DataMap map = new DataMap();
        map.putString(NAME, this.name);
        map.putString(UUID, this.uuid);
        map.putString(REMARKS, this.remark);
        map.putString(SCHEDULE , this.schedule);
        prescriptionsMap = new ArrayList<>();
        for(Prescription prescription :this.prescriptions){
            prescriptionsMap.add(prescription.writeToDataMap());
        }
        map.putDataMapArrayList(PRESCRIPTIONS, this.prescriptionsMap);
        return map;
    }

    public ArrayList<Prescription> getPrescriptionsFromMap(ArrayList<DataMap> prescriptionsMap) {
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        for (DataMap map : prescriptionsMap) {
            Prescription prescription = new Prescription();
            prescriptions.add(prescription.getFromDataMap(map));
        }
        return prescriptions;
    }

    @Override
    public String toString() {
        return this.name+","+this.remark;

    }
}

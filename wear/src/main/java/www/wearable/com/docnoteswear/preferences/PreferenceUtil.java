package www.wearable.com.docnoteswear.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import www.wearable.com.docnoteswear.model.PatientData;

/**
 * Created by arvind on 6/4/16.
 */
public class PreferenceUtil {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public PreferenceUtil(Context context) {
        sharedPref = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void saveModel(PatientData data)
    {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("json" , json);
        editor.apply();
    }

    public PatientData getPatientData()
    {
        Gson gson = new Gson();
        String json = sharedPref.getString("json" , "");
        PatientData data = new PatientData();
        if(!json.equals("")){
            data = gson.fromJson(json , PatientData.class);
        }
        return data;
    }



}

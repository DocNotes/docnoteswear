package www.wearable.com.docnoteswear.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import www.wearable.com.docnoteswear.model.PatientData;

/**
 * Created by arvind on 5/19/16.
 */
public class CompanyHttpClient  extends HttpClient{

    public CompanyHttpClient(Context context) {
        super(context);
    }

    public void getAllPatients(Response.Listener<String> companyList , Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(Request.Method.GET , Api.GET_PATIENTS, companyList,  errorListener);
        queue.add(request);
    }
}

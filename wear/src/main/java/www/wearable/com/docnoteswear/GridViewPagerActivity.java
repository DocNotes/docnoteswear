package www.wearable.com.docnoteswear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import www.wearable.com.docnoteswear.adapter.PrescriptionActionsAdapter;
import www.wearable.com.docnoteswear.model.PatientData;
import www.wearable.com.docnoteswear.model.Prescription;
import www.wearable.com.docnoteswear.preferences.PreferenceUtil;

public class GridViewPagerActivity extends Activity implements GridViewPager.OnPageChangeListener {


    private static final int SPEECH_REQUEST_CODE = 201;
    private static final String TAG = GridViewPagerActivity.class.getName();
    private GridViewPager gridView;
    PrescriptionActionsAdapter adapter;
    PatientData data;
    ArrayList<Prescription> prescriptions;
    int position = 0;
    PreferenceUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        prescriptions = new ArrayList<>();
        util = new PreferenceUtil(this);
        data = new PatientData();
        data = util.getPatientData();




        /*
            the code below needs to removed
         */

        if (data.name == null) {
            data.name = "Arvind";
            data.remark = "something";
            Prescription prescription1 = new Prescription();
            prescription1.medication_type = "Panadol 100 mg";
            prescription1.time_interval = 1;
            prescriptions.add(prescription1);
            Prescription prescription2 = new Prescription();
            prescription2.medication_type = "Panadol 200 mg";
            prescription2.time_interval = 2;
            prescriptions.add(prescription2);
            Prescription prescription3 = new Prescription();
            prescription3.medication_type = "Panadol 300 mg";
            prescription3.time_interval = 3;
            prescriptions.add(prescription3);
            data.prescriptions = prescriptions;
            util.saveModel(data);
        } else {
            prescriptions = data.prescriptions;
        }
        Log.d(TAG, "onCreate: " + data.toString());
        gridView = (GridViewPager) findViewById(R.id.pager);
        adapter = new PrescriptionActionsAdapter(this, data ,  actionListeners );
        adapter.setItems(new Prescription().getAllPrescriptions(prescriptions));
        gridView.setAdapter(adapter);
        DotsPageIndicator dots = (DotsPageIndicator) findViewById(R.id.indicator);
        dots.setPager(gridView);
        gridView.setOnPageChangeListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.d(TAG, "onResume: " + data.toString());


    }

    View.OnClickListener actionListeners = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (position == prescriptions.size() + 1) {
                displaySpeechRecognizer();
                position = -1;
            }
            else if (position > 0) {
                Log.d("click" , prescriptions.size() +"");
                Prescription prescription = prescriptions.get(position - 1 );
                prescription.status = Prescription.STATUS_COMPLETED;
                prescription.time_interval = prescription.time_interval - 1;
                prescriptions.set(position - 1 , prescription);
                data.prescriptions = prescriptions;
                util.saveModel(data);
                gridView.setCurrentItem(0 , position + 1 , true);
                if (position == prescriptions.size() + 1  ) // last prescribed data
                {
                    gridView.setCurrentItem(0, 0, true);
                    //check for all completed and sync.
                    boolean isCompleted = prescription.isCompleted(data.prescriptions);
                    if(isCompleted) {
                        data.schedule = Prescription.STATUS_COMPLETED;
                    }
                    else {
                        data.schedule = Prescription.STATUS_STARTED;
                    }
                    util.saveModel(data);

                }

                Intent intent = new Intent(GridViewPagerActivity.this, ConfirmationActivity.class);
                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
                startActivity(intent);

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Intent intent = new Intent(GridViewPagerActivity.this, ConfirmationActivity.class);
            intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
            startActivity(intent);
            gridView.setCurrentItem(0, 0, true);

        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void displaySpeechRecognizer() {
        Log.d("starting ", "voice activity");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }


    @Override
    public void onPageScrolled(int position, int i1, float v, float v1, int i2, int i3) {

    }

    @Override
    public void onPageSelected(int position, int i1) {
        this.position = i1;
        Log.d("position" , position+"");
    }

    @Override
    public void onPageScrollStateChanged(int i) {
       /* this.position = i;
        Log.d("position" , position+"");*/

    }
}

package www.wearable.com.docnoteswear.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.ActionPage;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import www.wearable.com.docnoteswear.R;
import www.wearable.com.docnoteswear.model.PatientData;
import www.wearable.com.docnoteswear.model.Prescription;

/**
 * Created by arvind on 6/4/16.
 */
public class PrescriptionActionsAdapter extends GridPagerAdapter {

    private final Context mContext;
    private View.OnClickListener actionListener;
    private ArrayList<Prescription> mRows;
    private PatientData data;
    public PrescriptionActionsAdapter(Context mContext, PatientData data , View.OnClickListener actionListener) {
        super();
        this.mContext = mContext;
        this.data = data;
        this.actionListener = actionListener;
        this.mRows = new ArrayList<>();
    }

    public void setItems(ArrayList<Prescription> prescriptions) {
        mRows = prescriptions;
        notifyDataSetChanged();
    }


    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return mRows.size() + 2;
    }


    @Override
    public Object instantiateItem(ViewGroup viewGroup, int row, int col) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(
                R.layout.grid_item, viewGroup, false);
        ActionPage page = (ActionPage) view.findViewById(R.id.action);
        page.setOnClickListener(actionListener);
        if (col == 0) {
            view = mLayoutInflater.inflate(R.layout.profile, viewGroup, false);
            ((TextView)view.findViewById(R.id.name)).setText(data.name);
        } else if (col == mRows.size() + 1) {
            page.setText("Report Emergency");
            page.setImageResource(R.mipmap.emergency_icon);
        } else {
            page.setText(mRows.get(col - 1).medication_type);
            page.setImageResource(R.mipmap.drugs_icon);
        }
        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int row, int col, Object o) {
        viewGroup.removeView((View) o);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }


}

package com.ats.mongi_production.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.activity.MistryWiseDateWiseReportActivity;
import com.ats.mongi_production.activity.MistryWiseItemWiseReportActivity;
import com.ats.mongi_production.activity.MistryWiseReportActivity;
import com.ats.mongi_production.activity.RateChangedReportActivity;
import com.ats.mongi_production.activity.SpCakeWtDiffReportActivity;
import com.ats.mongi_production.activity.StockTransferReportActivity;

public class ReportsFragment extends Fragment implements View.OnClickListener {

    private TextView tvReport1, tvReport2, tvReport3, tvReport4, tvReport5, tvReport6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        HomeActivity.tvTitle.setText("Reports");

        tvReport1 = view.findViewById(R.id.tvReport1);
        tvReport2 = view.findViewById(R.id.tvReport2);
        tvReport3 = view.findViewById(R.id.tvReport3);
        tvReport4 = view.findViewById(R.id.tvReport4);
        tvReport5 = view.findViewById(R.id.tvReport5);
        tvReport6 = view.findViewById(R.id.tvReport6);

        tvReport1.setOnClickListener(this);
        tvReport2.setOnClickListener(this);
        tvReport3.setOnClickListener(this);
        tvReport4.setOnClickListener(this);
        tvReport5.setOnClickListener(this);
        tvReport6.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvReport1) {
            startActivity(new Intent(getContext(), StockTransferReportActivity.class));
        } else if (view.getId() == R.id.tvReport2) {
            startActivity(new Intent(getContext(), SpCakeWtDiffReportActivity.class));
        } else if (view.getId() == R.id.tvReport3) {
            startActivity(new Intent(getContext(), MistryWiseDateWiseReportActivity.class));
        } else if (view.getId() == R.id.tvReport4) {
            startActivity(new Intent(getContext(), MistryWiseReportActivity.class));
        } else if (view.getId() == R.id.tvReport5) {
            startActivity(new Intent(getContext(), MistryWiseItemWiseReportActivity.class));
        } else if (view.getId() == R.id.tvReport6) {
            startActivity(new Intent(getContext(), RateChangedReportActivity.class));
        }
    }
}

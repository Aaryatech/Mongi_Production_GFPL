package com.ats.mongi_production.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.BuildConfig;
import com.ats.mongi_production.R;
import com.ats.mongi_production.adapter.MistryListAdapter;
import com.ats.mongi_production.adapter.MistryWiseItemWiseReportAdapter;
import com.ats.mongi_production.adapter.MistryWiseReportAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.Employee;
import com.ats.mongi_production.model.FrWiseSpCakeOrd;
import com.ats.mongi_production.model.MistryWiseItemWiseModel;
import com.ats.mongi_production.model.MistryWiseReportModel;
import com.ats.mongi_production.model.WorkerList;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MistryWiseItemWiseReportActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    MistryWiseItemWiseReportAdapter reportAdapter;

    ArrayList<MistryWiseItemWiseModel> reportArrayList = new ArrayList<>();

    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    Dialog dialog;
    CheckBox checkBox;
    Boolean isTouched = false;

    public static ArrayList<Employee> staticWorkerList = new ArrayList<>();
    public static Map<Integer, Boolean> staticWorkerMap = new HashMap<Integer, Boolean>();

    MistryListAdapter mistryAdapter;

    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    int day, month, year;
    long dateInMillis;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mistry_wise_item_wise_report);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Mistry Wise Item Wise Report");

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        getWorkerList();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            new FilterDialog(MistryWiseItemWiseReportActivity.this).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_pdf) {
            generatePDF("" + fromDateMillis, "" + toDateMillis, reportArrayList);
            return true;
        } else if (id == R.id.action_excel) {
            generateExcel("" + fromDateMillis, "" + toDateMillis, reportArrayList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMistryWiseItemWiseReport(String fromDate, String toDate, ArrayList<Integer> mistryList, String table) {

        Log.e("PARAMETER : ", "   FROM DATE : " + fromDate + "                 TO DATE : " + toDate + "                  MISTRY LIST : " + mistryList + "                     TABLE  : " + table);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<MistryWiseItemWiseModel>> listCall = Constants.myInterface.getMistryWiseItemWiseReport(fromDate, toDate, mistryList, table);
            listCall.enqueue(new Callback<ArrayList<MistryWiseItemWiseModel>>() {
                @Override
                public void onResponse(Call<ArrayList<MistryWiseItemWiseModel>> call, Response<ArrayList<MistryWiseItemWiseModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("REPORT DATA : ", "------------" + response.body());

                            ArrayList<MistryWiseItemWiseModel> data = response.body();

                            reportArrayList.clear();
                            reportArrayList = data;

                            reportAdapter = new MistryWiseItemWiseReportAdapter(data, MistryWiseItemWiseReportActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MistryWiseItemWiseReportActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(reportAdapter);

                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<MistryWiseItemWiseModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public class FilterDialog extends Dialog {

        EditText edFromDate, edToDate;
        TextView tvFromDate, tvToDate;
        RadioButton rbSP, rbREG;
        ImageView ivClose;
        CardView cardView;


        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_filter_mistry_wise_report);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edFromDate = findViewById(R.id.edFromDate);
            edToDate = findViewById(R.id.edToDate);
            tvFromDate = findViewById(R.id.tvFromDate);
            tvToDate = findViewById(R.id.tvToDate);
            Button btnFilter = findViewById(R.id.btnFilter);
            ivClose = findViewById(R.id.ivClose);
            rbSP = findViewById(R.id.rbSP);
            rbREG = findViewById(R.id.rbREG);
            LinearLayout llChooseMistry = findViewById(R.id.llChooseMistry);

            edFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (fromDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(fromDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(MistryWiseItemWiseReportActivity.this, fromDateListener, yr, mn, dy);
                    dialog.show();
                }
            });

            edToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (toDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(toDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(MistryWiseItemWiseReportActivity.this, toDateListener, yr, mn, dy);
                    dialog.show();
                }
            });

            llChooseMistry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMistryDialog(staticWorkerList);
                }
            });


            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edFromDate.getText().toString().isEmpty()) {
                        edFromDate.setError("Select From Date");
                        edFromDate.requestFocus();
                    } else if (edToDate.getText().toString().isEmpty()) {
                        edToDate.setError("Select To Date");
                        edToDate.requestFocus();
                    } else {
                        dismiss();

                        String fromDate = tvFromDate.getText().toString();
                        String toDate = tvToDate.getText().toString();

                        ArrayList<Integer> tempIdArray = new ArrayList<>(staticWorkerMap.keySet());
                        ArrayList<Integer> empList = new ArrayList<>();
                        if (tempIdArray != null) {
                            if (tempIdArray.size() > 0) {
                                for (int i = 0; i < tempIdArray.size(); i++) {
                                    empList.add(tempIdArray.get(i));
                                }
                            } else {
                                empList.add(-1);
                            }
                        } else {
                            empList.add(-1);
                        }

                        String table = "";
                        if (rbSP.isChecked()) {
                            table = "SP";
                        } else if (rbREG.isChecked()) {
                            table = "REG";
                        }

                        if (table.isEmpty()) {
                            Toast.makeText(MistryWiseItemWiseReportActivity.this, "Please select type", Toast.LENGTH_SHORT).show();
                        } else {

                            getMistryWiseItemWiseReport(fromDate, toDate, empList, table);
                            dismiss();

                        }

                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edFromDate.setText(dd + "-" + mm + "-" + yyyy);
                tvFromDate.setText(yyyy + "-" + mm + "-" + dd);
                CustomSharedPreference.putString(MistryWiseItemWiseReportActivity.this, CustomSharedPreference.KEY_SP_FROM_DATE, yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                fromDateMillis = calendar.getTimeInMillis();
            }
        };

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edToDate.setText(dd + "-" + mm + "-" + yyyy);
                tvToDate.setText(yyyy + "-" + mm + "-" + dd);
                CustomSharedPreference.putString(MistryWiseItemWiseReportActivity.this, CustomSharedPreference.KEY_SP_TO_DATE, yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                toDateMillis = calendar.getTimeInMillis();
            }
        };
    }

    public void showMistryDialog(final ArrayList<Employee> empList) {
        dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_choose_menu, null, false);
        dialog.setContentView(v);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView tvClear = dialog.findViewById(R.id.tvClear);
        checkBox = dialog.findViewById(R.id.allMenuCheckbox);
        final RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        mistryAdapter = new MistryListAdapter(empList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mistryAdapter);

        isTouched = false;
        checkBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isTouched = false;

                    if (empList.size() > 0) {
                        for (int i = 0; i < empList.size(); i++) {
                            staticWorkerMap.put(empList.get(i).getEmpId(), true);
                            empList.get(i).setCheckedStatus(true);
                        }
                        mistryAdapter.notifyDataSetChanged();
                    }
                } else {

                    if (isTouched) {
                        Log.e("CHECKBOX", "-----------ISTOUCHED");
                        isTouched = false;
                        if (empList.size() > 0) {
                            for (int i = 0; i < empList.size(); i++) {
                                staticWorkerMap.put(empList.get(i).getEmpId(), false);
                                empList.get(i).setCheckedStatus(false);
                            }
                            mistryAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (empList.size() > 0) {
                    for (int i = 0; i < empList.size(); i++) {
                        empList.get(i).setCheckedStatus(false);
                    }
                    checkBox.setChecked(false);
                    mistryAdapter.notifyDataSetChanged();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                ArrayList<Integer> workerIds = new ArrayList<>();

                try {
                    Log.e("STOCK TRANSFER", "-------------MENU ARRAY------------- " + empList);
                    for (int i = 0; i < empList.size(); i++) {
                        if (staticWorkerMap.get(empList.get(i).getEmpId())) {
                            workerIds.add(empList.get(i).getEmpId());
                        }
                    }
                } catch (Exception e) {
                }

                Log.e("Selected Ids", "--------------" + workerIds);

            }
        });

        dialog.show();
    }

    public void getWorkerList() {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<WorkerList> listCall = Constants.myInterface.getWorkerList(101);
            listCall.enqueue(new Callback<WorkerList>() {
                @Override
                public void onResponse(Call<WorkerList> call, Response<WorkerList> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Worker Data : ", "------------" + response.body());

                            WorkerList data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                if (!data.getErrorMessage().isError()) {
                                    if (staticWorkerList != null) {
                                        staticWorkerList.clear();
                                    }

                                    if (data.getGetEmpList().size() > 0) {
                                        for (int i = 0; i < data.getGetEmpList().size(); i++) {
                                            Employee emp = data.getGetEmpList().get(i);
                                            emp.setCheckedStatus(false);
                                            staticWorkerList.add(emp);
                                        }
                                    }

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                    ArrayList<Integer> idArray = new ArrayList<>();
                                    idArray.add(-1);

                                    getMistryWiseItemWiseReport(sdf.format(System.currentTimeMillis()), sdf.format(System.currentTimeMillis()), idArray, "SP");

                                }

                                commonDialog.dismiss();

                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<WorkerList> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void generatePDF(String fromDate, String toDate, ArrayList<MistryWiseItemWiseModel> reportList) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date fromDt = new Date();
        fromDt.setTime(Long.parseLong(fromDate));
        Date toDt = new Date();
        toDt.setTime(Long.parseLong(toDate));
        Log.e("DATE : ", "FROM : " + sdf.format(fromDt.getTime()) + "           TO : " + sdf.format(toDt.getTime()));

        final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();

        doc.setMargins(-50, -50, 40, 40);
        Font headerBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normalTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "MISTRY_WISE_ITEM_WISE_REPORT_" + System.currentTimeMillis() + ".pdf";
            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            doc.open();

            try {

                //-----------------HEADER TABLE-----------------------
                PdfPTable tableHeader = new PdfPTable(1);

                PdfPTable ptHead = new PdfPTable(1);
                ptHead.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("GFPL", headerBoldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                cell.setPadding(10);
                tableHeader.addCell(cell);

                doc.add(tableHeader);
                //-----------------HEADER TABLE-----------------------

                //-----------------BLANK TABLE-----------------------
                PdfPTable tableBlank = new PdfPTable(1);

                PdfPTable ptBlank = new PdfPTable(1);
                ptBlank.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph(" "));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                tableBlank.addCell(cell);

                doc.add(tableBlank);
                //-----------------BLANK TABLE-----------------------

                //-----------------HEADER TABLE-----------------------
                PdfPTable tableHeader2 = new PdfPTable(1);

                PdfPTable ptHead2 = new PdfPTable(1);
                ptHead2.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("Mistry Wise Item Wise Report", headerBoldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                cell.setPadding(10);
                tableHeader2.addCell(cell);

                doc.add(tableHeader2);
                //-----------------HEADER TABLE-----------------------

                //-----------------DATE TABLE--------------------------
                PdfPTable tableDate = new PdfPTable(2);
                float[] columnwdt = new float[]{50, 50};
                tableDate.setWidths(columnwdt);
                tableDate.setTotalWidth(columnwdt);

                cell = new PdfPCell(new Phrase("FROM DATE : " + sdf.format(fromDt), boldTextFont));
                cell.setHorizontalAlignment(0);
                cell.setBorder(Rectangle.NO_BORDER);
                tableDate.addCell(cell);

                cell = new PdfPCell(new Phrase("TO DATE : " + sdf.format(toDt), boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                tableDate.addCell(cell);

                doc.add(tableDate);

                //--------------------DATE TABLE---------------------------------

                //-----------------BLANK TABLE-----------------------
                PdfPTable tableBlank1 = new PdfPTable(1);

                PdfPTable ptBlank1 = new PdfPTable(1);
                ptBlank1.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph(" "));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                tableBlank1.addCell(cell);

                doc.add(tableBlank1);
                //-----------------BLANK TABLE-----------------------

                //-----------------DATA TABLE--------------------------
                PdfPTable table1 = new PdfPTable(11);
                float[] columnWidth1 = new float[]{20, 30, 40, 30, 40, 30, 30, 30, 40, 30, 30};
                table1.setWidths(columnWidth1);
                table1.setTotalWidth(columnWidth1);

                cell = new PdfPCell(new Phrase("Sr.", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Franchise Code", boldTextFont));
                cell.setHorizontalAlignment(1);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Franchise", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Item Code", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Item", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Weight Franchise", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Weight Production", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Weight Difference", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Mistry", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Start Time", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("End Time", boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                doc.add(table1);

                if (reportList.size() > 0) {
                    for (int i = 0; i < reportList.size(); i++) {

                        PdfPTable table2 = new PdfPTable(11);
                        float[] columnWidth2 = new float[]{20, 30, 40, 30, 40, 30, 30, 30, 40, 30, 30};
                        table2.setWidths(columnWidth2);
                        table2.setTotalWidth(columnWidth2);

                        cell = new PdfPCell(new Phrase("" + (i + 1), normalTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getFrCode(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getFrName(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getItemCode(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getItemName(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getInputKgFr(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getInputKgProd(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getWeightDiff(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        cell = new PdfPCell(new Phrase("" + reportList.get(i).getEmpName(), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss");

                        Date startDate = new Date();
                        startDate.setTime(reportList.get(i).getStartTimeStamp());

                        cell = new PdfPCell(new Phrase("" + sdfTime.format(startDate.getTime()), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);

                        Date endDate = new Date();
                        endDate.setTime(reportList.get(i).getEndTimeStamp());

                        cell = new PdfPCell(new Phrase("" + sdfTime.format(endDate.getTime()), boldTextFont));
                        cell.setHorizontalAlignment(0);
                        table2.addCell(cell);


                        doc.add(table2);
                    }
                }

                //-----------------DATA TABLE--------------------------


            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(this, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(this, authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(this, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
                }

            }


        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(this, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateExcel(String fromDate, String toDate, ArrayList<MistryWiseItemWiseModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date fromDt = new Date();
        fromDt.setTime(Long.parseLong(fromDate));
        Date toDt = new Date();
        toDt.setTime(Long.parseLong(toDate));
        Log.e("DATE : ", "FROM : " + sdf.format(fromDt.getTime()) + "           TO : " + sdf.format(toDt.getTime()));


        final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "MISTRY_WISE_ITEM_WISE_REPORT_" + System.currentTimeMillis() + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("GFPL");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Mistry Wise Item Wise Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + sdf.format(fromDt.getTime()));

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + sdf.format(toDt.getTime()));

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("Sr.");

            c = headerRow.createCell(1);
            c.setCellValue("Franchise Code");

            c = headerRow.createCell(2);
            c.setCellValue("Franchise");

            c = headerRow.createCell(3);
            c.setCellValue("Item Code");

            c = headerRow.createCell(4);
            c.setCellValue("Item");

            c = headerRow.createCell(5);
            c.setCellValue("Weight Franchise");

            c = headerRow.createCell(6);
            c.setCellValue("Weight Production");

            c = headerRow.createCell(7);
            c.setCellValue("Weight Difference");

            c = headerRow.createCell(8);
            c.setCellValue("Mistry");

            c = headerRow.createCell(9);
            c.setCellValue("Start Time");

            c = headerRow.createCell(10);
            c.setCellValue("End Time");

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getFrCode());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getFrName());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getItemCode());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getItemName());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getInputKgFr());

                c = newRow.createCell(6);
                c.setCellValue("" + reportList.get(i).getInputKgProd());

                c = newRow.createCell(7);
                c.setCellValue("" + reportList.get(i).getWeightDiff());

                c = newRow.createCell(8);
                c.setCellValue("" + reportList.get(i).getEmpName());

                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss");

                Date startDate = new Date();
                startDate.setTime(reportList.get(i).getStartTimeStamp());

                c = newRow.createCell(9);
                c.setCellValue("" + sdfTime.format(startDate.getTime()));

                Date endDate = new Date();
                endDate.setTime(reportList.get(i).getEndTimeStamp());

                c = newRow.createCell(10);
                c.setCellValue("" + sdfTime.format(endDate.getTime()));

            }

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (12 * 500));
            sheet1.setColumnWidth(2, (12 * 500));
            sheet1.setColumnWidth(3, (12 * 500));
            sheet1.setColumnWidth(4, (12 * 500));
            sheet1.setColumnWidth(5, (12 * 500));
            sheet1.setColumnWidth(6, (12 * 500));
            sheet1.setColumnWidth(7, (12 * 500));
            sheet1.setColumnWidth(8, (12 * 500));
            sheet1.setColumnWidth(9, (12 * 500));
            sheet1.setColumnWidth(10, (12 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(this, authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(this, "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

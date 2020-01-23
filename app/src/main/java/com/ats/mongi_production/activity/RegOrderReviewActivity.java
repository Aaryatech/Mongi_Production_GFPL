package com.ats.mongi_production.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.BuildConfig;
import com.ats.mongi_production.R;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.Info;
import com.ats.mongi_production.model.RegCakeOrder;
import com.ats.mongi_production.model.RegSpOrd;
import com.ats.mongi_production.model.WorkerList;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.PermissionsUtil;
import com.ats.mongi_production.util.RealPathUtil;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.activity.HomeActivity.tvTitle;

public class RegOrderReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCake, ivImage1, ivImage2;
    private TextView tvStartTime, tvEndTime, tvTotalTime, tvSrNo, tvRSeqNo, tvRoute, tvFrName, tvCake, tvDelivery, tvImage1, tvImage2;
    private Button btnPrint, btnStart, btnEnd;

    RegCakeOrder regCakeOrder = null;
    RegSpOrd regSpOrd = null;

    private ArrayList<Integer> workerIdList = new ArrayList<>();
    private ArrayList<String> workerNameList = new ArrayList<>();

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "Mon_Production");
    File f;

    Bitmap myBitmap1 = null, myBitmap2 = null;
    public static String path1, imagePath1, path2, imagePath2;

    int dialogOpen = 0;
    int isDispatch = 0;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    private TextInputLayout inputLayoutBillTo, inputLayoutEmailTo;
    double totalAmount = 0;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_order_review);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("REG Order Review");

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/REGOrders";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ivCake = findViewById(R.id.ivCake);
        ivCake.setOnClickListener(this);

        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvSrNo = findViewById(R.id.tvSrNo);
        tvRSeqNo = findViewById(R.id.tvRSeqNo);
        tvRoute = findViewById(R.id.tvRoute);
        tvFrName = findViewById(R.id.tvFrName);
        tvCake = findViewById(R.id.tvCake);
        tvDelivery = findViewById(R.id.tvDelivery);

        btnPrint = findViewById(R.id.btnPrint);
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        btnPrint.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);

        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }

        createFolder();
        getWorkerList();

        try {
            isDispatch = getIntent().getIntExtra("isDispatch", 0);

            if (isDispatch == 0) {
                String jsonStr = getIntent().getStringExtra("model");
                Log.e("JSON STR ", "------------------------ " + jsonStr);

                Gson gson = new Gson();
                regCakeOrder = gson.fromJson(jsonStr, RegCakeOrder.class);

                if (regCakeOrder != null) {

                    tvSrNo.setText("" + regCakeOrder.getSrNo());
                    tvRSeqNo.setText("" + regCakeOrder.getNoInRoute());
                    tvRoute.setText("" + regCakeOrder.getRouteName());
                    tvFrName.setText("" + regCakeOrder.getFrName());
                    tvCake.setText("" + regCakeOrder.getItemName());
                    tvDelivery.setText("" + regCakeOrder.getRspPlace());

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

                    if (regCakeOrder.getStartTime() != null) {
                        tvStartTime.setText("" + sdf.format(regCakeOrder.getStartTime()));
                    }

                    if (regCakeOrder.getEndTime() != null) {
                        tvEndTime.setText("" + sdf.format(regCakeOrder.getEndTime()));
                    }

                    if (regCakeOrder.getStartTime() != null && regCakeOrder.getEndTime() != null) {

                        long diff = regCakeOrder.getEndTime() - regCakeOrder.getStartTime();
                        long hrs = diff / (60 * 60 * 1000) % 24;
                        long mins = diff / (60 * 1000) % 60;
                        long sec = diff / 1000 % 60;

                        String timeReq = "" + hrs + ":" + mins + ":" + sec;
                        tvTotalTime.setText("" + timeReq);
                    }


                    Log.e("START TIME : ", "----------------------------- " + regCakeOrder.getStartTime());

                    if (regCakeOrder.getStartTime() == null && regCakeOrder.getEndTime() == null) {
                        btnStart.setVisibility(View.VISIBLE);
                        btnEnd.setVisibility(View.GONE);
                    } else if (regCakeOrder.getStartTime() > 0 && regCakeOrder.getEndTime() > 0) {
                        btnStart.setVisibility(View.GONE);
                        btnEnd.setVisibility(View.GONE);
                    } else if (regCakeOrder.getStartTime() == 0 && regCakeOrder.getEndTime() == 0) {
                        btnStart.setVisibility(View.VISIBLE);
                        btnEnd.setVisibility(View.GONE);
                    } else if (regCakeOrder.getStartTime() > 0) {
                        btnStart.setVisibility(View.GONE);
                        btnEnd.setVisibility(View.VISIBLE);
                    }


                    try {
                        Log.e("IMAGE PATH : ", "----------------------------" + Constants.IMAGE_PATH_MSP_CAKE + regCakeOrder.getItemImage());
                        Picasso.get().load(Constants.IMAGE_PATH_MSP_CAKE + regCakeOrder.getItemImage())
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logo)
                                .into(ivCake);
                    } catch (Exception e) {
                        Log.e("IMAGE : ", "------------------ " + e.getMessage());
                        e.printStackTrace();
                    }

                }

                dialogOpen = getIntent().getIntExtra("dialog", 0);

                if (dialogOpen == 1) {
                    getWorkerListAndShowEndDialog();
                }


            } else if (isDispatch == 1) {

                btnStart.setVisibility(View.GONE);
                btnEnd.setVisibility(View.GONE);

                String jsonStr = getIntent().getStringExtra("model");
                Log.e("JSON STR ", "------------------------ " + jsonStr);

                Gson gson = new Gson();
                regSpOrd = gson.fromJson(jsonStr, RegSpOrd.class);

                if (regSpOrd != null) {

                    tvSrNo.setText("" + regSpOrd.getSrNo());
                    tvRSeqNo.setText("" + regSpOrd.getNoInRoute());
                    tvRoute.setText("" + regSpOrd.getRouteName());
                    tvFrName.setText("" + regSpOrd.getFrName());
                    tvCake.setText("" + regSpOrd.getItemName());
                    tvDelivery.setText("" + regSpOrd.getRspPlace());

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

                    if (regSpOrd.getStartTime() != null) {
                        tvStartTime.setText("" + sdf.format(regSpOrd.getStartTime()));
                    }

                    if (regSpOrd.getEndTime() != null) {
                        tvEndTime.setText("" + sdf.format(regSpOrd.getEndTime()));
                    }

                    if (regSpOrd.getStartTime() != null && regSpOrd.getEndTime() != null) {

                        long diff = regSpOrd.getEndTime() - regSpOrd.getStartTime();
                        long hrs = diff / (60 * 60 * 1000) % 24;
                        long mins = diff / (60 * 1000) % 60;
                        long sec = diff / 1000 % 60;

                        String timeReq = "" + hrs + ":" + mins + ":" + sec;
                        tvTotalTime.setText("" + timeReq);
                    }

                    try {
                        Log.e("IMAGE PATH : ", "----------------------------" + Constants.IMAGE_PATH_MSP_CAKE + regSpOrd.getItemImage());
                        Picasso.get().load(Constants.IMAGE_PATH_MSP_CAKE + regSpOrd.getItemImage())
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logo)
                                .into(ivCake);
                    } catch (Exception e) {
                        Log.e("IMAGE : ", "------------------ " + e.getMessage());
                        e.printStackTrace();
                    }

                }

                dialogOpen = getIntent().getIntExtra("dialog", 0);

            }

        } catch (Exception e) {
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivCake) {
            Intent intent = new Intent(RegOrderReviewActivity.this, ImageZoomActivity.class);
            if (isDispatch == 0) {
                intent.putExtra("image", Constants.IMAGE_PATH_MSP_CAKE + regCakeOrder.getItemImage());
            } else if (isDispatch == 1) {
                intent.putExtra("image", Constants.IMAGE_PATH_MSP_CAKE + regSpOrd.getItemImage());
            }
            startActivity(intent);
        } else if (v.getId() == R.id.btnPrint) {
            if (isDispatch == 0) {
                generatePDF(regCakeOrder);
            } else if (isDispatch == 1) {
                generateRegSpOrderPDF(regSpOrd);
            }
        } else if (v.getId() == R.id.btnStart) {
            startRegOrder(regCakeOrder.getSupId());
        } else if (v.getId() == R.id.btnEnd) {
            showEndDialog(regCakeOrder);
        }
    }


    public void showEndDialog(final RegCakeOrder regCakeOrder) {
        final Dialog openDialog = new Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_start);
        openDialog.setCancelable(false);

        TextView tvFrName = openDialog.findViewById(R.id.tvFrName);
        TextView tvCake = openDialog.findViewById(R.id.tvCake);
        TextView tvWeight = openDialog.findViewById(R.id.tvWeight);
        TextView tvFlavour = openDialog.findViewById(R.id.tvFlavour);

        Button btnSubmit = openDialog.findViewById(R.id.btnSubmit);
        Button btnCancel = openDialog.findViewById(R.id.btnCancel);

        final RadioButton rbYes = openDialog.findViewById(R.id.rbYes);
        final RadioButton rbNo = openDialog.findViewById(R.id.rbNo);

        final Spinner spActualWt = openDialog.findViewById(R.id.spActualWt);
        final EditText edActualWt = openDialog.findViewById(R.id.edActualWt);

        ivImage1 = openDialog.findViewById(R.id.ivImage1);
        ivImage2 = openDialog.findViewById(R.id.ivImage2);
        ImageView ivCamera1 = openDialog.findViewById(R.id.ivCamera1);
        ImageView ivCamera2 = openDialog.findViewById(R.id.ivCamera2);

        tvImage1 = openDialog.findViewById(R.id.tvImage1);
        tvImage2 = openDialog.findViewById(R.id.tvImage2);

        LinearLayout llWeight = openDialog.findViewById(R.id.llWeight);
        llWeight.setVisibility(View.GONE);

        LinearLayout llFlavour = openDialog.findViewById(R.id.llFlavour);
        llFlavour.setVisibility(View.GONE);

        tvFrName.setText("" + regCakeOrder.getFrName());
        tvCake.setText("" + regCakeOrder.getItemName());

        final Spinner spWorker = openDialog.findViewById(R.id.spWorker);

        ArrayList<Float> wtArray = new ArrayList<>();
        float weight = 1;
        for (float i = 0; i < 30; i++) {
            wtArray.add(weight);
            weight = weight + 0.5f;
        }

        ArrayAdapter<Float> wtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, wtArray);
        spActualWt.setAdapter(wtAdapter);

        ArrayAdapter<String> workerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workerNameList);
        spWorker.setAdapter(workerAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogOpen == 1) {
                    openDialog.dismiss();
                    finish();
                } else {
                    openDialog.dismiss();
                }
            }
        });

        ivCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraDialog(1);
            }
        });

        ivCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraDialog(2);
            }
        });

        spActualWt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                edActualWt.setText("");
                edActualWt.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String image1 = "", image2 = "";

                if (imagePath1 == null) {
                    imagePath1 = "";
                }

                if (imagePath2 == null) {
                    imagePath2 = "";
                }

                int mistryId = workerIdList.get(spWorker.getSelectedItemPosition());
                String mistryName = spWorker.getSelectedItem().toString();

                String isCharUsed = "";
                if (rbYes.isChecked()) {
                    isCharUsed = "Yes";
                } else if (rbNo.isChecked()) {
                    isCharUsed = "No";
                }

                float wt = 0;
                if (edActualWt.getText().toString().isEmpty()) {
                    wt = Float.parseFloat(spActualWt.getSelectedItem().toString());
                } else {
                    wt = Float.parseFloat(edActualWt.getText().toString());
                }

                if (imagePath1.isEmpty() && imagePath2.isEmpty()) {


                    long diff = System.currentTimeMillis() - regCakeOrder.getStartTime();
                    long hrs = diff / (60 * 60 * 1000) % 24;
                    long mins = diff / (60 * 1000) % 60;
                    long sec = diff / 1000 % 60;

                    String timeReq = "" + hrs + ":" + mins + ":" + sec;

                    endOrder(System.currentTimeMillis(), wt, "na", "na", regCakeOrder.getSupId(), mistryId, mistryName, isCharUsed, 2, timeReq);


                    //    Toast.makeText(RegOrderReviewActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                    Log.e("BOTH EMPTY", "--------------------------------------");


                } else if (!imagePath1.isEmpty() && imagePath2.isEmpty()) {

                    File imgFile = new File(imagePath1);
                    int pos = imgFile.getName().lastIndexOf(".");
                    String ext = imgFile.getName().substring(pos + 1);
                    image1 = System.currentTimeMillis() + "." + ext;

                    sendImageWithOrder(image1, "regspprodapp", 1, System.currentTimeMillis(), wt, image1, image2, regCakeOrder.getSupId(), mistryId, mistryName, isCharUsed, 2);
                    Log.e("2 EMPTY", "--------------------------------------");

                } else if (imagePath1.isEmpty() && !imagePath2.isEmpty()) {

                    File imgFile = new File(imagePath2);
                    int pos = imgFile.getName().lastIndexOf(".");
                    String ext = imgFile.getName().substring(pos + 1);
                    image2 = System.currentTimeMillis() + "." + ext;

                    sendImageWithOrder(image2, "regspprodapp", 2, System.currentTimeMillis(), wt, image1, image2, regCakeOrder.getSupId(), mistryId, mistryName, isCharUsed, 2);
                    Log.e("1 EMPTY", "--------------------------------------");

                } else {

                    File imgFile = new File(imagePath1);
                    int pos = imgFile.getName().lastIndexOf(".");
                    String ext = imgFile.getName().substring(pos + 1);
                    image1 = System.currentTimeMillis() + "." + ext;

                    File imgFile2 = new File(imagePath2);
                    int pos2 = imgFile2.getName().lastIndexOf(".");
                    String ext2 = imgFile2.getName().substring(pos2 + 1);
                    image2 = System.currentTimeMillis() + "." + ext2;

                    sendImages(image1, "regspprodapp", image2, System.currentTimeMillis(), wt, image1, image2, regCakeOrder.getSupId(), mistryId, mistryName, isCharUsed, 2);
                    Log.e("NOT EMPTY", "--------------------------------------");


                }


            }
        });

        openDialog.show();
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
                                    workerIdList.clear();
                                    workerNameList.clear();

                                    if (data.getGetEmpList().size() > 0) {
                                        for (int i = 0; i < data.getGetEmpList().size(); i++) {
                                            workerIdList.add(data.getGetEmpList().get(i).getEmpId());
                                            workerNameList.add(data.getGetEmpList().get(i).getEmpName());
                                        }
                                    }

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

    public void getWorkerListAndShowEndDialog() {
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
                                    workerIdList.clear();
                                    workerNameList.clear();

                                    if (data.getGetEmpList().size() > 0) {
                                        for (int i = 0; i < data.getGetEmpList().size(); i++) {
                                            workerIdList.add(data.getGetEmpList().get(i).getEmpId());
                                            workerNameList.add(data.getGetEmpList().get(i).getEmpName());
                                        }
                                    }

                                    showEndDialog(regCakeOrder);

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

    public void startRegOrder(int supId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.startRegOrder(Calendar.getInstance().getTimeInMillis(), supId, 1);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Info Data : ", "------------" + response.body());

                            Info data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(RegOrderReviewActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            } else {

                                if (!data.isError()) {
                                    Toast.makeText(RegOrderReviewActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegOrderReviewActivity.this, HomeActivity.class);
                                    intent.putExtra("fragment", "REG");
                                    startActivity(intent);
                                    finish();


                                } else {
                                    Toast.makeText(RegOrderReviewActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                                }

                                commonDialog.dismiss();

                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(RegOrderReviewActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(RegOrderReviewActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(RegOrderReviewActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCameraDialog(final int sequence) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Choose");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pictureActionIntent = null;
                pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (sequence == 1) {
                    startActivityForResult(pictureActionIntent, 101);
                } else if (sequence == 2) {
                    startActivityForResult(pictureActionIntent, 201);
                }
            }
        });
        builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        f = new File(folder + File.separator, "Camera.jpg");

                        String authorities = BuildConfig.APPLICATION_ID + ".provider";
                        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        if (sequence == 1) {
                            startActivityForResult(intent, 102);
                        } else if (sequence == 2) {
                            startActivityForResult(intent, 202);
                        }

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        f = new File(folder + File.separator, "Camera.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        if (sequence == 1) {
                            startActivityForResult(intent, 102);
                        } else if (sequence == 2) {
                            startActivityForResult(intent, 202);
                        }

                    }
                } catch (Exception e) {
                    ////Log.e("select camera : ", " Exception : " + e.getMessage());
                }
            }
        });
        builder.show();
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    //--------------------------IMAGE-----------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String realPath;
        Bitmap bitmap = null;

        if (resultCode == RESULT_OK && requestCode == 102) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivImage1.setImageBitmap(myBitmap1);

                    myBitmap1 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath1 = f.getAbsolutePath();
                tvImage1.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap1 = getBitmapFromCameraData(data, this);

                ivImage1.setImageBitmap(myBitmap1);
                imagePath1 = uriFromPath.getPath();
                tvImage1.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 202) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivImage2.setImageBitmap(myBitmap2);

                    myBitmap2 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap2.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath2 = f.getAbsolutePath();
                tvImage2.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 201) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap2 = getBitmapFromCameraData(data, this);

                ivImage2.setImageBitmap(myBitmap2);
                imagePath2 = uriFromPath.getPath();
                tvImage2.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap2.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        }
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        path1 = picturePath;
        cursor.close();

        Bitmap bitm = shrinkBitmap(picturePath, 720, 720);
        Log.e("Image Size : ---- ", " " + bitm.getByteCount());

        return bitm;
        // return BitmapFactory.decodeFile(picturePath, options);
    }

    public static Bitmap shrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    private void sendImageWithOrder(String filename, String type, final int sequence, final long timestamp, final float inputKg, final String pic1, final String pic2, final int spCakeSupNo, final int mistryId, final String mistryName, final String isCharUsed, final int status) {
        final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = null;

        if (sequence == 1) {
            imgFile = new File(imagePath1);
        } else if (sequence == 2) {
            imgFile = new File(imagePath2);
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imgFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);

        RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), filename);
        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), type);

        Call<JSONObject> call = Constants.myInterface.imageUpload(body, imgName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();

                long diff = timestamp - regCakeOrder.getStartTime();
                long hrs = diff / (60 * 60 * 1000) % 24;
                long mins = diff / (60 * 1000) % 60;
                long sec = diff / 1000 % 60;

                String timeReq = "" + hrs + ":" + mins + ":" + sec;

                endOrder(timestamp, inputKg, pic1, pic2, spCakeSupNo, mistryId, mistryName, isCharUsed, status, timeReq);

                if (sequence == 1) {
                    imagePath1 = "";
                } else if (sequence == 2) {
                    imagePath2 = "";
                }
                Log.e("Response : ", "--" + response.body());
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(RegOrderReviewActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendImages(String filename1, final String type, final String filename2, final long timestamp, final float inputKg, final String pic1, final String pic2, final int spCakeSupNo, final int mistryId, final String mistryName, final String isCharUsed, final int status) {
        final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = new File(imagePath1);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imgFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);

        RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), filename1);
        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), type);

        Call<JSONObject> call = Constants.myInterface.imageUpload(body, imgName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();

                sendImageWithOrder(filename2, type, 2, timestamp, inputKg, pic1, pic2, spCakeSupNo, mistryId, mistryName, isCharUsed, status);

                imagePath1 = "";
                Log.e("Response : ", "--" + response.body());
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(RegOrderReviewActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void endOrder(long timestamp, float inputKg, String pic1, String pic2, int spCakeSupNo, int mistryId, String mistryName, String isCharUsed, int status, String timeReq) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(RegOrderReviewActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> adminDataCall = Constants.myInterface.endRegOrder(timestamp, inputKg, pic1, pic2, spCakeSupNo, mistryId, mistryName, isCharUsed, status, timeReq);
            adminDataCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            Log.e("End Order  : ", " DATA : " + data);
                            commonDialog.dismiss();

                            if (!data.isError()) {
                                Toast.makeText(RegOrderReviewActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegOrderReviewActivity.this, HomeActivity.class);
                                intent.putExtra("fragment", "REG");
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegOrderReviewActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(RegOrderReviewActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("Login : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(RegOrderReviewActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("Login : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(RegOrderReviewActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("Login : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void generatePDF(RegCakeOrder regCakeOrder) {

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

            String fileName = regCakeOrder.getSrNo() + "_" + regCakeOrder.getFrName() + "_Reg" + ".pdf";
            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            doc.open();

            try {

                //-----------------HEADER TABLE-----------------------
                PdfPTable tableHeader = new PdfPTable(1);

                PdfPTable ptHead = new PdfPTable(1);
                ptHead.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("AURANGABAD MONGINIS", headerBoldFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                cell.setPadding(10);
                tableHeader.addCell(cell);

                doc.add(tableHeader);
                //-----------------HEADER TABLE-----------------------

                //-----------------DATA TABLE--------------------------
                PdfPTable table1 = new PdfPTable(3);
                float[] columnWidth1 = new float[]{40, 90, 60};
                table1.setWidths(columnWidth1);
                table1.setTotalWidth(columnWidth1);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.getSrNo(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.getFrName(), boldTextFont));
                cell.setHorizontalAlignment(1);
                table1.addCell(cell);

                Date date = new Date();
                date.setTime(Long.parseLong(regCakeOrder.getProdDate()));

                cell = new PdfPCell(new Phrase("" + sdf.format(date), boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                doc.add(table1);


                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{40, 90, 60};
                table2.setWidths(columnWidth2);
                table2.setTotalWidth(columnWidth2);

                cell = new PdfPCell(new Phrase("Sp,Cake Code / Name", normalTextFont));
                cell.setHorizontalAlignment(0);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.getItemName(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", boldTextFont));
                cell.setHorizontalAlignment(0);
                table2.addCell(cell);

                doc.add(table2);

               /* PdfPTable table3 = new PdfPTable(3);
                float[] columnWidth3 = new float[]{40, 90, 60};
                table3.setWidths(columnWidth3);
                table3.setTotalWidth(columnWidth3);

                cell = new PdfPCell(new Phrase("Quantity", normalTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.get(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                cell = new PdfPCell(new Phrase("Flavour - " + spCakeOrder.getSpfName(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                doc.add(table3);*/


                PdfPTable table5 = new PdfPTable(3);
                float[] columnWidth5 = new float[]{40, 90, 60};
                table5.setWidths(columnWidth5);
                table5.setTotalWidth(columnWidth5);

                cell = new PdfPCell(new Phrase("Date of Delivery", normalTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                Date date1 = new Date();
                date1.setTime(Long.parseLong(regCakeOrder.getRspDeliveryDt()));

                cell = new PdfPCell(new Phrase("" + sdf.format(date1), boldTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                cell = new PdfPCell(new Phrase("Place of Delivery - " + regCakeOrder.getRspPlace(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                doc.add(table5);

                //-----------------DATA TABLE--------------------------


            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(this, "Unable To pdf", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Unable To Generate pdf", Toast.LENGTH_SHORT).show();
                }

            }


        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(this, "Unable To Generate pdf", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateRegSpOrderPDF(RegSpOrd regCakeOrder) {

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

            String fileName = regCakeOrder.getSrNo() + "_" + regCakeOrder.getFrName() + "_Reg" + ".pdf";
            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            doc.open();

            try {

                //-----------------HEADER TABLE-----------------------
                PdfPTable tableHeader = new PdfPTable(1);

                PdfPTable ptHead = new PdfPTable(1);
                ptHead.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("AURANGABAD MONGINIS", headerBoldFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                cell.setPadding(10);
                tableHeader.addCell(cell);

                doc.add(tableHeader);
                //-----------------HEADER TABLE-----------------------

                //-----------------DATA TABLE--------------------------
                PdfPTable table1 = new PdfPTable(3);
                float[] columnWidth1 = new float[]{40, 90, 60};
                table1.setWidths(columnWidth1);
                table1.setTotalWidth(columnWidth1);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.getSrNo(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.getFrName(), boldTextFont));
                cell.setHorizontalAlignment(1);
                table1.addCell(cell);

                Date date = new Date();
                date.setTime(Long.parseLong(regCakeOrder.getProdDate()));

                cell = new PdfPCell(new Phrase("" + sdf.format(date), boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                doc.add(table1);


                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{40, 90, 60};
                table2.setWidths(columnWidth2);
                table2.setTotalWidth(columnWidth2);

                cell = new PdfPCell(new Phrase("Sp,Cake Code / Name", normalTextFont));
                cell.setHorizontalAlignment(0);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.getItemName(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", boldTextFont));
                cell.setHorizontalAlignment(0);
                table2.addCell(cell);

                doc.add(table2);

               /* PdfPTable table3 = new PdfPTable(3);
                float[] columnWidth3 = new float[]{40, 90, 60};
                table3.setWidths(columnWidth3);
                table3.setTotalWidth(columnWidth3);

                cell = new PdfPCell(new Phrase("Quantity", normalTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                cell = new PdfPCell(new Phrase("" + regCakeOrder.get(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                cell = new PdfPCell(new Phrase("Flavour - " + spCakeOrder.getSpfName(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                doc.add(table3);*/


                PdfPTable table5 = new PdfPTable(3);
                float[] columnWidth5 = new float[]{40, 90, 60};
                table5.setWidths(columnWidth5);
                table5.setTotalWidth(columnWidth5);

                cell = new PdfPCell(new Phrase("Date of Delivery", normalTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                Date date1 = new Date();
                date1.setTime(Long.parseLong(regCakeOrder.getRspDeliveryDt()));

                cell = new PdfPCell(new Phrase("" + sdf.format(date1), boldTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                cell = new PdfPCell(new Phrase("Place of Delivery - " + regCakeOrder.getRspPlace(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                doc.add(table5);

                //-----------------DATA TABLE--------------------------


            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(this, "Unable To pdf", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Unable To Generate pdf", Toast.LENGTH_SHORT).show();
                }

            }


        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(this, "Unable To Generate pdf", Toast.LENGTH_SHORT).show();
        }
    }

}

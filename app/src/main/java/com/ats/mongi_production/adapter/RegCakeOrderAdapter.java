package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.BuildConfig;
import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.activity.OrderReviewActivity;
import com.ats.mongi_production.activity.RegOrderReviewActivity;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.Info;
import com.ats.mongi_production.model.RegCakeOrder;
import com.ats.mongi_production.model.SPCakeOrder;
import com.ats.mongi_production.util.CommonDialog;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegCakeOrderAdapter extends RecyclerView.Adapter<RegCakeOrderAdapter.MyViewHolder> {

    private ArrayList<RegCakeOrder> orderList;
    private Context context;

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


    public RegCakeOrderAdapter(ArrayList<RegCakeOrder> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/REGOrders";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tvRoute, tvFrName, tvCake, tvStart, tvEnd;
        public LinearLayout llPrint, linearLayout;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvRoute = view.findViewById(R.id.tvRoute);
            tvFrName = view.findViewById(R.id.tvFrName);
            tvCake = view.findViewById(R.id.tvCake);
            tvStart = view.findViewById(R.id.tvStart);
            tvEnd = view.findViewById(R.id.tvEnd);
            llPrint = view.findViewById(R.id.llPrint);
            linearLayout = view.findViewById(R.id.linearLayout);
            cardView = view.findViewById(R.id.cardView);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_regular_cake_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RegCakeOrder model = orderList.get(position);
        // Log.e("Adapter : ", " model : " + model);

        holder.tvSrNo.setText("" + model.getSrNo());
        holder.tvRoute.setText("" + model.getRouteName());
        holder.tvFrName.setText("" + model.getFrName());
        holder.tvCake.setText("" + model.getItemName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                String jsonStr = gson.toJson(model);

                Intent intent = new Intent(context, RegOrderReviewActivity.class);
                intent.putExtra("model", jsonStr);
                intent.putExtra("dialog", 0);
                context.startActivity(intent);

            }
        });

        if (model.getStatus() == 1) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorStart));
        } else if (model.getStatus() == 2) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorEnd));
        }else  {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }


        if (model.getStartTime() == null && model.getEndTime() == null) {
            holder.tvStart.setVisibility(View.VISIBLE);
            holder.tvEnd.setVisibility(View.GONE);
        } else if (model.getStartTime() > 0 && model.getEndTime() > 0) {
            holder.tvStart.setVisibility(View.GONE);
            holder.tvEnd.setVisibility(View.GONE);
        } else if (model.getStartTime() == 0 && model.getEndTime() == 0) {
            holder.tvStart.setVisibility(View.VISIBLE);
            holder.tvEnd.setVisibility(View.GONE);
        } else if (model.getStartTime() > 0) {
            holder.tvStart.setVisibility(View.GONE);
            holder.tvEnd.setVisibility(View.VISIBLE);
        }

        holder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegOrder(model.getSupId());
            }
        });

        holder.tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(model);

                Intent intent = new Intent(context, RegOrderReviewActivity.class);
                intent.putExtra("model", jsonStr);
                intent.putExtra("dialog", 1);
                context.startActivity(intent);
            }
        });

        holder.llPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF(model);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void startRegOrder(int supId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
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
                                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                            } else {

                                if (!data.isError()) {
                                    Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, HomeActivity.class);
                                    intent.putExtra("fragment", "REG");
                                    context.startActivity(intent);


                                } else {
                                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                                }

                                commonDialog.dismiss();

                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void generatePDF(RegCakeOrder regCakeOrder) {

        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

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
                Toast.makeText(context, "Unable To pdf", Toast.LENGTH_SHORT).show();
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
                            Uri uri = FileProvider.getUriForFile(context, authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Generate pdf", Toast.LENGTH_SHORT).show();
                }

            }


        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(context, "Unable To Generate pdf", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateList(ArrayList<RegCakeOrder> list) {
        orderList = list;
        notifyDataSetChanged();
    }

}

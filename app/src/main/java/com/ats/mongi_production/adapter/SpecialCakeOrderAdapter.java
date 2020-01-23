package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.BuildConfig;
import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.activity.OrderReviewActivity;
import com.ats.mongi_production.activity.SplashActivity;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.Info;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialCakeOrderAdapter extends RecyclerView.Adapter<SpecialCakeOrderAdapter.MyViewHolder> {

    private ArrayList<SPCakeOrder> orderList;
    private ArrayList<SPCakeOrder> orderListFiltered;
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

    public SpecialCakeOrderAdapter(ArrayList<SPCakeOrder> orderList, Context context) {
        this.orderList = orderList;
        this.orderListFiltered = orderList;
        this.context = context;

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/SPOrders";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tvRSeq, tvFrName, tvCakeCode, tvFlavour, tvStart, tvEnd, tvCategory;
        public LinearLayout llPrint, linearLayout, llCategory;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvRSeq = view.findViewById(R.id.tvRSeq);
            tvFrName = view.findViewById(R.id.tvFrName);
            tvCakeCode = view.findViewById(R.id.tvCakeCode);
            tvFlavour = view.findViewById(R.id.tvFlavour);
            tvStart = view.findViewById(R.id.tvStart);
            tvEnd = view.findViewById(R.id.tvEnd);
            llPrint = view.findViewById(R.id.llPrint);
            linearLayout = view.findViewById(R.id.linearLayout);
            cardView = view.findViewById(R.id.cardView);
            tvCategory = view.findViewById(R.id.tvCategory);
            llCategory = view.findViewById(R.id.llCategory);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_special_cake_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SPCakeOrder model = orderListFiltered.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvSrNo.setText("" + model.getSrNo());
        holder.tvRSeq.setText("" + model.getNoInRoute());
        holder.tvFrName.setText("" + model.getFrName());

        holder.tvFlavour.setText("" + model.getSpfName());
        holder.tvCategory.setText("" + model.getSpName());

        if (model.getIsAllocated() == 1) {

            holder.llCategory.setVisibility(View.VISIBLE);

            try {

                String[] strArr = model.getSpCode().split("#", 3);
                holder.tvCakeCode.setText("" + strArr[1]);

            } catch (Exception e) {
                holder.tvCakeCode.setText("" + model.getSpCode());
            }


        } else {
            holder.tvCakeCode.setText("" + model.getSpCode());
        }

        if (model.getStatus() == 1) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorStart));
        } else if (model.getStatus() == 2) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorEnd));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                String jsonStr = gson.toJson(model);

                Intent intent = new Intent(context, OrderReviewActivity.class);
                intent.putExtra("model", jsonStr);
                intent.putExtra("dialog", 0);
                intent.putExtra("isDispatch", 0);
                context.startActivity(intent);

            }
        });

        if (model.getStartTimeStamp() == null && model.getEndTimeStamp() == null) {
            holder.tvStart.setVisibility(View.VISIBLE);
            holder.tvEnd.setVisibility(View.GONE);
        } else if (model.getStartTimeStamp() > 0 && model.getEndTimeStamp() > 0) {
            holder.tvStart.setVisibility(View.GONE);
            holder.tvEnd.setVisibility(View.GONE);
        } else if (model.getStartTimeStamp() == 0 && model.getEndTimeStamp() == 0) {
            holder.tvStart.setVisibility(View.VISIBLE);
            holder.tvEnd.setVisibility(View.GONE);
        } else if (model.getStartTimeStamp() > 0) {
            holder.tvStart.setVisibility(View.GONE);
            holder.tvEnd.setVisibility(View.VISIBLE);
        }

        holder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSPOrder(model.gettSpCakeSupNo(), model.getIsAllocated());
            }
        });


        holder.tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(model);

                Intent intent = new Intent(context, OrderReviewActivity.class);
                intent.putExtra("model", jsonStr);
                intent.putExtra("dialog", 1);
                context.startActivity(intent);
            }
        });

        holder.llPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CommonDialog commonDialog = new CommonDialog(context, "Generating PDF", "Please Wait...");
                commonDialog.show();

                Log.e("PHOTO 1 NAME : ", "----------------------------" + Constants.IMAGE_PATH_SP_CAKE + "" + model.getOrderPhoto());
                Picasso.get()
                        .load(Constants.IMAGE_PATH_SP_CAKE + "" + model.getOrderPhoto())
                        .into(new Target() {
                                  @Override
                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                      try {
                                          String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/Photos";
                                          File myDir = new File(root);
                                          if (!myDir.exists()) {
                                              myDir.mkdirs();
                                          }

                                          String name = model.getOrderPhoto();
                                          myDir = new File(myDir, name);
                                          FileOutputStream out = new FileOutputStream(myDir);
                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                          out.flush();
                                          out.close();
                                          //  generatePDF(model);

                                      } catch (Exception e) {
                                          e.printStackTrace();
                                          //  generatePDF(model);
                                      }
                                  }

                                  @Override
                                  public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                  }

                                  @Override
                                  public void onPrepareLoad(Drawable placeHolderDrawable) {
                                  }
                              }
                        );

                Picasso.get()
                        .load(Constants.IMAGE_PATH_CUST_CHOICE_PHOTO_CAKE + "" + model.getOrderPhoto2())
                        .into(new Target() {
                                  @Override
                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                      try {
                                          String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/Photos";
                                          File myDir = new File(root);
                                          if (!myDir.exists()) {
                                              myDir.mkdirs();
                                          }

                                          String name = model.getOrderPhoto();
                                          myDir = new File(myDir, name);
                                          FileOutputStream out = new FileOutputStream(myDir);
                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                          out.flush();
                                          out.close();
                                          //  generatePDF(model);

                                      } catch (Exception e) {
                                          e.printStackTrace();
                                          //  generatePDF(model);
                                      }
                                  }

                                  @Override
                                  public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                  }

                                  @Override
                                  public void onPrepareLoad(Drawable placeHolderDrawable) {
                                  }
                              }
                        );

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        commonDialog.dismiss();
                        generatePDF(model);
                    }
                }, 3000);


            }
        });


    }

    @Override
    public int getItemCount() {
        return orderListFiltered.size();
    }


    public void startSPOrder(int spCakeId, final int isAllocated) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.startSPOrder(Calendar.getInstance().getTimeInMillis(), spCakeId, 1);
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
                                    if (isAllocated == 1) {
                                        intent.putExtra("fragment", "ALBUM");
                                    } else {
                                        intent.putExtra("fragment", "SP");
                                    }
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


    public void generatePDF(SPCakeOrder spCakeOrder) {

        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
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

            String fileName = spCakeOrder.getSrNo() + "_" + spCakeOrder.getFrName() + ".pdf";
            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);


            String imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/Photos/" + spCakeOrder.getOrderPhoto();
            String imgPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Production_Dispatch/Photos/" + spCakeOrder.getOrderPhoto2();

            byte[] bitmapdata1 = null;
            try {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap1 = BitmapFactory.decodeFile(imgPath, bmOptions);
                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                bitmapdata1 = stream1.toByteArray();
            } catch (Exception e) {
                Log.e("BITMAP DATA 1 ", " -------------- " + bitmapdata1);
            }

            byte[] bitmapdata2 = null;
            try {
                BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
                Bitmap bitmap2 = BitmapFactory.decodeFile(imgPath2, bmOptions2);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                bitmapdata2 = stream2.toByteArray();
            } catch (Exception e) {
                Log.e("BITMAP DATA 2 ", " -------------- " + bitmapdata2);
            }

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

                cell = new PdfPCell(new Phrase("" + spCakeOrder.getSrNo(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("" + spCakeOrder.getFrName(), boldTextFont));
                cell.setHorizontalAlignment(1);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("" + spCakeOrder.getDate(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table1.addCell(cell);

                doc.add(table1);


                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{40, 90, 60};
                table2.setWidths(columnWidth2);
                table2.setTotalWidth(columnWidth2);

                if (spCakeOrder.getIsAllocated() == 1) {
                    cell = new PdfPCell(new Phrase("Category / Cake Code / Name", normalTextFont));
                    cell.setHorizontalAlignment(0);
                    table2.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("Sp,Cake Code / Name", normalTextFont));
                    cell.setHorizontalAlignment(0);
                    table2.addCell(cell);
                }

                if (spCakeOrder.getIsAllocated() == 1) {

                    String name=spCakeOrder.getSpCode();
                    try {
                        String strArr[]=spCakeOrder.getSpCode().split("#",3);
                        name=strArr[1]+" / "+strArr[2];
                    }catch (Exception e){}

                    cell = new PdfPCell(new Phrase("" + spCakeOrder.getSpName() + " / " + name, boldTextFont));
                    cell.setHorizontalAlignment(0);
                    table2.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("" + spCakeOrder.getSpName() + " - " + spCakeOrder.getSpCode(), boldTextFont));
                    cell.setHorizontalAlignment(0);
                    table2.addCell(cell);
                }



                cell = new PdfPCell(new Phrase("Message - " + spCakeOrder.getSpEvents() + " " + spCakeOrder.getSpEventsName(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table2.addCell(cell);

                doc.add(table2);

                PdfPTable table3 = new PdfPTable(3);
                float[] columnWidth3 = new float[]{40, 90, 60};
                table3.setWidths(columnWidth3);
                table3.setTotalWidth(columnWidth3);

                cell = new PdfPCell(new Phrase("Weight", normalTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                cell = new PdfPCell(new Phrase("" + spCakeOrder.getInputKgFr(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                cell = new PdfPCell(new Phrase("Flavour - " + spCakeOrder.getSpfName(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table3.addCell(cell);

                doc.add(table3);

                PdfPTable table4 = new PdfPTable(3);
                float[] columnWidth4 = new float[]{40, 90, 60};
                table4.setWidths(columnWidth4);
                table4.setTotalWidth(columnWidth4);

                cell = new PdfPCell(new Phrase("Special Instructions", normalTextFont));
                cell.setHorizontalAlignment(0);
                table4.addCell(cell);

                cell = new PdfPCell(new Phrase("" + spCakeOrder.getSpInstructions(), boldTextFont));
                cell.setHorizontalAlignment(0);
                cell.setBorder(Rectangle.NO_BORDER);
                table4.addCell(cell);

                cell = new PdfPCell(new Phrase("", boldTextFont));
                cell.setHorizontalAlignment(0);
                cell.setBorder(Rectangle.RIGHT);
                table4.addCell(cell);

                doc.add(table4);

                PdfPTable table5 = new PdfPTable(3);
                float[] columnWidth5 = new float[]{40, 90, 60};
                table5.setWidths(columnWidth5);
                table5.setTotalWidth(columnWidth5);

                cell = new PdfPCell(new Phrase("Date of Delivery", normalTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                cell = new PdfPCell(new Phrase("" + spCakeOrder.getSpDeliveryDate(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                cell = new PdfPCell(new Phrase("Place of Delivery - " + spCakeOrder.getSpDeliveryPlace(), boldTextFont));
                cell.setHorizontalAlignment(0);
                table5.addCell(cell);

                doc.add(table5);

                PdfPTable table6 = new PdfPTable(3);
                float[] columnWidth6 = new float[]{40, 90, 60};
                table6.setWidths(columnWidth6);
                table6.setTotalWidth(columnWidth6);

                cell = new PdfPCell(new Phrase("Photo", normalTextFont));
                cell.setHorizontalAlignment(0);
                table6.addCell(cell);

                if (bitmapdata1 != null) {
                    Image photo1 = Image.getInstance(bitmapdata1);
                    photo1.scaleAbsolute(100f, 100f);
                    cell.addElement(photo1);
                    cell.setHorizontalAlignment(0);
                    table6.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("", boldTextFont));
                    cell.setHorizontalAlignment(0);
                    table6.addCell(cell);
                }

                if (bitmapdata2 != null) {
                    Image photo2 = Image.getInstance(bitmapdata2);
                    photo2.scaleAbsolute(100f, 100f);
                    cell.addElement(photo2);
                    cell.setHorizontalAlignment(0);
                    table6.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("", boldTextFont));
                    cell.setHorizontalAlignment(0);
                    table6.addCell(cell);
                }

                doc.add(table6);

                //-----------------DATA TABLE--------------------------


            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(context, "Unable To Generate pdf", Toast.LENGTH_SHORT).show();
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


    public void updateList(ArrayList<SPCakeOrder> list) {
        orderListFiltered = list;
        notifyDataSetChanged();
    }

}

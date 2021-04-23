package com.mobatia.naisapp.activities.universityguidance.guidanceessential.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.model.GuidanceEssentialDetailModel;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.model.GuidanceEssentialModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class GuidanceEssentialDetailAdapter extends RecyclerView.Adapter<GuidanceEssentialDetailAdapter.MyViewHolder> {
     PDFView pdfView;
    private Context mContext;
    private ArrayList<GuidanceEssentialDetailModel> informationModelArrayList;
    Bitmap bitmap;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textOnlyTxt,imageTypeTxt,pdfTxt;
        RelativeLayout textRelative,imageTypeRelative,pdfTypeRelative,pdfRelative;
        ImageView imageTypeImg,playImg;


        public MyViewHolder(View view) {
            super(view);
            textRelative = (RelativeLayout) view.findViewById(R.id.textRelative);
            textOnlyTxt = (TextView) view.findViewById(R.id.textOnlyTxt);

            imageTypeRelative = (RelativeLayout) view.findViewById(R.id.imageTypeRelative);
            imageTypeTxt = (TextView) view.findViewById(R.id.imageTypeTxt);
            imageTypeImg = (ImageView) view.findViewById(R.id.imageTypeImg);
            playImg = (ImageView) view.findViewById(R.id.playImg);

            pdfTypeRelative = (RelativeLayout) view.findViewById(R.id.pdfTypeRelative);
            pdfRelative = (RelativeLayout) view.findViewById(R.id.pdfRelative);
            pdfTxt = (TextView) view.findViewById(R.id.pdfTxt);
            pdfView = (PDFView) view.findViewById(R.id.pdfView);
        }
    }


    public GuidanceEssentialDetailAdapter(Context mContext, ArrayList<GuidanceEssentialDetailModel> informationModelArrayList) {
        this.mContext = mContext;
        this.informationModelArrayList = informationModelArrayList;

    }

    @Override
    public GuidanceEssentialDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_guidance_essential_detail, parent, false);

        return new GuidanceEssentialDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

      holder.pdfTxt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });

        if (informationModelArrayList.get(position).getFile_type().equalsIgnoreCase(""))
        {
            holder.imageTypeRelative.setVisibility(View.GONE);
            holder.pdfTypeRelative.setVisibility(View.GONE);
            holder.textRelative.setVisibility(View.VISIBLE);
            holder.textOnlyTxt.setText(informationModelArrayList.get(position).getDescription());
        }
        else if (informationModelArrayList.get(position).getFile_type().equalsIgnoreCase("Image"))
        {
            holder.imageTypeRelative.setVisibility(View.VISIBLE);
            holder.textRelative.setVisibility(View.GONE);
            holder.pdfTypeRelative.setVisibility(View.GONE);
            holder.playImg.setVisibility(View.GONE);
            Picasso.with(mContext).load(AppUtils.replace(informationModelArrayList.get(position).getFile_url())).placeholder(R.drawable.boy).fit().into(holder.imageTypeImg);
            if (informationModelArrayList.get(position).getDescription().equalsIgnoreCase(""))
            {
                holder.imageTypeTxt.setVisibility(View.GONE);
            }
            else
            {
                holder.imageTypeTxt.setVisibility(View.VISIBLE);
                holder.imageTypeTxt.setText(informationModelArrayList.get(position).getDescription());
            }
        }
        else if (informationModelArrayList.get(position).getFile_type().equalsIgnoreCase("PDF"))
        {
            holder.imageTypeRelative.setVisibility(View.GONE);
            holder.textRelative.setVisibility(View.GONE);
            holder.pdfTypeRelative.setVisibility(View.VISIBLE);
            Log.e("FILE NAME",informationModelArrayList.get(position).getFile_url());
            new RetrivePDFfromUrl().execute(informationModelArrayList.get(position).getFile_url());
            if (informationModelArrayList.get(position).getDescription().equalsIgnoreCase(""))
            {
                holder.pdfTxt.setVisibility(View.GONE);
            }
            else
            {
                holder.pdfTxt.setVisibility(View.VISIBLE);
                holder.pdfTxt.setText(informationModelArrayList.get(position).getDescription());
            }


        }

        else if (informationModelArrayList.get(position).getFile_type().equalsIgnoreCase("Video"))
        {
            holder.imageTypeRelative.setVisibility(View.VISIBLE);
            holder.textRelative.setVisibility(View.GONE);
            holder.pdfTypeRelative.setVisibility(View.GONE);
            holder.playImg.setVisibility(View.VISIBLE);
            if (informationModelArrayList.get(position).getDescription().equalsIgnoreCase(""))
            {
                holder.imageTypeTxt.setVisibility(View.GONE);
            }
            else
            {
                holder.imageTypeTxt.setVisibility(View.VISIBLE);
                holder.imageTypeTxt.setText(informationModelArrayList.get(position).getDescription());
            }

            if (informationModelArrayList.get(position).getFile_url().contains("https://youtu.be/"))
            {
                String main=informationModelArrayList.get(position).getFile_url();
                System.out.println(main.substring(main.lastIndexOf("/") + 1));
                String imageURL="https://img.youtube.com/vi/"+main.substring(main.lastIndexOf("/") + 1)+"/hqdefault.jpg";
                Picasso.with(mContext).load(AppUtils.replace(imageURL)).placeholder(R.drawable.boy).fit().into(holder.imageTypeImg);
                Log.e("IMAGE URL",imageURL);
            }
            else {
                try {
                    bitmap  =retriveVideoFrameFromVideo(informationModelArrayList.get(position).getFile_url());
                    if (bitmap !=null){
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        ((Activity) mContext).getWindowManager()
                                .getDefaultDisplay()
                                .getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;
                        bitmap = Bitmap.createScaledBitmap(bitmap,width,500,false);
                        holder.imageTypeImg.setImageBitmap(bitmap);
                    }
                }catch (Throwable throwable){
                    throwable.printStackTrace();
                }
            }


        }


//        holder.listTxtTitle.setText(informationModelArrayList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return informationModelArrayList.size();
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
         //   pdfView.fromStream(inputStream).enableSwipe(false).enableDoubletap(false).load();
            // pdfView.enableSwipe(false);
            pdfView.scrollBy(1, 1);
            pdfView.fromStream(inputStream).enableDoubletap(false).enableSwipe(false).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                 //   loader.setVisibility(View.GONE);

                }
            }).load();
            pdfView.setVisibility(View.VISIBLE);


        }
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
 }

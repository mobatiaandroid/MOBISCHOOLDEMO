package com.mobatia.naisapp.activities.universityguidance.guidanceessential.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.VideoViewActivity;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.model.GuidanceEssentialDetailModel;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.model.GuidanceEssentialModel;
import com.mobatia.naisapp.constants.ClickInter;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class GuidanceEssentialDetailAdapter extends RecyclerView.Adapter<GuidanceEssentialDetailAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<GuidanceEssentialDetailModel> informationModelArrayList;
    private ClickInter listener;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textOnlyTxt,imageTypeTxt,pdfTxt;
        RelativeLayout textRelative,pdfRelative,imagRel;
        LinearLayout imageTypeRelative;
        ImageView imageTypeImg,playImg,pdfImg,pdfImgIcon;
        LinearLayout pdfTypeRelative;

        public MyViewHolder(View view) {
            super(view);
            textRelative = (RelativeLayout) view.findViewById(R.id.textRelative);
            textOnlyTxt = (TextView) view.findViewById(R.id.textOnlyTxt);

            imageTypeRelative = (LinearLayout) view.findViewById(R.id.imageTypeRelative);
            imageTypeTxt = (TextView) view.findViewById(R.id.imageTypeTxt);
            imageTypeImg = (ImageView) view.findViewById(R.id.imageTypeImg);
            playImg = (ImageView) view.findViewById(R.id.playImg);
            pdfImg = (ImageView) view.findViewById(R.id.pdfImg);
            pdfImgIcon = (ImageView) view.findViewById(R.id.pdfImgIcon);

            pdfTypeRelative = (LinearLayout) view.findViewById(R.id.pdfTypeRelative);
            pdfRelative = (RelativeLayout) view.findViewById(R.id.pdfRelative);
            imagRel = (RelativeLayout) view.findViewById(R.id.imagRel);
            pdfTxt = (TextView) view.findViewById(R.id.pdfTxt);
            pdfRelative.setOnClickListener(this);
            imageTypeImg.setOnClickListener(this);
            imagRel.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
       //     listenerRef.get().onPositionClicked(getAdapterPosition());
            if (listener != null) listener.onPositionClicked(getAdapterPosition());
            if (v.getId()==imageTypeImg.getId())
            {
                Log.e("VIDEO CHECK","VIDEO IMAGE CLICK");
                if (informationModelArrayList.get(getAdapterPosition()).getFile_type().equalsIgnoreCase("Video"))
                {
                    Intent mIntent = new Intent(mContext, VideoViewActivity.class);
                    mIntent.putExtra("URL", informationModelArrayList.get(getAdapterPosition()).getFile_url());
                    mContext.startActivity(mIntent);
                }
            }
           if (v.getId()==pdfRelative.getId())
           {
               Intent intent = new Intent(mContext, PdfReaderActivity.class);
               intent.putExtra("pdf_url",informationModelArrayList.get(getAdapterPosition()).getFile_url());
               mContext.startActivity(intent);
           }
             if (v.getId()==pdfImg.getId())
           {
               Intent intent = new Intent(mContext, PdfReaderActivity.class);
               intent.putExtra("pdf_url",informationModelArrayList.get(getAdapterPosition()).getFile_url());
               mContext.startActivity(intent);
           }

             if (v.getId()==pdfImg.getId())
           {
               Intent intent = new Intent(mContext, PdfReaderActivity.class);
               intent.putExtra("pdf_url",informationModelArrayList.get(getAdapterPosition()).getFile_url());
               mContext.startActivity(intent);
           }
            if (v.getId()==pdfImgIcon.getId())
           {
               Intent intent = new Intent(mContext, PdfReaderActivity.class);
               intent.putExtra("pdf_url",informationModelArrayList.get(getAdapterPosition()).getFile_url());
               mContext.startActivity(intent);
           }

            if (v.getId()==imagRel.getId())
           {
               Log.e("VIDEO CHECK","VIDEO IMAGE CLICK");
               if (informationModelArrayList.get(getAdapterPosition()).getFile_type().equalsIgnoreCase("Video"))
               {
                   Intent mIntent = new Intent(mContext, VideoViewActivity.class);
                   mIntent.putExtra("URL", informationModelArrayList.get(getAdapterPosition()).getFile_url());
                   mContext.startActivity(mIntent);
               }
           }

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void setClickListener(ClickInter itemClickListener) {
        this.listener = itemClickListener;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {

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
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                holder.textOnlyTxt.setText(Html.fromHtml(informationModelArrayList.get(position).getDescription(), Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                holder.textOnlyTxt.setText(Html.fromHtml(informationModelArrayList.get(position).getDescription()));
//            }
         //   holder.textOnlyTxt.setText(informationModelArrayList.get(position).getDescription());
          //  setTextViewHTML(holder.textOnlyTxt,informationModelArrayList.get(position).getDescription());
            holder.textOnlyTxt.setText( Html.fromHtml( informationModelArrayList.get(position).getDescription() ) );
            holder.textOnlyTxt.setMovementMethod(LinkMovementMethod.getInstance());
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
                holder.imageTypeTxt.setText( Html.fromHtml( informationModelArrayList.get(position).getDescription() ) );
                holder.imageTypeTxt.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        else if (informationModelArrayList.get(position).getFile_type().equalsIgnoreCase("PDF"))
        {
            holder.imageTypeRelative.setVisibility(View.GONE);
            holder.textRelative.setVisibility(View.GONE);
            holder.pdfTypeRelative.setVisibility(View.VISIBLE);
            if (informationModelArrayList.get(position).getPdf_thumbnail_url().equalsIgnoreCase(""))
            {
                holder.pdfImg.setBackgroundColor(R.color.white);
            }
            else {
                Picasso.with(mContext).load(AppUtils.replace(informationModelArrayList.get(position).getPdf_thumbnail_url())).placeholder(R.drawable.pdf_icon).fit().into(holder.pdfImg);

            }
            Log.e("FILE NAME",informationModelArrayList.get(position).getFile_url());
            if (informationModelArrayList.get(position).getDescription().equalsIgnoreCase(""))
            {
                holder.pdfTxt.setVisibility(View.GONE);
            }
            else
            {
                holder.pdfTxt.setVisibility(View.VISIBLE);
                holder.pdfTxt.setText( Html.fromHtml( informationModelArrayList.get(position).getDescription() ) );
                holder.pdfTxt.setMovementMethod(LinkMovementMethod.getInstance());
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
                holder.imageTypeTxt.setText( Html.fromHtml( informationModelArrayList.get(position).getDescription() ) );
                holder.imageTypeTxt.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (informationModelArrayList.get(position).getFile_url().contains("https://youtu.be/") || informationModelArrayList.get(position).getFile_url().contains("https://www.youtube.com/") )
            {
                if (informationModelArrayList.get(position).getImage_url().equalsIgnoreCase(""))
                {

                }
                else
                {
                    Picasso.with(mContext).load(AppUtils.replace(informationModelArrayList.get(position).getImage_url())).placeholder(R.drawable.boy).fit().into(holder.imageTypeImg);
                }
            }
            else
            {
                if(informationModelArrayList.get(position).getBitmap().equals(""))
                {

                }
                else {
                    holder.imageTypeImg.setImageBitmap(informationModelArrayList.get(position).getBitmap());
                }
            }

        }

    }


    @Override
    public int getItemCount() {
        return informationModelArrayList.size();
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


    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
 }

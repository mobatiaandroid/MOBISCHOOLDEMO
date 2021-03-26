package com.mobatia.naisapp.fragments.permission_slips;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;

import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class PerformingSlipsListAdapter extends RecyclerView.Adapter<PerformingSlipsListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SecondaryModel> mnNewsLetterModelArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIcon;

        TextView pdfTitle;
        public MyViewHolder(View view) {
            super(view);

            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = (TextView) view.findViewById(R.id.pdfTitle);


        }
    }


    public PerformingSlipsListAdapter(Context mContext, ArrayList<SecondaryModel> mnNewsLetterModelArrayList) {
        this.mContext = mContext;
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_pdf_adapter_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
        holder.pdfTitle.setText(mnNewsLetterModelArrayList.get(position).getTitle());
        holder.imageIcon.setVisibility(View.GONE);
      /*  if (mnNewsLetterModelArrayList.get(position).getmFile().endsWith(".pdf")) {
        holder.imageIcon.setBackgroundResource(R.drawable.pdfdownloadbutton);
        }
        else
        {
            holder.imageIcon.setBackgroundResource(R.drawable.webcontentviewbutton);

        }
*/
    }


    @Override
    public int getItemCount() {
        return mnNewsLetterModelArrayList.size();
    }

}

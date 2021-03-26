package com.mobatia.naisapp.activities.trips.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.trips.model.InstallmentListModel;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.ClickListener;
import com.mobatia.naisapp.fragments.trips.TripListModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PaymentDetailAdapterNew extends RecyclerView.Adapter<PaymentDetailAdapterNew.MyViewHolder> {


    private Context mContext;
    private ArrayList<TripListModel> mntripModelArrayList;
    private ArrayList<InstallmentListModel> mInstallmentModelArrayList=new ArrayList<>();
    int adapterSize,Position,mPosition;
    int s=1;
    Boolean isInstallment=false;
    Boolean isFullPayment=false;
    String installment_amount="";
    String amount="";
    String payment_option="";
    String installment_id="";
    String installment="";
    WebView webView;
    LinearLayout mainLinear;
    int clickedPosition=0;
    String orderPaid;
    boolean isAddToCalendar=false;
    private WeakReference<ClickListener> listenerRefRead;
    private ClickListener listener;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView installmentTxt;
        TextView dateTxt;
        TextView totalInstallTxt;
        TextView paidTxt;
        TextView amountInstallTxt;
        ImageView Imagicon;
        ImageView paidImg;
        Button paybuttonInstall;
        TextView payBlack;
        TextView totalAmount;
        Button payTotalButton;
        LinearLayout addCal;
        LinearLayout installmentLinear;
        LinearLayout installmentFirstLinear;
        LinearLayout totalLinear;
        LinearLayout mainInstallLinear;
        ImageView checked;

        public MyViewHolder(View view) {
            super(view);
            installmentTxt = view.findViewById(R.id.installmentTxt);
            dateTxt = view.findViewById(R.id.dateTxt);
            totalInstallTxt = view.findViewById(R.id.totalInstallTxt);
            paidTxt = view.findViewById(R.id.paidTxt);
            amountInstallTxt = view.findViewById(R.id.amountInstallTxt);
            totalAmount = view.findViewById(R.id.totalAmount);
            Imagicon = view.findViewById(R.id.imageIcon);
            paybuttonInstall = view.findViewById(R.id.paybuttonInstall);
            addCal = view.findViewById(R.id.addCal);
            checked=view.findViewById(R.id.checked);
            mainInstallLinear=view.findViewById(R.id.mainInstallLinear);
            installmentLinear=view.findViewById(R.id.installmentLinear);
            totalLinear=view.findViewById(R.id.totalLinear);
            paidImg=view.findViewById(R.id.paidImg);
            payTotalButton=view.findViewById(R.id.payTotalButton);
            listenerRefRead = new WeakReference<>(listener);
            paybuttonInstall.setOnClickListener(this);
            payTotalButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == paybuttonInstall)
            {

                System.out.println("clicked Position" + clickedPosition);
                if (mntripModelArrayList.get(AppController.Position).isEmiAvailable() && mInstallmentModelArrayList.get(clickedPosition).getEnable().equalsIgnoreCase("-1")) {
                    installment_amount = mInstallmentModelArrayList.get(clickedPosition).getInst_amount();
                    amount = mInstallmentModelArrayList.get(clickedPosition).getInst_amount();
                    // for installment payment payment option will be 1
                    payment_option = "1";
                    installment_id = mInstallmentModelArrayList.get(clickedPosition).getInstallment_id();
                    installment = "1";
                    isFullPayment = false;
                    isInstallment = true;
                } else
                    {
                    isFullPayment = true;
                    isInstallment = false;
                    installment_amount = mntripModelArrayList.get(AppController.Position).getAmount();
                    amount = mntripModelArrayList.get(AppController.Position).getAmount();
                    payment_option = "2";
                    installment_id = "";
                    installment = "2";
                }
                listenerRefRead.get().onPositionClicked(getAdapterPosition(), isFullPayment, isInstallment, installment_amount, amount, payment_option, installment_id, installment, orderPaid);

            }
            else if (view == payTotalButton)
            {
                System.out.println("clicked total");
                isInstallment = false;
                isFullPayment = true;
                installment_amount = mntripModelArrayList.get(AppController.Position).getRemaining_amount();
                amount = mntripModelArrayList.get(AppController.Position).getRemaining_amount();
                payment_option = "2";
                installment_id = mInstallmentModelArrayList.get(clickedPosition).getInstallment_id();
                installment = "1";
                listenerRefRead.get().onPositionClicked(getAdapterPosition(), isFullPayment, isInstallment, installment_amount, amount, payment_option, installment_id, installment, orderPaid);

            }
        }

        }



    public PaymentDetailAdapterNew(Context mContext, ArrayList<TripListModel> mntripModelArrayList, int adapterSize, int Position, ClickListener listener) {
        this.mContext = mContext;
        this.mntripModelArrayList = mntripModelArrayList;
        this.adapterSize=adapterSize;
        this.Position=Position;
        this.listener = listener;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_withinstallment_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        mInstallmentModelArrayList=mntripModelArrayList.get(AppController.Position).getInstallmentArrayList();
        int lastPositionInstallment=adapterSize-1;

        // Installment Available
        if (mntripModelArrayList.get(AppController.Position).isEmiAvailable()) {
            System.out.println("Emi available working");
            System.out.println("working add++"+mInstallmentModelArrayList.size());
            for (int i=0;i<mInstallmentModelArrayList.size();i++)
            {
                if (mInstallmentModelArrayList.get(i).getInst_date_status().equalsIgnoreCase("0"))
                {
                    if (mInstallmentModelArrayList.get(i).getPaid_status().equalsIgnoreCase("0"))
                    {
                        isAddToCalendar=true;
                    }
                }
            }
           /* if (mntripModelArrayList.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("1"))
            {
                System.out.println("working add to");
                isAddToCalendar=false;
            }*/
            if (mInstallmentModelArrayList.size() == 1)
            {
                if (mntripModelArrayList.get(AppController.Position).getPayment_status().equalsIgnoreCase("1")) {
                    holder.installmentLinear.setVisibility(View.GONE);
                    holder.mainInstallLinear.setVisibility(View.GONE);
                    holder.totalInstallTxt.setVisibility(View.VISIBLE);
                    holder.totalLinear.setVisibility(View.VISIBLE);
                    holder.totalAmount.setVisibility(View.VISIBLE);
                    holder.paidImg.setVisibility(View.VISIBLE);
                    holder.totalAmount.setText(mntripModelArrayList.get(AppController.Position).getAmount());
                    holder.payTotalButton.setVisibility(View.GONE);
                    holder.paybuttonInstall.setVisibility(View.GONE);
                }
            }
            else
            {
                if (lastPositionInstallment != position)
                {
                    System.out.println("working not last position");
                    holder.installmentLinear.setVisibility(View.VISIBLE);
                    holder.installmentTxt.setVisibility(View.VISIBLE);
                    holder.installmentTxt.setText("Installment " + (position + 1));
                    holder.dateTxt.setVisibility(View.VISIBLE);
                    if (isAddToCalendar)
                    {
                        holder.addCal.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.addCal.setVisibility(View.GONE);


                    }
                    if (mInstallmentModelArrayList.get(position).getPaid_status().equalsIgnoreCase("1"))
                    {
                        holder.addCal.setVisibility(View.GONE);
                        holder.paidTxt.setVisibility(View.VISIBLE);
                        holder.checked.setVisibility(View.VISIBLE);
                        holder.paybuttonInstall.setVisibility(View.GONE);
                        if (mInstallmentModelArrayList.get(position).getPayment_option().equalsIgnoreCase("2"))
                        {
                            holder.dateTxt.setText("(Remaining)");

                        }
                        else
                        {
                            holder.dateTxt.setText("(" + AppUtils.dateConversionddmmyyyy(mntripModelArrayList.get(AppController.Position).getInstallmentArrayList().get(position).getPaid_date()) + ")");

                        }
                    }
                    else
                    {
                       // holder.addCal.setVisibility(View.VISIBLE);
                        holder.paidTxt.setVisibility(View.GONE);
                        holder.checked.setVisibility(View.GONE);
                        holder.paybuttonInstall.setVisibility(View.VISIBLE);
                        holder.dateTxt.setText("(" + AppUtils.dateConversionddmmyyyy(mntripModelArrayList.get(AppController.Position).getInstallmentArrayList().get(position).getLast_payment_date()) + ")");

                    }
                    if (mntripModelArrayList.get(AppController.Position).getInstallmentArrayList().get(position).getPaid_status().equalsIgnoreCase("0")) {
                        System.out.println("working 6");
                        holder.amountInstallTxt.setText(mntripModelArrayList.get(AppController.Position).getInstallmentArrayList().get(position).getInst_amount());

                    } else {
                        holder.amountInstallTxt.setText(mntripModelArrayList.get(AppController.Position).getInstallmentArrayList().get(position).getPaid_amount());

                    }
                    holder.mainInstallLinear.setVisibility(View.VISIBLE);
                    holder.totalInstallTxt.setVisibility(View.GONE);
                    holder.totalLinear.setVisibility(View.GONE);

                    if (mInstallmentModelArrayList.get(position).getEnable().equalsIgnoreCase("-1")) {
                        clickedPosition = position;
                    if (clickedPosition != 0) {
                        orderPaid = "1";
                    } else {
                        orderPaid = "0";
                    }
                        if (mInstallmentModelArrayList.get(position).getInst_date_status().equalsIgnoreCase("0")) {
                            System.out.println("working 10");
                            holder.paybuttonInstall.setBackground(mContext.getResources().getDrawable(R.color.rel_two));
                            holder.paybuttonInstall.setClickable(true);
                        }
                        // if date over  button click disable
                        else {
                            System.out.println("working 11");
                            holder.paybuttonInstall.setBackground(mContext.getResources().getDrawable(R.drawable.button_grey));
                            holder.paybuttonInstall.setClickable(false);
                        }
                    }
                    // setEnable is not -1 the click disable even if date is not over
                    else {
                        holder.paybuttonInstall.setBackground(mContext.getResources().getDrawable(R.drawable.button_grey));
                        holder.paybuttonInstall.setClickable(false);
                    }

                   /* if (mInstallmentModelArrayList.get(position).getPaid_status().equalsIgnoreCase("0")) {
                        System.out.println("working 8");
                        holder.addCal.setVisibility(View.VISIBLE);
                        holder.paybuttonInstall.setVisibility(View.VISIBLE);
                        holder.paidTxt.setVisibility(View.GONE);
                        holder.checked.setVisibility(View.GONE);
                        // the position which can be enable to pay if getEnble is -1 then click enable else click disable
                        // the click only be enable if getEnable is -1 and payment date status  0 otherwise click is disable


                    }
                    // installment is paid
                    else {
                        System.out.println("working 5");
                        holder.installmentLinear.setVisibility(View.VISIBLE);
                        holder.totalLinear.setVisibility(View.GONE);
                        holder.installmentTxt.setVisibility(View.VISIBLE);
                        holder.installmentTxt.setText("Installment " + (position + 1));
                        holder.dateTxt.setVisibility(View.VISIBLE);
                        holder.mainInstallLinear.setVisibility(View.VISIBLE);

                        holder.totalInstallTxt.setVisibility(View.GONE);
                        holder.addCal.setVisibility(View.GONE);
                        holder.paybuttonInstall.setVisibility(View.GONE);
                        holder.paidTxt.setVisibility(View.VISIBLE);
                        holder.checked.setVisibility(View.VISIBLE);
                    }*/
                }
                else
                {

                // last position of recycler view
                // if payment date status i s not over for full payment
                System.out.println("working 6");
                if (mntripModelArrayList.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("0")) {
                    System.out.println("working 7");
                    if (mntripModelArrayList.get(AppController.Position).getPayment_status().equalsIgnoreCase("0")) {
                        System.out.println("working 8");
                        holder.installmentLinear.setVisibility(View.GONE);
                        holder.totalInstallTxt.setVisibility(View.VISIBLE);
                        holder.mainInstallLinear.setVisibility(View.GONE);
                        holder.totalLinear.setVisibility(View.VISIBLE);
                        holder.totalAmount.setVisibility(View.VISIBLE);
                        holder.totalAmount.setText(mntripModelArrayList.get(AppController.Position).getAmount());
                        holder.payTotalButton.setVisibility(View.VISIBLE);
                        holder.payTotalButton.setText("PAY :" + mntripModelArrayList.get(AppController.Position).getRemaining_amount());
                        holder.payTotalButton.setBackground(mContext.getResources().getDrawable(R.color.rel_two));
                        holder.payTotalButton.setClickable(true);

                    } else {
                        System.out.println("working 9");
                        holder.installmentLinear.setVisibility(View.GONE);
                        holder.mainInstallLinear.setVisibility(View.GONE);
                        holder.totalInstallTxt.setVisibility(View.VISIBLE);
                        holder.totalLinear.setVisibility(View.VISIBLE);
                        holder.totalAmount.setVisibility(View.VISIBLE);
                        holder.paidImg.setVisibility(View.VISIBLE);
                        holder.totalAmount.setText(mntripModelArrayList.get(AppController.Position).getAmount());
                        holder.payTotalButton.setVisibility(View.GONE);
                        holder.paybuttonInstall.setVisibility(View.GONE);
                    }
                }
                // payment date status over
                else {
                    System.out.println("working 10");
                    holder.installmentLinear.setVisibility(View.GONE);
                    // holder.payTotalButton.setVisibility(View.GONE);
                    holder.mainInstallLinear.setVisibility(View.GONE);
                    holder.totalInstallTxt.setVisibility(View.VISIBLE);
                    holder.totalLinear.setVisibility(View.VISIBLE);
                    holder.totalAmount.setVisibility(View.VISIBLE);
                    holder.payTotalButton.setVisibility(View.VISIBLE);
                    holder.payTotalButton.setText("PAY :" + mntripModelArrayList.get(AppController.Position).getRemaining_amount());
                    holder.totalAmount.setText(mntripModelArrayList.get(AppController.Position).getAmount());
                    holder.payTotalButton.setBackground(mContext.getResources().getDrawable(R.drawable.button_grey));
                    holder.payTotalButton.setClickable(false);
                }


            }}

        }
        //Emi not Available
         else
             {
                 if (mntripModelArrayList.get(AppController.Position).getPayment_status().equalsIgnoreCase("0"))
                      {
                 holder.totalLinear.setVisibility(View.GONE);
                 holder.installmentTxt.setVisibility(View.GONE);
                 holder.mainInstallLinear.setVisibility(View.GONE);
                 holder.dateTxt.setVisibility(View.GONE);
                 holder.installmentLinear.setVisibility(View.VISIBLE);
                 holder.totalInstallTxt.setVisibility(View.VISIBLE);
                 holder.addCal.setVisibility(View.VISIBLE);
                 holder.totalInstallTxt.setText("TOTAL");
                 holder.paybuttonInstall.setVisibility(View.VISIBLE);
                 holder.paidTxt.setVisibility(View.GONE);
                 holder.checked.setVisibility(View.GONE);
                 holder.amountInstallTxt.setText(mntripModelArrayList.get(AppController.Position).getAmount());
             }
             else {
                 holder.totalLinear.setVisibility(View.VISIBLE);
                 holder.installmentLinear.setVisibility(View.GONE);
                 holder.paidImg.setVisibility(View.VISIBLE);
                 holder.payTotalButton.setVisibility(View.GONE);
                 holder.paybuttonInstall.setVisibility(View.GONE);
                 holder.totalAmount.setText(mntripModelArrayList.get(AppController.Position).getAmount());
             }
        }

         holder.addCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paymentMode = "";
                String title = "";
                String last_payment_date = "";
                if (mntripModelArrayList.get(AppController.Position).getInstallmentArrayList().size() > 0) {
                    paymentMode = "1";
                    title = mntripModelArrayList.get(AppController.Position).getTitle() + "Installment " + (position + 1);
                    last_payment_date = mInstallmentModelArrayList.get(position).getLast_payment_date();


                } else {
                    paymentMode = "2";
                    title = mntripModelArrayList.get(AppController.Position).getTitle();
                    last_payment_date = mntripModelArrayList.get(AppController.Position).getClosing_date();
                }
                long startTime = 0;

                try {
                    System.out.println("date"+last_payment_date);
                    Date dateStart = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(last_payment_date);
                    startTime = dateStart.getTime();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", startTime);
                intent.putExtra("allDay", false);
                intent.putExtra("title", title);
                mContext.startActivity(intent);
            }

        });

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        System.out.println("adapter size "+adapterSize);
        return adapterSize;
    }

}
/* if (mntripModelArrayList.get(Position).getInstallment().equalsIgnoreCase("2"))
        {
            System.out.println("working");
            if (mntripModelArrayList.get(Position).getFullpaymentArrayList().size()>0)
            {
                holder.totalLinear.setVisibility(View.VISIBLE);
                holder.installmentLinear.setVisibility(View.GONE);
                holder.paidImg.setVisibility(View.VISIBLE);
                holder.payTotalButton.setVisibility(View.GONE);
                holder.totalAmount.setText(mntripModelArrayList.get(Position).getFullpaymentArrayList().get(position).getPaid_amount());

            }
            else {
                // No installment Not paid
                System.out.println("working else");
                holder.totalLinear.setVisibility(View.GONE);
                holder.installmentTxt.setVisibility(View.GONE);
                holder.installmentLinear.setVisibility(View.VISIBLE);
                holder.totalInstallTxt.setVisibility(View.VISIBLE);
                holder.addCal.setVisibility(View.VISIBLE);
                holder.totalInstallTxt.setText("TOTAL");
                holder.paybuttonInstall.setVisibility(View.VISIBLE);
                holder.paidTxt.setVisibility(View.GONE);
                holder.checked.setVisibility(View.GONE);
                holder.amountInstallTxt.setText(mntripModelArrayList.get(Position).getAmount());

            }
        }
        else if (mntripModelArrayList.get(Position).getInstallment().equalsIgnoreCase("1"))
        {

            //Lost position of the recyclerview
            if (lastPositionInstallment==position)
            {
                if (mntripModelArrayList.get(Position).getPayment_status().equalsIgnoreCase("0"))
                {
                    if (mntripModelArrayList.get(Position).getPayment_date_status().equalsIgnoreCase("0"))
                    {
                        holder.totalLinear.setVisibility(View.VISIBLE);
                        holder.installmentLinear.setVisibility(View.GONE);
                        holder.paidImg.setVisibility(View.GONE);
                        holder.payTotalButton.setVisibility(View.VISIBLE);
                        holder.totalAmount.setVisibility(View.VISIBLE);
                        holder.paybuttonInstall.setBackgroundColor(mContext.getResources().getColor(R.color.rel_one));
                        holder.payTotalButton.setText("PAY: "+mntripModelArrayList.get(Position).getRemaining_amount());
                        holder.totalAmount.setText(mntripModelArrayList.get(Position).getAmount());
                    }
                    else {
                        holder.totalLinear.setVisibility(View.VISIBLE);
                        holder.installmentLinear.setVisibility(View.GONE);
                        holder.paidImg.setVisibility(View.GONE);
                        holder.payTotalButton.setVisibility(View.VISIBLE);
                        holder.totalAmount.setVisibility(View.VISIBLE);
                        holder.paybuttonInstall.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
                        holder.payTotalButton.setText("PAY: "+mntripModelArrayList.get(Position).getRemaining_amount());
                        holder.totalAmount.setText(mntripModelArrayList.get(Position).getAmount());
                    }

                }
                else
                {
                    holder.totalLinear.setVisibility(View.VISIBLE);
                    holder.installmentLinear.setVisibility(View.GONE);
                    holder.paidImg.setVisibility(View.VISIBLE);
                    holder.payTotalButton.setVisibility(View.GONE);
                    holder.totalAmount.setVisibility(View.VISIBLE);
                    holder.payTotalButton.setText(mntripModelArrayList.get(Position).getAmount());
                }
            }
            else {
                if (mntripModelArrayList.get(Position).getInstallmentArrayList().get(position).getPaid_status().equalsIgnoreCase("0"))
                {
                    holder.totalLinear.setVisibility(View.GONE);
                    holder.paidImg.setVisibility(View.GONE);
                    holder.payTotalButton.setVisibility(View.GONE);
                    holder.totalAmount.setVisibility(View.GONE);
                    holder.installmentLinear.setVisibility(View.VISIBLE);
                    holder.addCal.setVisibility(View.VISIBLE);
                    holder.installmentTxt.setText("Installment "+(position+1));
                    holder.amountInstallTxt.setText(mntripModelArrayList.get(Position).getInstallmentArrayList().get(position).getInst_amount());
                    holder.totalInstallTxt.setVisibility(View.GONE);
                    holder.paidTxt.setVisibility(View.GONE);
                    holder.checked.setVisibility(View.GONE);
                    holder.installmentTxt.setVisibility(View.VISIBLE);
                    holder.paybuttonInstall.setVisibility(View.VISIBLE);

                }
                else {
                    holder.totalLinear.setVisibility(View.GONE);
                    holder.paidImg.setVisibility(View.GONE);
                    holder.payTotalButton.setVisibility(View.GONE);
                    holder.totalAmount.setVisibility(View.GONE);
                    holder.installmentLinear.setVisibility(View.VISIBLE);
                    holder.installmentTxt.setText("Installment "+position+1);
                    holder.amountInstallTxt.setText(mntripModelArrayList.get(Position).getInstallmentArrayList().get(position).getInst_amount());
                    holder.totalInstallTxt.setVisibility(View.GONE);
                    holder.paidTxt.setVisibility(View.VISIBLE);
                    holder.checked.setVisibility(View.VISIBLE);
                    holder.installmentTxt.setVisibility(View.VISIBLE);
                    holder.paybuttonInstall.setVisibility(View.GONE);
                    holder.addCal.setVisibility(View.GONE);
                }
            }

        }*/
/*if (mntripModelArrayList.get(Position).getInstallment().equalsIgnoreCase("2"))
                {
                    isFullPayment=true;
                    isInstallment=false;
                    installment_amount=mntripModelArrayList.get(Position).getAmount();
                    amount=mntripModelArrayList.get(Position).getAmount();
                    payment_option="2";
                    installment_id="";
                    installment="2";


                }   click view*/
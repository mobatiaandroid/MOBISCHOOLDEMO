package com.mobatia.naisapp.constants;

/**
 * Created by krishnaraj on 17/07/18.
 */

public interface ClickListener {
    void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amount,String amount,String payment_option,String installment_id,String installment,String orderPaid);

    void onLongClicked(int position);
}

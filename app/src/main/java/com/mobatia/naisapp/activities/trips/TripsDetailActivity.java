package com.mobatia.naisapp.activities.trips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobatia.naisapp.R;

public class TripsDetailActivity extends Activity {
    TextView amountTxt;
    Button proceedToPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_details);
        amountTxt=findViewById(R.id.amountTxt);
        proceedToPay=findViewById(R.id.proceedToPay);

        proceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TripsDetailActivity.this,PaymentDetailActivity.class);
                intent.putExtra("amount",amountTxt.getText().toString());
                intent.putExtra("random","TS210012");
                startActivity(intent);
            }
        });

    }

}

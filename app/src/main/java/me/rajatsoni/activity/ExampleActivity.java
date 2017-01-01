package me.rajatsoni.activity;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import me.rajatsoni.optionspicker.OptionPicker;
import me.rajatsoni.optionspicker.R;

/**
 * Created by Rajat Soni
 */

public class ExampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.example);

        final OptionPicker mOptionPicker = (OptionPicker) findViewById(R.id.iphone_style_option);

        ArrayList<String> mArrayList = new ArrayList<>();
        mArrayList.add("Google India");
        mArrayList.add("Teleperformance");
        mArrayList.add("SAP Labs");
        mArrayList.add("Intuit India Product Research and Development Center");
        mArrayList.add("Cadence Design Systems India");
        mArrayList.add("PayPal India");
        mArrayList.add("Cisco Systems India");
        mArrayList.add("Adobe Systems India");
        mArrayList.add("Google India");
        mArrayList.add("Teleperformance");
        mArrayList.add("SAP Labs");
        mArrayList.add("Intuit India Product Research and Development Center");
        mArrayList.add("Cadence Design Systems India");
        mArrayList.add("PayPal India");
        mArrayList.add("Cisco Systems India");
        mArrayList.add("Adobe Systems India");
        mArrayList.add("Teleperformance");
        mArrayList.add("SAP Labs");
        mArrayList.add("Intuit India Product Research and Development Center");
        mArrayList.add("Cadence Design Systems India");
        mArrayList.add("PayPal India");
        mArrayList.add("Cisco Systems India");
        mArrayList.add("Adobe Systems India");

//        mOptionPicker.setOnItemSelectedListener(new OptionSelectorContainer.OnItemSelectedListener() {
//            @Override
//            public void onSelection(int index) {
//                mOptionPicker.updatePivotBoxStroke(index, 0xFFBDBDBD);
//            }
//        });
//        mOptionPicker.setTextViewCustomizer(new OptionSelectorContainer.CustomBehaviour() {
//            @Override
//            public void selectedTextView(TextView selectedTextView) {
//                selectedTextView.setTextColor(0xFFCC0000);
//                selectedTextView.setTypeface(Typeface.DEFAULT_BOLD);
//            }
//
//            @Override
//            public void unselectedTextViews(TextView unselectedTextView) {
//                unselectedTextView.setTextColor(0xFFBDBDBD);
//                unselectedTextView.setTypeface(Typeface.SANS_SERIF);
//            }
//        });
//        mOptionPicker.setCustomTextView(layoutId);

        mOptionPicker.render(mArrayList,10);


    }
}

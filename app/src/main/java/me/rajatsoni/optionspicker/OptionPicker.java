package me.rajatsoni.optionspicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionPicker extends RelativeLayout {

    private int customTextViewLayoutId = 0;
    private OptionPickerContainer optionPickerContainer;
    private TextView pivotBox;

    public OptionPicker(Context context) {
        this(context, null);
    }

    public OptionPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OptionPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        logger("constructor");
        optionPickerContainer = new OptionPickerContainer(context);
    }

    public void render(ArrayList<String> dataSet, int unfocusedVisibleItemsCountParam) {
        logger("render");
        initialize(dataSet, unfocusedVisibleItemsCountParam);
    }

    public void render(ArrayList<String> dataSet) {
        logger("render2");
        initialize(dataSet, 4);
    }

    public int getSelectedItemIndex(){
        return optionPickerContainer.selectedItemIndex;
    }

    public void setCustomTextView(int customTextViewLayoutId){
        this.customTextViewLayoutId = customTextViewLayoutId;
    }

    private void initialize(ArrayList<String> dataSet, int unfocusedVisibleItemsCountParam){

        if(getChildCount() == 0) {

            int dataSetSize = dataSet.size();
            if(dataSetSize < unfocusedVisibleItemsCountParam){
                unfocusedVisibleItemsCountParam = dataSetSize;
            }
            if(customTextViewLayoutId!=0){
                optionPickerContainer.setUp(dataSet, unfocusedVisibleItemsCountParam % 2 != 0 ? unfocusedVisibleItemsCountParam + 1 : unfocusedVisibleItemsCountParam, customTextViewLayoutId);
            }else {
                optionPickerContainer.setUp(dataSet, unfocusedVisibleItemsCountParam % 2 != 0 ? unfocusedVisibleItemsCountParam + 1 : unfocusedVisibleItemsCountParam);
            }
            addView(optionPickerContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addPivotBox();
        }
    }

    public void updatePivotBoxStroke(int strokeWidth, int color) {
        GradientDrawable gd = (GradientDrawable) pivotBox.getBackground();
        gd.setStroke(strokeWidth, color);
    }

    public void updatePivotBoxStroke(int strokeWidth, String color) {
        GradientDrawable gd = (GradientDrawable) pivotBox.getBackground();
        gd.setStroke(strokeWidth, Color.parseColor(color));
    }

    public void setPivotBoxDrawableBackground(Drawable d) {
        pivotBox.setBackgroundDrawable(d);
    }

    public void setPivotBoxBackgroundColor(int color){
        pivotBox.setBackgroundColor(color);
    }

    public void setPivotBoxBackgroundColor(String color){
        pivotBox.setBackgroundColor(Color.parseColor(color));
    }

    private void addPivotBox() {
        if(customTextViewLayoutId!=0){
            pivotBox = (TextView) LayoutInflater.from(getContext()).inflate(customTextViewLayoutId, null, false);
            pivotBox.setBackgroundColor(0x00000000);
            pivotBox.setText("");
        }else {
            pivotBox = optionPickerContainer.createDefaultTabView(getContext());
        }

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0x00000000); // Changes this drawable to use a single color instead of a gradient
        gd.setStroke(3, 0xFFBDBDBD);
        pivotBox.setBackgroundDrawable(gd);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        pivotBox.setLayoutParams(params);
        addView(pivotBox);
    }

    public void setTextViewCustomizer(OptionPickerContainer.CustomBehaviour mCustomBehaviour){
        this.optionPickerContainer.setTextViewCustomizer(mCustomBehaviour);
    }

    public void setOnItemSelectedListener(OptionPickerContainer.OnItemSelectedListener mOnItemSelectedListener) {
        this.optionPickerContainer.setOnItemSelectedListener(mOnItemSelectedListener);
    }

    public void logger(String log) {
        Log.e(this.getClass().getSimpleName(), log);
    }

}

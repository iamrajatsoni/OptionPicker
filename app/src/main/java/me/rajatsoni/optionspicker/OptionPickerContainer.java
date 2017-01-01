package me.rajatsoni.optionspicker;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionPickerContainer extends ScrollView {

    private OnItemSelectedListener mOnItemSelectedListener;
    private CustomBehaviour mCustomBehaviour;

    private int customTextViewLayoutId = 0;
    private final LinearLayout mContainer;

    int selectedItemIndex = 0;

    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final int TAB_VIEW_PADDING_DIPS = 16;

    private int childViewTop, itemHeight, bigHeight;
    private ArrayList<Integer> snaps;
    private ArrayList<String> dataSet;

    private int unfocusedVisibleItemsCount;
    private int unfocusedVisibleItemsHalfCount;

    private int fadingEdgesLength;

    public OptionPickerContainer(Context context) {
        this(context, null);
    }

    public OptionPickerContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OptionPickerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        logger("constructor");
        setVerticalScrollBarEnabled(false);
        setFillViewport(true);
        fadingEdgesLength = (int)(getResources().getDisplayMetrics().density*8);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        addView(mContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setUp(ArrayList<String> dataSet, int unfocusedVisibleItemsCountParam) {
        logger("render");
        initialize(dataSet, unfocusedVisibleItemsCountParam);
    }


    public void setUp(ArrayList<String> dataSet, int unfocusedVisibleItemsCountParam, int customTextViewLayoutId) {
        logger("render");
        this.customTextViewLayoutId = customTextViewLayoutId;
        initialize(dataSet, unfocusedVisibleItemsCountParam);
    }


    public void initialize(ArrayList<String> dataSet, int unfocusedVisibleItemsCountParam){
        this.dataSet = dataSet;
        this.unfocusedVisibleItemsCount = unfocusedVisibleItemsCountParam;
        this.unfocusedVisibleItemsHalfCount = unfocusedVisibleItemsCount/2;
        final OnClickListener itemClickListener = new ItemClickListener();

        fillUpForPadding();
        for (int i = 0; i < dataSet.size(); i++) {
            TextView itemTextView;
            if(customTextViewLayoutId!=0){
                itemTextView = (TextView) LayoutInflater.from(getContext()).inflate(customTextViewLayoutId, mContainer, false);
                itemTextView.setSingleLine(true);
            }else {
                itemTextView = createDefaultTabView(getContext());
            }
            itemTextView.setText(dataSet.get(i));
            itemTextView.setTag(i);
            itemTextView.setOnClickListener(itemClickListener);
            mContainer.addView(itemTextView);
        }
        fillUpForPadding();

        post(new Runnable() {
            @Override
            public void run() {
                View childView = mContainer.getChildAt(1);
                itemHeight = childView.getHeight();
                bigHeight = itemHeight * unfocusedVisibleItemsCount;
                getLayoutParams().height = bigHeight;
                requestLayout();
                childViewTop = (childView.getTop() / 2);

                logger("itemH: "+itemHeight+"    bigH: "+bigHeight+"    childViewTop: "+childViewTop);

                snaps = new ArrayList<>();
                for (int i = 0; i < OptionPickerContainer.this.dataSet.size(); i++) {
                    snaps.add((i * itemHeight) + childViewTop);
                    logger(""+i+") "+((i * itemHeight) + childViewTop));
                }
                setOnTouchListener(new ItemSnapController());
                ViewGroup parent = (ViewGroup)getParent();
                parent.getLayoutParams().height = bigHeight;
                parent.requestLayout();
            }
        });
    }

    private void fillUpForPadding() {
        for (int i = 0; i < unfocusedVisibleItemsHalfCount; i++) {
            TextView itemTextView;
            if(customTextViewLayoutId!=0) {
                itemTextView = (TextView) LayoutInflater.from(getContext()).inflate(customTextViewLayoutId, mContainer, false);
                itemTextView.setBackgroundColor(0x00000000);
                itemTextView.setText("");
            }else {
                itemTextView = createDefaultTabView(getContext());
            }
            itemTextView.setText("");
            mContainer.addView(itemTextView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        logger("Attached to window");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                snapTo(0);
            }
        }, 200);
    }

    private class ItemSnapController implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        snapTo(OptionPickerContainer.this.getScrollY());
                    }
                });
            }
            return false;
        }
    }

    private void snapTo(int scrollY) {
        int a = Math.abs(scrollY - snaps.get(0));
        int snapPos = childViewTop;
        int tempSnapPos = 0;
        for (int i = 1; i < snaps.size(); i++) {
            int b = Math.abs(scrollY - snaps.get(i));
            if (b < a) {
                a = b;
                snapPos = snaps.get(i);
                tempSnapPos = i;
            } else {
                break;
            }

        }

        selectedItemIndex = tempSnapPos;
        if(mOnItemSelectedListener!=null) mOnItemSelectedListener.onSelection(tempSnapPos);
        smoothScrollTo(0, snapPos);

        for (int i = 0; i < mContainer.getChildCount(); i++) {
            TextView textView = (TextView) mContainer.getChildAt(i);
            if (i == (tempSnapPos + unfocusedVisibleItemsHalfCount)) {
                if(mCustomBehaviour!=null){
                    mCustomBehaviour.selectedTextView(textView);
                }else {
                    textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    textView.setMarqueeRepeatLimit(5);
                    textView.setSelected(true);
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    textView.setTextColor(0xff000000);
                }
            } else {
                if(mCustomBehaviour!=null){
                    mCustomBehaviour.unselectedTextViews(textView);
                }else {
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setSelected(false);
                    textView.setTypeface(Typeface.DEFAULT);
                    textView.setTextColor(0xff757575);
                }
            }
        }
    }

    private class ItemClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPos = (int)v.getTag();
            snapTo(snaps.get(itemPos));
        }
    }

    private void logger(String log) {
        Log.e(this.getClass().getSimpleName(), log);
    }

    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setSingleLine(true);
        textView.setHorizontalFadingEdgeEnabled(true);
        textView.setFadingEdgeLength(fadingEdgesLength);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    void setTextViewCustomizer(CustomBehaviour mCustomBehaviour){
        this.mCustomBehaviour = mCustomBehaviour;
    }

    void setOnItemSelectedListener(OnItemSelectedListener mOnItemSelectedListener) {
        this.mOnItemSelectedListener = mOnItemSelectedListener;
    }

    public interface OnItemSelectedListener {
        void onSelection(int index);
    }

    public interface CustomBehaviour {
        void selectedTextView(TextView selectedTextView);
        void unselectedTextViews(TextView unselectedTextView);
    }

}

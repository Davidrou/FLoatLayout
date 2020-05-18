package com.example.flowlayout_lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 1.标准的ViewGroup 仅仅支持addView或者在布局中添加View
 * 支持流式显示布局　根据ＴextView长度如果该行显示不下　则自动换行
 * 支持配置横下和竖向的子View间距
 *
 * 支持设置Adapter来组织数据
 * 支持选中效果，支持点击后的回调处理
 * 限制可选中的个数
 * 支持删除单个数据
 * 支持右侧对齐
 * //requestLayout和invalidate区别
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FloatLayout";
    private ArrayList<Integer> lastIndexEveryLine = new ArrayList<>();
    private int mVerticalSpacing = 0;
    private int mHorizontalSpacing = 0;


    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FlowLayout);
        mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0);
        mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizationSpacing, 0);
        a.recycle();
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        lastIndexEveryLine.clear();
        int totalWidth = getPaddingLeft() + getPaddingRight();
        int currentWidthUsed = getPaddingLeft() + getPaddingRight();//当前行已经使用的宽度
        Log.d(TAG, "onMeasure getPaddingLeft:" + getPaddingLeft() + " getPaddingRight:" + getPaddingRight());
        int totalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);//这里直接测量孩子？ Framework中是怎样和孩子中的设置的布局参数结合的 如Wrap_content 看measureChild方法
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();//强转的前提是重写generateLayoutParams
            int measuredHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;//Measure后只能拿到MeasuredHeight 使用getHeight为0 原因呢？
            int measuredWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin; // 因为padding会算到View的宽度和高度中，所以不需要在计算。而margin不会，需要再加上
            totalHeight = Math.max(measuredHeight, totalHeight);

            if (currentWidthUsed + measuredWidth +mHorizontalSpacing > widthSize) { //剩余空间不足以显示当前child 换行
                currentWidthUsed = getPaddingLeft() + getPaddingRight() +measuredWidth ;
                lastIndexEveryLine.add(i - 1);
            } else {
                currentWidthUsed += measuredWidth+mHorizontalSpacing;
            }
            totalWidth = Math.max(totalWidth, currentWidthUsed);
            Log.d(TAG, "measuredHeight:" + measuredHeight + " measuredWidth:" + measuredWidth + "   " + getChildAt(i).getHeight());
        }
        totalHeight = (totalHeight*(lastIndexEveryLine.size()+1))+getPaddingBottom() + getPaddingTop();
        totalHeight += mVerticalSpacing*lastIndexEveryLine.size();
        //如果测量模式是Exactly 布局中配置的为match_parent/具体的dp 则直接使用onMeasure参数，否则是wrap_content 使用子View宽度
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : totalWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int currentLeftToStart = getPaddingLeft();//不能使用l +getPaddingLeft 因为设置margin_left后　l不为０　
        int currentTop = getPaddingTop();
        Log.d(TAG, "onLayout: l:" + l + " getPaddingLeft:" + getPaddingLeft() + " currentLeftToStart:" + currentLeftToStart);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int left = currentLeftToStart + lp.leftMargin;
            int right = left + child.getMeasuredWidth() + lp.rightMargin;
            child.layout(left, currentTop, right, currentTop + child.getMeasuredHeight());

            //构建下一次layout坐标
            currentLeftToStart = right + mHorizontalSpacing;
            if(lastIndexEveryLine.contains(i)){//需要进行换行
                currentLeftToStart = getPaddingLeft();
                currentTop += child.getMeasuredHeight()+mVerticalSpacing;
            }
        }
    }

    public void setHorizontalSpacing(int horizontalSpacing){
        mHorizontalSpacing = horizontalSpacing;
        //requestLayout();是否需要requestLayout
    }

    public void setVerticalSpacing(int verticalSpacing){
        mVerticalSpacing = verticalSpacing;
        //requestLayout();是否需要requestLayout
    }


}

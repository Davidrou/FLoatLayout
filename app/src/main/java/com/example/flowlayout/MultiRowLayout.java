//package com.example.flowlayout;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import java.util.ArrayList;
//
///**
// * Created by wuyexiong on 9/22/14.
// */
//public class MultiRowLayout extends ViewGroup {
//
//    public static MultiRowLayout.LayoutParams sItemTagLayoutParams = new MultiRowLayout.LayoutParams(
//            MultiRowLayout.LayoutParams.WRAP_CONTENT,
//            MultiRowLayout.LayoutParams.WRAP_CONTENT);
//
//    private int mVerticalSpacing = 0;
//    private int mHorizontalSpacing = 0;
//    private int mMaxRow = -1;
//
//    public static class RowInfo {
//        int width;
//        int height;
//        int childCount;
//
//        public int getWidth() {
//            return width;
//        }
//
//        public int getChildCount() {
//            return childCount;
//        }
//
//        public int getHeight() {
//            return height;
//        }
//    }
//
//    public static class LayoutParams extends MarginLayoutParams {
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//        }
//
//        public LayoutParams(int width, int height) {
//            super(width, height);
//        }
//
//        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(MarginLayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(LayoutParams source) {
//            super(source);
//        }
//    }
//
//    protected ArrayList<RowInfo> mRows = new ArrayList<RowInfo>();
//
//    public MultiRowLayout(Context context) {
//        super(context);
//    }
//
//    public MultiRowLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs, 0);
//    }
//
//    public MultiRowLayout(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(attrs, defStyle);
//    }
//
//    protected void init(AttributeSet attrs, int defStyle) {
//        TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.MultiRowLayout, defStyle, 0);
//        setVerticalSpacing(a.getDimensionPixelSize(R.styleable.MultiRowLayout_tagVerticalSpacing, 0));
//        setHorizontalSpacing(a.getDimensionPixelSize(R.styleable.MultiRowLayout_tagHorizontalSpacing,
//                0));
//        a.recycle();
//    }
//
//    public int getRowCount() {
//        return mRows.size();
//    }
//
//    public RowInfo getRowInfo(int i) {
//        return mRows.get(i);
//    }
//
//    public void setMaxRow(int maxRow) {
//        mMaxRow = maxRow;
//    }
//
//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new LayoutParams(getContext(), attrs);
//    }
//
//    public void setVerticalSpacing(int verticalSpacing) {
//        mVerticalSpacing = verticalSpacing;
//        requestLayout();
//    }
//
//    public void setHorizontalSpacing(int horizontalSpacing) {
//        mHorizontalSpacing = horizontalSpacing;
//        requestLayout();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        int count = getChildCount();
//        int heightUsed = 0;
//        int maxRowWidth = 0;
//
//        mRows.clear();
//        RowInfo rowInfo = new RowInfo();
//        for (int i = 0; i < count; ++i) {
//
//            View child = getChildAt(i);
//
//            // Measure the current child.
//            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
//            int childHeight = child.getMeasuredHeight();
//            int childWidth = child.getMeasuredWidth();
//
//            int newWidth = rowInfo.width + childWidth;
//            if (rowInfo.childCount > 0) {
//                newWidth += mHorizontalSpacing;
//            }
//            if (widthMode != MeasureSpec.UNSPECIFIED && newWidth > (widthSize - getPaddingLeft() - getPaddingRight())) {
//                // The current row has insufficient width left for the child, so
//                if (heightMode != MeasureSpec.UNSPECIFIED && heightUsed >= heightSize) {
//                    // The new row is completely outside of the view. Drop the
//                    // rest of the children.
//                    break;
//                }
//                // End the current row.
//                if (mRows.size() > 0) {
//                    heightUsed += mVerticalSpacing;
//                }
//                heightUsed += rowInfo.height;
//                mRows.add(rowInfo);
//
//                // Starts a new row.
//                rowInfo = new RowInfo();
//            }
//
//            if (mMaxRow > 0){
//                if (mRows.size() >= mMaxRow) {
//                    break;
//                }
//            }
//
//            if (rowInfo.childCount > 0) {
//                rowInfo.width += mHorizontalSpacing;
//            }
//            rowInfo.width += childWidth;
//            ++rowInfo.childCount;
//            maxRowWidth = Math.max(maxRowWidth, rowInfo.width);
//            rowInfo.height = Math.max(rowInfo.height, childHeight);
//        }
//
//
//
//        // Ends the last row.
//        if (rowInfo.childCount > 0) {
//            if (mRows.size() > 0) {
//                heightUsed += mVerticalSpacing;
//            }
//            heightUsed += rowInfo.height;
//            mRows.add(rowInfo);
//        }
//
//        setMeasuredDimension(
//                resolveSize(maxRowWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec),
//                resolveSize(heightUsed + getPaddingTop() + getPaddingBottom(), heightMeasureSpec));
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int i = 0;
//        int y = getPaddingTop();
//        for (int row = 0; row < mRows.size(); ++row) {
//            int x = getPaddingLeft();
//            for (int column = 0; column < mRows.get(row).childCount; ++column) {
//                View child = getChildAt(i);
//                int x2 = x + child.getMeasuredWidth();
//
//                child.layout(x, y, x2, y + child.getMeasuredHeight());
//                x = x2 + mHorizontalSpacing;
//                ++i;
//            }
//            y += mRows.get(row).height + mVerticalSpacing;
//        }
//    }
//}
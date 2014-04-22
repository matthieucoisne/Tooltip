/*
 * Copyright (C) 2014 Matthieu Coisne (http://www.stackdroid.com/blog/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stackdroid.tooltip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Tooltip {
	
	private PopupWindow mPopupWindow;
	
	private LinearLayout mLlyTooltip;
	private LinearLayout mLlyContent;
	private TextView mTxtTitle;
	private TextView mTxtMessage;
	private ImageView mImgTooltipUp;
	private ImageView mImgTooltipDown;
	
	private String mColor;
	
	private int mScreenWidth;
	private int mTooltipMargin;
	
	private enum IndicatorType {
		UP,
		DOWN;
	}

	private Tooltip(Context context) {
		mPopupWindow = new PopupWindow(context);
		mPopupWindow.setContentView(LayoutInflater.from(context).inflate(R.layout.tooltip, null, false));
		mPopupWindow.setWidth(LayoutParams.WRAP_CONTENT);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); 
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		
		// Bind views
		mLlyContent = (LinearLayout) mPopupWindow.getContentView().findViewById(R.id.tooltip_llyContent);
		mLlyTooltip = (LinearLayout) mPopupWindow.getContentView().findViewById(R.id.tooltip_llyGlobal);
		mTxtTitle = (TextView) mPopupWindow.getContentView().findViewById(R.id.tooltip_txtTitle);
		mTxtMessage = (TextView) mPopupWindow.getContentView().findViewById(R.id.tooltip_txtMessage);
		mImgTooltipUp = (ImageView) mPopupWindow.getContentView().findViewById(R.id.tooltip_imgUp);
		mImgTooltipDown = (ImageView) mPopupWindow.getContentView().findViewById(R.id.tooltip_imgDown);
		
		mScreenWidth = Utils.getScreenWidth(context);
		mTooltipMargin = Utils.dpToPx(context, 4);
	}
	
	public static Tooltip createWith(Context context) {
		return new Tooltip(context);
	}
	
	public Tooltip setTitle(String title) {
		mTxtTitle.setText(title);
		return this;
	}
	
	public Tooltip setMessage(String message) {
		mTxtMessage.setText(message);
		return this;
	}
	
	public Tooltip setColor(String color) {
		mColor = color;
		return this;
	}
	
	public void attachTo(final View v) {
		final int[] viewPosition = new int[2];
		v.getLocationOnScreen(viewPosition);
		
		mPopupWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, viewPosition[0], viewPosition[1]);

		mLlyTooltip.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				mLlyTooltip.getViewTreeObserver().removeGlobalOnLayoutListener(this);

				int tooltipHeight = mLlyTooltip.getHeight();
				int tooltipWidth = mLlyTooltip.getWidth();
				if (tooltipWidth > (mScreenWidth * 0.8f)) {
					tooltipWidth = (int) (mScreenWidth * 0.8f);
				}

				// Calculate the tooltip's X coordinate
				int tooltipX = 0;
				if (viewPosition[0] + tooltipWidth < mScreenWidth) {
					// Left Anchor
					tooltipX = viewPosition[0];
				} else if (viewPosition[0] + v.getWidth() - tooltipWidth > 0) {
					// Right Anchor
					tooltipX = viewPosition[0] + v.getWidth() - tooltipWidth;
				} else {
					// Middle of the screen
					tooltipX = (mScreenWidth - tooltipWidth) / 2;
				}

				int leftMargin = viewPosition[0] - tooltipX + (v.getWidth() / 2) - (mImgTooltipDown.getWidth() / 2);

				// Calculate the tooltip's Y coordinate
				int tooltipY = 0;
				if (viewPosition[1] - tooltipHeight - mImgTooltipDown.getHeight() - mTooltipMargin > 0) {
					// Above the view
					tooltipY = viewPosition[1] - tooltipHeight + mImgTooltipDown.getHeight() - mTooltipMargin;
					setIndicator(IndicatorType.DOWN, leftMargin);
				} else {
					// Below the view
					tooltipY = viewPosition[1] + v.getHeight() + mTooltipMargin;
					setIndicator(IndicatorType.UP, leftMargin);
				}

				// Change the tooltip color if needed
				applyColorFilter();
				
				// Update the tooltip with calculated X and Y coordinates and its new width.
				// The height is set to wrap_content (-1)
				mPopupWindow.update(tooltipX, tooltipY, tooltipWidth, -1);
			}
		});
	}
	
	private void applyColorFilter() {
		PorterDuffColorFilter filter = null;
		if (mColor != null) {
			filter = new PorterDuffColorFilter(Color.parseColor(mColor), Mode.SRC_ATOP);
		}
		mImgTooltipUp.getDrawable().setColorFilter(filter);
		mImgTooltipDown.getDrawable().setColorFilter(filter);
		mLlyContent.getBackground().setColorFilter(filter);
	}

	private void setIndicator(IndicatorType type, int leftMargin) {
		View indicator;
		switch (type) {
		case UP:
			mImgTooltipUp.setVisibility(View.VISIBLE);
			mImgTooltipDown.setVisibility(View.GONE);
			indicator = mImgTooltipUp;
			break;
		default: // DOWN
			mImgTooltipUp.setVisibility(View.GONE);
			mImgTooltipDown.setVisibility(View.VISIBLE);
			indicator = mImgTooltipDown;
			break;
		}

		LinearLayout.LayoutParams param = (LayoutParams) indicator.getLayoutParams();
		param.leftMargin = leftMargin;
		indicator.setLayoutParams(param);
	}

}

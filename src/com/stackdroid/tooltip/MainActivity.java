package com.stackdroid.tooltip;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView mImgInfo1;
	private ImageView mImgInfo2;
	private ImageView mImgInfo3;
	private ImageView mImgInfo4;
	private ImageView mImgInfo5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImgInfo1 = (ImageView) findViewById(R.id.imgInfo1);
		mImgInfo2 = (ImageView) findViewById(R.id.imgInfo2);
		mImgInfo3 = (ImageView) findViewById(R.id.imgInfo3);
		mImgInfo4 = (ImageView) findViewById(R.id.imgInfo4);
		mImgInfo5 = (ImageView) findViewById(R.id.imgInfo5);

		mImgInfo1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTooltip(v, "Tooltip #1", "Hello", "#DD0266C8");
			}
		});
		mImgInfo2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTooltip(v, "Tooltip #2", "My name is Matthieu", "#DDF90101");
			}
		});
		mImgInfo3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTooltip(v, "Tooltip #3", "The cake is a lie!", "#AAFEE101");
			}
		});
		mImgInfo4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTooltip(v, "Tooltip #4", "Bacon ipsum dolor sit amet sausage brisket fatback turducken short loin biltong spare ribs.", "#DD009925");
			}
		});
		mImgInfo5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTooltip(v, "Tooltip #5", "This is a tooltip...");
			}
		});
	}
	
	private void showTooltip(View view, String title, String message, String color) {
		Tooltip.createWith(this)
			.setTitle(title)
			.setMessage(message)
			.setColor(color)
			.attachTo(view);
	}
	
	private void showTooltip(View view, String title, String message) {
		Tooltip.createWith(this)
			.setTitle(title)
			.setMessage(message)
			.attachTo(view);
	}
	
}

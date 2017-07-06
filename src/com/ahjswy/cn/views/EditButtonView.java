package com.ahjswy.cn.views;

import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.views.OnTextChange.OnTextClearChangeListener;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;

public class EditButtonView extends EditText implements OnTextClearChangeListener {
	public static final int DEFAULT = 1;
	private static final int MAX_SIZE = 10;
	public static final int NEGATIVE = 2;
	private int DIG_SIZE = 3;
	private int MODE = 1;
	private Drawable drClear;
	Context context;

	public EditButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public EditButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public EditButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.DIG_SIZE = 3;
		this.MODE = 1;
		String tag = this.getTag() != null ? this.getTag().toString() : "1";
		this.MODE = Integer.parseInt(tag);
		// this.drClear = this.getResources().getDrawable(R.drawable.logo);
		drClear = this.getResources().getDrawable(0x7f0200a4);
		this.drClear.setBounds(0, 0, this.drClear.getIntrinsicWidth(), this.drClear.getIntrinsicHeight());
		this.addTextChangedListener(new OnTextChange(this));
		this.setOnTouchListener(new ClearListener(drClear.getIntrinsicWidth()));
		setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if ((((EditText) v).getInputType() == 0) || (!v.isEnabled())) {
					return;
				}
				reset();
			}
		});

		setFilters(new InputFilter[] { this.filter });
		setKeyListener(this.keyListener);
		// TODO 背景高度
		// Drawable drawable = getResources().getDrawable(0x7f0200a4);
		// setBackground(drawable);
		setMinHeight(100);
		setPadding(14, 0, 10, 0);

	}

	public EditButtonView(Context context) {
		super(context);
	}

	private InputFilter filter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			// String arg6 = source.toString();
			// Spanned arg9 = dest;
			// if (!TextUtils.isEmpty(arg6.toString())) {
			// if (("-".equals(arg6.toString())) &&
			// (TextUtils.isEmpty(arg9.toString()))) {
			// if (EditButtonView.this.MODE != 1) {
			// return "";
			// } else {
			// return "";
			// }
			// }
			//
			// if ("-".equals(arg6.toString())) {
			// if (EditButtonView.this.MODE == 1) {
			// if (arg9.length() <= 0 || arg9.charAt(0) != 45) {
			// EditButtonView.this.setText("-" + arg9);
			// } else {
			// EditButtonView.this.setText(arg9.subSequence(1, arg9.length()));
			// }
			//
			// EditButtonView.this.setSelection(EditButtonView.this.getText().length());
			// }
			//
			// return "";
			// }
			//
			// String v0 = arg9.toString();
			// if ((".".equals(arg6.toString())) && (v0.contains("."))) {
			// return "";
			// }
			// if (v0.length() >= 10) {
			// return "";
			// }
			//
			// if (!v0.contains(".")) {
			// return "";
			// }
			//
			// if (v0.substring(v0.indexOf(".")).length() <=
			// EditButtonView.this.DIG_SIZE) {
			// return "";
			// }
			//
			// if (v0.indexOf(".") >= dstart) {
			// return "";
			// }
			//
			// }
			return source.toString();

			// if (TextUtils.isEmpty(source.toString())) {
			// return null;
			// }
			// if ((!"-".equals(source.toString())) ||
			// (!TextUtils.isEmpty(source.toString()))) {
			// return null;
			// }
			// if ((dest.length() <= 0) || (dest.charAt(0) != '-')) {
			// return null;
			// } else {
			// setText(dest.subSequence(1, dest.length()));
			// }
			// setSelection(getText().length());
			// setText("-" + dest);
			// if ((".".equals(source.toString())) &&
			// (source.toString().contains("."))) {
			// return "";
			// }
			// if (dest.length() >= 10) {
			// return "";
			// }
			// if ((!dest.toString().contains(".")) ||
			// (dest.toString().substring(dest.toString().indexOf("."))
			// .length() <= EditButtonView.this.DIG_SIZE) ||
			// (dest.toString().indexOf(".") >= dstart)) {
			//
			// }

		}
	};
	private KeyListener keyListener = new NumberKeyListener() {
		protected char[] getAcceptedChars() {
			return new char[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 46 };
		}

		public int getInputType() {
			return 3;
		}
	};

	public void setDecNum(int DIG_SIZE) {
		this.DIG_SIZE = DIG_SIZE;
	}

	public void setMode(int MODE) {
		this.MODE = MODE;
	}

	@Override
	public void addTo(String addTo) {
		reset();
	}

	@Override
	public void cutTo(String cutTo) {
		reset();
	}

	public void reset() {
		if ((this.isFocused()) && TextUtils.isEmptyS(this.getText().toString())) {
			System.out.println("drClear   isFocused()" + isFocused());
			this.setCompoundDrawables(null, null, drClear, null);
		} else {
			this.setCompoundDrawables(null, null, null, null);
		}

	}
}

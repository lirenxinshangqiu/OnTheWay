package com.road.ui.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.road.bean.Info;
import com.road.bean.Task;
import com.road.utils.Configure;
import com.road.utils.ThreadPoolManager;
import com.squareup.picasso.Picasso;
import com.zhou.ontheway.R;

public abstract class BaseActivity extends Activity implements OnClickListener {

	ThreadPoolManager mThreadPoolManager;

	protected Context mContext;

	protected ProgressDialog progress;

	@SuppressWarnings("rawtypes")
	protected Info mInfo;

	protected final String INFO_NAME = "info";

	/**
	 * ��ǰ���ִ�е��߳�����,task��ID���Կ��������ж��߳��������Ⱥ�
	 */
	protected Task lastTask = new Task(0) {

		@Override
		public void run() {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Configure.init(this);
		mThreadPoolManager = ThreadPoolManager.getInstance();
		mContext = this;

		progress = getProgressDialog("���ڼ���,���Ժ�...");
		progress.setCancelable(true);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public ProgressDialog getProgressDialog(String msg) {
		ProgressDialog progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(true);
		return progressDialog;
	}

	/**
	 * ���������߳��е�Toast
	 */
	public void showToastOnUI(final String msg) {
		runOnUI(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			}
		});

	}

	/**
	 * ����Toast
	 * 
	 * @param msg
	 */
	public void showToast(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * UI�߳�ִ��һ������
	 * 
	 * @param run
	 */
	public void runOnUI(Runnable run) {
		runOnUiThread(run);
	}

	/**
	 * ���߳�ִ��һ������
	 * 
	 * @param task
	 */
	public void executeTask(Task task) {
		this.lastTask = task;
		mThreadPoolManager.executeTask(task);
	}

	/**
	 * 
	 * @param run
	 */
	public void executeTask(Runnable run) {
		mThreadPoolManager.executeTask(run);
	}

	/**
	 * �������뷨
	 * 
	 * @param context
	 * @param achor
	 */
	public void hideSoftInput(Context context, View achor) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(achor.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void dismissProgressDialog() {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				if (progress != null) {
					progress.dismiss();
				}

			}
		});

	}

	public void showProgressDialog() {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				if (progress == null) {
					progress = new ProgressDialog(mContext);
					progress.setMessage("���ڼ���,���Ժ�...");
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				}
				progress.setCancelable(true);
				try {
					progress.show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	public void showProgressDialog(final String msg, final boolean isCancel) {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				progress = new ProgressDialog(mContext);
				progress.setMessage(msg);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setCancelable(isCancel);
				try {
					progress.show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public void onDestroy() {
		if (progress != null) {
			progress.dismiss();
		}
		super.onDestroy();
	}

	/**
	 * �Ƿ������ݴ��ݹ���
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean hasInfo() {
		mInfo = (Info) getIntent().getSerializableExtra(INFO_NAME);
		if (mInfo != null) {
			return true;
		}
		return false;
	}

	/**
	 * ��һ��Activity
	 * 
	 * @param clazz
	 * @param info
	 */
	@SuppressWarnings("rawtypes")
	public void openActivity(final Class clazz, final Info info) {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(mContext, clazz);
				if (info != null) {
					intent.putExtra(INFO_NAME, info);
				}
				startActivity(intent);
			}
		});

	}

	/**
	 * ��һ��Activity for result
	 * 
	 * @param clazz
	 * @param info
	 */
	@SuppressWarnings("rawtypes")
	public void openActivity4Result(final Class clazz, final Info info, final int requestCode) {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(mContext, clazz);
				if (info != null) {
					intent.putExtra(INFO_NAME, info);
				}
				startActivityForResult(intent, requestCode);
			}
		});

	}

	/**
	 * �������ݵ���һ��activity
	 * 
	 * @param resultCode
	 * @param data
	 */
	@SuppressWarnings("rawtypes")
	public void closeActivity4Result(int resultCode, Info data) {
		Intent intent = getIntent();
		intent.putExtra(INFO_NAME, data);
		setResult(resultCode, intent);
		finish();
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param tuContainer
	 * @param item
	 */
	public void loadIMG(ImageView img, String url) {
		Picasso.with(mContext).load(url).error(R.drawable.error).into(img);
	}

	public void loadIMG(ImageView img, String url, int errorId) {
		Picasso.with(this).load(url).error(errorId).placeholder(errorId)
				.into(img);
	}

	public void loadIMG(ImageView img, String url, int placeholder, int errorId) {
		Picasso.with(mContext).load(url).error(errorId)
				.placeholder(placeholder).into(img);
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param tuContainer
	 * @param item
	 */
	public void loadIMG(ImageView img, File file) {
		Picasso.with(mContext).load(file).error(R.drawable.error).into(img);
	}

	/**
	 * ���ر���ͼƬ
	 * 
	 * @param tuContainer
	 * @param item
	 */
	public void loadIMG(ImageView img, int id) {
		Picasso.with(mContext).load(id).into(img);
	}

	/**
	 * ��������ļ�����
	 * 
	 * @param id
	 * @return
	 */
	public float getDimens(int id) {
		return getResources().getDimension(id);
	}

	/**
	 * ��������ļ�����
	 * 
	 * @param id
	 * @return
	 */
	public int getDimensPixelSize(int id) {
		return getResources().getDimensionPixelSize(id);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}

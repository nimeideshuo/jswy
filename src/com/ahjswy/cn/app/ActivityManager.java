package com.ahjswy.cn.app;

import android.app.Activity;
import java.util.Stack;

public class ActivityManager {
	private static Stack<Activity> activityStack;
	private static ActivityManager instance;

	public static ActivityManager getScreenManager() {
		if (instance == null) {
			instance = new ActivityManager();
		}
		return instance;
	}

	public Activity currentActivity() {
		Activity localActivity = null;
		if (!activityStack.empty()) {
			localActivity = activityStack.lastElement();
		}
		return localActivity;
	}

	public void popActivity(Activity activity) {
		if (activity != null && !activity.isFinishing()) {
			activity.finish();
			activityStack.remove(activity);
		}
	}

	public void popAllActivityExceptOne(Class<?> paramClass) {
		// 循环把每个 activity 杀死
		for (int i = activityStack.size(); i > 0; i--) {
			Activity activity = currentActivity();
			if (activity != null) {
				popActivity(activity);
			}
		}
	}

	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}
}
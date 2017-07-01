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

	public void popActivity(Activity paramActivity) {
		if (paramActivity != null) {
			paramActivity.finish();
			activityStack.remove(paramActivity);
		}
	}

	public void popAllActivityExceptOne(Class<?> paramClass) {
		// 循环把每个 activity 杀死
		for (int i = activityStack.size(); i > 0; i--) {
			Activity localActivity = currentActivity();
			if (localActivity != null) {
				popActivity(localActivity);
			}
		}
	}

	public void pushActivity(Activity paramActivity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(paramActivity);
	}
}
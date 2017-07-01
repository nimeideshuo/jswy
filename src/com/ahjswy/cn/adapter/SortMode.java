package com.ahjswy.cn.adapter;

public enum SortMode {
	ASC, DESC;

	public static SortMode getAnother(SortMode paramSortMode) {
		if (paramSortMode == ASC)
			return DESC;
		return ASC;
	}

}

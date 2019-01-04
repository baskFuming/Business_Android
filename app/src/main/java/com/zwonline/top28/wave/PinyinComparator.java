package com.zwonline.top28.wave;

import java.util.Comparator;

/**
 * 非A-Z的以@/#排序
 */
public class PinyinComparator implements Comparator<SortModel>{

	public int compare(SortModel o1, SortModel o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return 1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return -1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}

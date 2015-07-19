package cardbl;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import main.Launcher;

/**
 * 卡片的计算
 * @author cylong
 * @version 2015年7月17日 下午8:09:42
 */
public abstract class Create {

	/** 数字范围 */
	protected int[] numRange = new int[Launcher.MAX_NUM - Launcher.MIN_NUM + 1];
	/** 范围内数字是否命中 */
	protected boolean[] numExist = new boolean[Launcher.MAX_NUM - Launcher.MIN_NUM + 1];
	/** 生成的卡片 */
	public Card[][] cardList = new Card[Launcher.CARD_NUM][Launcher.STATE];

	public static int minR = 10000;
	/** 最小R的卡片 */
	public static Card[][] minCardList;
	/** 初始的10个基础数 */
	protected int[][] firstNum;

	public int start(int[][] firstNum) {
		this.firstNum = firstNum;
		for(int i = 0; i < numRange.length; i++) {
			numRange[i] = i + Launcher.MIN_NUM;
		}

		for(int i = 0; i < cardList.length; i++) {
			for(int j = 0; j < cardList[i].length; j++) {
				cardList[i][j] = new Card();
				cardList[i][j].numList[0] = firstNum[i][j];
				cardList[i][j].index++;
				numIsExist(firstNum[i][j]); // 初始化的10个数字出现过
			}
		}
		startLoop();
		return minR;
	}

	protected void startLoop() {
		for(int i = 0; i < 15; i++) {
			calc();
			int r = findR();
			if (r != -1) {
				if (r == Launcher.R) {
					print(cardList);
					writeToFile();
				}
			} else {
				break;
			}
		}
	}

	protected abstract void calc();
	
	protected void handle(int...args) {
		int sum = 0;
		for(int i = 0; i < args.length; i++) {
			sum += cardList[i][args[i]].numList[0];
		}
		if (isFirst(sum)) {
			return;
		}
		int temp = numIsExist(sum);
		if (temp == 2) {
			Card[][] newCardList = copyArray(this.cardList);
			newCardList = remove(newCardList, sum);
			addToCardList(newCardList, sum, args);

			int curTmpR = getTempR(this.cardList);
			int newTmpR = getTempR(newCardList);
			int curNum = getTotalNum(this.cardList);
			int newNum = getTotalNum(newCardList);
			double curVar = getVar(this.cardList);
			double newVar = getVar(newCardList);
			if (newTmpR < curTmpR) { // 取R小的
				this.cardList = newCardList;
				return;
			} else if (newTmpR == curTmpR) {
				if (newNum < curNum) { // 取数字数量小的
					this.cardList = newCardList;
					return;
				} else if (newNum == curNum) {
					if (newVar < curVar) { // 取方差小的
						this.cardList = newCardList;
						return;
					}
				}
			}
		}
		if (temp == 1) {
			addToCardList(cardList, sum, args);
		}
	}

	/** 求出来的和加入到卡片中 */
	protected void addToCardList(Card[][] cardList, int sum, int...args) {
		for(int i = 0; i < args.length; i++) {
			if(args[i] != 0) {
				cardList[i][args[i]].numList[cardList[i][args[i]].index++] = sum;
			}
		}
	}

	/**
	 * 获得卡片面中数字个数的方差
	 * @author cylong
	 * @version 2015年7月18日 下午9:08:24
	 */
	protected double getVar(Card[][] cardList) {
		int n = cardList.length * (cardList[0].length - 1);
		double avg = getTotalNum(cardList) * 1.0 / n;
		double varSum = 0.0;
		for(int i = 0; i < cardList.length; i++) {
			for(int j = 1; j < cardList[0].length; j++) {
				varSum += Math.pow((cardList[i][j].index - avg), 2);
			}
		}
		double var = varSum / n;
		return var;
	}

	/**
	 * 获得卡片的数字个数
	 * @author cylong
	 * @version 2015年7月18日 下午8:22:38
	 */
	protected int getTotalNum(Card[][] cardList) {
		int sum = 0;
		for(int i = 0; i < cardList.length; i++) {
			for(int j = 1; j < cardList[i].length; j++) {
				sum += cardList[i][j].index;
			}
		}
		return sum;
	}

	/** 获得当前的R */
	protected int getTempR(Card[][] cardList) {
		int maxR = 0;
		for(int i = 0; i < cardList.length; i++) {
			for(int j = 1; j < cardList[i].length; j++) {
				maxR = maxR > cardList[i][j].index ? maxR : cardList[i][j].index;
			}
		}
		return maxR;
	}

	/**
	 * 遇到重复的和，去除掉之前添加到卡片中的
	 * @author cylong
	 * @version 2015年7月17日 下午8:59:46
	 */
	protected Card[][] remove(Card[][] newCardList, int sum) {
		for(int i = 0; i < newCardList.length; i++) {
			for(int j = 0; j < newCardList[i].length; j++) {
				int numList[] = newCardList[i][j].numList;
				for(int k = 0; k < numList.length; k++) {
					if (numList[k] == sum) {
						for(int m = k; m < numList.length - 1; m++) {
							numList[m] = numList[m + 1];
						}
						newCardList[i][j].index--;
					}
				}
			}
		}
		return newCardList;
	}

	/** 计算卡片的R */
	protected int findR() {
		// 判断所有的数是否都出现过
		for(int i = 0; i < numExist.length; i++) {
			if (!numExist[i]) {
				return -1;
			}
		}

		int maxR = 0;
		for(int i = 0; i < cardList.length; i++) {
			for(int j = 1; j < cardList[i].length; j++) {
				if (cardList[i][j].index > maxR) {
					maxR = cardList[i][j].index;
				}
			}
		}

		if (minR > maxR) {
			minR = maxR;
			minCardList = cardList;
		}
		return maxR;
	}

	/* 打印卡片 */
	public static void print(Card[][] cardList) {
		int sum = 0;
		for(int i = 0; i < cardList.length; i++) {
			for(int j = 1; j < cardList[i].length; j++) {
				sum += cardList[i][j].index * cardList[i][j].numList[0];
				System.out.println("卡" + (i + 1) + (j == 1 ? "正面" : "反面") + "  数字个数: " + cardList[i][j].index);
				Arrays.sort(cardList[i][j].numList, 0, cardList[i][j].index);
				for(int k = 0; k < cardList[i][j].index; k++) {
					System.out.print(cardList[i][j].numList[k] + " ");
				}
				System.out.println();
				System.out.println();
			}
		}

		System.out.println("Sum = " + sum);
	}

	/** 写到文件中 */
	protected void writeToFile() {
		try {
			String dateString = Launcher.FORMATTER.format(Launcher.CURRENT_TIME);
			String dirName = "data/" + Launcher.MAX_NUM + "/" + dateString + "/";
			String fileName = dirName + Launcher.R;
			for(int i = 0; i < firstNum.length; i++) {
				for(int j = 1; j < firstNum[i].length; j++) {
					fileName += ("-" + firstNum[i][j]);
				}
			}
			fileName += ".txt";
			File dir = new File(dirName);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(fileName));
			int sum = 0;
			for(int i = 0; i < cardList.length; i++) {
				for(int j = 1; j < cardList[i].length; j++) {
					sum += cardList[i][j].index * cardList[i][j].numList[0];
					bufWriter.write("卡" + (i + 1) + (j == 1 ? "正面" : "反面") + "  数字个数: " + cardList[i][j].index);
					bufWriter.newLine();
					Arrays.sort(cardList[i][j].numList, 0, cardList[i][j].index);
					for(int k = 0; k < cardList[i][j].index; k++) {
						bufWriter.write(cardList[i][j].numList[k] + " ");
					}
					bufWriter.newLine();
					bufWriter.newLine();
				}
			}
			bufWriter.write("Sum = " + sum);
			bufWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 求出的和是否命中范围 */
	protected int numIsExist(int num) {
		for(int i = 0; i < numRange.length; i++) {
			if (num == numRange[i]) {
				if (numExist[i]) { // 这个数在之前已经出现过
					return 2;
				} else {
					numExist[i] = true;
					return 1;
				}
			}
		}

		return 0;
	}

	/** 和是否是基础的数 */
	protected boolean isFirst(int num) {
		for(int i = 0; i < firstNum.length; i++) {
			for(int j = 0; j < firstNum[i].length; j++) {
				if (firstNum[i][j] == num) {
					return true;
				}
			}
		}
		return false;
	}

	/** 拷贝卡片 */
	protected Card[][] copyArray(Card[][] cardList) {
		Card[][] newCardList = new Card[cardList.length][cardList[0].length];
		for(int i = 0; i < cardList.length; i++) {
			for(int j = 0; j < cardList[i].length; j++) {
				newCardList[i][j] = new Card();
				newCardList[i][j].index = cardList[i][j].index;
				newCardList[i][j].numList = cardList[i][j].numList.clone();
			}
		}

		return newCardList;
	}

}

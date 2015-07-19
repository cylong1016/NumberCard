package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import cardbl.Card;
import cardbl.Create;
import cardbl.CreateFive;
import cardbl.CreateFour;
import cardbl.CreateThree;
import ui.UI;

/**
 * 数卡设计
 * @author cylong
 * @version 2015年7月17日 下午6:03:02
 */
public class Launcher {
	
	public static UI ui = new UI();
	public static final Date CURRENT_TIME = new Date(); // 当前日期，当作文件名
	public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static int CARD_NUM; // 卡片数量
	public static final int STATE = 3; // 使用正、反面或者不使用
	
	public static Card[][] cardList; // 一套卡片
	
	public static int MIN_NUM = 1; // 最小值，就是1
	public static int MAX_NUM;	// 识别量
	public static int LOWER_R;	// R的下界
	public static int MIN_R;	// 计算出来的最小R
	public static int R = 0; 	// 想要输出的容量为r的数卡
	
	
	//---------------------- 三张卡片的基础数------------------
	public static int[][] firstNum_three = {{0, 1, 2},
	                                        {0, 3, 0},
	                                        {0, 0, 0}};
	
	public static int[][] range_three = {{4, 6},
	                                     {6, 9},
	                                     {10, 18}};
	//---------------------- 三张卡片的基础数------------------
	
	
	//---------------------- 四张卡片的基础数------------------
	public static int[][] firstNum_four = {{0, 1, 2},
	                                       {0, 3, 0},
	                                       {0, 0, 0},
	                                       {0, 0, 0}};

	public static int[][] range_four = {{4, 6},
	                                    {6, 9},
	                                    {10, 18},
	                                    {14, 27},
	                                    {28, 54}};
	//---------------------- 四张卡片的基础数------------------
	
	
	//---------------------- 五张卡片的基础数------------------
	public static int[][] firstNum_five = {{0, 1, 2},
	                                       {0, 3, 6},
	                                       {0, 9, 0},
	                                       {0, 0, 0},
	                                       {0, 0, 0}};
	
	public static int[][] range_five_100 = {{16, 17},
			                                {22, 25},
			                                {32, 37},
			                                {44, 51},
			                                {56, 64}};
	
	public static int[][] range_five_1 = {{15, 17},
	                                      {22, 26},
	                                      {28, 42},
	                                      {36, 62},
	                                      {46, 75}};
	
	public static int[][] range_five_2 = {{18, 18},
	                                      {27, 27},
	                                      {36, 54},
	                                      {55, 81},
	                                      {67, 110}};
	
	public static int[][] range_five_3 = {{18, 18},
	                                      {27, 27},
	                                      {54, 54},
	                                      {70, 81},
	                                      {96, 162}};
	//---------------------- 五张卡片的基础数------------------
	
	
	public static void main(String[] args) {
		ui.btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ui.setContent("R的最优结果计算中...\r\n");
				new Thread() {
					public void run() {
						Create.minR = 1000;
						MAX_NUM = Integer.parseInt(ui.getInput());
						String out = startCalc();
						ui.setContent(out);
					};
				}.start();
			}
		});
	}
	
	private static synchronized String startCalc() {
		String out = "";
		if(MAX_NUM <= 0) {
			out += "请输入大于0的数\r\n";
			return out;
		}
		out += "R的最优结果计算中...\r\n";
		if(MAX_NUM <= 8) { // 前8个识别量直接输出
			createCard(MAX_NUM);
		} else {
			CARD_NUM = getCardNum(MAX_NUM); // 求卡片数量
			LOWER_R = getLowerR(MAX_NUM); // 求r的下界
			if(CARD_NUM == 3) { // 3张卡片的数卡
				startCalcThree(firstNum_three, range_three);
			} else if(CARD_NUM == 4) { // 4张卡片的数卡
				startCalcFour(firstNum_four, range_four);
			} else if(CARD_NUM == 5) { // 5张卡片的数卡
				if(MAX_NUM == 100) { // 题目中的数字，单独计算
					startCalcFive(firstNum_five, range_five_100);
				} else if(MAX_NUM >= 81 && MAX_NUM <= 120) {
					startCalcFive(firstNum_five, range_five_1);
				} else if(MAX_NUM >= 121 && MAX_NUM <= 161) {
					startCalcFive(firstNum_five, range_five_2);
				} else if(MAX_NUM >= 162 && MAX_NUM <= 242) {
					startCalcFive(firstNum_five, range_five_3);
				}
			} else {
				out += "输入数字范围不正确\r\n";
				return out;
			}
			MIN_R = Create.minR;
			cardList = Create.minCardList;
		}
		
		out += ("卡片数量 = " + CARD_NUM + "\r\n");
		out += ("R的下界 = " + LOWER_R + "\r\n");
		out += ("R最优结果  = " + MIN_R + "\r\n");
		out += "\r\n数卡: \r\n";
		int sum = 0;
		for(int i = 0; i < cardList.length; i++) {
			for(int j = 1; j < cardList[i].length; j++) {
				if(cardList[i][j].index == 1 && cardList[i][j].numList[0] > MAX_NUM) {
					cardList[i][j].index = 0;
				}
				sum += cardList[i][j].index * cardList[i][j].numList[0];
				out += ("卡" + (i + 1) + (j == 1 ? "正面" : "反面") + "  数字个数: " + cardList[i][j].index + "\r\n");
				Arrays.sort(cardList[i][j].numList, 0, cardList[i][j].index);
				for(int k = 0; k < cardList[i][j].index; k++) {
					out += (cardList[i][j].numList[k] + " ");
				}
				out += "\r\n\r\n";
			}
		}

		out += ("Sum = " + sum + "\r\n");
		return out;
	}
	
	public static void createCard(int n) {
		if(n <= 2) {
			CARD_NUM = 1;
			LOWER_R = 1;
			MIN_R = 1;
			cardList = new Card[Launcher.CARD_NUM][Launcher.STATE];
			for(int i = 0; i < cardList.length; i++) {
				for(int j = 0; j < cardList[i].length; j++) {
					cardList[i][j] = new Card();
				}
			}
			switch(n) {
			case 1:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].index = 1;
				
				break;
			case 2:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].index = 1;
				
				cardList[0][2].numList[0] = 2;
				cardList[0][2].index = 1;
				break;
			}
		} else {
			CARD_NUM = 2;
			cardList = new Card[Launcher.CARD_NUM][Launcher.STATE];
			for(int i = 0; i < cardList.length; i++) {
				for(int j = 0; j < cardList[i].length; j++) {
					cardList[i][j] = new Card();
				}
			}
			switch(n) {
			case 3:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].index = 1;
				
				cardList[0][2].numList[0] = 2;
				cardList[0][2].index = 1;
				
				cardList[1][1].numList[0] = 3;
				cardList[1][1].index = 1;
				
				LOWER_R = 1;
				MIN_R = 1;
				break;
			case 4:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].index = 1;
				
				cardList[0][2].numList[0] = 2;
				cardList[0][2].index = 1;
				
				cardList[1][1].numList[0] = 3;
				cardList[1][1].index = 1;
				
				cardList[1][2].numList[0] = 4;
				cardList[1][2].index = 1;
				
				LOWER_R = 1;
				MIN_R = 1;
				break;
			case 5:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].index = 1;
				
				cardList[0][2].numList[0] = 2;
				cardList[0][2].numList[1] = 5;
				cardList[0][2].index = 2;
				
				cardList[1][1].numList[0] = 3;
				cardList[1][1].numList[1] = 5;
				cardList[1][1].index = 2;
				
				cardList[1][2].numList[0] = 4;
				cardList[1][2].index = 1;
				
				LOWER_R = 2;
				MIN_R = 2;
				break;
			case 6:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].numList[1] = 4;
				cardList[0][1].index = 2;
				
				cardList[0][2].numList[0] = 2;
				cardList[0][2].numList[1] = 5;
				cardList[0][2].index = 2;
				
				cardList[1][1].numList[0] = 3;
				cardList[1][1].numList[1] = 4;
				cardList[1][1].numList[2] = 5;
				cardList[1][1].index = 3;
				
				cardList[1][2].numList[0] = 6;
				cardList[1][2].index = 1;
				
				LOWER_R = 3;
				MIN_R = 3;
				break;
			case 7:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].numList[1] = 4;
				cardList[0][1].numList[2] = 7;
				cardList[0][1].index = 3;
				
				cardList[0][2].numList[0] = 2;
				cardList[0][2].numList[1] = 5;
				cardList[0][2].index = 2;
				
				cardList[1][1].numList[0] = 3;
				cardList[1][1].numList[1] = 4;
				cardList[1][1].numList[2] = 5;
				cardList[1][1].index = 3;
				
				cardList[1][2].numList[0] = 6;
				cardList[1][2].numList[1] = 7;
				cardList[1][2].index = 2;
				
				LOWER_R = 3;
				MIN_R = 3;
				break;
			case 8:
				cardList[0][1].numList[0] = 1;
				cardList[0][1].numList[1] = 4;
				cardList[0][1].numList[2] = 7;
				cardList[0][1].index = 3;
				
				cardList[0][2].numList[0] = 2;
				cardList[0][2].numList[1] = 5;
				cardList[0][2].numList[2] = 8;
				cardList[0][2].index = 3;
				
				cardList[1][1].numList[0] = 3;
				cardList[1][1].numList[1] = 4;
				cardList[1][1].numList[2] = 5;
				cardList[1][1].index = 3;
				
				cardList[1][2].numList[0] = 6;
				cardList[1][2].numList[1] = 7;
				cardList[1][2].numList[2] = 8;
				cardList[1][2].index = 3;
				
				LOWER_R = 3;
				MIN_R = 3;
				break;

			default:
				break;
			}
			
		}
		
	}

	private static void startCalcThree(int[][] firstNum, int[][] range) {
		for(int i = range[0][0]; i <= range[0][1]; i++) {
			for(int j = range[1][0]; j <= range[1][1]; j++) {
				for(int k = range[2][0]; k <= range[2][1]; k++) {
					if(isUp(i, j, k)) {
						firstNum[1][2] = i;
						firstNum[2][1] = j;
						firstNum[2][2] = k;
						CreateThree createCard = new CreateThree();
						createCard.start(firstNum);
						if(Create.minR == LOWER_R) {
							return; // 找到一个最小r就好
						}
					}
				}
			}
		}
	}
	
	private static void startCalcFour(int[][] firstNum, int[][] range) {
		for(int i = range[0][0]; i <= range[0][1]; i++) {
			for(int j = range[1][0]; j <= range[1][1]; j++) {
				for(int k = range[2][0]; k <= range[2][1]; k++) {
					for(int m = range[3][0]; m <= range[3][1]; m++) {
						for(int n = range[4][0]; n <= range[4][1]; n++) {
							if(isUp(i, j, k, m, n)) {
								firstNum[1][2] = i;
								firstNum[2][1] = j;
								firstNum[2][2] = k;
								firstNum[3][1] = m;
								firstNum[3][2] = n;
								CreateFour createCard = new CreateFour();
								createCard.start(firstNum);
								if(Create.minR == LOWER_R) {
									return; // 找到一个最小r就好
								}
							}
						}
					}
				}
			}
		}
	}
	
	private static void startCalcFive(int[][] firstNum, int[][] range) {
		for(int i = range[0][0]; i <= range[0][1]; i++) {
			for(int j = range[1][0]; j <= range[1][1]; j++) {
				for(int k = range[2][0]; k <= range[2][1]; k++) {
					for(int m = range[3][0]; m <= range[3][1]; m++) {
						for(int n = range[4][0]; n <= range[4][1]; n++) {
							if(isUp(i, j, k, m, n)) {
								firstNum[2][2] = i;
								firstNum[3][1] = j;
								firstNum[3][2] = k;
								firstNum[4][1] = m;
								firstNum[4][2] = n;
								CreateFive createCard = new CreateFive();
								createCard.start(firstNum);
								if(Create.minR == LOWER_R) {
									return; // 找到一个最小r就好
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * @author cylong
	 * @version 2015年7月19日  下午7:13:07
	 * @return 
	 */
	private static boolean isUp(int...args) {
		for(int i = 1; i < args.length; i++) {
			if(args[i - 1] >= args[i]) {
				return false;
			}
		}
		return true;
	}

	private static int getCardNum(int n) {
		double d_cardNum = Math.log(n + 1) / Math.log(STATE);
		int i_cardName = (int)d_cardNum;
		if(i_cardName == d_cardNum) {
			return i_cardName;
		} else {
			return i_cardName + 1;
		}
	}
	
	/**
	 * @author cylong
	 * @version 2015年7月19日  下午5:32:11
	 */
	private static int getLowerR(int n) {
		int k = getCardNum(n);
		int[] arr = new int[k];
		arr[0] = 2 * Cnr(k, 1);
		int t = 0;
		for(int i = 1; i < arr.length; i++) {
			arr[i] = (int)(Math.pow(2, i + 1) * Cnr(k, i + 1) + arr[i - 1]);
			if(n >= arr[i - 1] && n <= arr[i]) {
				t = i - 1;
			}
		}
		
		int a = n - arr[t];
		int s = 0;
		for(int i = 1; i <= t + 1; i++) {
			s += Math.pow(2, i) * Cnr(k, i) * i;
		}
		s += a * (t + 2);
		double d_r = s * 1.0 / (2 * k);
		int i_r = (int)d_r;
		if(i_r == d_r) {
			int m = n * (n + 1) / 2;
			if(m % i_r == 0) {
				return i_r;
			} else {
				return i_r + 1;
			}
		} else {
			return i_r + 1;
		}
	}
	
	private static int Cnr(int n, int r) {
		int pro = 1;
		for(int i = n - r + 1; i <= n; i++) {
			pro *= i;
		}
		int res = pro / factorial(r);
		return res;
	}
	
	private static int factorial(int n) {
		if(n < 0) {
			return -1;
		} else if(n == 0) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}
	}
	
}

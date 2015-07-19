package cardbl;
import main.Launcher;

/**
 * 四张张卡片的计算
 * @author cylong
 * @version 2015年7月17日 下午8:09:42
 */
public class CreateFour extends Create {

	@Override
	protected void calc() {
		for(int i = Launcher.STATE - 1; i >= 0; i--) {
			for(int j = Launcher.STATE - 1; j >= 0; j--) {
				for(int k = Launcher.STATE - 1; k >= 0; k--) {
					for(int m = Launcher.STATE - 1; m >= 0; m--) {
						handle(i, j, k, m);
					}
				}
			}
		}
	}

}

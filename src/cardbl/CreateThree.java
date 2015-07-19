package cardbl;
import main.Launcher;

/**
 * 三张卡片的计算
 * @author cylong
 * @version 2015年7月20日  上午1:57:18
 */
public class CreateThree extends Create {
	
	@Override
	protected void calc() {
		for(int i = Launcher.STATE - 1; i >= 0; i--) {
			for(int j = Launcher.STATE - 1; j >= 0; j--) {
				for(int k = Launcher.STATE - 1; k >= 0; k--) {
					handle(i, j, k);
				}
			}
		}
	}
}

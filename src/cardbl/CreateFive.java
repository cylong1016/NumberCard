package cardbl;
import main.Launcher;

/**
 * 5张卡片的计算
 * @author cylong
 * @version 2015年7月20日  上午1:50:25
 */
public class CreateFive extends Create {
	
	@Override
	protected void calc() {
		for(int i = Launcher.STATE - 1; i >= 0; i--) {
			for(int j = Launcher.STATE - 1; j >= 0; j--) {
				for(int k = Launcher.STATE - 1; k >= 0; k--) {
					for(int m = Launcher.STATE - 1; m >= 0; m--) {
						for(int n = Launcher.STATE - 1; n >= 0; n--) {
							handle(i, j, k, m, n);
						}
					}
				}
			}
		}
	}

}

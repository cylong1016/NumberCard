package ui.button;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

/**
 * 自定义Button
 * @author cylong
 * @version 2014年12月12日 上午4:00:21
 */
public class Button extends JLabel {

	/** serialVersionUID */
	private static final long serialVersionUID = -5073422084920844212L;
	
	/*----------------------------通用按钮配置--------------------------------*/
	/** button字体 */
	public static Font BTN_FONT = new Font("黑体", Font.PLAIN, 16);
	/** button字体颜色 */
	public static Color BTN_FORE_COLOR = Color.BLACK;
	/** button背景颜色 */
	public static Color BTN_BACK_COLOR = Color.LIGHT_GRAY;
	/** 移动到button上的背景颜色 */
	public static Color ENTERED_BTN_BACK_COLOR = Color.GRAY;
	/*----------------------------通用按钮配置--------------------------------*/

	public Button(String text) {
		super(text, JLabel.CENTER);
		this.setFont(BTN_FONT);	// 文本字体
		this.setForeground(BTN_FORE_COLOR);	// 文本颜色
		this.setOpaque(true);
		this.addMouseListener(new ButtonListener());
		this.setBackground(BTN_BACK_COLOR);	// 按钮背景色
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.setBackground(BTN_BACK_COLOR);
		super.setEnabled(enabled);
	}
	
	private class ButtonListener extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			if(Button.this.isEnabled()) {
				Button.this.setBackground(ENTERED_BTN_BACK_COLOR);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(Button.this.isEnabled()) {
				Button.this.setBackground(BTN_BACK_COLOR);
			}
		}

		public void mousePressed(MouseEvent e) {
			if(Button.this.isEnabled()) {
				Button.this.setBackground(BTN_BACK_COLOR);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(Button.this.isEnabled()) {
				Button.this.setBackground(ENTERED_BTN_BACK_COLOR);
			}
		}

	}
}

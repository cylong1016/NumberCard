package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.button.Button;
import ui.frame.Frame;
import ui.textfield.TextField;

/**
 * @author cylong
 * @version 2015年7月20日  上午2:50:13
 */
public class UI {
	
	private Frame frame = new Frame();
	private TextField textField = new TextField();
	private TextArea textArea= new TextArea();
	public Button btn = new Button("开始计算");
	private Font font = new Font("黑体", Font.PLAIN, 16);
	private Dimension dimen = new Dimension(150, 30);
	
	public UI() {
		JPanel borderLayoutPanel = new JPanel();
		borderLayoutPanel.setLayout(new BorderLayout());
		frame.add(borderLayoutPanel);
		
		JPanel panelTool = new JPanel();
		borderLayoutPanel.add(panelTool, BorderLayout.NORTH);
		
		JLabel hint = new JLabel("请输入识别量n(1 <= n <= 242)：");
		hint.setFont(font);
		panelTool.add(hint);
		
		textField.setPreferredSize(dimen);
		textField.setFont(font);
		panelTool.add(textField);
		
		btn.setPreferredSize(dimen);
		panelTool.add(btn);
		
		borderLayoutPanel.add(textArea, BorderLayout.CENTER);
		textArea.setFont(font);
		textArea.setEditable(false);
		
		frame.setVisible(true);
		frame.start();
	}
	
	public void setContent(String str) {
		textArea.setText(str);
	}
	
	public String getInput() {
		return textField.getText();
	}

}

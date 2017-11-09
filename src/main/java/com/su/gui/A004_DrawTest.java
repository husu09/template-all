package com.su.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 * 2D 图形
 * @author husu
 * 2017/10/17
 * */
public class A004_DrawTest {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JFrame frame = new DrawFrame();
			frame.setTitle("DrawTest");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}
class DrawFrame extends JFrame {
	public DrawFrame() {
		add(new DrawComponent());
		pack();
	}
}
class DrawComponent extends JComponent {
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 400;
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// 绘制矩形
		double leftX = 100;
		double topY = 100;
		double width = 200;
		double height = 150;
		Rectangle2D rect = new Rectangle2D.Double(leftX, topY, width, height);
		g2.draw(rect);
		// 绘制椭圆
		Ellipse2D ellipse = new Ellipse2D.Double();
		ellipse.setFrame(rect);
		g2.draw(ellipse);
		// 绘制斜线
		g2.draw(new Line2D.Double(leftX, topY, leftX + width, topY + height));
		//绘制圆
		double centerX = rect.getCenterX();
		double centerY = rect.getCenterY();
		double radius = 150;
		Ellipse2D circle = new Ellipse2D.Double();
		circle.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);
		g2.draw(circle);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
}

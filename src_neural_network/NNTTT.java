import java.applet.Applet;
import java.awt.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.awt.geom.Line2D;

import javax.swing.*;


public class NNTTT extends Applet
implements MouseListener, ActionListener, WindowListener
{
	private static RandomMove random = new RandomMove();
	private static Neural neural = new Neural();
	private static ttt_expert expert = new ttt_expert();
	int filler=25;
	JFrame f;
	int flag = 1;
	int n;
	int m;
	int i = 0;
	static boolean isStart=false;
	static int bug = 0;
	static String board;
	static boolean firstMove=true;
	final int allFil=200;
	static int winCount = 0;
	static int loseCount = 0;
	static int drawCount = 0;
	static int pieceCount=0;

	static boolean OBuffer=false;
	JRadioButton HumanButton;
	JRadioButton RandomButton;
	JRadioButton ExpertButton;
	JRadioButton NeuralButton;
	JButton ClearStat;
	JButton Clear;
	JButton Start;
	JLabel title0;
	JLabel title1;
	JLabel winCountLabel;
	JLabel loseCountLabel;
	JLabel drawCountLabel;
	JLabel pos0pos, pos1pos, pos2pos, pos3pos, pos4pos, pos5pos, pos6pos, pos7pos, pos8pos;

	public NNTTT()
	{
		this.f = new JFrame("Tic Tac Toe");
		this.HumanButton = new JRadioButton("Human");
		this.RandomButton = new JRadioButton("Random");
		this.ExpertButton = new JRadioButton("Expert");
		this.NeuralButton = new JRadioButton("Neural");
		this.ClearStat = new JButton("ClearStat");
		this.Clear = new JButton("Clear");
		this.Start = new JButton("Start");
		this.title0 = new JLabel("Tic Tac Toe");
		this.title1 = new JLabel("with Neural Network");
		this.winCountLabel = new JLabel("Win  : 0");
		this.loseCountLabel = new JLabel("Lose : 0");
		this.drawCountLabel = new JLabel("Draw  : 0");

		this.pos0pos = new JLabel("");
		this.pos1pos = new JLabel("");
		this.pos2pos = new JLabel("");
		this.pos3pos = new JLabel("");
		this.pos4pos = new JLabel("");
		this.pos5pos = new JLabel("");
		this.pos6pos = new JLabel("");
		this.pos7pos = new JLabel("");
		this.pos8pos = new JLabel("");

		this.f.add(HumanButton);
		this.f.add(RandomButton);
		this.f.add(ExpertButton);
		this.f.add(NeuralButton);
		this.f.add(ClearStat);
		this.f.add(Clear);
		this.f.add(Start);
		this.f.add(title0);
		this.f.add(title1);
		this.f.add(winCountLabel);
		this.f.add(loseCountLabel);
		this.f.add(drawCountLabel);
		this.f.add(pos0pos);
		this.f.add(pos1pos);
		this.f.add(pos2pos);
		this.f.add(pos3pos);
		this.f.add(pos4pos);
		this.f.add(pos5pos);
		this.f.add(pos6pos);
		this.f.add(pos7pos);
		this.f.add(pos8pos);
		
		this.f.addWindowListener(this);
		this.f.getContentPane().setBackground(Color.WHITE);
		this.f.setLayout(null);
		this.f.setSize(1000, 850);      // Window size

		NeuralButton.setSelected(true);
		HumanButton.setSelected(true);

		this.HumanButton.setBounds(850, 230, 90, 30);
		this.RandomButton.setBounds(850, 315, 90, 30);
		this.ExpertButton.setBounds(850, 350, 90, 30);
		this.NeuralButton.setBounds(850, 385, 90, 30);
		this.ClearStat.setBounds(850, 690, 100, 50);
		this.Clear.setBounds(690, 560, 260, 80);
		this.Start.setBounds(690, 460, 260, 80);
		this.title0.setBounds(10, -30, 800, 200);
		this.title1.setBounds(150, 50, 800, 200);
		this.winCountLabel.setBounds(690, 650, 200,50);
		this.loseCountLabel.setBounds(690, 690, 200,50);
		this.drawCountLabel.setBounds(690, 730, 200,50);
		this.pos0pos.setBounds(160, 367, 50, 20);
		this.pos1pos.setBounds(360, 367, 50, 20);
		this.pos2pos.setBounds(560, 367, 50, 20);
		this.pos3pos.setBounds(160, 567, 50, 20);
		this.pos4pos.setBounds(360, 567, 50, 20);
		this.pos5pos.setBounds(560, 567, 50, 20);
		this.pos6pos.setBounds(160, 767, 50, 20);
		this.pos7pos.setBounds(360, 767, 50, 20);
		this.pos8pos.setBounds(560, 767, 50, 20);

		this.pos0pos.setForeground(Color.red);
		this.pos1pos.setForeground(Color.red);
		this.pos2pos.setForeground(Color.red);
		this.pos3pos.setForeground(Color.red);
		this.pos4pos.setForeground(Color.red);
		this.pos5pos.setForeground(Color.red);
		this.pos6pos.setForeground(Color.red);
		this.pos7pos.setForeground(Color.red);
		this.pos8pos.setForeground(Color.red);
		this.title0.setFont(new Font("Courier New", Font.ITALIC, 80));
		this.title1.setFont(new Font("Courier New", Font.ITALIC, 60));
		Start.setFont(new Font("Arial", Font.PLAIN, 24));
		Clear.setFont(new Font("Arial", Font.PLAIN, 24));

		this.f.addMouseListener(this);
		board = "000000000";
		pieceCount = 0;
		this.HumanButton.addActionListener(this);
		this.RandomButton.addActionListener(this);
		this.ExpertButton.addActionListener(this);
		this.NeuralButton.addActionListener(this);
		this.ClearStat.addActionListener(this);
		this.Clear.addActionListener(this);
		this.Start.addActionListener(this);
		this.f.setVisible(true);

		//      Graphics g = this.f.getGraphics();
		//      drawBoard(g);
		//      this.f.setVisible(true);
		//      setVisible(true);
	}

	public void keyPressed(KeyEvent k)
	{
		System.out.print("");
	}

	public void keyTyped(KeyEvent k) {
		//this.s1 += k.getKeyChar();
	}

	public void keyReleased(KeyEvent k) {
		System.out.print("");
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.RandomButton)
		{
			RandomButton.setSelected(true);
			ExpertButton.setSelected(false);
			NeuralButton.setSelected(false);

		}
		if (ae.getSource() == this.ExpertButton)
		{
			RandomButton.setSelected(false);
			ExpertButton.setSelected(true);
			NeuralButton.setSelected(false);
		}
		if (ae.getSource() == this.NeuralButton)
		{
			RandomButton.setSelected(false);
			ExpertButton.setSelected(false);
			NeuralButton.setSelected(true);
		}
		if (ae.getSource() == this.ClearStat)
		{
			winCount = 0;
			loseCount = 0;
			drawCount = 0;
			updateStat();
		}
		if (ae.getSource() == this.Clear)
		{
			this.f.setVisible(false);
			bug = 0;
			new NNTTT();
			Graphics g = this.f.getGraphics();
			drawBoard(g);
			this.f.setVisible(true);
			board = "000000000";
			clearPos();
		}
		if (ae.getSource() == this.Start)
		{
			Graphics g = this.f.getGraphics();
			drawBoard(g);
			//getMove();
			isStart=true;
			//System.exit(0);
		}
	}

	private void updateStat() {
		winCountLabel.setText("Win  : "+winCount);
		loseCountLabel.setText("Lose : "+loseCount);
		drawCountLabel.setText("Draw  : "+drawCount);
	}

	private void updatePos() {
		float [] prob ;
		prob= neural.getProbs();
		pos0pos.setText(String.format("%.3f", prob[0]));
		pos1pos.setText(String.format("%.3f", prob[1]));
		pos2pos.setText(String.format("%.3f", prob[2]));
		pos3pos.setText(String.format("%.3f", prob[3]));
		pos4pos.setText(String.format("%.3f", prob[4]));
		pos5pos.setText(String.format("%.3f", prob[5]));
		pos6pos.setText(String.format("%.3f", prob[6]));
		pos7pos.setText(String.format("%.3f", prob[7]));
		pos8pos.setText(String.format("%.3f", prob[8]));
	}

	private void clearPos() {
		pos0pos.setText("");
		pos1pos.setText("");
		pos2pos.setText("");
		pos3pos.setText("");
		pos4pos.setText("");
		pos5pos.setText("");
		pos6pos.setText("");
		pos7pos.setText("");
		pos8pos.setText("");
	}

	public void windowClosing(WindowEvent de)
	{
		System.exit(0); 
	}

	public void windowOpened(WindowEvent de) { }

	public void windowClosed(WindowEvent de) { }

	public void windowActivated(WindowEvent de) { }

	public void windowDeactivated(WindowEvent de) { }

	public void windowIconified(WindowEvent de) { }

	public void windowDeiconified(WindowEvent de) {  }

	public void mouseClicked(MouseEvent e) {

		if(isStart){
			Graphics g = this.f.getGraphics();
			//g.drawLine(10, 10, 310, 310);
			int x = e.getX();
			int y = e.getY();
			int xpos = e.getX()*3/600;
			int ypos = (e.getY()-200)*3/600;
			int pos=xpos+3*ypos;
			//	System.out.println(pos);
			//getMove(g);
			drawBoard(g);

			if((pos<9) && expert.isEmpty(board, pos)) {
				pieceCount++;
				drawX(g, (pos%3)*200, (pos/3)*200, 180);
				board = board.substring(0, 8-pos)+"1"+board.substring(8-pos+1,9);
				winCheck(board, g);
				printboard(board);
				if(pieceCount<9) getMove();
			}
		}
	}


	private void drawO(Graphics g, int x, int y, int size) {
		g.setColor(Color.BLACK);
		g.drawOval(40+x, 40+allFil+y, size, size);
	}

	private void drawX(Graphics g, int x, int y, int size) {
		g.setColor(Color.BLACK);
		g.drawLine(40+x, 40+allFil+y, size+x, size+allFil+y);
		g.drawLine(40+x, size+allFil+y, size+x, 40+allFil+y);
	}

	private void drawBoard(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));

		// TTT Board
		g.setColor(Color.WHITE);
		g.drawRect(20,220, 600,600);

		g2.setColor(Color.BLACK);
		g.drawLine(220, 220, 220, 820);  // vertical
		g.drawLine(420, 220, 420, 820);
		g.drawLine(20, 420, 620, 420);  // horizonal
		g.drawLine(20, 620, 620, 620);

		g2.setStroke(new BasicStroke(1));

		//X & O
		drawX(g, 700, 0, 100);
		drawO(g, 700, 110, 75);
	}

	private void getMove() {
		if(OBuffer){
			OBuffer=false;
		}
		else{
			int pos=0;
			Graphics2D g2;
			Graphics g = this.f.getGraphics();
			drawBoard(g);


			String ternary="";

			if(NeuralButton.isSelected()) {
				pos=neural.feedforward(board);
				System.out.println("Neural Called : "+board +" "+pos);
				updatePos();
			} else if(ExpertButton.isSelected()) {
				pos=expert.ask_ottt_expert(board);
				System.out.println("Expert Called " + board +" "+pos);
			} else if(RandomButton.isSelected()) {
				pos=random.getMove(ternary);
				System.out.println("Random Called "+ board +" "+pos);
			}
			board = board.substring(0, 8-pos)+"2"+board.substring(8-pos+1,9);
			pieceCount++;
			drawO(g, (pos%3)*200, (pos/3)*200, 160);
			winCheck(board, g);
		}
	}

	private void winCheck(String board, Graphics g) {
		if(expert.xWin(board)) Xwins();
		else if(expert.oWin(board)) Owins();
		else if(pieceCount==9){
			drawGame();
	//		boardClear();
		}
	}

	private void boardClear() {
		Graphics g2 = this.f.getGraphics();

		// TTT Board
		g2.setColor(Color.WHITE);
		g2.fillRect(10,210, 650,650);
		System.out.println("swafoiek");
		g2.setColor(Color.BLACK);
		g2.drawLine(220, 220, 220, 820);  // vertical
		g2.drawLine(420, 220, 420, 820);
		g2.drawLine(20, 420, 620, 420);  // horizonal
		g2.drawLine(20, 620, 620, 620);
		board="000000000";
		pieceCount=0;
	}
	
	private void drawGame() {
		System.out.println("Draw");
		drawCount++;
		isStart=false;
		JOptionPane.showMessageDialog(null, "Draw!!!");
		updateStat();
	}

	private void Owins() {

		System.out.println("O Wins");
		loseCount++;
		isStart=false;
		JOptionPane.showMessageDialog(null, "O Win!!!");
		updateStat();
	}


	private void Xwins() {
		System.out.println("X Wins");
		winCount++;
		isStart=false;
		JOptionPane.showMessageDialog(null, "X Win!!!");
		OBuffer=true;
		updateStat();
	}

	private void printboard(String board) {
		//	System.out.println(board);
	}

	public void mouseReleased(MouseEvent e)
	{
		System.out.print("");
	}

	public void mouseEntered(MouseEvent e)
	{
		System.out.print("");
	}

	public void mouseExited(MouseEvent e) {
		System.out.print("");
	}

	public void mousePressed(MouseEvent e) {
		System.out.print("");
	}

	public static void main(String[] args)
	{
		new NNTTT();
	}

}
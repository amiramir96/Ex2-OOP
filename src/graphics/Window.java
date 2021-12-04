package graphics;

import api.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.*;
public class Window extends JFrame {

    DirectedWeightedGraphAlgorithms currAlgo;
    Menu menu;
    double zoomInOut;

    public Window(DirectedWeightedGraphAlgorithms algos){

        this.currAlgo = algos;

//        initializePoints(ourMaze);
        this.setTitle("Amir & Ori DWG GUI!");
        this.setSize(750,750);
//        this.setLocationRelativeTo(null);
//        this.setLayout(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.zoomInOut = 1.0;

        // init icon
        ImageIcon im = new ImageIcon("dwg.png");
        this.setIconImage(im.getImage());

        DrawGraph drawer = new DrawGraph(this.currAlgo);
//        drawer.paint(this.getGraphics());
        this.add(drawer);
//        menu.add(drawer);
        menu = new Menu(this.currAlgo, drawer);
        this.setJMenuBar(menu);
        this.pack();

    }
//
//        // init Labels
//        JLabel isConnected = new JLabel("isConnected");
////        isConnected.setBackground(Color.darkGray);
//        isConnected.setFont(new Font("m", Font.BOLD, 12));
//
//        JLabel shortestPathDist = new JLabel("shortestPathDist");
////        isConnected.setBackground(Color.darkGray);
//        isConnected.setFont(new Font("m", Font.BOLD, 12));
//
//        JButton but = new JButton();
//        but.setText("isConnected");
//        but.addActionListener(e -> System.out.println("yay"));
//        but.setBounds(500, 150, 100, 50);
//        but.setFont(new Font("h", Font.BOLD, 12));
//        but.setBorder(BorderFactory.createEtchedBorder());
//
//        // init panel
//        JPanel algoPanel = new JPanel();
//        algoPanel.setBackground(Color.RED);
//        algoPanel.setBounds(0, 100, 100, 500);
//        // add labels to panel
//        algoPanel.add(isConnected);
//        algoPanel.add(shortestPathDist);
////        algoPanel.add(but);
//        this.add(algoPanel);
//        this.add(but);
//


//        this.getContentPane().setBackground();
//        this.initMenu();
//        this.initButtons();
//    private void initButtons() {
//        Button btnIsConnected = new Button("isConnected");
//        btnIsConnected.setLocation(210, 210);
//        btnIsConnected.setSize(30, 30);
//        this.add(btnIsConnected);
//        Button btnShortestPathDist = new Button("shortestPathDist");
//        Button btnShortestPath = new Button("shortestPath");
//        Button btnCenter = new Button("center");
//        Button btnTsp = new Button("tsp");
//    }
//
//

}

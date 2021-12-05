package graphics;

import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Menu extends JMenuBar implements ActionListener {

    Window w;
    DirectedWeightedGraphAlgorithms algoGraph;
    JMenu menu, runAlgo;
    JMenuItem loadGraph, saveGraph, isConnected, center, shortestPathDist, shortestPath, tsp;
    JFileChooser fileChooser;
    DrawGraph drawer;
    JTextField userTextIn;
    JButton submitBut;

    public Menu(DirectedWeightedGraphAlgorithms g, DrawGraph d, Window w) {
//        super();
        this.w = w;
        // init main objects
        this.drawer = d;
        this.algoGraph = g;
        this.menu = new JMenu("File");
        this.fileChooser = new JFileChooser();
        // init menu items ("buttons")
        this.loadGraph = new JMenuItem("load graph");
        this.saveGraph = new JMenuItem("save graph");
//        this.exitGui = new JMenuItem("exit");
        this.loadGraph.addActionListener(this);
        this.saveGraph.addActionListener(this);
//        this.exitGui.addActionListener(this);
        // add them to the bar
        this.menu.add(this.loadGraph);
        this.menu.add(this.saveGraph);
//        this.menu.add(this.exitGui);
//        this.loadGraph.setMnemonic(KeyEvent.VK_L);

        // init one more tool options for the menu
        this.runAlgo = new JMenu("Algo_Command");

        this.isConnected = new JMenuItem("isConnected");
        this.center = new JMenuItem("center");
        this.shortestPath = new JMenuItem("shortestPath");
        this.shortestPathDist = new JMenuItem("shortestPathDist");
        this.tsp = new JMenuItem("tsp");

        this.isConnected.addActionListener(this);
        this.center.addActionListener(this);
        this.shortestPath.addActionListener(this);
        this.shortestPathDist.addActionListener(this);
        this.tsp.addActionListener(this);

        // add options to "RunAlgorithm" menu
        this.runAlgo.add(this.isConnected);
        this.runAlgo.add(this.center);
        this.runAlgo.add(this.shortestPath);
        this.runAlgo.add(this.shortestPathDist);
        this.runAlgo.add(this.tsp);

        this.add(this.menu);
        this.add(this.runAlgo);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.loadGraph) { // load graph
            System.out.println(this.algoGraph.getGraph());
            this.fileChooser.showOpenDialog(null);
            if (this.fileChooser.getSelectedFile() == null) {
                return;
            } else {
                this.algoGraph.load(this.fileChooser.getSelectedFile().getPath());
                this.drawer.updateDrawer(this.algoGraph);
                System.out.println(this.fileChooser.getSelectedFile().getPath());
                System.out.println(this.algoGraph.getGraph());
                System.out.println("loaded from: " + this.fileChooser.getSelectedFile().getPath());
//               this.drawer.repaint();
                getTopLevelAncestor().repaint();

            }
        }
        else if (e.getSource() == this.saveGraph) { // save graph
            this.fileChooser.showSaveDialog(null);
            if (this.fileChooser.getSelectedFile() == null) {
                return;
            } else {
                this.algoGraph.save(this.fileChooser.getSelectedFile().getPath());
                System.out.println("saved at: " + this.fileChooser.getSelectedFile().getPath());
            }
        }
        else if (e.getSource() == this.isConnected) { // is connected
            System.out.println("connected");
            if (this.algoGraph.isConnected()) {
                this.drawer.setColors(Color.RED, Color.CYAN);
                this.drawer.setFlagAllSameColor(true);
//                this.drawer.repaint();
                getTopLevelAncestor().repaint();
            }
        }
        else if (e.getSource() == this.shortestPath || e.getSource() == this.tsp) { // shortest path
            System.out.println("shortestPath");
            JFrame n = new JFrame();
            n.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            n.setLayout(new FlowLayout());
            this.submitBut = new JButton("Submit");
            this.submitBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == submitBut){
                        System.out.println("hi thats worked");
                        String s = userTextIn.getText();
                        String[] str = s.split(",");
                        ArrayList<Integer> nodes = new ArrayList<>();
                        for (String n : str){
                            nodes.add(Integer.parseInt(n));
                        }
                        drawer.specialNodes = algoGraph.shortestPath(nodes.get(0), nodes.get(1));
                        drawer.specialEdges = currPathEdges(drawer.specialNodes);
                        drawer.setFlagAllSameColor(false);
                        drawer.setColors(Color.GREEN, Color.GREEN);
                        getTopLevelAncestor().repaint();
                        n.setVisible(false);
                        n.dispose();
                    }
                }
            });
//            this.submitBut.setFocusPainted(true);
            this.userTextIn = new JTextField(16);
            this.userTextIn.setPreferredSize(new Dimension(250, 50));
            n.add(this.submitBut);
            n.add(this.userTextIn);
            n.pack();
            n.setVisible(true);
//            this.getTopLevelAncestor().add(n);
//            w.shortestPathField();
        }
        else if (e.getSource() == this.shortestPathDist) {// shortest Path Dist
            System.out.println("shortestPathDist");
        }
        else if (e.getSource() == this.center) { // center
            System.out.println("center");
            this.drawer.specialNodes = new LinkedList<>();
            this.drawer.specialNodes.add(this.algoGraph.center());
            this.drawer.setColors(Color.GREEN, Color.RED);
            this.drawer.setFlagAllSameColor(false);
//            this.drawer.repaint();
            getTopLevelAncestor().repaint();
        }
    }

    private HashMap<String, Integer> currPathEdges(List<NodeData> nodes) {
        int i=0;
        HashMap<String, Integer> edges = new HashMap<>();
        while (i+1 < nodes.size()){
            edges.put(""+nodes.get(i).getKey()+","+nodes.get(i+1).getKey(), 0);
            System.out.println(edges.size());
            i++;
        }
        return edges;
    }
}
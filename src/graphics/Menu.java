package graphics;

import api.*;

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
    JMenu menu, runAlgo, graphToolBar;
    JMenuItem loadGraph, saveGraph, isConnected, center, shortestPathDist, shortestPath, tsp;
    JMenuItem addNode, delNode, addEdge, delEdge, newDwg;
    JFileChooser fileChooser;
    DrawGraph drawer;
    JTextField userTextIn;
    JButton submitBut;
    ActionEvent funcEvent;

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

        this.graphToolBar = new JMenu("Graph_Management");

        this.addNode = new JMenuItem("add node");
        this.delNode = new JMenuItem("delete node");
        this.addEdge = new JMenuItem("add edge");
        this.delEdge = new JMenuItem("delete edge");
        JMenuItem nothing = new JMenuItem("------");
        this.newDwg = new JMenuItem("create empty graph");

        this.addNode.addActionListener(this);
        this.delNode.addActionListener(this);
        this.addEdge.addActionListener(this);
        this.delEdge.addActionListener(this);
        this.newDwg.addActionListener(this);

        this.graphToolBar.add(this.addNode);
        this.graphToolBar.add(this.delNode);
        this.graphToolBar.add(this.addEdge);
        this.graphToolBar.add(this.delEdge);
        this.graphToolBar.add(nothing);
        this.graphToolBar.add(this.newDwg);

        this.add(this.menu);
        this.add(this.runAlgo);
        this.add(this.graphToolBar);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        this.funcEvent = e;
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
            }
            else {
                this.algoGraph.save(this.fileChooser.getSelectedFile().getPath());
            }
        }
        else if (e.getSource() == this.isConnected) { // is connected
            System.out.println("connected");
            if (this.algoGraph.isConnected()) {
                this.drawer.setColors(Color.GREEN, drawer.defEdge);
                this.drawer.setFlagAllSameColor(true);
                getTopLevelAncestor().repaint();
            }
            else {
                this.drawer.setColors(Color.RED, drawer.defEdge);
                this.drawer.setFlagAllSameColor(true);
                getTopLevelAncestor().repaint();
            }
        }
        else if (e.getSource() == this.shortestPath || e.getSource() == this.tsp || e.getSource() == this.shortestPathDist) { // shortest path/dist and tsp
            System.out.println("shortestPath");
            JFrame n = new JFrame();
            n.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            n.setLayout(new FlowLayout());
            n.setLocationRelativeTo(null);
            this.submitBut = new JButton("Submit");
            this.submitBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    JLabel tempLabel = new JLabel();
                    try {
                        if (event.getSource() == submitBut){
                            System.out.println("hi thats worked");
                            String s = userTextIn.getText();
                            String[] str = s.split(",");
                            ArrayList<Integer> nodes = new ArrayList<>();
                            for (String n : str){
                                nodes.add(Integer.parseInt(n));
                            }
                            if (funcEvent.getSource() == shortestPath){
                                drawer.specialNodes = algoGraph.shortestPath(nodes.get(0), nodes.get(1));
                                drawer.specialEdges = currPathEdges(drawer.specialNodes);
                                drawer.setFlagAllSameColor(false);
                                drawer.setColors(Color.PINK, Color.RED);
                            }
                            else if (funcEvent.getSource() == shortestPathDist){
                                drawer.specialNodes = algoGraph.shortestPath(nodes.get(0), nodes.get(1));
                                drawer.specialEdges = currPathEdges(drawer.specialNodes);
                                double distToPrint = algoGraph.shortestPathDist(nodes.get(0), nodes.get(1));
                                drawer.setFlagAllSameColor(false);
                                drawer.setColors(Color.PINK, Color.GREEN);
                                tempLabel.setText("<html> shortestPathDist between: "+nodes.get(0)+" to "+nodes.get(1)+"<br>"+distToPrint +"</html>");
                                JFrame j = new JFrame();
                                j.setBounds(200,200, 300, 100);
                                j.setTitle("shortestPathDist");
                                j.setLocationRelativeTo(null);
                                JButton okBut = new JButton();
                                okBut.setPreferredSize(new Dimension(60,40));
                                okBut.setText("OK");
                                okBut.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent eq) {
                                        if (eq.getSource() == okBut){
                                            j.setVisible(false);
                                            j.dispose();
                                        }
                                    }
                                });
                                JPanel tq = new JPanel();
                                tq.add(tempLabel);
                                tq.add(okBut);
                                tq.setLocation(300,50);
                                j.add(tq);
                                j.setVisible(true);
                                j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            }
                            else { //funcEvent is tsp!
                                ArrayList<NodeData> tspInput = new ArrayList<>();
                                for (Integer i : nodes){
                                    tspInput.add(algoGraph.getGraph().getNode(i));
                                }
                                drawer.specialNodes = algoGraph.tsp(tspInput);
                                drawer.specialEdges = currPathEdges(drawer.specialNodes);
                                drawer.setFlagAllSameColor(false);
                                drawer.setColors(Color.CYAN, Color.RED);
                            }
                            getTopLevelAncestor().repaint();
                            n.setVisible(false);
                            n.dispose();
                            }
                        }
                        catch (Exception e) {
                            n.setVisible(false);
                            n.dispose();
                            tempLabel.setText("<html> for shortestPath and shortestPathDist: please enter pair of nodes via 'int,int' node keys <br>" +
                                    "for tsp: please enter sequence of nodes via 'int,int,....,int' <br> "+
                                    "or there is existing path for ur query <br>" +
                                    "please ENSURE that u enter input without space </html>");
                            JFrame j = new JFrame();
                            j.setBounds(200,200, 550, 150);
                            j.setTitle("ERROR");
                            j.setLocationRelativeTo(null);
                            JButton okBut = new JButton();
                            okBut.setPreferredSize(new Dimension(60,40));
                            okBut.setText("OK");
                            okBut.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eq) {
                                    if (eq.getSource() == okBut){
                                        j.setVisible(false);
                                        j.dispose();
                                    }
                                }
                            });
                            JPanel tq = new JPanel();
                            tq.add(tempLabel);
                            tq.add(okBut);
                            tq.setLocation(300,50);
                            j.add(tq);
                            j.setVisible(true);
                            j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            return;
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
        else if (e.getSource() == this.center) { // center
            System.out.println("center");
            this.drawer.specialNodes = new LinkedList<>();
            NodeData center = this.algoGraph.center();
            if (center != null){
                this.drawer.specialNodes.add(center);
                this.drawer.setColors(Color.YELLOW, drawer.defEdge);
                this.drawer.setFlagAllSameColor(false);
            }
            else {
                JLabel tempLabel = new JLabel();
                tempLabel.setText("graph is not connected so there is no center node in the graph");
                JFrame j = new JFrame();
                j.setBounds(200,200, 450, 100);
                j.setTitle("ERROR");
                j.setLocationRelativeTo(null);
                JButton okBut = new JButton();
                okBut.setPreferredSize(new Dimension(60,40));
                okBut.setText("OK");
                okBut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent eq) {
                        if (eq.getSource() == okBut){
                            j.setVisible(false);
                            j.dispose();
                        }
                    }
                });
                JPanel tq = new JPanel();
                tq.add(tempLabel);
                tq.add(okBut);
                tq.setLocation(300,50);
                j.add(tq);
                j.setVisible(true);
                j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                return;
            }
//            this.drawer.repaint();
            getTopLevelAncestor().repaint();
        }
        else if (e.getSource() == this.newDwg){
            DirectedWeightedGraph a = new Dwg();
            this.algoGraph.init(a);
            this.drawer.updateDrawer(new DwgMagic(a));
            getTopLevelAncestor().repaint();
        }
    }

    private HashMap<String, Integer> currPathEdges(List<NodeData> nodes) {
        int i=0;
        HashMap<String, Integer> edges = new HashMap<>();
        while (i+1 < nodes.size()){
            edges.put(""+nodes.get(i).getKey()+","+nodes.get(i+1).getKey(), 0);
            i++;
        }
        return edges;
    }
}
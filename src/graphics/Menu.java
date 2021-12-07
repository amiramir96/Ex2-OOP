package graphics;

import api.*;
import impGraph.Dwg;
import impGraph.DwgMagic;
import impGraph.Node;
import impGraph.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
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
        this.fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        // init menu items ("buttons")
        this.loadGraph = new JMenuItem("load graph");
        this.saveGraph = new JMenuItem("save graph");
//        this.exitGui = new JMenuItem("exit");
        this.loadGraph.addActionListener(this);
        this.saveGraph.addActionListener(this); // add them to the bar
        this.menu.add(this.loadGraph);
        this.menu.add(this.saveGraph);


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
        JSeparator nothing = new JSeparator();
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
            this.fileChooser.showOpenDialog(null);
            if (this.fileChooser.getSelectedFile() == null) {
                return;
            } else {
                this.algoGraph.load(this.fileChooser.getSelectedFile().getPath());
                this.drawer.updateDrawer(this.algoGraph);
                this.drawer.setColors(this.drawer.defNode, this.drawer.defEdge);
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
            drawer.setFlagTsp(false);
            this.drawer.setFlagAllSameColor(true);
            if (this.algoGraph.isConnected()) {
                this.drawer.setColors(Color.GREEN, drawer.defEdge);
                getTopLevelAncestor().repaint();
            }
            else {
                this.drawer.setColors(Color.RED, drawer.defEdge);
                getTopLevelAncestor().repaint();
            }
        }
        else if (e.getSource() == this.shortestPath || e.getSource() == this.tsp || e.getSource() == this.shortestPathDist) { // shortest path/dist and tsp
            JFrame n = new JFrame();
            JLabel nLabel = new JLabel();
            if (e.getSource() == this.shortestPath){
                n.setTitle("input for shortestPath");
                nLabel.setText("<html> please set pair integers <br>via pattern 'int,int'</html>");
            }
            else if(e.getSource() == this.shortestPathDist){
                n.setTitle("input for shortestPathDist");
                nLabel.setText("<html> please set pair integers <br>via pattern 'int,int'</html>");
            }
            else if(e.getSource() == this.tsp){
                n.setTitle("input for tsp");
                nLabel.setText("<html> please set integers sequence <br>via pattern 'int,int,...,int'</html>");
            }
            n.setDefaultCloseOperation(n.DISPOSE_ON_CLOSE);
            n.setLayout(new FlowLayout());
            n.setLocationRelativeTo(null);
            this.submitBut = new JButton("Submit");
            this.submitBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    JLabel tempLabel = new JLabel();
                    try {
                        if (event.getSource() == submitBut){
                            String s = userTextIn.getText();
                            String[] str = s.split(",");
                            ArrayList<Integer> nodes = new ArrayList<>();
                            for (String n : str){
                                nodes.add(Integer.parseInt(n));
                            }
                            if (funcEvent.getSource() == shortestPath){ // funcEvent is shortestPath
                                if (nodes.size() > 2){
                                    invalidInput();
                                }
                                drawer.specialNodes = algoGraph.shortestPath(nodes.get(0), nodes.get(1));
                                drawer.specialEdges = currPathEdges(drawer.specialNodes);
                                drawer.setFlagTsp(false);
                                drawer.setFlagAllSameColor(false);
                                drawer.setColors(Color.PINK, Color.RED);
                            }
                            else if (funcEvent.getSource() == shortestPathDist){ // funcEvent is shortestPathDist
                                if (nodes.size() > 2){
                                    invalidInput();
                                }
                                drawer.specialNodes = algoGraph.shortestPath(nodes.get(0), nodes.get(1));
                                drawer.specialEdges = currPathEdges(drawer.specialNodes);
                                double distToPrint = algoGraph.shortestPathDist(nodes.get(0), nodes.get(1));
                                drawer.setFlagTsp(false);
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

                                if (drawer.specialNodes == null || drawer.specialNodes.size() == 0){
                                    drawer.specialNodes = tspInput;
                                    drawer.setColors(Color.RED, drawer.defNode);
                                    drawer.setFlagAllSameColor(false);
                                }
                                else {
                                    drawer.specialEdges = currPathEdges(drawer.specialNodes);
                                    drawer.makeTspString();
                                    drawer.removeDuplicate();
                                    drawer.setFlagTsp(true);
                                    drawer.setFlagAllSameColor(false);
                                    drawer.setColors(Color.GREEN, Color.PINK);
                                }
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
            this.userTextIn = new JTextField(16);
            this.userTextIn.setPreferredSize(new Dimension(250, 50));
            n.add(nLabel);
            n.add(this.submitBut);
            n.add(this.userTextIn);
            n.pack();
            n.setVisible(true);
        }
        else if (e.getSource() == this.center) { // center
            drawer.setFlagTsp(false);
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
        else if (e.getSource() == this.addNode || e.getSource() == this.addEdge){
            JFrame addCom = new JFrame();
            JLabel addText = new JLabel();
            if (e.getSource() == this.addNode){
                addCom.setTitle("add node");
                addText.setText("<html> please set node_id,x_cord,y,cord <br>via pattern 'int,float,float'");
            }
            else {
                addCom.setTitle("add edge");
                addText.setText("<html> please set node_id_src,node_id_dest,edge_weight <br>via pattern 'int,int,float'");
            }
            this.submitBut = new JButton("Submit");
            addCom.setDefaultCloseOperation(addCom.DISPOSE_ON_CLOSE);
            addCom.setLayout(new FlowLayout());
            addCom.setLocationRelativeTo(null);
            this.submitBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ac) {
                    try {
                        if (ac.getSource() == submitBut){
                            String s = userTextIn.getText();
                            String[] str = s.split(",");
                            drawer.setColors(drawer.defNode, drawer.defEdge);
                            if (e.getSource() == addNode){
                                if (str.length > 3 || algoGraph.getGraph().getNode(Integer.parseInt(str[0])) != null){
                                    invalidInput();
                                }
                                algoGraph.getGraph().addNode(new Node(new Point3D(Double.parseDouble(str[1]), Double.parseDouble(str[2]), 0.0), Integer.parseInt(str[0])));
                                algoGraph.init(algoGraph.getGraph());
                                drawer.updateDrawer(algoGraph);
                            }
                            else if (e.getSource() == addEdge){
                                if (str.length > 3){
                                    invalidInput();
                                }
                                algoGraph.getGraph().connect(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Double.parseDouble(str[2]));
                            }
                            getTopLevelAncestor().repaint();
                            addCom.setVisible(false);
                            addCom.dispose();
                        }
                    }
                    catch (Exception ex){
                        addCom.setVisible(false);
                        addCom.dispose();
                        addText.setText("<html> for addNode: please enter node_id and pair of x,y cord via 'int,float,float' pattern <br>" +
                                "for addEdge: please enter pair of node_id and weight of edge via 'int,int,float' pattern<br> "+
                                "please ENSURE to enter input without space && node_id is UNIQUE! </html>");
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
                        tq.add(addText);
                        tq.add(okBut);
                        tq.setLocation(300,50);
                        j.add(tq);
                        j.setVisible(true);
                        j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        return;
                    }
                }
            });
            this.userTextIn = new JTextField(16);
            this.userTextIn.setPreferredSize(new Dimension(250, 50));
            addCom.add(addText);
            addCom.add(this.submitBut);
            addCom.add(this.userTextIn);
            addCom.pack();
            addCom.setVisible(true);
        }
        else if (e.getSource() == this.delEdge || e.getSource() == this.delNode){
            JFrame delCom = new JFrame();
            JLabel delText = new JLabel();
            if (e.getSource() == this.delNode){
                delCom.setTitle("delete node");
                delText.setText("<html> please input integer node_id <br>");
            }
            else { // e.getsource equal to this.delEdge
                delCom.setTitle("delete edge");
                delText.setText("<html>input pair of integer node_id for source-dest of edge<br> via pattern 'int,int'");
            }
            this.submitBut = new JButton("Submit");
            delCom.setDefaultCloseOperation(delCom.DISPOSE_ON_CLOSE);
            delCom.setLayout(new FlowLayout());
            delCom.setLocationRelativeTo(null);
            this.submitBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent delAc) {
                    try {
                        if (delAc.getSource() == submitBut){
                            String s = userTextIn.getText();
                            String[] str = s.split(",");
                            drawer.setColors(drawer.defNode, drawer.defEdge);
                            if (e.getSource() == delNode){
                                if (str.length > 1){ // delete node
                                    invalidInput();
                                }
                                algoGraph.getGraph().removeNode(Integer.parseInt(str[0]));
                                drawer.specialNodes = new ArrayList<>();
                                drawer.specialEdges = new HashMap<>();
                                algoGraph.init(algoGraph.getGraph());
                                drawer.updateDrawer(algoGraph);
                            }
                            else if (e.getSource() == delEdge){ // delete edge
                                if (str.length > 2){
                                    invalidInput();
                                }
                                drawer.specialNodes = new ArrayList<>();
                                drawer.specialEdges = new HashMap<>();
                                algoGraph.getGraph().removeEdge(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
                            }
                            getTopLevelAncestor().repaint();
                            delCom.setVisible(false);
                            delCom.dispose();
                        }
                    }
                    catch (Exception ex){
                        delCom.setVisible(false);
                        delCom.dispose();
                        delText.setText("<html> for delNode: please enter integer node_id via 'int' pattern <br>" +
                                "for delEdge: please enter pair of node_id as src->dest edge via 'int,int' pattern<br> "+
                                "please ENSURE to enter input without space </html>");
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
                        tq.add(delText);
                        tq.add(okBut);
                        tq.setLocation(300,50);
                        j.add(tq);
                        j.setVisible(true);
                        j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        return;
                    }


                }
            });
            this.userTextIn = new JTextField(16);
            this.userTextIn.setPreferredSize(new Dimension(250, 50));
            delCom.add(delText);
            delCom.add(this.submitBut);
            delCom.add(this.userTextIn);
            delCom.pack();
            delCom.setVisible(true);

        }
    }

    private void invalidInput() {
        drawer.specialNodes.get(-1); // create exception cuz invalid input
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


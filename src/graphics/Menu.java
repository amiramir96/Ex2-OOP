package graphics;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Menu extends JMenuBar implements ActionListener {

    DirectedWeightedGraphAlgorithms currGraph;
    JMenu menu, runAlgo;
    JMenuItem loadGraph, saveGraph, isConnected, center, shortestPathDist, shortestPath, tsp;
    JFileChooser fileChooser;

    public Menu(DirectedWeightedGraphAlgorithms g){
//        super();
        // init main objects
        this.currGraph = g;
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
        this.runAlgo = new JMenu("Run_Algorithm");

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
        if (e.getSource() == this.loadGraph){
           System.out.println(this.currGraph.getGraph());
           this.fileChooser.showOpenDialog(null);
           if (this.fileChooser.getSelectedFile() == null){
               return;
           }
           else {
               this.currGraph.load(this.fileChooser.getSelectedFile().getPath());
               System.out.println(this.fileChooser.getSelectedFile().getPath());
               System.out.println(this.currGraph.getGraph());
               System.out.println("loaded from: "+this.fileChooser.getSelectedFile().getPath());
//               repaint();

           }
        }
        else if (e.getSource() == this.saveGraph){
            this.fileChooser.showSaveDialog(null);
            if (this.fileChooser.getSelectedFile() == null){
                return;
            }
            else {
                this.currGraph.save(this.fileChooser.getSelectedFile().getPath());
                System.out.println("saved at: "+this.fileChooser.getSelectedFile().getPath());
            }
        }
        else if (e.getSource() == this.isConnected){
           System.out.println("connected");
        }
        else if (e.getSource() == this.shortestPath){
            System.out.println("shortestPath");
        }
        else if (e.getSource() == this.shortestPathDist){
            System.out.println("shortestPathDist");
        }
        else if (e.getSource() == this.center){
            System.out.println("center");
        }
        else if (e.getSource() == this.tsp){
            System.out.println("tsp");
        }
    }
}
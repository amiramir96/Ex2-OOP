package graphics;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DrawGraph extends JPanel  implements MouseListener, MouseMotionListener, MouseWheelListener{
    // init params
    private double widthArrow;
    private double heightArrow;
    private double widthPoint;
    private double heightPoint;
    DirectedWeightedGraphAlgorithms algoGraph;
    DirectedWeightedGraph currGraph;
    double[] min_max_cord; // idx: 0-minX, 1-minY, 2-maxX, 3-maxY
    double zoomInOut;

    // stroke and fonts
    final Stroke edgeStroke = new BasicStroke((float)1.5);
    final Stroke nodeStroke = new BasicStroke((float)5);
    final Font amirFont = new Font("a", Font.BOLD, 12);

    // for representing functions output (via colors)
    boolean flagAllSameColor;
    final Color defEdge, defNode; // default colors
    Color colorE, colorN; // special colors for events
    HashMap<String, Integer> specialEdges;
    List<NodeData> specialNodes;

    //save mouse points, help to make picture accurate to client presses
    private Point2D mousePoint;
    private Point2D mousePrevPos;
    private Point2D mouseNextPos;

    public DrawGraph(DirectedWeightedGraphAlgorithms al){
        // initialize the drawer parameters and vars
        this.algoGraph = al;
        this.currGraph = al.getGraph();
        this.min_max_cord = new double[4]; // idx: 0-minX, 1-minY, 2-maxX, 3-maxY
        this.zoomInOut = 1; // can be chaned later
        this.widthArrow = 4.0;
        this.heightArrow = 4.0;
        this.widthPoint = 5.0;
        this.heightPoint = 5.0;
        mousePoint = new Point(0,0);
        mousePrevPos = (Point2D)mousePoint.clone();
        mouseNextPos = (Point2D)mousePoint.clone();
        updateMinMax();

        // init colors block
        this.defNode = Color.BLACK; // black
        this.defEdge = new Color(0,100,200); // regular blue
        this.flagAllSameColor = false;
        this.specialNodes = new LinkedList<>();
        this.specialEdges = new HashMap<>();

        // as needed..
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    void updateDrawer(DirectedWeightedGraphAlgorithms newAlgo){
        // init params
        this.algoGraph = newAlgo;
        this.currGraph = newAlgo.getGraph();
        this.zoomInOut = 1;
        this.widthArrow = 4.0;
        this.heightArrow = 4.0;
        this.widthPoint = 5.0;
        this.heightPoint = 5.0;
        mousePoint = new Point(0,0);
        mousePrevPos = (Point2D)mousePoint.clone();
        mouseNextPos = (Point2D)mousePoint.clone();
        // init flag for coloring (zeroing)
        this.flagAllSameColor = false;
        // back to default
        updateMinMax();
    }

    void setColors(Color nodeNewColor, Color edgeNewColor){
        this.colorN = nodeNewColor;
        this.colorE = edgeNewColor;
    }

    void setFlagAllSameColor(boolean b){
        this.flagAllSameColor = b;
    }

    public void setZoom(double zoom){
        this.zoomInOut = zoom;
        this.widthArrow *= this.zoomInOut;
        this.heightArrow *= this.zoomInOut;
        this.widthPoint *= this.zoomInOut;
        this.heightPoint *= this.zoomInOut;
    }

    /**
     * credit to Shai Aharon teacher
     * this function ensure that the program will draw everything on a back image
     * after the prog is done to draw the curr image, its replace between cur and new images
     * @param g - graphics
     */
    public void paint(Graphics g) {
        // create new image
        Image bufferImage = createImage(750, 750);
        Graphics bufferGraphics = bufferImage.getGraphics();

        // draw at new image
        paintComponents(bufferGraphics);

        // "Switch" the old image for the new one
        g.drawImage(bufferImage, 0, 0, this);
    }

    /**
     * executing the draw process via neccessary params (colors etc..)
     * draw edges then nodes (image look better this way)
     * @param g - graphics of the curr drawer
     */
    public void paintComponents(Graphics g){
        Graphics2D graphic = (Graphics2D) g;

        // paint edges
        // init params
        graphic.setStroke(this.edgeStroke);
        double[] cordSrc, cordDest;
        EdgeData tempE;
        Iterator<EdgeData> itEdge = this.currGraph.edgeIter();
        while (itEdge.hasNext()){ // draw all edges
            // init edge
            tempE = itEdge.next();
            cordSrc = linearTransform(this.currGraph.getNode(tempE.getSrc()).getLocation());
            cordDest = linearTransform(this.currGraph.getNode(tempE.getDest()).getLocation());
            // init color
            if (this.flagAllSameColor || this.specialEdges.containsKey(""+tempE.getSrc()+","+tempE.getDest()) || this.specialEdges.containsKey(""+tempE.getDest()+","+tempE.getSrc())){
                System.out.println(tempE);
                if (this.flagAllSameColor || this.specialEdges.containsKey(""+tempE.getSrc()+","+tempE.getDest())){
                    graphic.setColor(this.colorE);
                    drawArrow(graphic, cordSrc[0], cordSrc[1], cordDest[0], cordDest[1]); // draw arrow (edge)
                }
            }
            else{ // default color
                System.out.println(tempE);
                graphic.setColor(this.defEdge);
                drawArrow(graphic, cordSrc[0], cordSrc[1], cordDest[0], cordDest[1]); // draw arrow (edge)
            }
        }

        // paint nodes
        graphic.setStroke(this.nodeStroke);
        Iterator<NodeData> itNode = this.currGraph.nodeIter();
        double[] cord;
        NodeData tempN;
        graphic.setFont(amirFont);
        while (itNode.hasNext()) { // draw all nodes
            // init node
            tempN = itNode.next();
            cord = linearTransform(tempN.getLocation()); // linear transfer regular cord to width/height cord
            // init color
            if (this.flagAllSameColor || this.specialNodes.contains(tempN)){
                graphic.setColor(this.colorN);
            }
            else {
                graphic.setColor(this.defNode);
            }
            // draw curr node
            graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint*zoomInOut, this.heightPoint*zoomInOut));
            graphic.drawString(""+tempN.getKey(), (int)cord[0], (int)cord[1]);
        }
    }

    /**
     * draw arrow between src node cords to dest node cords
     * credit for algebric formulas to https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
     * in general the func do:
     * calculate from src,dest points a 3 points that represent a arrowHead which shall be drawen as polygon
     * (one of the points is destNode cords which is the headArrowPeak , and the other two is the base triangular nodes)
     * @param g - graphics obj
     * @param xSrc - x cord of src node
     * @param ySrc - y cord of src node
     * @param xDest - x cord of dest node
     * @param yDest - y cord of dest node
     */
    private void drawArrow(Graphics g, double xSrc, double ySrc, double xDest, double yDest) {
        // init vars via formulas
        double xHeadArrow1, xHeadArrow2, yHeadArrow1, yHeadArrow2; // which combine to 2 extra points that create triangular for the arrow head
        double delX = (xDest - xSrc), delY = (yDest - ySrc); // cal delta x,y
        double distBetNodes = Math.sqrt(delX*delX + delY*delY); // cal distance src->dest nodes
        double sinVal = delY / distBetNodes , cosVal = delX / distBetNodes; // const math via algebra, basic rules of sin/cos

        yHeadArrow1 = (distBetNodes - this.widthArrow)*sinVal + this.heightArrow*cosVal + ySrc; // via formula
        xHeadArrow1 = (distBetNodes - this.widthArrow)*cosVal - this.heightArrow*sinVal + xSrc; // via formula

        yHeadArrow2 = (distBetNodes - this.widthArrow)*sinVal + -1*(this.heightArrow)*cosVal + ySrc; // via formula
        xHeadArrow2 = (distBetNodes - this.widthArrow)*cosVal - -1*(this.heightArrow)*sinVal + xSrc; // via formula

        // arrays for x,y cordinates to draw the polygon
        int[] xpoints = {(int)(xDest), (int) (xHeadArrow1), (int) (xHeadArrow2)};
        int[] ypoints = {(int)(yDest), (int) (yHeadArrow1), (int) (yHeadArrow2)};
        // draw arrow line
        g.drawLine((int)(xSrc), (int)(ySrc), (int)(xDest), (int)(yDest));
        // draw arrow head
        g.drawPolygon(xpoints, ypoints, 3);
    }

    /**
     * save an array of min/max x,y cords in array size 4
     * idx: 0-minX, 1-minY, 2-maxX, 3-maxY
     */
    private void updateMinMax() {
        Iterator<NodeData> it = this.currGraph.nodeIter();
        NodeData tempN;
        double minX = 0, minY = 0, maxX = 0, maxY = 0;
        if (it.hasNext()) { // save first node for indicate
            tempN = it.next();
            minX = tempN.getLocation().x();
            maxX = tempN.getLocation().x();
            minY = tempN.getLocation().y();
            maxY = tempN.getLocation().y();
        }
        while (it.hasNext()) { // for every node from now on, check min/max params
            tempN = it.next();
            if (tempN.getLocation().x() < minX) {
                minX = tempN.getLocation().x();
            }
            else if (tempN.getLocation().x() > maxX) {
                maxX = tempN.getLocation().x();
            }
            if (tempN.getLocation().y() < minY) {
                minY = tempN.getLocation().y();
            }
            else if (tempN.getLocation().y() > maxY) {
                maxY = tempN.getLocation().y();
            }
        }
        this.min_max_cord[0] = minX;
        this.min_max_cord[1] = minY;
        this.min_max_cord[2] = maxX;
        this.min_max_cord[3] = maxY;
    }

    /**
     * transform point of GEOLOCATION type to x,y cordinates that match the picture
     * @param point - var obj of point
     * @return - x,y cordinates that match the picture
     */
    double[] linearTransform(GeoLocation point){ // credit to daniel rosenberg, student of our class which showed us the formula in the internet
        double x = (((this.min_max_cord[2] - point.x()) / (this.min_max_cord[2] - this.min_max_cord[0]))*700*0.8+700*0.1 + mousePoint.getX())*zoomInOut;
        double y = (((this.min_max_cord[3] - point.y()) / (this.min_max_cord[3] - this.min_max_cord[1]))*700*0.8+700*0.1 + mousePoint.getY())*zoomInOut;
        return new double[]{x,y};
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseNextPos = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePrevPos = (Point2D)mousePoint.clone();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("entered");

    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("exited");

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePoint.setLocation(mousePrevPos.getX() + (e.getX() - mouseNextPos.getX())/this.zoomInOut , mousePrevPos.getY() + (e.getY() - mouseNextPos.getY())/this.zoomInOut);
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.zoomInOut = this.zoomInOut + (double)(-e.getWheelRotation()) / 7;
        repaint();
    }
}
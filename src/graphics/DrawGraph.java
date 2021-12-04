package graphics;

import api.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;

public class DrawGraph implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private final double widthArrow, heightArrow, widthPoint, heightPoint;
    DirectedWeightedGraphAlgorithms algoGraph;
    DirectedWeightedGraph currGraph;
    double[] min_max_cord; // idx: 0-minX, 1-minY, 2-maxX, 3-maxY
    double zoomInOut;

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
        updateMinMax();
    }

    public void paint(Graphics g){
        Graphics2D graphic = (Graphics2D) g;
//        NodeData n = this.currGraph.getNode(0);
//        Ellipse2D EN = new Ellipse2D.Double(50, 50, 10, 10);
//        Ellipse2D ne = new Ellipse2D.Float((float)200.2, (float)200.8, 20, 20);
//        graphic.draw(ne);
//        graphic.draw(EN);

        graphic.setColor(new Color(0,150,0));
//        graphic.setBackground(new Color(0, 150, 0));
        graphic.setStroke(new BasicStroke((float) 1.5));
        double[] cordSrc, cordDest, cordArrowHead;
        EdgeData tempE;
        Iterator<EdgeData> itEdge = this.currGraph.edgeIter();
        Polygon tempPoly;
        while (itEdge.hasNext()){
            tempE = itEdge.next();
            cordSrc = linearTransform(this.currGraph.getNode(tempE.getSrc()).getLocation());
            cordDest = linearTransform(this.currGraph.getNode(tempE.getDest()).getLocation());
            drawArrow(graphic, cordSrc[0], cordSrc[1], cordDest[0], cordDest[1]);
//            cordArrowHead = arrowHead(cordSrc, cordDest);
//            graphic.drawLine((int)(cordSrc[0]*zoomInOut), (int)(cordSrc[1]*zoomInOut), (int)(cordDest[0]*zoomInOut), (int)(cordDest[1]*zoomInOut));
//            graphic.drawPolygon(new int[]{(int)(cordDest[0]*zoomInOut), (int)(cordArrowHead[0]*zoomInOut), (int)(cordArrowHead[2]*zoomInOut)},
//                    new int[]{(int)(cordDest[1]*zoomInOut), (int)(cordArrowHead[1]*zoomInOut), (int)(cordArrowHead[3]*zoomInOut)}, 3);
        }

        graphic.setColor(Color.BLACK);
        graphic.setStroke(new BasicStroke((float) 5));
        Iterator<NodeData> itNode = this.currGraph.nodeIter();
        double[] cord;
        NodeData tempN;
        Font amirFont = new Font("a", Font.BOLD, 12);
        graphic.setFont(amirFont);
        while (itNode.hasNext()) {
            tempN = itNode.next();
            cord = linearTransform(tempN.getLocation());
            graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint*zoomInOut, this.heightPoint*zoomInOut));
//            System.out.println(cord[0]+", "+cord[1]);
            graphic.drawString(""+tempN.getKey(), (int)cord[0], (int)cord[1]);
        }

////        graphic.setBackground(new Color(0, 0, 0));
//        graphic.setStroke(new BasicStroke((float) 10));
//

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
        int[] xpoints = {(int)xDest, (int) xHeadArrow1, (int) xHeadArrow2};
        int[] ypoints = {(int)yDest, (int) yHeadArrow1, (int) yHeadArrow2};
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
        if (it.hasNext()) {
            tempN = it.next();
            minX = tempN.getLocation().x();
            maxX = tempN.getLocation().x();
            minY = tempN.getLocation().y();
            maxY = tempN.getLocation().y();
        }
        while (it.hasNext()) {
            tempN = it.next();
            if (tempN.getLocation().x() < minX) {
                minX = tempN.getLocation().x();
            }
            if (tempN.getLocation().y() < minY) {
                minY = tempN.getLocation().y();
            }
            if (tempN.getLocation().x() > maxX) {
                maxX = tempN.getLocation().x();
            }
            if (tempN.getLocation().y() > maxY) {
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
        double x = (((this.min_max_cord[2] - point.x()) / (this.min_max_cord[2] - this.min_max_cord[0]))*700*0.85+700*0.15)*zoomInOut;
        double y = (((this.min_max_cord[3] - point.y()) / (this.min_max_cord[3] - this.min_max_cord[1]))*700*0.85+700*0.15)*zoomInOut;
        return new double[]{x,y};
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }
}

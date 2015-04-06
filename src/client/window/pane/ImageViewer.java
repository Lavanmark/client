package client.window.pane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import shared.model.Field;
import client.backend.BatchState;
import client.backend.BatchStateListener;
import client.backend.Cell;
import client.backend.facade.ClientFacade;

@SuppressWarnings("serial")
public class ImageViewer extends JPanel implements BatchStateListener{
	
	private boolean highlights;
	private boolean inverted;
	private boolean dragging;
	
	private BufferedImage batchImg;
	private Rectangle2D[][] imgRect;
	
	private double zoomLevel;
	
	private int w_originX;
	private int w_originY;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartOriginX;
	private int w_dragStartOriginY;
	
	
	
	
	
	public ImageViewer(){
		super();
		this.setPreferredSize(new Dimension(500,500));
		
		setHighlights(true);
		setInverted(false);
		
		w_originX = 0;
		w_originY = 0;
		initDrag();
		
		batchImg = null;
		imgRect = null;
		setZoomLevel(1.0);
		
		this.setBackground(Color.GRAY);
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		
	}
	
	public void loadBatch(){
		if(BatchState.getBatch() != null){
			try {
				batchImg = ImageIO.read(new URL( ClientFacade.getURLPrefix() + "/" + BatchState.getBatch().getImgFile()));
				batchImg.getScaledInstance(batchImg.getWidth(null) / 2, batchImg.getHeight(null) / 2, Image.SCALE_DEFAULT);
			} catch (IOException e) {
				e.printStackTrace();
			}
					
			imgRect = new Rectangle[BatchState.getRows()][BatchState.getColumns()];
			
			int y = BatchState.getProject().getFirstYCoord();
			for(int f = 0; f < BatchState.getColumns(); f++){
				Field cur = BatchState.getField(f);
				for(int r = 0; r < BatchState.getRows(); r++){
					imgRect[r][f] = new Rectangle(cur.getxCoord(), y, cur.getWidth(), BatchState.getProject().getRecordHeight());
					y += BatchState.getProject().getRecordHeight();
				}
				y = BatchState.getProject().getFirstYCoord();
			}
			//TODO finish setting this crap up
			//build a class for the highlights and jazz..
			
			repaint();
		}
	}
	
	public void reset(){
		w_originX = 0;
		w_originY = 0;
		initDrag();
		
		batchImg = null;
		imgRect = null;
		setZoomLevel(1.0);
		repaint();
	}
	
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartOriginX = 0;
		w_dragStartOriginY = 0;
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth()/2,getHeight()/2);
			transform.scale(getZoomLevel(), getZoomLevel());
			transform.translate(-w_originX, -w_originY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
		
			initDrag();
			dragging = true;
			w_dragStartX = w_X;
			w_dragStartY = w_Y;		
			w_dragStartOriginX = w_originX;
			w_dragStartOriginY = w_originY;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth()/2,getHeight()/2);
			transform.scale(getZoomLevel(), getZoomLevel());
			transform.translate(-w_originX, -w_originY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			if(BatchState.getBatch() != null){
				if(w_Y > BatchState.getProject().getFirstYCoord()){
					int row = ((w_Y-BatchState.getProject().getFirstYCoord())/BatchState.getProject().getRecordHeight());
					if(row < BatchState.getProject().getRecordsPerImage()){
						for(int i = 0; i < imgRect[row].length; i++){
							//TODO change this so it calculates the i not going through all of it.
							if(imgRect[row][i].contains(new Point(w_X,w_Y))){
								BatchState.setSelectedCell(new Cell(row,i));
								return;
							}
						}
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				AffineTransform transform = new AffineTransform();
				transform.translate(getWidth()/2,getHeight()/2);
				transform.scale(getZoomLevel(), getZoomLevel());
				transform.translate(-w_dragStartOriginX, -w_dragStartOriginY);
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					transform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				w_originX = w_dragStartOriginX - w_deltaX;
				w_originY = w_dragStartOriginY - w_deltaY;
				
				//notifyOriginChanged(w_originX, w_originY);
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e){
			if(e.getWheelRotation() < 0){
				zoomIn();
			}else{
				zoomOut();
			}
		}
	};
	
	
	
	public void invertImage(){
		inverted = (!inverted);
		if(batchImg != null){
			RescaleOp op = new RescaleOp(-1.0f, 255f, null);
			batchImg = op.filter(batchImg, null); 
			repaint();
		}
	}
	
	public void zoomIn(){
		setZoomLevel(getZoomLevel() + .1);
		repaint();
	}
	
	public void zoomOut(){
		setZoomLevel(getZoomLevel() - .1);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(batchImg != null){
			Graphics2D g2d = (Graphics2D)g;
			
			g2d.translate(this.getWidth()/2, this.getHeight()/2);
			g2d.scale(getZoomLevel(), getZoomLevel());
			g2d.translate(-w_originX, -w_originY);
			//TODO make it go to the middle
			g2d.drawImage(batchImg, 0, 0, batchImg.getWidth(null), batchImg.getHeight(null), null);
			if(isHighlights())
				drawHighlight(g2d);
		}
	}
	
	public void drawHighlight(Graphics2D g2d){
		Cell cur = BatchState.getSelectedCell();
		if(cur != null){
			Color myc = new Color(0,128,255,128);
			g2d.setColor(myc);
			if(cur.column == -1)
				g2d.fill(imgRect[cur.row][cur.column+1]);
			else
				g2d.fill(imgRect[cur.row][cur.column]);
		}
	}
	
	public void toggleHighlights(){
		setHighlights((!isHighlights()));
		repaint();
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public boolean isHighlights() {
		return highlights;
	}

	public void setHighlights(boolean highlights) {
		this.highlights = highlights;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	@Override
	public void valueChanged(Cell cell, String newValue) {
		//do nothing
	}

	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		repaint();
	}

	@Override
	public void tableChanged(String[][] table, Cell selectedCell) {
		repaint();
	}
}

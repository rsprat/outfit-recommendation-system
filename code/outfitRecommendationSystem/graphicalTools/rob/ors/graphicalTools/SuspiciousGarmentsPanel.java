package rob.ors.graphicalTools;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.Logger;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hibernate.ScrollableResults;

import rob.ors.core.config.Paths;
import rob.ors.core.model.api.Category;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.DBConnection;
import rob.ors.core.utils.CvMatUtils;
import rob.ors.core.utils.ImagePrinter;
import rob.ors.core.utils.Utils;
import rob.ors.garmentssimilarity.imageProcessing.BackgroundExtractor;
import rob.ors.garmentssimilarity.imageProcessing.GarmentProportionsExtractor;
import static com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class SuspiciousGarmentsPanel extends JPanel implements ComponentListener, ChangeListener,ActionListener{
	private static Logger LOGGER = Logger.getLogger(SuspiciousGarmentsPanel.class.getCanonicalName());
	private static final String IMAGES_SRC_PATH = Paths.BIG_IMAGES_FOLDER;
	private static final String BLACKLIST_FILE = Paths.garmentS_BLACK_LIST_FILE;
	protected int[] garments;
	protected Map<Integer, ImageIcon> garmentImageMap;
	protected JPanel concreteGarmentsPanel;
	protected JScrollPane scroller;
	protected static Image blackImage;
	private static int garmentS_PER_ROW = 6;
	protected InspectionThread worker;	
	static{
		try {
			
			blackImage =  ImageIO.read(new File(Paths.BIG_IMAGES_FOLDER+"black.jpg"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

			

	public SuspiciousGarmentsPanel()
	{	
		garmentImageMap = new HashMap<Integer, ImageIcon>();
		garments = new int[0];
		worker = new InspectionThread(10){

			@Override
			public void push()
			{
				 workerPush();				
			}
		};
		Thread t = new Thread(worker);
		t.start();
		
		concreteGarmentsPanel = new JPanel();		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		scroller = new JScrollPane(concreteGarmentsPanel);  
		scroller.getHorizontalScrollBar().setUnitIncrement(100);
		scroller.getVerticalScrollBar().setUnitIncrement(100);
		add(scroller);
	}
	
	protected void workerPush()
	{
		int[] newGarments = worker.pull();
		updateGarmentsPanel(newGarments);
	}
	public void removeGarment(int iid)
	{
		storeGarmentIdInFile(iid);
		int[] aux = Arrays.copyOf(garments,garments.length);
		boolean found = false;
		int i;
		for(i=0;i<garments.length;i++)if(garments[i]==iid){ found=true; break;}
		if(found)
		{					
			int[] aux2 = Arrays.copyOf(aux, i);		
			aux2 = concatenate(aux2,	Arrays.copyOfRange(aux,i+1,aux.length));
			garments=aux2;
			updateGarmentsPanel(new int[0]);
		}			
	}
	
	private void storeGarmentIdInFile(int iid)
	{
		try
		{
			FileWriter fw = new FileWriter(BLACKLIST_FILE,true);
			fw.write(iid+System.getProperty("line.separator"));
			fw.close();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private int[] concatenate (int[] a, int[] b)
	{
		int[] c = new int[a.length+b.length];
		for(int i=0; i<a.length;i++)
		{
			c[i]=a[i];
		}
		for(int i=0;i<b.length;i++)
		{
			c[i+a.length]=b[i];
		}
		return c;
	}
	
	public void updateGarmentsPanel(int[] newGarments)
	{
		garments= concatenate(garments,newGarments);
		int numGarments = garments.length;
		int numRows;
		int numCols;
		if(numGarments<garmentS_PER_ROW)
		{
			numRows = 1;
			numCols = numGarments;
		}
		else
		{
			numRows = numGarments/garmentS_PER_ROW;
			numCols = garmentS_PER_ROW;
			if(numGarments%garmentS_PER_ROW!=0)numRows+=1;
		}
		concreteGarmentsPanel.setLayout(new GridLayout(numRows,numCols));
		loadGarmentsImages(newGarments);
		concreteGarmentsPanel.removeAll();
		for(final int garmentId : garments)
		{			
			JPanel comp = new JPanel();
			comp.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					removeGarment(garmentId);
					
				}
			});
			comp.setBorder(BorderFactory.createTitledBorder("garment: "+garmentId));			
			ConcreteGarment garment = null;
			JLabel imageLabel = new JLabel("",garmentImageMap.get(garmentId),SwingConstants.CENTER);
			imageLabel.setFont(new Font("default", Font.PLAIN, 10));
			comp.add(imageLabel);
			concreteGarmentsPanel.add(comp);
		}
	     revalidate();
	     repaint();
		
	}
		
	protected void loadGarmentsImages(int[] garments)
	{		
		
		for(int garment: garments)
		{		
			BufferedImage garmentImage = loadImage(garment);
			ImageIcon icon = new ImageIcon( garmentImage.getScaledInstance(-1, 75,  java.awt.Image.SCALE_SMOOTH) );
			garmentImageMap.put(garment, icon);
		}
	}
	

	
	protected BufferedImage loadImage(int garmentId)
	{
		BufferedImage myPicture = (BufferedImage) blackImage;
    	try
		{
    	
	    	myPicture = ImageIO.read(new File(Paths.BIG_IMAGES_FOLDER+garmentId+".jpg"));
			
		} catch (IOException e)
		{
		}    	
    	return myPicture;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stubA
		
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		scroller.setPreferredSize(getSize());
	    revalidate();
	    repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		scroller.setPreferredSize(getSize());
	    revalidate();
	    repaint();		
	}
		
	@Override
	public void stateChanged(ChangeEvent arg0)
	{
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	
		//e.getSource().equals(categoryComboBox)
	}
	
	
	
	
	private abstract class InspectionThread implements Runnable
	{
		private int[] garments;
		private int numGarments;
		private int maxGarments;
		private int[] blackListedGarments; //garments that are already in the black list, they shouldn't be checked
		
		
		private int[] readBlackListFile()
		{
			try
			{
				//Count the number of garments in the file (a little bit "cutre" but does the job
			    BufferedReader in = new BufferedReader(new FileReader(BLACKLIST_FILE));			    
			    String str;
			    int numLines = 0;
			    while ((str = in.readLine()) != null) numLines++;		
			    in.close();
			    
			    int[] garments = new int[numLines];
			    in = new BufferedReader(new FileReader(BLACKLIST_FILE));	
			    int i = 0;
			    while ((str = in.readLine()) != null){
			    	 try{garments[i++]=Integer.parseInt(str);}catch(Exception ex){LOGGER.error("ERROR PARSING INT "+str,ex);}}
			    in.close();
			    Arrays.sort(garments);
			    return garments;
			    
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new int[0];
		}
		
		public InspectionThread(int garmentsLimit)
		{
			maxGarments = garmentsLimit;
			numGarments = 0;
			garments = new int[maxGarments];
			//blackListedGarments=readBlackListFile();
			blackListedGarments=new int[1];
	
		}
		public abstract void push();
		
		public int[] pull()
		{
			int[] aux = Arrays.copyOf(garments, numGarments);
			numGarments = 0;
			garments = new int[maxGarments];
			return aux;
		}
		
		public boolean isSuspicious(int iid)
		{
			if(!Utils.hasImage(iid)) return false;
			System.out.println(iid);
			IplImage anGarmentImageAux = Utils.loadImage(iid);
			IplImage anGarmentMask = null;
			boolean suspicious = false;
			try
			{
				anGarmentMask = BackgroundExtractor.colorMaskIpl(anGarmentImageAux.clone());
				GarmentProportionsExtractor extr = new GarmentProportionsExtractor() {
					
					@Override
					public void printProportions(ImagePrinter printer, CvMat mask) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public Map<Integer, Integer> getProportions(CvMat mask) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Set<Category> getAplicableCategories() {
						// TODO Auto-generated method stub
						return null;
					}
				};
				IplROI initialRoi = anGarmentMask.roi();
				int anGarmentTop = GarmentProportionsExtractor.getTop(anGarmentMask.asCvMat());
				int anGarmentHeight = Math.abs(GarmentProportionsExtractor.getBottom(anGarmentMask.asCvMat())-anGarmentTop);		
				
				float maxWidth = Float.MIN_VALUE;
				float minWidth = Float.MAX_VALUE;
						for(int i = 0; i<100; i++)
				{
					
					float widthAtPoint = GarmentProportionsExtractor.widthAtHeigth(anGarmentMask, i, anGarmentTop, anGarmentHeight,anGarmentMask.asCvMat().rows(),anGarmentMask.asCvMat().cols());			
				
					if(widthAtPoint<minWidth)minWidth=widthAtPoint;
					if(widthAtPoint>maxWidth)maxWidth=widthAtPoint;
				}			
				suspicious = (((maxWidth-minWidth)/(maxWidth+minWidth))<(0.3f));
				
				if(!suspicious)
				{
					 anGarmentMask.roi(initialRoi);
					float nonzero = (float)cvCountNonZero(anGarmentMask);				
					nonzero = (nonzero/(anGarmentMask.width()*anGarmentMask.height()));
					if(nonzero>0.8 || nonzero<0.2f)suspicious = true;
				}
				
			}catch(Exception e){}
			finally{			
				anGarmentMask.release();
				anGarmentImageAux.release();			
			}
			
			return suspicious;
		
			
		}
		
		@Override
		public void run()
		{
			DBConnection.transaction();
			BigInteger total = (BigInteger) DBConnection.session().createSQLQuery("SELECT count(*) from garments").list().get(0);
			ScrollableResults garmentCursor =DBConnection.session().createSQLQuery("SELECT id from garments").scroll();
			
			int count=0;
			while ( garmentCursor.next() )
			{
				while(numGarments==maxGarments)
				{
					push();
				}
				Integer iid = (Integer)garmentCursor.get(0);
			    
				if(Arrays.binarySearch(blackListedGarments, iid)<0 && isSuspicious(iid))
				{
					garments[numGarments]=iid;
					numGarments++;
				}
			    if ( ++count % 100 == 0 )
			    {
			    	LOGGER.info(count+"/"+total);
			    }
			    
			}
		}
	}
	
}




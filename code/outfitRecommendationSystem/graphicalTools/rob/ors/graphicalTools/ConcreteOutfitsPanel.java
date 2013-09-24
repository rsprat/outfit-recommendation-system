package rob.ors.graphicalTools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import rob.ors.core.config.Paths;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.ConcreteOutfit;

class ConcreteOutfitsPanel extends JPanel implements ComponentListener
{
	private ConcreteOutfit outfit;
	private static Image blackImage;
	private Map<JLabel,BufferedImage> imagesMap = new HashMap<JLabel,BufferedImage>();
	static{
		try {
			
			blackImage =  ImageIO.read(new File(Paths.BIG_IMAGES_FOLDER+"black.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ConcreteOutfitsPanel(ConcreteOutfit outfit) 
	{
		
		super(new GridLayout(1, outfit.getGarments().size()));
		setBackground(Color.lightGray);
	    setBorder(BorderFactory.createTitledBorder("Set: "+outfit.getId()));
	    
	    int imageH =150;
	    
	    List<ConcreteGarment> garments = new LinkedList<ConcreteGarment>(outfit.getGarments());
	    
	    Collections.sort(garments,new Comparator<ConcreteGarment>(){

			@Override
			public int compare(ConcreteGarment arg0, ConcreteGarment arg1) {
				return arg0.getCategory().getId()- arg1.getCategory().getId();
			}
	    	
	    });
	    
	    
	    for(ConcreteGarment garment : garments)
	    {
	    	
	    	BufferedImage garmentImage = loadImage(garment);
			ImageIcon icon = new ImageIcon( garmentImage.getScaledInstance(-1, imageH,  java.awt.Image.SCALE_SMOOTH) );
			JLabel imageLabel = new JLabel(""+garment.getId(),icon,SwingConstants.CENTER);
			imagesMap.put(imageLabel, garmentImage);
			add(imageLabel);
	    }
	    setPreferredSize(new Dimension((imageH+10)*outfit.getGarments().size(),imageH+60));
	}
	
	public BufferedImage loadImage(ConcreteGarment garment)
	{
		BufferedImage myPicture = (BufferedImage) blackImage;
    	try
		{
	    	if(garment!=null)
	    	{
	    		myPicture = ImageIO.read(new File(Paths.BIG_IMAGES_FOLDER+garment.getId()+".jpg"));
			}
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
		// TODO Auto-generated method stub
	}

	@Override
	public void componentResized(ComponentEvent e) {

		for(Component c: getComponents())
		{
			if(c.getClass().equals(JLabel.class))
			{
				JLabel label = (JLabel)c;
				label.setIcon(new ImageIcon(imagesMap.get(label).getScaledInstance(-1, (int)(getHeight()*0.8), java.awt.Image.SCALE_SMOOTH)));
			}
		}
	    revalidate();
	    repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}

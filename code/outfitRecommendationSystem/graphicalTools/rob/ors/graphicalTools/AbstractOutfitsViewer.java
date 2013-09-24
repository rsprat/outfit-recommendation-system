package rob.ors.graphicalTools;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import rob.ors.core.model.api.AbstractOutfit;
import rob.ors.core.model.api.DBConnection;


public class AbstractOutfitsViewer extends JPanel implements ActionListener, ComponentListener {
	private static Logger LOGGER = Logger.getLogger(AbstractOutfitsViewer.class.getCanonicalName());
	private JComboBox abstractOutfitComboBox;
	private AbstractOutfitsPanel abstractOutfitPanel;
	public AbstractOutfitsViewer()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		DBConnection.transaction();
		List<AbstractOutfit> aoutfits = DBConnection.session().createQuery("from AbstractOutfitI").list();
		LOGGER.info(""+aoutfits.size());
		Collections.sort(aoutfits, new Comparator<AbstractOutfit>(){

			@Override
			public int compare(AbstractOutfit arg0, AbstractOutfit arg1) {
				return arg0.getGarments().size() - arg1.getGarments().size();
		
			}			
		});
	
		String[] ids = new String[aoutfits.size()];
		for(int i=0;i<aoutfits.size();i++) ids[i] = aoutfits.get(i).getId()+" #:"+aoutfits.get(i).getOutfits().size()+" itms:"+aoutfits.get(i).getOutfits().iterator().next().getGarments().size();
		abstractOutfitComboBox = new JComboBox(ids);

		abstractOutfitComboBox.setMaximumSize(new Dimension(300,30));
		abstractOutfitComboBox.addActionListener(this);
		add(abstractOutfitComboBox);
		addComponentListener(this);


	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		 JComboBox cb = (JComboBox)e.getSource();
	     Integer setId = new Integer(((String)cb.getSelectedItem()).split(" ")[0]);
	     DBConnection.transaction();
	     if(abstractOutfitPanel!=null)remove(abstractOutfitPanel);
	     removeComponentListener(abstractOutfitPanel);

	     abstractOutfitPanel=new AbstractOutfitsPanel( (AbstractOutfit) DBConnection.session().createQuery("from AbstractOutfitI where id="+setId).list().get(0));
	     abstractOutfitPanel.setPreferredSize(getSize());
	     addComponentListener(abstractOutfitPanel);
	    
	     add(abstractOutfitPanel);
	    // abstractOutfitPanel.componentResized(null);

	     revalidate();
	     repaint();

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
		if(abstractOutfitPanel!=null)
		{
			 abstractOutfitPanel.setPreferredSize(getSize());
		     revalidate();
		     repaint();
			
		}

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}

package application;



import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Portepion extends ImageView{
	
	private static Image rouge = new Image("prp6.png");
	private static Image noir = new Image("pnp6.png");
    
	

	//vide = 0 ; rouge = 1 ; noir = 2; ;ce sont les status .... 
	private int status;
	
	public Portepion(){
		this.status = 0;
	}
	
	public void set(int j){
		if(j ==1)
			
			this.setImage(rouge);
		if(j==2)
			this.setImage(noir);

		this.status = j;
	}
	
	
	public int getStatus(){
		return status;
	}
	
}   
 

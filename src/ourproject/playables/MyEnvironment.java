package ourproject.playables;

import java.util.Random;

import mark.geometry.Vector2D;

import com.andrapp.spaceshutter.Constants;

public class MyEnvironment {

	public Player myObject;		//my player
	public Player otherObject;	//other player
	
	
	public Monster[] enemy;	//monsters
	
	public int numOfMonsters=3;

	public PlayPath myPath;			//path of my player
	public PlayPath otherPath;			//path of other player
	
	public PlayPath chasingPath;

	public PlayPolygon myPoly;			//boundaries polygon
	
	public MyEnvironment(){
		
		myObject=new Player((short)(Constants.MARGIN_PADDING+10),(short)Constants.MARGIN_PADDING,0);

		otherObject=new Player(Constants.MARGIN_PADDING,Constants.MARGIN_PADDING,1);
		
		enemy=new Monster[numOfMonsters];
		
		Random randomGenerator = new Random();
		
		for(int i=0;i<numOfMonsters;i++){
			enemy[i]=new Monster(
				(short)(40+i*30),
				(short)(40+i*30)
			);
			
			short aa = (short)(randomGenerator.nextInt(100)-100);
			short bb = (short)(randomGenerator.nextInt(100)-100);
			
			if(Math.abs(aa)<2) aa=50;
			if(Math.abs(bb)<2) bb=-50;
			
			enemy[i].setOrientation(new Vector2D.Short(aa,bb));
			
		}
		

		
		myPath=new PlayPath(0);
		otherPath=new PlayPath(1);
		
		chasingPath=new PlayPath(2);
		
		myPoly=new PlayPolygon();


		myPoly.start(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
		
	}
	
	

}

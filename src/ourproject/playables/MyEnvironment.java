package ourproject.playables;

import java.util.Random;

import mark.geometry.Vector2D;

import com.andrapp.spaceshutter.Constants;

public class MyEnvironment {

	public Player myObject;		//my player
	public Player otherObject;	//other player
	
	
	public Monster[] enemy;	//monsters
	
	public short numOfMonsters=10;

	public PlayPath myPath;				//path of my player
	public PlayPath otherPath;			//path of other player
	
	public PlayPath chasingPath;
	public Vector2D.Short chasingPathRest=null;

	public PlayPolygon myPoly;			//boundaries polygon
	
	public MyEnvironment(){
		
		myObject=new Player((short)(Constants.MARGIN_PADDING+10),(short)Constants.MARGIN_PADDING,0);

		otherObject=new Player(Constants.MARGIN_PADDING,Constants.MARGIN_PADDING,1);
		
		enemy=new Monster[numOfMonsters];
		
		 Random randomGenerator = new Random();
		
		for(int i=0;i<numOfMonsters;i++){
			enemy[i]=new Monster(
				(short)(100),
				(short)(100)
			);
			
			int xx=(short)(randomGenerator.nextInt(100)-40);
			int yy=(short)(randomGenerator.nextInt(100)-60);
			
			Vector2D.Short veeecc = new Vector2D.Short((short)(xx),(short)(yy));
			
			veeecc.setLength(4);
			
			enemy[i].setOrientation(veeecc);

			
		}
		

		
		myPath=new PlayPath(0);
		otherPath=new PlayPath(1);
		
		chasingPath=new PlayPath(2);
		
		myPoly=new PlayPolygon();


		myPoly.start((short)(Constants.MARGIN_PADDING),(short)(Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING),(short)(Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING),(short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.MARGIN_PADDING),(short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.MARGIN_PADDING), (short)(Constants.MARGIN_PADDING));
		
	}
	
	

}

package ourproject.playables;

import java.util.Random;

import mark.geometry.Vector2D;

import com.andrapp.spaceshutter.Constants;

public class MyEnvironment {

	public Player myObject;		//my player
	public Player otherObject;	//other player
	
	
	public Monster[] enemy;	//monsters
	
	public short numOfMonsters=1;

	public PlayPath myPath;				//path of my player
	public PlayPath otherPath;			//path of other player
	
	public PlayPath chasingPath;
	public Vector2D.Short chasingPathRest=null;

	public PlayPolygon myPoly;			//boundaries polygon
	
	public MyEnvironment(){
		
		myObject=new Player((short)(Constants.MARGIN_PADDING+10),(short)Constants.MARGIN_PADDING,0);

		otherObject=new Player(Constants.MARGIN_PADDING,Constants.MARGIN_PADDING,1);
		
		enemy=new Monster[numOfMonsters];
		
		for(int i=0;i<numOfMonsters;i++){
			enemy[i]=new Monster(
				(short)(100),
				(short)(40)
			);
			
			
			if(i==0)
				enemy[i].setOrientation(new Vector2D.Short((short)2,(short)2));
			if(i==1)
				enemy[i].setOrientation(new Vector2D.Short((short)2,(short)-2));
			if(i==2)
				enemy[i].setOrientation(new Vector2D.Short((short)-2,(short)-2));
			if(i==3)
				enemy[i].setOrientation(new Vector2D.Short((short)-2,(short)2));
			
		}
		

		
		myPath=new PlayPath(0);
		otherPath=new PlayPath(1);
		
		chasingPath=new PlayPath(2);
		
		myPoly=new PlayPolygon();


		myPoly.start((short)10,(short)10);
		
		myPoly.proceed((short)10, (short)(190));
		
		myPoly.proceed((short)(190), (short)(190));
		
		myPoly.proceed((short)(190), (short)10);
		
		myPoly.proceed((short)10, (short)10);
		
	}
	
	

}

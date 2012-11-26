package com.andrapp.spaceshutter.model;

import java.util.LinkedList;

import com.andrapp.spaceshutter.controllers.Ctrl;
import com.andrapp.spaceshutter.util.Map;
import com.andrapp.spaceshutter.util.MonsterCrayon;
import com.andrapp.spaceshutter.util.Updatable;

//The Monster should have different strategy:
//manipulated: observe self change on other device - move direction, fire etc. Test Collision with players(maybe), 
//Ai		:	test collisions with players , make move and fire decision, notify manipulated clone of self decisions.

//The monster will have Crayon to draw self.
public class Monster implements Updatable {

	private LinkedList<Player> players = new LinkedList<Player>(); // reference to players
	private int x	= 200; 	// position
	private int y	= 400; 
	private Map map;		//reference to map
	private Ctrl ctrl;

	private MonsterCrayon crayon;

	// C-tor
	public Monster(MonsterCrayon mc,Ctrl ct) {
		crayon = mc;
		ctrl= ct;
		
		crayon.setHost(this);
		crayon.init();
		
		ctrl.registerCrayon(crayon);
	}

	void collideWith(Player p) { /* loop over players and collide with them */
	};

	void collideWith(Map m) {/* collide with map and get new direction */
	}

	boolean registerPlayer(Player p) {
		return players.add(p);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

}

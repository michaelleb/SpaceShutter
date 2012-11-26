package com.andrapp.spaceshutter.util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.andrapp.spaceshutter.activity.R;
import com.andrapp.spaceshutter.model.Monster;

public class MonsterCrayon extends Crayon {

	private Monster monster;

	// C-tor
	public MonsterCrayon(Resources appRes) {
		super(appRes);
		paint = new Paint();

	}

	@Override
	public void setHost(Monster m) {
		monster = m;
	}

	@Override
	public void draw(Canvas c) {

		c.drawBitmap(bitmap, monster.getX(), monster.getY(), paint);
	}

	@Override
	public void init() {
		bitmap = BitmapFactory.decodeResource(res, R.drawable.monstr);

	}

}

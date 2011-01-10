/*******************************************************************************
 * Copyright (c) 2010 Wayne Beaton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Wayne Beaton
 *******************************************************************************/
package ca.rokc.kittymunch;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ca.rokc.kittymunch.game.KittyMunch;
import ca.rokc.kittymunch.geometry.Point;
import ca.rokc.kittymunch.geometry.Rectangle;
import ca.rokc.kittymunch.graphics.GameRenderer;

public class PlayView extends SurfaceView {

	private Timer timer;
	private TimerTask task;
	private KittyMunch game;
	private GameRenderer drawer;

	Queue<GameEvent> events = new ConcurrentLinkedQueue<GameEvent>();
	private final Context context;
	
	interface GameEvent {
		void process(KittyMunch game);
	}
	
	public PlayView(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.context = context;
		getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				startTimer();
			}
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				stopTimer();
			}
						
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				// TODO Auto-generated method stub
			}
		});
	}

	protected synchronized void startTimer() {
		if (timer != null) return; // bail out if the timer has already been started.
		
		game = new KittyMunch(context, new Rectangle(0,0,getWidth(), getHeight()));		
		drawer = new GameRenderer(game, context);
		
		timer = new Timer(true);
		task = new TimerTask() {
			@Override
			public void run() {
				tick();
			}
		};
		timer.scheduleAtFixedRate(task, 0, 75);
	}

	protected void stopTimer() {
		if (timer == null) return;
		timer.cancel();
		timer = null;
		task = null;
	}
	
	protected void tick() {
		while (!events.isEmpty()) {
			events.remove().process(game);
		}
		
		Canvas canvas = null;
		try {
            canvas = getHolder().lockCanvas(null);
    		long start = System.currentTimeMillis();
            game.tick();
            doDraw(canvas);
            long end = System.currentTimeMillis();
            
            canvas.drawText(String.valueOf(end-start), 10, 10, new Paint());
            
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
	}

	private void doDraw(Canvas canvas) {
		drawer.drawOn(canvas);
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			events.add(new GameEvent() {
				@Override
				public void process(KittyMunch game) {
					game.protagonist.moveToward(new Point((int)event.getX(), (int)event.getY()));
				}
			});
			break;
		case MotionEvent.ACTION_UP:
			events.add(new GameEvent() {
				@Override
				public void process(KittyMunch game) {
					game.protagonist.release();
				}
			});
		}
		return true;
	}
}

package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Maze extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

	boolean[][] board;
	int blockSize = 10;
	int seedAmount = 1;
	float x = 30;
	float y = 50;
	float x2 = 70;
	float y2 = 100;
	float width = 500;
	float height = 600;
	float radius = 5;
	int pi;
	int pj;
	int maxSteps = 0;
	int goalx;
	int goaly;
	LinkedList<Integer> directions = new LinkedList<Integer>();
	Thread thread;

	@Override
	public void create () {
		directions.add(0);
		directions.add(1);
		directions.add(2);
		directions.add(3);
		Collections.shuffle(directions);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		//Create board, dividing area into blocks
		board = new boolean[Gdx.graphics.getWidth() / blockSize][Gdx.graphics.getHeight() / blockSize];

		//Maze generation in a thread so we can watch it work (optional)

		
		new Thread(){
			public void run(){
				
				while(true){
					if(thread==null || !thread.isAlive()){
						thread = makeThread();
						thread.start();
					}
				}
				
			}
		}.start();
		//makeThread();

	}
	Thread makeThread(){
		return new Thread()
		{
			
	
			
			
			public void run() {
				for(int i =0; i<board.length; i++){
					for(int j=0; j < board[0].length; j++){
						board[i][j] = false;
					}
				}
				
				
				try{
					Prims(board.length / 2, board[0].length / 2,0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}




				//after maze created
				Random rnd = new Random();
				//				while(!board[pi][pj]){
				//					pi= rnd.nextInt(board.length);
				//					pj = rnd.nextInt(board[0].length);
				//				}

				assert(board[board.length/2][board[0].length/2]);
				solver(board.length/2, board[0].length/2, -1);
				this.interrupt();
				//	pi = board.length / 2;
				//	pj = board[0].length / 2;

				//				while(true)
				//				{
				//					System.out.println(board[pi][pj]);
				//					//pi++;
				//					
				//					try {
				//						Thread.sleep(25);
				//					} catch (InterruptedException e) {
				//						// TODO Auto-generated catch block
				//						e.printStackTrace();
				//					}
				//					int t = rnd.nextInt(4);
				//
				//					switch(t){
				//					case 0:
				//						if(board[pi+1][pj]){
				//						pi +=1;
				//						}
				//						break;
				//					case 1:
				//						if(board[pi-1][pj]){
				//						pi -=1;
				//						}
				//						break;
				//					case 2:
				//						if(board[pi][pj+1]){
				//						pj +=1;
				//						}
				//						break;
				//					case 3:
				//						if(board[pi][pj-1]){
				//						pj -=1;
				//						}
				//						break;
				//					}
				//					
				//				}


			}
			public boolean onGrid(int w, int g)
			{
				return(board.length > w && board[0].length > g);

			}
			boolean solver(int x, int y, int lastMove){
				int oldpi =pi;
				int oldpj = pj;
				pi = x;
				pj = y;
				if(x==goalx && y == goaly){
					System.out.println("WONNNN");
					this.interrupt();
					return true;
				}
				//Random rnd = new Random();



				//System.out.println(board[pi][pj]);
				//pi++;

				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//int t = rnd.nextInt(4);

				for(Integer i:directions){

					switch(i){

					//RIGHT
					case 0:
						if(onGrid(pi+1, pj) &&board[pi+1][pj] && lastMove != 1){
							if (solver(x+1,y,0)) return true;
							//pi +=1;

						}
						break;
						//LEFT
					case 1:
						if(onGrid(pi-1, pj) && board[pi-1][pj]&& lastMove != 0){
							//pi -=1;
							if(solver(x-1,y,1)) return true;

						}
						break;
						//UP
					case 2:
						if(onGrid(pi, pj+1) &&board[pi][pj+1]&& lastMove != 3){
							//pj +=1;
							if (solver(x,y+1,2)) return true;
						}
						break;
						//DOWN
					case 3:
						if(onGrid(pi, pj-1) &&board[pi][pj-1]&& lastMove != 2){
							//	pj -=1;
							if (solver(x,y-1,3)) return true;
						}
						break;
					}
				}

				// solver(x,y,lastMove);
				pi = oldpi;
				pj=oldpj;
				return false;
			}


			void Prims(int x, int y, int steps) throws InterruptedException{

				if (x == board.length - 1 || y == board[x].length - 1){
					return;
				}
				List<Integer> directions = new ArrayList<Integer>();
				//Check which neighbors are passable
				int numSpaces = 0;
				if (x > 0){
					directions.add(0);
					if(board[x - 1][y])
						numSpaces++;
				}
				if (x < board.length - 1) {
					directions.add(1);
					if(board[x + 1][y])
						numSpaces++;
				}
				if (y > 0){
					directions.add(2);
					if(board[x][y - 1])
						numSpaces++;
				}
				if (y < board[x].length - 1) {
					directions.add(3);
					if(board[x][y + 1])
						numSpaces++;
				}
				if(numSpaces > 1)
					return;

				board[x][y] = true;
				if(steps>maxSteps){
					goalx = x;
					goaly = y;
					maxSteps = steps;
				}
				Thread.sleep(0);

				Collections.shuffle(directions);

				for(Integer neighbor : directions) {
					boolean hasOpposite = false;
					switch(neighbor){
					case 0:
						if(x - 2 > 0)
							Prims(x - 1, y,steps+1);
						break;
					case 1:
						if(x + 2 < board.length)
							Prims(x + 1, y, steps+1);
						break;
					case 2:
						if(y - 2 > 0)
							Prims(x, y - 1,steps+1);
						break;
					case 3:
						if(y + 2 < board[x].length)
							Prims(x, y + 1,steps+1);
						break;
					}

				}
			}
		};


	}


	@Override
	public void render () {
		camera.update();
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapeRenderer.begin(ShapeType.Filled);

		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{

				if(board[i][j])
				{
					shapeRenderer.setColor(0, 0, 0, 1);
					shapeRenderer.rect(i*blockSize, j*blockSize, 10,10);
				}


			}

		}

		shapeRenderer.setColor(255,255,255, 1);
		shapeRenderer.rect(pi*blockSize, pj*blockSize, 10,10);

		shapeRenderer.setColor(255,0,255, 1);
		shapeRenderer.rect(goalx*blockSize, goaly*blockSize, 10,10);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.end();
	}
}
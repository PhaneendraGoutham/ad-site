package pl.stalkon.ad.core;

//Author: Linda MacPhee-Cobb 2008
//http://www.herselfsai.com/2008/06/santafe-ant-trail.html


//Simple demo of SantaFe Ant problem in genetic life
//Creative Commons Non Commercial Share Alike 3.0

//89 bits of food are laid out in a trail for the ants.
//See webpage for more details
//best score so far for me is 69

import java.awt.*; 
import java.awt.event.*; 
import java.util.*;


//Main sets up simulation window and jump starts program
public class SantaFeAnts extends Frame implements Runnable, WindowListener 
{ 

 int dim = 670;												// window size
	int max = 670;												// allow for title bars and margins
	int max_ants = 300;											// constant population
	int max_days = 400;											// steps to take per ant per generation
	int max_generations = 500;									// how many generations
	int max_scores = max_ants/10;								// top scorers
	int current_generation = 0;
	int current_average_score = 0;
	int state_machine_length = 12;								// amount of instructions ants store
							
 Thread animThread;											// to continuously call repaint();
 long delay = 0L;											// speed up or slow down animation here - larger is slower but may miss updates so critters jump
	boolean world_created = false;								// don't start drawing loop till we get ourselves setup
	int number_ants_to_view = max_ants;							// how many ants show we view in animation all or just a few
	
	int SantaFeTrail[][] = new int[32][32];						// SantaFe food trail
	Ant ants[] = new Ant[max_ants];								// Ants
	Ant top_scorers[] = new Ant[max_ants/10];
	
	Random random = new Random ( System.currentTimeMillis() );	// jump start random number generator
	

	
 public static void main(String[] args) 
 { 
		new SantaFeAnts(); 
 } 
 
 
 // initialize things then tell the animation to start 
 public SantaFeAnts() 
 { 	
		//create a window ----------------------------------------------------------------------------
		super ( "  SantaFeAnts  " );							//title
		setBounds ( 0, 0, dim, dim );							//window size
		setVisible ( true );									//window interaction stuff....
		addWindowListener ( this );								// allows user to close window and stop program in gui
		
		
		//  Set up Santa Fe Trail --------------------------------------------------------------------
		for ( int i=0; i<32; i++){
			for ( int j=0; j<32; j++){
				SantaFeTrail[i][j] = 0;
			}
		}
		
		SantaFeTrail[1][0] = 1;
		SantaFeTrail[2][0] = 1;
		SantaFeTrail[3][0] = 1;

		SantaFeTrail[3][1] = 1;
		SantaFeTrail[3][2] = 1;
		SantaFeTrail[3][3] = 1;
		SantaFeTrail[3][4] = 1;
		SantaFeTrail[3][5] = 1;
		
		SantaFeTrail[4][5] = 1;
		SantaFeTrail[5][5] = 1;
		SantaFeTrail[6][5] = 1;
		
		SantaFeTrail[8][5] = 1;
		SantaFeTrail[9][5] = 1;
		SantaFeTrail[10][5] = 1;
		SantaFeTrail[11][5] = 1;
		SantaFeTrail[12][5] = 1;
		
		SantaFeTrail[12][6] = 1;
		SantaFeTrail[12][7] = 1;
		SantaFeTrail[12][8] = 1;
		SantaFeTrail[12][9] = 1;
		
		SantaFeTrail[12][11] = 1;
		SantaFeTrail[12][12] = 1;
		SantaFeTrail[12][13] = 1;
		SantaFeTrail[12][14] = 1;
		
		SantaFeTrail[12][17] = 1;
		SantaFeTrail[12][18] = 1;
		SantaFeTrail[12][19] = 1;
		SantaFeTrail[12][20] = 1;
		SantaFeTrail[12][21] = 1;
		SantaFeTrail[12][22] = 1;
		SantaFeTrail[12][23] = 1;
		
		SantaFeTrail[11][24] = 1;
		SantaFeTrail[10][24] = 1;
		SantaFeTrail[9][24] = 1;
		SantaFeTrail[8][24] = 1;
		SantaFeTrail[7][24] = 1;
		
		SantaFeTrail[4][24] = 1;
		SantaFeTrail[3][24] = 1;
		
		SantaFeTrail[1][25] = 1;
		SantaFeTrail[1][26] = 1;
		SantaFeTrail[1][27] = 1;
		SantaFeTrail[1][28] = 1;
		
		SantaFeTrail[2][30] = 1;
		SantaFeTrail[3][30] = 1;
		SantaFeTrail[4][30] = 1;
		SantaFeTrail[5][30] = 1;
		
		SantaFeTrail[7][29] = 1;
		SantaFeTrail[7][28] = 1;
		
		SantaFeTrail[8][27] = 1;
		SantaFeTrail[9][27] = 1;
		SantaFeTrail[10][27] = 1;
		SantaFeTrail[11][27] = 1;
		SantaFeTrail[12][27] = 1;
		SantaFeTrail[13][27] = 1;
		SantaFeTrail[14][27] = 1;
		
		SantaFeTrail[16][26] = 1;
		SantaFeTrail[16][25] = 1;
		SantaFeTrail[16][24] = 1;
		
		SantaFeTrail[16][21] = 1;
		SantaFeTrail[16][20] = 1;
		SantaFeTrail[16][19] = 1;
		SantaFeTrail[16][18] = 1;
		
		SantaFeTrail[17][15] = 1;
		
		SantaFeTrail[20][14] = 1;
		SantaFeTrail[20][13] = 1;
		
		SantaFeTrail[20][10] = 1;
		SantaFeTrail[20][9] = 1;
		SantaFeTrail[20][8] = 1;
		SantaFeTrail[20][7] = 1;
		
		SantaFeTrail[21][5] = 1;
		SantaFeTrail[22][5] = 1;
		
		SantaFeTrail[24][4] = 1;
		SantaFeTrail[24][3] = 1;
		
		SantaFeTrail[25][2] = 1;
		SantaFeTrail[26][2] = 1;
		SantaFeTrail[27][2] = 1;
		
		SantaFeTrail[29][3] = 1;
		SantaFeTrail[29][4] = 1;
		
		SantaFeTrail[29][6] = 1;
		
		SantaFeTrail[29][9] = 1;
		
		SantaFeTrail[29][12] = 1;

		SantaFeTrail[28][14] = 1;
		SantaFeTrail[27][14] = 1;
		SantaFeTrail[26][14] = 1;
		
		SantaFeTrail[23][15] = 1;
		
		SantaFeTrail[24][18] = 1;
		
		SantaFeTrail[27][19] = 1;
		
		SantaFeTrail[26][22] = 1;

		SantaFeTrail[23][23] = 1;


		// create ants  ----------------------------------------------------------------
		for ( int i=0; i<max_ants; i++){
			ants[i] = new Ant( SantaFeTrail, random );
		}

		//  load up init values in top scorers  ----------------------------------------
		for ( int i=0; i<max_scores; i++){
			top_scorers[i] = new Ant ( SantaFeTrail, random );
		}
		
		// start animation loop --------------- ----------------------------------------		
		animThread = new Thread ( this );						// main program thread.
		animThread.start();


	
 } 
 


	//****************************************************************************************************
	// main program loop    
	public synchronized void run() 
 { 
		current_generation = 0;

		try{
			
			while ( current_generation < max_generations){	// for each generation

				int j=0;
				while ( j<max_days ){						// for each day

					for ( int k=0; k<max_ants; k++){		// move each ant one state
						ants[k].move(j);
					}

					repaint( delay );						// request redraw 
					wait();									// wait for redraw 
					j++;									// next day
				}
				
				sort();										// sort ants high score to low score
				print();									// print info to command line
				generation();								// mate and reset trail and food scores
				current_generation++;						// next generation
			}
		
		} catch( Exception ex ) { System.err.println( "Error: " + ex ); } 
		
 } 
	//****************************************************************************************************


	// generational stuff to do at end of each generation
	void generation()
	{
		
		// mate fittest half of population  1&2, 2&3, 3&4 ....
		int array_middle = max_ants/2;
		for ( int j=0; j< array_middle; j++){
			Ant a = ants[j];
			Ant b = ants[j+1];
			ants[array_middle + j] = a.mate( a, b, SantaFeTrail, random );	// add new beings to bottom of array, overwriting lesser creatures
		}
		
		
		// mix things up a bit
		int mix = 10;
		for ( int q=mix; q<max_ants; q+=mix )
		{	
			ants[q] = ants[q].adjust_dna( ants[q], random );
		}					
					
									
																							
		// re-set food to zero for all ants and reset SantaFeTrail for all ants, and start everyone facing east
		for ( int j=0; j<max_ants; j++){
			ants[j].food = 0;
			ants[j].d = 1;	
			ants[j].x = 0;
			ants[j].y = 0;	
								
			for ( int k=0; k<32; k++){				// lay out the food trail
				for ( int l=0; l<32; l++){
					for ( int m=0; m<32; m++){
						ants[j].santa_fe[l][m] = SantaFeTrail[l][m];
					}
				}
			}
				
		}
	}
	
	
	
	
	
	
	//    ***********    helper functions ****************************************************************
	

	
	
	

 //print results to screen
	void print()
	{
	
		System.out.println  ( " New Generation " + current_generation + " ###  food ahead   ##########  Average = " + current_average_score + "  ###   no food ahead   ###########"  );

		int fitness = 0;
		
		for ( int i=0; i<max_scores; i++){       //just print top scorers
		
			System.out.print ( " Ant " + i +  ":  " );
			Ant a = ants[i];
			for ( int j=0; j<state_machine_length; j++){
				System.out.print ( "  " + a.if_food_ahead[j] );
			}
			System.out.print ( "   ***   " );
			for ( int j=0; j<state_machine_length; j++){
				System.out.print ( "  " + a.if_not_food_ahead[j] );
			}
			fitness = a.food*100/89;
			System.out.println( "    Food:  " + a.food + "   Fitness:   " + fitness + " %  " );
		}
	}
	
	
	
	//sort out the smart ants from the dumb ants order smart starting at 0 and dumb ones at end of array
	public void sort()
	{
		ArrayList<Ant> sorted = new ArrayList<Ant>();
		
		//prime loop
		sorted.add( ants[0] );
		
		for ( int i=1; i<max_ants; i++){
			
			int added = 0;
			int end = sorted.size()-1;

			for ( int j=0; j<end; j++){

				if ( ants[i].food >= sorted.get(j).food ) { 
					sorted.add( j, ants[i]); 
					added = 1;
					end = 0;
				}
			}
			if ( added == 0 ) { sorted.add ( ants[i] );   }
		}
				
		int total = 0;										
		for ( int i=0; i<max_ants; i++){
			ants[i] = sorted.get(i);
			total += ants[i].food;
		}
		
		current_average_score = total/max_ants;
																						
	}
	


		
	
	
	
	// ***************************** graphics stuff  *******************************************************************************
 public void update(Graphics g) { paint(g); } 
 
 //repaint the scene
 public synchronized void paint(Graphics g) 
 { 
		//draw background
		g.setColor( Color.black ); 
		g.fillRect( 0, 0, dim, dim ); 
	
		// Colors
		Color background = new Color ( 0, 0, 0 );
		Color food = new Color ( 0, 100, 0 );
		Color ant = new Color ( 0, 0, 180 );
			
		// draw the food
		g.setColor ( food );
		
		for ( int i=0; i<32; i++){
			for ( int j=0; j<32; j++ ){
				if ( SantaFeTrail[i][j] == 1 ){
					g.fillRect ( (i*20)+15, (j*20)+30, 18, 18 );
				}
			}
		}
			
		
		// draw ants
		g.setColor( ant );
		for ( int i=0; i<number_ants_to_view; i++){				// show top few ants
			Ant a = (Ant)ants[i];
			g.fillOval ( (a.x*20)+15, (a.y*20)+30, 10, 10 );
		}


		notifyAll(); 
 } 
 
 
 // gui stuff
 public void windowOpened( WindowEvent ev ) {} 
 public void windowActivated( WindowEvent ev ) {} 
 public void windowIconified( WindowEvent ev ) {} 
 public void windowDeiconified( WindowEvent ev ) {} 
 public void windowDeactivated( WindowEvent ev ) {} 
 public void windowClosed( WindowEvent ev ) {} 
 
 
 public void windowClosing(WindowEvent ev) 
 { 
		animThread = null; 
		setVisible(false); 
		dispose(); 
		System.exit(0); 
 } 

 
} 


//*******************  Ant class ************************************************************
//***************************************************************************************************
//12 step program, one move per cycle in order 0 = move forward; 1 = right, 2 = left
//x, y are current position
//d is the direction we are facing 0 = N, 1 = E, 2 = S, 3 = W
class Ant
{
	int program_length = 12;
	int food = 0;
	int steps = 0;
	int if_food_ahead[] = new int[program_length];
	int if_not_food_ahead[] = new int[program_length];
	int x = 0, y = 0, d = 1;		
	int santa_fe[][] = new int[32][32];
	Random random;

	
	// new ant
	Ant( int sft[][], Random r )
	{
		random = r;
		
		// lay out the food trail
		for ( int i=0; i<32; i++){
			for ( int j=0; j<32; j++){
				santa_fe[i][j] = sft[i][j];
			}
		}
		
		// create a if-food and if-not-food program to follow 
		//  we can turn left, turn right, or move
		//  0 = m; 1 = r; 2 = l;
		
		for ( int i=0; i<program_length; i++){
			 if_food_ahead[i] = random.nextInt()%3;
			 if ( if_food_ahead[i] < 0 ) { if_food_ahead[i] *= -1; }
		}
		for ( int i=0; i<program_length; i++){
			if_not_food_ahead[i] = random.nextInt()%3;
			if ( if_not_food_ahead[i] < 0 ) { if_not_food_ahead[i] *= -1; }
		}
		
		// pick a starting direction to face
		d = 1;
	}
	
		
		
	//  one move along our dna state machine
	void move ( int day )
	{
		int c = day % program_length;
		int m = 0;
		int see_food = 0;
			
		//check for food in square in front of us
		if ( d == 0 ){ if ( y > 0 ){ if ( santa_fe[x][y-1] == 1) { see_food = 1; } } }
		else if ( d == 1 ) { if ( x < 31 ) { if ( santa_fe[x+1][y] == 1) { see_food = 1; } } }
		else if ( d == 2 ) { if ( y < 31 ) { if ( santa_fe[x][y+1] == 1) { see_food = 1;  } } }
		else if ( d == 3 ) { if ( x > 0 ) { if ( santa_fe[x-1][y] == 1) { see_food = 1; } } }
		
		
		// if food in front of us follow if_food_ahead program
		if ( see_food == 1 ){
			if ( if_food_ahead[c] == 0 ){		// move forward one and fetch the food
				if ( d == 0 ){ 
					
					if ( y > 0 ){ y--;}
					santa_fe[x][y] = 0;
					food++; 
				
				}else if ( d == 1 ){
					
					if ( x < 31 ){ x++; }
					santa_fe[x][y] = 0;
					food++;
					
				}else if ( d == 2 ){

					if ( y < 31 ){ y++; }
					santa_fe[x][y] = 0;
					food++;
					 
				}else if ( d == 3 ) {
					if ( x > 0 ){ x--; }
					santa_fe[x][y] = 0;
					food++;
				}	
			}else if ( if_food_ahead[c] == 1 ){	//  turn right

				if ( d == 0 ){ d = 1; }
				else if ( d == 1 ) { d = 2; }
				else if ( d == 2 ) { d = 3; }
				else if ( d == 3 ) { d = 0; }
				
			}else{  // must be 2 so turn left
				if ( d == 0 ){ d = 3; }
				else if ( d == 1 ) { d = 2; }
				else if ( d == 2 ) { d = 1; }
				else if ( d == 3 ) { d = 0; }
			}
			
			see_food = 0;
			
		}else{  // else follow if_not_food_ahead
			if ( if_not_food_ahead[c] == 0 ){		// move forward one and fetch the food
				if ( d == 0 ){ 
					if ( y > 0 ){ y--;}
				
				}else if ( d == 1 ){
					
					if ( x < 31 ){ x++; }
					
				}else if ( d == 2 ){

					if ( y < 31 ){ y++; }
					 
				}else if ( d == 3 ) {
					if ( x > 0 ){ x--; }
									}	
			}else if ( if_not_food_ahead[c] == 1 ){	//  turn right

				if ( d == 0 ){ d = 1; }
				else if ( d == 1 ) { d = 2; }
				else if ( d == 2 ) { d = 3; }
				else if ( d == 3 ) { d = 0; }
				
			}else{  // must be 2 so turn left
				if ( d == 0 ){ d = 3; }
				else if ( d == 1 ) { d = 2; }
				else if ( d == 2 ) { d = 1; }
				else if ( d == 3 ) { d = 0; }
			}
	
			if ( santa_fe[x][y] == 1 ){
				santa_fe[x][y] = 0;
				food++;
			}
			see_food = 0;
		} //end else if not food ahead
	}
	
	
	
	// randomly adjust some bit of dna in each of our two state machines
	Ant adjust_dna ( Ant a, Random random )
	{
		//randomize dna
		//for ( int i=0; i<4; i++){
			a.if_food_ahead[random.nextInt( program_length )] = random.nextInt()%3;
			a.if_not_food_ahead[random.nextInt( program_length )] = random.nextInt()%3;
		//}
		
		for ( int i=0; i<program_length; i++){
			if( a.if_food_ahead[i] < 0 ){ a.if_food_ahead[i] *= -1; }
			if( a.if_not_food_ahead[i] < 0 ) { a.if_not_food_ahead[i] *= -1; }
		}
		
		return a;
	}
	
	
	
	// mate two ants and return the baby/
	// one dna from mom, one from dad and randomize one bit in each
	Ant mate ( Ant a, Ant b, int sft[][], Random random )
	{
		Ant baby = new Ant( sft, random );
		
		// lay out a fresh food trail
		for ( int i=0; i<32; i++){
			for ( int j=0; j<32; j++){
				baby.santa_fe[i][j] = sft[i][j];
			}
		}
		
		//swap dna one piece from mom and one from dad
		if( random.nextInt()%2 == 0 ){
			for ( int i=0; i<program_length; i++){
				baby.if_food_ahead[i] = a.if_food_ahead[i];
				baby.if_not_food_ahead[i] = b.if_not_food_ahead[i];
			}
		}else{
			for ( int i=0; i<program_length; i++){
				baby.if_food_ahead[i] = b.if_food_ahead[i];
				baby.if_not_food_ahead[i] = a.if_not_food_ahead[i];
			}
		}
		
		
		
		baby.adjust_dna ( baby, random );							//randomize dna
		
		return baby;												// hand baby off to the stork to deliver
	}
	
	
}



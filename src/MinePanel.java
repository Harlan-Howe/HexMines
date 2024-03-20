import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MinePanel extends JPanel {

	public static final int numCellsAcross = 45;
	public static final int numCellsDown = 30;
	public static final int numMines = 250;
	private MineHexBlock[][] myCells;
	private MineHexBlock pressedCell;
        private boolean successfulFirstMove;
	/**
	 * Creates the mine panel, including a grid of (numCellsAcross x numCellsDown) MineSquares.
	 *
	 */
	public MinePanel()
	{
            /*  super(new GridLayout(numCellsDown,numCellsAcross));
		myCells = new MineSquare[numCellsAcross][numCellsDown];
		for (int i=0; i<numCellsDown;i++)
			for (int j=0; j<numCellsAcross; j++)
			{
				myCells[j][i] = new MineSquare();
				add(myCells[j][i]);
			}
		setPreferredSize(new Dimension(numCellsAcross*MineSquare.size,numCellsDown*MineSquare.size));
		setRandomMines();
		doNeighborCount();
		addMouseListener(new clickListener());
            */
            myCells = new MineHexBlock[numCellsAcross][numCellsDown];
		for (int i=0; i<numCellsDown;i++)
			for (int j=0; j<numCellsAcross; j++)
			{
				myCells[j][i] = new MineHexBlock(20+j*MineHexBlock.WIDTH+(i%2)*MineHexBlock.WIDTH/2,
                                                                   20+i*MineHexBlock.HEIGHT*3/4);
			}
           setRandomMines();
           doNeighborCount();
           addMouseListener(new clickListener());
           successfulFirstMove = false;
	}
	
	/**
	 * precondition: all the cells are cleared - no mines!
	 * postcondition: randomly distributes exactly numMines mines around the grid. 
	 */
	public final void setRandomMines()
	{
		Random generator = new Random();
		int x, y;
		boolean placed;
		for (int n = 0; n<numMines; n++)
		{
			placed = false;
			do
			{
				x = generator.nextInt(numCellsAcross);
				y = generator.nextInt(numCellsDown);
				if (!myCells[x][y].hasAMine())
				{
					myCells[x][y].setMine(true);
					placed = true;
				}
				//System.out.println("("+x+", "+y+")");
			}while (!placed);
		}
	}
	/**
	 * precondition: the cells in the grid already exist. Presumably, there are some mines out there.
	 * postcondition: each cell in the area now knows how many bombs [0...8] there
	 * are in its neighborhood.
	 *
	 */
	public final void doNeighborCount()
	{
		for (int i=0; i<numCellsAcross; i++)
			for (int j=0; j<numCellsDown; j++)
				countMyNeighbors(i,j);
	}
	
	/**
	 * A "safe" way to check whether there is a mine at the given location, (x,y).
	 * Precondition: The cells in the array exist, but x and y do not need to be
	 * in the range of the grid.
	 * @return true if there is a mine at this location; false if there is no mine or if (x,y) is out of bounds.
	 */
	private boolean locationHasMine(int x, int y)
	{
		if ((x>=0)&&(x<numCellsAcross)&&(y>=0)&&(y<numCellsDown))
			return myCells[x][y].hasAMine();
		return false;
	}
	
	/**
	 * Precondition: the cell at (x,y) exists
	 * Postcondition: the cell now knows how many mines [0...8] are in its 
	 * immediate neighborhood. 
	 */
	private void countMyNeighbors(int x, int y)
	{
		int count = 0;
		/*for (int i=-1;i<2; i++)
			for (int j=-1;j<2;j++)
				if (locationHasMine(x+i,y+j))
					count++;
		if (locationHasMine(x,y))
			count--;
                */
                ArrayList<Integer> validNeighbors = getValidNeighborsOf(x,y);
                for (int i : validNeighbors)
                    if (locationHasMine(i%numCellsAcross,i/numCellsAcross))
                        count++;
                       
		myCells[x][y].setNeighboringMines(count);
	}
        
        
        ArrayList<Integer> getValidNeighborsOf(int x, int y)
        {
            ArrayList<Integer> result = new ArrayList<Integer>();
                if (y-1>-1 && y-1<numCellsDown && x-1+y%2>-1 && x-1+y%2<numCellsAcross)
                    result.add(x-1+y%2 + numCellsAcross * (y-1));
                if (y-1>-1 && y-1<numCellsDown && x+y%2>-1 && x+y%2<numCellsAcross)
                    result.add(x+y%2 + numCellsAcross * (y-1));
                if (y>-1 && y<numCellsDown && x-1>-1 && x-1<numCellsAcross)
                    result.add(x-1 + numCellsAcross * (y));
                if (y>-1 && y<numCellsDown && x+1>-1 && x+1<numCellsAcross)
                    result.add(x+1 + numCellsAcross * (y));
                if (y+1>-1 && y+1<numCellsDown && x-1+y%2>-1 && x-1+y%2<numCellsAcross)
                    result.add(x-1+y%2 + numCellsAcross * (y+1));
                if (y+1>-1 && y+1<numCellsDown && x+y%2>-1 && x+y%2<numCellsAcross)
                    result.add(x+y%2 + numCellsAcross * (y+1));                
            return result;
        }
                
        /**
	
	/**
	 * precondition: the cells all exist.
	 * postcondition: any cell with a mine in it has its appearance changed: 
	 * if it has a flag, it shows the bomb, but if it doesn't, it shows an explosion.
	 */
	public void revealAllMines()
	{
		for (int i=0; i<numCellsAcross; i++)
			for (int j=0; j<numCellsDown; j++)
				if (myCells[i][j].hasAMine())
					if (myCells[i][j].getMyStatus()==MineStatus.FLAGGED)
						myCells[i][j].setMyStatus(MineStatus.BOMB_REVEALED);
					else
						myCells[i][j].setMyStatus(MineStatus.EXPLODED);
                                else
                                    if (myCells[i][j].isFlagged())
                                        myCells[i][j].setMyStatus(MineStatus.BOMB_REVEALED);
	}
	/**
	 * precondition: all the cells exist.
	 * postcondition: the cells are cleared of mines, new mines are distributed,
	 * the neighboring cells are counted, and the appearance of all the cells
	 * are reset.
	 */
	public void reset()
	{
		for (int i=0; i<numCellsAcross; i++)
			for (int j=0; j<numCellsDown; j++)
			{	myCells[i][j].setMyStatus(MineStatus.ORIGINAL);
				myCells[i][j].setMine(false);
			}
		setRandomMines();
		doNeighborCount();
		pressedCell=null;
                repaint();
                successfulFirstMove = false;
	}
	/**
	 * precondition: the cell exists
	 * postcondition: if this cell has zero mines in its neighborhood, it reveals
	 * all its neighbors. Of course, if any of them have zero mines, they reveal 
	 * their neighbors, too.
	 */
	public void checkForZeroes(int x, int y)
	{
		// this is the method you need to write!
            myCells[x][y].setMyStatus(MineStatus.NUMBER_REVEALED);
            if (myCells[x][y].getNeighboringMines()==0)
            {
                ArrayList<Integer> locs = getValidNeighborsOf(x,y);
            
                for (int l: locs)
                {
                    if (myCells[l%numCellsAcross][l/numCellsAcross].getMyStatus()==MineStatus.ORIGINAL)
                        checkForZeroes(l%numCellsAcross,l/numCellsAcross);
                }
            }
	}
        /**
         * given screen coordinates x and y, identify which cell is closest to
         * this location, or -1 if the distance is too far.
         * @param x
         * @param y
         * @return the location, packed in the format of x + numCellsAcross*y
         */
        public int identifyHexAtLocation(int x, int y)
        {
            
            double closest = 100000;
            int bestX = -1;
            int bestY = -1;
            for (int i=0; i<numCellsAcross; i++)
                for (int j=0; j<numCellsDown; j++)
                {
                    double d2 = myCells[i][j].distanceSquaredToPoint(x,y);
                    if (d2<closest)
                    {
                        closest = d2;
                        bestX = i;
                        bestY = j;
                    }
                }
            if (closest>Math.pow(Math.min(MineHexBlock.HEIGHT,MineHexBlock.WIDTH)/2,2))
                return -1;
            else
                return bestX+numCellsAcross*bestY;
            
        }
        
        public boolean playerHasWon()
        {
            int numFlags = 0;
            int numNums = 0;
            for (int i=0; i<numCellsAcross; i++)
                for (int j=0; j<numCellsDown; j++)
                {
                    if (myCells[i][j].getMyStatus()==MineStatus.FLAGGED)
                        numFlags++;
                    if (myCells[i][j].getMyStatus()==MineStatus.NUMBER_REVEALED)
                        numNums++;
                }
            
            return (numFlags == numMines && numFlags+numNums == numCellsAcross*numCellsDown);
        }
        
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            for (int i=0; i<numCellsDown;i++)
                for (int j=0; j<numCellsAcross; j++)
                {
                        myCells[j][i].drawSelf(g);
                }
        }
        
        
	public class clickListener extends MouseAdapter
	{
		/**
		 * postcondition: the variable pressedCell is set to the cell where the
 button was pressed.
		 */
		public void mousePressed(MouseEvent me)
		{
                    int loc = identifyHexAtLocation(me.getX(),me.getY());
                    if (loc == -1)
                        return;
                    pressedCell = myCells[loc%numCellsAcross][loc/numCellsAcross];
		}
		/**
		 * postcondition: if this is the same cell as when the button was pressed,
		 * it will handle the action of clicking this cell.
		 */
		public void mouseReleased(MouseEvent me)
		{
                    //System.out.println("Clicked.");
                    int loc = identifyHexAtLocation(me.getX(),me.getY());
                    if (loc == -1)
                    {
                        pressedCell = null;
                        return;
                    }
                    MineHexBlock clickedCell = myCells[loc%numCellsAcross][loc/numCellsAcross];
                        
			if (clickedCell != pressedCell)
			{
				pressedCell = null;
				return;
			}
			if ((me.getModifiers()&MouseEvent.SHIFT_MASK)==MouseEvent.SHIFT_MASK)
			{
				if (clickedCell.getMyStatus()==MineStatus.ORIGINAL)
					clickedCell.setMyStatus(MineStatus.FLAGGED);
				else if (clickedCell.getMyStatus()==MineStatus.FLAGGED)
					clickedCell.setMyStatus(MineStatus.ORIGINAL);
                                repaint();
			}
			else
			{
                            while(!successfulFirstMove)
                            {
                                if (clickedCell.hasAMine())
                                   reset();
                                else
                                    successfulFirstMove = true;
                            }
				if (clickedCell.hasAMine())
				{
					revealAllMines();
                                        repaint();
					JOptionPane.showMessageDialog(null, "Play Again?");
					reset();
				}
				else
				{
					checkForZeroes(loc%numCellsAcross,loc/numCellsAcross);
					clickedCell.setMyStatus(MineStatus.NUMBER_REVEALED);// this may be redundant.
                                        repaint();
				}
			}
			pressedCell = null;
                        if (playerHasWon())
                        {
                            JOptionPane.showMessageDialog(null, "You win!\nPlay again?");
                            reset();
                        }
		}
	}
}

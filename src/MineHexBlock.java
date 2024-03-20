

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author harlan.howe
 */
public class MineHexBlock {
        private boolean hasAMine;
	private int neighboringMines;
	private MineStatus myStatus;
        private static Image flagImage; // the "static" on these variables means
                                           // that all the cells share these images.
	private static Image[] numberImage;
        private static Image originalImage;
        private static Image correctImage;
        private static Image incorrectImage;
        private static Image explosionImage;
        
        public final static int HEIGHT = 32;
        public final static int WIDTH = 28;
    
        private int screenX,screenY;
        private boolean iAmFlagged;
        
        public MineHexBlock(int x, int y)
        {
            screenX = x;
            screenY = y;
            myStatus = MineStatus.ORIGINAL;
            if (flagImage == null)
            {
                flagImage = new ImageIcon("flag.png").getImage();
                originalImage = new ImageIcon("original.png").getImage();
                correctImage = new ImageIcon("correct.png").getImage();
                incorrectImage = new ImageIcon("incorrect.png").getImage();
                explosionImage = new ImageIcon("explosion.png").getImage();
                numberImage = new Image[7];
                for (int i=0; i<7;i++)
                    numberImage[i] = new ImageIcon(i+".png").getImage();
            }
            iAmFlagged = false;
        }
        
        public boolean isFlagged()
        {
            return iAmFlagged;
        }
        
        /**
	 * @return whether this cell has a mine.
	 */
	public boolean hasAMine() {
		return hasAMine;
	}
	/**
	 * @return  myStatus
	 */
	public MineStatus getMyStatus() {
		return myStatus;
	}
	/**
	 * @return the neighboringMines around this cell.
	 */
	public int getNeighboringMines() {
		return neighboringMines;
	}

	/**
	 * postcondition: sets this cell to either have a mine (true), or not (false).
	 */
	public void setMine(boolean hasAMine) {
		this.hasAMine = hasAMine;
	}
	/**
	 * @param sets myStatus to a given MineStatus.
	 */
	public void setMyStatus(MineStatus myStatus) {
            this.myStatus = myStatus;
            if (myStatus == MineStatus.FLAGGED)
                iAmFlagged = true;
            else if (iAmFlagged && myStatus == MineStatus.ORIGINAL)
                iAmFlagged = false;
           
	}
        
        /**
	 * @param sets the number of neighboringMines
	 */
	public void setNeighboringMines(int neighboringMines) {
		this.neighboringMines = neighboringMines;
	}	

        /**
         * gets the square of the distance from the _center_ of this hex to the 
         * given screen coordinate
         * @param x
         * @param y
         * @return the distance squared from (centerX,centerY) to (x,y)
         */
        public double distanceSquaredToPoint(int x, int y)
        {
            return Math.pow(screenX+WIDTH/2-x,2)+Math.pow(screenY+HEIGHT/2-y,2);
        }
        
        /**
         * draws the hex determined by myStatus and neighboringMines
         */
        public void drawSelf(Graphics g)
        {
            switch (myStatus)
            {
                case ORIGINAL:
                    g.drawImage(originalImage, screenX, screenY, null);
                break;
                case FLAGGED:
                    g.drawImage(flagImage, screenX, screenY, null);
                break;
                case NUMBER_REVEALED:
                    g.drawImage(numberImage[neighboringMines], screenX, screenY, null);
                break;
                case BOMB_REVEALED:
                    if (iAmFlagged && hasAMine)
                        g.drawImage(correctImage, screenX, screenY, null);
                    if (iAmFlagged && !hasAMine)
                        g.drawImage(incorrectImage, screenX, screenY, null);
                break;
                case EXPLODED:
                    g.drawImage(explosionImage, screenX, screenY, null);
                break;

                
            }
        }
        public String toString()
	{
		return "neighbors: "+getNeighboringMines()+" has bomb: "+hasAMine()+" flagged: "+iAmFlagged;
	}
}



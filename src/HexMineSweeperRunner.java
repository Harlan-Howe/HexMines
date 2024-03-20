import javax.swing.JFrame;
import java.awt.GridLayout;

public class HexMineSweeperRunner extends JFrame
{
	public static void main(String[] args)
	{
		HexMineSweeperRunner app = new HexMineSweeperRunner();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public HexMineSweeperRunner()
	{
		super("Hex Mine Sweeper");
		getContentPane().setLayout(new GridLayout(1,1));
		getContentPane().add(new MinePanel());
		//setSize(MineSquare.size*MinePanel.numCellsAcross,MineSquare.size*MinePanel.numCellsDown+32);
		setSize((int)((MinePanel.numCellsAcross+0.5)*MineHexBlock.WIDTH+40),
				MinePanel.numCellsDown*MineHexBlock.HEIGHT*3/4+80);
                setVisible(true);
		setResizable(false);
	}
}

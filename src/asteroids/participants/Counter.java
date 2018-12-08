package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import asteroids.game.Participant;

public class Counter extends Participant
{
    /** A fake outline so that the move method doesn't break */
    private static final Shape FAKE_OUTLINE = new Rectangle(0, 0, 10, 10);
    
    /** The count to display */
    private int count;
    
    /**
     * Creates a new {@link Counter} at the given position
     * @param x the x cordinate to place the {@link Counter} at
     * @param y the y cordinate to place the {@link Counter} at
     */
    public Counter(int x, int y) {
        setPosition(x, y);
    }

    /**
     * Sets the count to be displayed
     * @param count the count to be displayed
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    @Override
    protected Shape getOutline ()
    {
        return FAKE_OUTLINE;
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void draw (Graphics2D g)
    {
        String countText = count + "";
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int textWidth = g.getFontMetrics(LABEL_FONT).stringWidth(countText);
        
        g.setFont(LABEL_FONT);
        g.drawString(countText, (float) getX() - textWidth / 2, (float) getY());
    }

}

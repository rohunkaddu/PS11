package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import asteroids.game.Participant;

/**
 * A counter for the ships lives
 * @author storm
 *
 */
public class LifeCounter extends Participant
{
    
    /**
     * Creates a new indicator
     * @param x the x coordinate of the indicator
     * @return
     */
    private static Shape makeIndicator(int x, int y) {
        AffineTransform trans = AffineTransform.getTranslateInstance(x, y + SHIP_HEIGHT);
        trans.concatenate(AffineTransform.getRotateInstance(-Math.PI / 2));
        return trans.createTransformedShape(Ship.makeShipOutline(0));
    }
    
    /** The numver of lives to display */
    private int lives = 3;
    
    /** The life indicators */
    private final Shape[] indicators;
    
    /**
     * Creates a new life counter at the given cordinates
     * @param x
     * @param y
     */
    public LifeCounter(int x, int y) {
        indicators = new Shape[3];
        
        indicators[0] = makeIndicator(x + SHIP_WIDTH / 2 + SHIP_SEPARATION, y);
        indicators[1] = makeIndicator(x + SHIP_WIDTH / 2 + SHIP_SEPARATION * 2 + SHIP_WIDTH, y);
        indicators[2] = makeIndicator(x + SHIP_WIDTH / 2 + SHIP_SEPARATION * 3 + SHIP_WIDTH * 2, y);
    }

    @Override
    protected Shape getOutline ()
    {
        return indicators[0];
    }

    @Override
    public void collidedWith (Participant p)
    {
    }

    /**
     * Sets the number of lives
     * @param lives the number of lives
     */
    public void setLives(int lives) {
        this.lives = lives;
    }
    
    @Override
    public void draw (Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); 
        
        for (int i = 0; i < lives; i++) {
            g.draw(indicators[i]);
        }
    }
}

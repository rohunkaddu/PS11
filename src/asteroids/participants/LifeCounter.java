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
    /** Spacing between the indicators */
    private final static int INDICATOR_SPACING = 15;
    
    /** The indicators */
    private final static Shape[] LIFE_INDICATORS = {
      makeIndicator(SHIP_WIDTH / 2 + INDICATOR_SPACING),
      makeIndicator(SHIP_WIDTH / 2 + INDICATOR_SPACING * 2 + SHIP_WIDTH),
      makeIndicator(SHIP_WIDTH / 2 + INDICATOR_SPACING * 3 + SHIP_WIDTH * 2)      
    };
    
    /**
     * Creates a new indicator
     * @param x the x cordinate of the indicator
     * @return
     */
    private static Shape makeIndicator(int x) {
        AffineTransform trans = AffineTransform.getTranslateInstance(x, SHIP_HEIGHT);
        trans.concatenate(AffineTransform.getRotateInstance(-Math.PI / 2));
        return trans.createTransformedShape(Ship.makeShipOutline(0));
    }
    
    /** The numver of lives to display */
    private int lives = 3;

    @Override
    protected Shape getOutline ()
    {
        return LIFE_INDICATORS[0];
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
            g.draw(LIFE_INDICATORS[i]);
        }
    }
}

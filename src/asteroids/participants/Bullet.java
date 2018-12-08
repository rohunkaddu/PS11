package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * A bullet participant
 * @author carson storm
 *
 */
public class Bullet extends Participant implements AsteroidDestroyer
{
    /**
     * A bullet from a alien ship
     * @author carson storm
     *
     */
    //public class AlienBullet extends Bullet {}
    
    
    /**
     * A bullet from the ship
     * @author carson storm
     *
     */
    //public class ShipBullet extends Bullet {}
    
    /** The outline of the bullet **/
    private Shape outline;
    
    /**
     * Creates a new bullet
     */
    public Bullet ()
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(-1, 0);
        poly.lineTo(0, 0);
        poly.lineTo(0, -1);
        poly.lineTo(-1, -1);
        outline = poly;
        
        new ParticipantCountdownTimer(this, "timeout", BULLET_DURATION);
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        Participant.expire(this); 
    }
    
    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("timeout")) {
            Participant.expire(this);
        }
    }

}

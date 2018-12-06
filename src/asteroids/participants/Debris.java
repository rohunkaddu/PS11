package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * A debris participant
 * @author rohun kaddu
 *
 */
public class Debris extends Participant
{
    /** The otline of the debris */
    private Shape outline;

    /**
     * Creates a new debris of the given length in a random orientation and moving in a random direction
     * @param length    the length to make the debris
     */
    public Debris (int length)
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(-1, 0);
        poly.lineTo(Math.random() * length, Math.random() * length);
        outline = poly;
        
        setVelocity(Math.random() * 2, Math.random() * 2);
        
        new ParticipantCountdownTimer(this, "timeout", 1000);
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
    }
    
    public void move () 
    {
        super.move();
    }
    
    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("timeout")) {
            Participant.expire(this);
        }
    }
}

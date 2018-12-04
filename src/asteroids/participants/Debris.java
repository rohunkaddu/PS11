package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class Debris extends Participant
{
    
    private Shape outline;

    public Debris ()
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(-1, 0);
        poly.lineTo(Math.random() * 20, Math.random() * 20);
        outline = poly;
        
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

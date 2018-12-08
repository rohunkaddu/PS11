package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.destroyers.*;
import asteroids.participants.Bullet;

public class ShipBullet extends Bullet
{
    
    private Shape outline;
    
    public ShipBullet ()
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
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
        }
    }
}

package asteroids.participants;

import static asteroids.game.Constants.BULLET_DURATION;
import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class AlienBullet extends Bullet
{

    private Shape outline;

    public AlienBullet ()
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
        if (p instanceof AlienShip)
        {
        }
        else
        {
            Participant.expire(this);
        }

    }
}

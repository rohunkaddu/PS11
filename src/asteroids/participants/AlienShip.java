package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import javax.sound.sampled.Clip;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.game.ShipBullet;
import sounds.AsteroidSounds;
import static asteroids.game.Constants.*;

public class AlienShip extends Participant implements AsteroidDestroyer, ShipDestroyer
{
    private final static int ALIEN_SOUND_INTERVAL = 200;

    private int size;

    private Controller controller;

    private int speed;

    private Shape outline;

    int delay = RANDOM.nextInt(5001) + ALIEN_DELAY;

    public AlienShip (int size, Controller controller)
    {
        this.size = size;
        this.controller = controller;
        setPosition(this.xPosition(), RANDOM.nextInt(SIZE));
        createShipOutline(size);

        new ParticipantCountdownTimer(this, "start", delay);

        new ParticipantCountdownTimer(this, "switchdirection", 5000);

        new ParticipantCountdownTimer(this, "fire", delay);
    }

    public void startSound ()
    {
        new ParticipantCountdownTimer(this, "sound", ALIEN_SOUND_INTERVAL);
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    public int xPosition ()
    {
        if (size == 2)
        {
            return SIZE + SHIP_HEIGHT - 20;
        }
        else
        {
            return SIZE + SHIP_HEIGHT - 33;
        }
    }

    private void createShipOutline (int size)
    {
        if (size == 2)
        {
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(23, 0);
            poly.lineTo(-23, 0);
            poly.moveTo(12, -10);
            poly.lineTo(-12, -10);
            poly.moveTo(23, 0);
            poly.lineTo(12, -10);
            poly.lineTo(10, -18);
            poly.lineTo(-10, -18);
            poly.lineTo(-12, -10);
            poly.lineTo(-23, 0);
            poly.lineTo(-12, 10);
            poly.lineTo(12, 10);
            poly.closePath();

            outline = poly;
        }
        else
        {
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(12, 0);
            poly.lineTo(-12, 0);
            poly.moveTo(6, -5);
            poly.lineTo(-6, -5);
            poly.moveTo(12, 0);
            poly.lineTo(6, -5);
            poly.lineTo(5, -9);
            poly.lineTo(-5, -9);
            poly.lineTo(-6, -5);
            poly.lineTo(-12, 0);
            poly.lineTo(-6, 5);
            poly.lineTo(6, 5);
            poly.closePath();

            outline = poly;
        }

    }

    public int getVelocity ()
    {
        if (size == 1)
        {
            this.speed = 4;
        }
        else
        {
            this.speed = 3;
        }
        return this.speed;
    }

    public int getSize ()
    {
        return size;
    }

    @Override
    public void collidedWith (Participant p)
    {

        if (p instanceof ShipDestroyer || p instanceof ShipBullet)
        {
            for (int i = 0; i < 5; i++)
            {
                Debris debris = new Debris(20);
                debris.setPosition(getX(), getY());
                controller.addParticipant(debris);
            }

            if (controller.getLevel() == 2)
            {
                controller.increaseScore(200);
            }
            else
            {
                controller.increaseScore(1000);
            }

            AsteroidSounds.playSound(AsteroidSounds.BANG_ALIEN_SHIP);

            Participant.expire(this);
            Participant.expire(p);

            new ParticipantCountdownTimer(new AlienShip(size, controller), "respawn", ALIEN_DELAY);
        }
    }

    double[] leftDirections = { Math.PI, Math.PI + 1, Math.PI - 1 };
    double[] rightDirections = { 0, 1, -1 };

    double direction = RANDOM.nextInt(2);

    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("switchdirection"))
        {
            if (direction == 0)
            {
                this.setVelocity(getVelocity(), RANDOM.nextInt(3) - 1);
            }
            else
            {
                this.setVelocity(getVelocity(), leftDirections[RANDOM.nextInt(3)]);
            }
            new ParticipantCountdownTimer(this, "switchdirection", 3000);
        }
        if (payload.equals("start"))
        {
            if (direction == 0)
            {
                setVelocity(getVelocity(), 0);
            }
            else
            {
                setVelocity(getVelocity(), Math.PI);
            }
        }
        if (payload.equals("fire"))
        {
            this.fire();
            new ParticipantCountdownTimer(this, "fire", 1000);
        }
        if (payload.equals("respawn"))
        {
            controller.addParticipant(this);

            startSound();
        }
        if (payload.equals("sound"))
        {
            if (size == 1)
            {
                AsteroidSounds.playContinuousSound(AsteroidSounds.SAUCER_SMALL);
            }
            else
            {
                AsteroidSounds.playContinuousSound(AsteroidSounds.SAUCER_BIG);
            }
        }
    }

    double max = (5 * (Math.PI / 180));
    double min = (-5 * (Math.PI / 180));

    public void fire ()
    {
        AsteroidSounds.playSound(AsteroidSounds.FIRE);

        AlienBullet bullet = new AlienBullet();
        bullet.setPosition(getX(), getY());
        if (getSize() == 2)
        {
            if (controller.getShip() != null)
            {
                bullet.setVelocity(BULLET_SPEED, RANDOM.nextDouble() * 2 * Math.PI);
            }
        }
        else
        {
            if (controller.getShip() != null)
            {
                bullet.setVelocity(BULLET_SPEED, (RANDOM.nextDouble() * (max - min) + min) + Math
                        .atan2(controller.getShip().getY() - this.getY(), controller.getShip().getX() - this.getX()));
            }
        }
        controller.addParticipant(bullet);
    }

}

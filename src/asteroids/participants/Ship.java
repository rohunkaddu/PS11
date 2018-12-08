package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import javax.sound.sampled.Clip;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ShipBullet;
import sounds.AsteroidSounds;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer, ShipDestroyer
{
    /**
     * Makes a outline for a ship with the given thruster size
     * @param thrusterSize  a size to make the thruster
     * @return              the outline of the thruster
     */
    public static Shape makeShipOutline(double thrusterSize) {
        Path2D.Double poly = new Path2D.Double();
        
        poly.moveTo(SHIP_HEIGHT/2, 0);
        poly.lineTo(-SHIP_HEIGHT/2, SHIP_WIDTH/2);
        poly.lineTo(-SHIP_HEIGHT/2 + 7, SHIP_WIDTH/2 - 2);
        
        // draw thrust
        poly.lineTo(-SHIP_HEIGHT/2 + 7 - 15 * thrusterSize, -SHIP_WIDTH/4 + 6);
        poly.lineTo(-SHIP_HEIGHT/2 + 7, -SHIP_WIDTH/2 + 2);
        poly.lineTo(-SHIP_HEIGHT/2 + 7, SHIP_WIDTH/2 - 2);
        
        // finish ship
        poly.lineTo(-SHIP_HEIGHT/2 + 7, -SHIP_WIDTH/2 + 2);
        poly.lineTo(-SHIP_HEIGHT/2, -SHIP_WIDTH/2);
        
        poly.closePath();
        
        return poly;
    }
    
    /** The outline of the ship */
    private Shape outline;

    /** Game controller */
    private Controller controller;
    
    /** The number of bullets fired **/
    private int bulletsFired;
    
    /** The bullets that the ship has fired **/
    private Bullet[] bullets = new Bullet[8];
    
    private boolean isFlaming;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);

        outline = makeShipOutline(0);
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
                
        super.move();
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
    }
    
    /**
     * Toggles the flame between on and off
     */
    public void toggleFlame() {
        isFlaming = ! isFlaming;
        
        AsteroidSounds.playContinuousSound(AsteroidSounds.THRUST);
        
        outline = makeShipOutline(isFlaming ? 1 : 0);
    }
    
    /**
     * Sets whether or not the flame is on
     * @param isOn  whether or not to make the flame on
     */
    public void setFlame(boolean isOn) {
        isFlaming = isOn;
        
        outline = makeShipOutline(isFlaming ? 1 : 0);
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {   
        if (p instanceof ShipDestroyer || p instanceof AlienBullet)
        {
            // Expire the ship from the game
            Participant.expire(this);
            
            for (int i = 0; i < 5; i++)
            {
                Debris debris = new Debris(20);
                debris.setPosition(getX(), getY());
                controller.addParticipant(debris);
            }

            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
            
            AsteroidSounds.playSound(AsteroidSounds.BANG_SHIP);
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {  
    }

    /**
     *  s a bullet from the ship
     */
    public void fire() {
        if (bullets[bulletsFired] != null && ! bullets[bulletsFired].isExpired())
            return;
            
        AsteroidSounds.playSound(AsteroidSounds.FIRE);
        
        ShipBullet bullet = new ShipBullet();
        bullet.setPosition(getXNose(), getYNose());
        bullet.setVelocity(BULLET_SPEED, getRotation());
        controller.addParticipant(bullet);
        
        bullets[bulletsFired] = bullet;
        
        bulletsFired = (bulletsFired + 1) % 7;
    }
}

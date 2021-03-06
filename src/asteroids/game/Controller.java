package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.sound.sampled.Clip;
import javax.swing.*;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.LifeCounter;
import asteroids.participants.Counter;
import asteroids.participants.Ship;
import sounds.AsteroidSounds;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener
{
    private LifeCounter lifeCounter;
    private Counter scoreCounter;
    private Counter levelCounter;

    /** The state of all the Participants */
    private ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;

    /** The timer of the beats */
    private Timer beatTimer;

    /** Whether or not to play beat 1 next */
    private boolean onBeat1 = true;

    /**
     * The current level of the game
     */
    private int level;

    /**
     * The current score of the game
     */
    protected int score;

    private boolean rightPressed;
    private boolean leftPressed;
    private boolean upPressed;
    private boolean firePressed;
    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;

    /** The game display */
    protected Display display;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Set up the beat timer
        beatTimer = new Timer(INITIAL_BEAT, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
        beatTimer.start();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;

    }

    public int getLevel ()
    {
        return level;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids(4);
        // addParticipant(new AlienShip(1, this));
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    protected void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
        
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");
        beatTimer = new Timer(INITIAL_BEAT, this);
        beatTimer.start();

    }

    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids (int n)
    {
        for (int i = 0; i < n; i++)
        {
            int x = i % 2 == 0 ? EDGE_OFFSET : SIZE - EDGE_OFFSET;
            int y = (i / 2) % 2 == 0 ? EDGE_OFFSET : SIZE - EDGE_OFFSET;

            addParticipant(new Asteroid(i % 3, 2, x, y, 3, this));
        }
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    protected void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids(4);
        // addParticipant(new AlienShip(1, this));

        // Place the ship
        placeShip();
        
        AsteroidSounds.SAUCER_BIG.stop();
        AsteroidSounds.SAUCER_SMALL.stop();
        
        

        // place counter
        lives = 3;
        level = 1;
        score = 0;

        lifeCounter = new LifeCounter(0, 20);
        scoreCounter = new Counter(LABEL_HORIZONTAL_OFFSET, LABEL_VERTICAL_OFFSET);
        levelCounter = new Counter(SIZE - LABEL_HORIZONTAL_OFFSET, LABEL_VERTICAL_OFFSET);
        
        
        levelCounter.setCount(level);

        addParticipant(lifeCounter);
        addParticipant(scoreCounter);
        addParticipant(levelCounter);

        // reset music
        beatTimer.restart();

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        // Null out the ship
        ship = null;
        
        
        beatTimer.stop();

        // Display a legend
        // display.setLegend("Ouch!");

        // Decrement lives
        lives--;
        lifeCounter.setLives(lives);
        if (lives == 0)
        {
            beatTimer.stop();
        }

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed ()
    {
        // If all the asteroids are gone, schedule a transition
        if (pstate.countAsteroids() == 0)
        {
            beatTimer.stop();
            scheduleTransition(END_DELAY);
        }

    }

    /**
     * Progresses to the next level
     */
    private void nextLevel ()
    {
        AsteroidSounds.SAUCER_BIG.stop();
        AsteroidSounds.SAUCER_SMALL.stop();
        beatTimer = new Timer(INITIAL_BEAT, this);
        beatTimer.restart();
        level++;
        levelCounter.setCount(level);

        // move ship to middle
        placeShip();

        // remove aliens
        for (Iterator<Participant> iter = pstate.getParticipants(); iter.hasNext();)
        {
            Participant part = iter.next();

            if (part instanceof AlienShip)
                Participant.expire(part);
        }

        placeAsteroids(level + 3);

        if (level > 1)
        {
            AlienShip alien = new AlienShip(level == 2 && level != 1 ? 2 : 1, this);
        }
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            beatTimer = new Timer(INITIAL_BEAT, this);
            initialScreen();
   
            
        }

        // Play beat and switch
        else if (e.getSource() == beatTimer)
        {
            if (onBeat1)
                AsteroidSounds.playSound(AsteroidSounds.BEAT_1);
            else
                AsteroidSounds.playSound(AsteroidSounds.BEAT_2);

            beatTimer.setDelay(Math.max(beatTimer.getDelay() - BEAT_DELTA, FASTEST_BEAT));
            onBeat1 = !onBeat1;
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();
            if (ship != null)
            {
                if (rightPressed)
                {
                    ship.turnRight();
                }
                if (leftPressed)
                {
                    ship.turnLeft();
                }
                if (upPressed)
                {
                    ship.accelerate();
                    ship.toggleFlame();
                }
                else
                {
                    ship.setFlame(false);
                }
                if (firePressed)
                {
                    ship.fire();
                }
            }

            // Refresh screen
            display.refresh();

        }
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return pstate.getParticipants();
    }
    
    public int getLives ()
    {
        return lives;
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                finalScreen();
            }
            else if (pstate.countAsteroids() == 0)
            {
                nextLevel();
            }
            else
            {
                placeShip();
            }
        }
    }

    /**
     * Increases the score by the given amount
     * 
     * @param amount the amount by which to increase the score
     */
    public void increaseScore (int amount)
    {
        score += amount;
        scoreCounter.setCount(score);
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_S:
                if (ship != null)
                    firePressed = true;
                break;
        }
    }

    /**
     * These events are ignored.
     */
    @Override
    public void keyTyped (KeyEvent e)
    {

    }

    /**
     * When controls are released stop moving
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (AsteroidSounds.THRUST.isRunning())
                {
                    AsteroidSounds.THRUST.stop();
                }
                upPressed = false;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_S:
                if (ship != null)
                    firePressed = false;
                break;
        }
    }
}

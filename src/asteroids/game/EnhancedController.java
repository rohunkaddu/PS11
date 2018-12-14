package asteroids.game;

import static asteroids.game.Constants.GAME_OVER;

public class EnhancedController extends Controller
{
    private int highScore = 0;
    
    @Override
    protected void finalScreen ()
    {
        if (score > highScore)
            highScore = score;
        display.setLegend(GAME_OVER + ", High Score: " + highScore);
        display.removeKeyListener(this);
    }

}

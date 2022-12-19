# Arkanoid

if (balls.get(0).getVelocityY() < 0 && powerUp.isVisible()) {
                player.setX(powerUp.getX() - (player.getWidth() / 2));
            } else {
                player.setX(balls.get(0).getX() - (player.getWidth() / randomX));
            }

## Needs to be fixed:
- **Side collision**
- **Overall collision - sometimes the ball goes through the platform**

## Needs to be added:
- **Levels via 2D array**
- **More power ups**
- **Random block color**
- **Normal sound effects, delete the temporary ones**

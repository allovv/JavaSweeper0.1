package sweeper;

public class Game
{
    private Bomb bomb;
    private Flag flag;
    private GameState state;
    private AudioRunner audio;
    private int countOfFlags;

    public GameState getState()
    {
        return state;
    }


    public Game (int cols, int rows, int bombs)
    {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();
        countOfFlags = 0;
    }

    public void start ()
    {
        bomb.start();
        flag.start();
        state = GameState.PLAYED;
        countOfFlags = 0;
    }

    public Box getBox (Coord coord)
    {
        if(flag.get(coord) == Box.OPENED)
            return bomb.get(coord);
        else
            return flag.get(coord);
    }

    public void pressLeftButton(Coord coord)
    {
        if (gameOver ())
            return;
        openBox (coord);
        checkWinner();
    }

    private void checkWinner ()
    {
        if (state == GameState.PLAYED)
            if (countOfFlags == bomb.getTotalBombs() && flag.getCountOfFlags() == countOfFlags && flag.getCountOfClosedBoxes() - flag.getCountOfFlags() == 0)
            {
                state = GameState.WINNER;
                //audio.playSound();
            }
//            else if (flag.getCountOfClosedBoxes() == bomb.getTotalBombs())
//                state = GameState.WINNER;

    }

    public int getCountOfFlags()
    {
        return countOfFlags;
    }

    private void  openBox (Coord coord)
    {
        switch (flag.get(coord))
        {
            case OPENED : setOpenedToClosedBoxesAroundNumber(coord); return;
            case FLAGED : return;
            case CLOSED :
                switch (bomb.get(coord))
                {
                    case ZERO : openBoxesAround (coord); return;
                    case BOMB : openBombs(coord); return;
                    default : flag.setOpenedToBox(coord); return;
                }
        }
    }

    private void setOpenedToClosedBoxesAroundNumber(Coord coord)
    {
        if(bomb.get(coord) != Box.BOMB)
            if (flag.getCountOfFlagedBoxesAround(coord) == bomb.get(coord).getNumber())
                for(Coord around : Ranges.getCoordsAround(coord))
                    if(flag.get(around) == Box.CLOSED)
                        openBox(around);
    }

    private void openBombs(Coord bombed)
    {
        state = GameState.BOMBED;
        flag.setBombedToBox (bombed);
        for (Coord coord : Ranges.getAllCoords())
            if (bomb.get(coord) == Box.BOMB)
                flag.setOpenedToClosedBombBox(coord);
            else
                flag.setNobombToFlagedSafeBox(coord);
    }

    private void openBoxesAround(Coord coord) //рекурсивно открывает ячейки вокруг из openBox'а
    {
        flag.setOpenedToBox(coord);
        for(Coord around : Ranges.getCoordsAround(coord))
            openBox(around);

    }

    public void pressRightButton(Coord coord)
    {
        if (gameOver ())
            return;
        flag.toggleFlagedToBox (coord);  //установить/убрать постановку флага в coord

        if (flag.getFlaggedOrNot() == 1)
            if (bomb.get(coord) == Box.BOMB)
                countOfFlags ++;
        if (flag.getFlaggedOrNot() == 0)
            if (bomb.get(coord) == Box.BOMB)
                countOfFlags --;

        checkWinner();
    }

    private boolean gameOver()  //проверка, закончена игра или нет
    {
        if (state == GameState.PLAYED)
            return false;
        start();
        return true;
    }

}

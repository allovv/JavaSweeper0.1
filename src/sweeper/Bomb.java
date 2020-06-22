package sweeper;

import java.util.Random;

class Bomb
{
    private Matrix bombMap;
    private int totalBombs;  //колво бомб

    Bomb (int totalBombs) //конструктор бомб
    {
        this.totalBombs = totalBombs;
        fixBombsCount();
    }

    void start () //размещение элементов
    {
        bombMap = new Matrix(Box.ZERO);
        for (int j = 0; j < totalBombs; j++)
            placeBomb ();
    }

    Box get (Coord coord) //геттер, чтобы узнать, что в клетке
    {
        return bombMap.get(coord); //возврат содержимого
    }

    private void fixBombsCount ()
    {
        int maxBombs = Ranges.getSize().x * Ranges.getSize().y / 2;
        if (totalBombs > maxBombs)
            totalBombs = maxBombs;
    }


    private void placeBomb () //размещение одной бомбы + NUMы
    {
        while (true)
        {
            Coord coord = Ranges.getRandomCoord();
            if (Box.BOMB == bombMap.get(coord)) //если там бомба, то пропускаем
                continue;
            bombMap.set(coord, Box.BOMB);
            incNumbersAroundBomb(coord);
            break;
        }

    }

    private void incNumbersAroundBomb (Coord coord)
    {
        for(Coord around : Ranges.getCoordsAround(coord))
            if(Box.BOMB != bombMap.get (around))
                bombMap.set(around, bombMap.get(around).getNextNumberBox());
    }

    int getTotalBombs()
    {
        return totalBombs;
    }

}

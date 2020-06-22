package sweeper;

public enum Box   //бокс с названиями
{
    ZERO,
    NUM1,
    NUM2,
    NUM3,
    NUM4,
    NUM5,
    NUM6,
    NUM7,
    NUM8,
    BOMB,
    OPENED,
    CLOSED,
    FLAGED,
    BOMBED,
    NOBOMB;

    public Object image;  // объект некоторого типа

    Box getNextNumberBox () //увеличение цифры в ячейке на +1
    {
        return Box.values()[this.ordinal() + 1];
    }

    int getNumber()
    {
        return this.ordinal(); //возврат текущего номера объекта
    }
}

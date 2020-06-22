import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sweeper.*;
import sweeper.Box;

public class JavaSweeper extends JFrame
{
    private Game game;
    private JPanel panel;
    private JLabel label, label1;



    private final int IMAGE_SIZE = 50;

    int COLS = 9;
    int ROWS =  9;
    int BOMBS = 10;
    int COEF = 5;
    String COMPLEXITY = "easy";

    public static void main(String[] args)
    {
        new JavaSweeper();
    }

    private JavaSweeper ()
    {
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initLabel();
        initPanel();
        //initFrame();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                initFrame();
            }
        });
    }


    private void initLabel ()
    {
        label = new JLabel();
        label.setText(getMessage());
        label.setFont(new Font("Serif", Font.PLAIN, 25));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.LIGHT_GRAY);
        //label.setIcon(new ImageIcon(getImage("bomb")));

        /*
        label1 = new JLabel();
        label1.setText(COMPLEXITY);
        label1.setFont(new Font("Serif", Font.PLAIN, 25));
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setOpaque(true);
        label1.setBackground(Color.LIGHT_GRAY);
        */

        add(label, BorderLayout.NORTH);
        //add(label1, BorderLayout.SOUTH);

    }

    private void  initPanel()
    {
        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                for(Coord coord : Ranges.getAllCoords())
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this); //ординал - порядковый номер в спсике


            }
        };


        panel.addMouseListener(new MouseAdapter()   //мышечный адаптер
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                int x = e.getX() / IMAGE_SIZE;  //встроенный метод коорд указателя
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.pressLeftButton(coord);
                if (e.getButton() == MouseEvent.BUTTON3)
                    game.pressRightButton(coord);
                if (e.getButton() == MouseEvent.BUTTON2)
                    game.start();
                label.setText(getMessage ());
                panel.repaint();                 //перерисовка формы
            }
        });

        panel.setPreferredSize (new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE)); //размер панели
        add (panel); //добавленеи панели
    }


    private String getMessage()
    {
        switch (game.getState())
        {
            case PLAYED : return  " ";
            case BOMBED : return "Поражение!";
            case WINNER : return "Победа!";
            default : return " ";
         }
    }

    private void initFrame ()
    {
        /*Следующий блок - создание стандартного фрейма*/
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //закрытие программы
        setTitle("Сапёр"); //заголовок
        setResizable(false); //произвольное изменение размера окна
        setVisible(true); //чтобы форму стало видно
        pack();   //изменение размера формы (устанавливает минимально возможный размер)
        setLocationRelativeTo(null); //заголовок по центру
        setIconImage(getImage("icon")); //установка иконки программы

        Font font = new Font("Verdana", Font.PLAIN, 11);
        JMenuBar menuBar = new JMenuBar();


        JMenu complexityMenu = new JMenu("Сложность");
        complexityMenu.setFont(font);

        JMenuItem eComplexityItem = new JMenuItem("Новичок");
        eComplexityItem.setFont(font);
        complexityMenu.add(eComplexityItem);
        eComplexityItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                COMPLEXITY = "easy";
                restartByButton();

            }
        });

        JMenuItem mComplexityItem = new JMenuItem("Любитель");
        mComplexityItem.setFont(font);
        complexityMenu.add(mComplexityItem);
        mComplexityItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int BUFBOMBS = BOMBS;
                BOMBS = BOMBS + COEF;
                COMPLEXITY = "medium";
                restartByButton();
                BOMBS = BUFBOMBS;

            }
        });

        JMenuItem hComplexityItem = new JMenuItem("Профессионал");
        hComplexityItem.setFont(font);
        complexityMenu.add(hComplexityItem);
        hComplexityItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int BUFBOMBS = BOMBS;
                BOMBS = BOMBS + 3 * COEF;
                COMPLEXITY = "hard";
                restartByButton();
                BOMBS = BUFBOMBS;

            }
        });


        JMenu sizeMenu = new JMenu("Размер поля");
        sizeMenu.setFont(font);

        JMenuItem smallItem = new JMenuItem("Маленькое");
        smallItem.setFont(font);
        sizeMenu.add(smallItem);
        smallItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                COLS = 9;
                ROWS =  9;
                BOMBS = 10;
                COEF = 5;

                int BUFBOMBS = BOMBS;
                if (COMPLEXITY == "easy")
                    BOMBS = 9;
                else if (COMPLEXITY == "medium")
                    BOMBS = 15;
                else if (COMPLEXITY == "hard")
                    BOMBS = 25;

                restartByButton();
                BOMBS = BUFBOMBS;
            }
        });

        JMenuItem mediumItem = new JMenuItem("Среднее");
        mediumItem.setFont(font);
        sizeMenu.add(mediumItem);
        mediumItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                COLS = 11;
                ROWS =  11;
                BOMBS = 15;
                COEF = 8;
                int BUFBOMBS = BOMBS;
                if (COMPLEXITY == "easy")
                    BOMBS = 15;
                else if (COMPLEXITY == "medium")
                    BOMBS = 23;
                else if (COMPLEXITY == "hard")
                    BOMBS = 39;

                restartByButton();
                BOMBS = BUFBOMBS;
            }
        });

        JMenuItem bigItem = new JMenuItem("Большое");
        bigItem.setFont(font);
        sizeMenu.add(bigItem);
        bigItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                COLS = 13;
                ROWS =  13;
                BOMBS = 21;
                COEF = 10;
                int BUFBOMBS = BOMBS;
                if (COMPLEXITY == "easy")
                    BOMBS = 21;
                else if (COMPLEXITY == "medium")
                    BOMBS = 31;
                else if (COMPLEXITY == "hard")
                    BOMBS = 51;

                restartByButton();
                BOMBS = BUFBOMBS;
            }
        });

        JMenu helpMenu = new JMenu("Помощь");
        helpMenu.setFont(font);

        JMenuItem descriptionItem = new JMenuItem("Описание");
        descriptionItem.setFont(font);
        helpMenu.add(descriptionItem);
        descriptionItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null,
                        "<html><h1 style=\"color:BLUE\" align=\"center\">ЦЕЛЬ ИГРЫ</h1>" +
                                "<p>Игроку нужно найти все мины и обезвредить их,</p>" +
                                "<p>выставив на них флажки правой кнопкой мыши.</p>" +
                                "<p>Левой кнопкой мыши мы должны открыть все клеточки,</p>" +
                                "<p>за которыми не скрываются мины.</p>" +
                                "<p></p>" +
                                "<h1 style=\"color:BLUE\" align=\"center\">ЧТО ЗНАЧАТ ЦИФРЫ</h1>" +
                                "<p>Цифры в Сапере — это подсказки, они нам говорят сколько и где расположено мин.</p>" +
                                "<p>Например, ячейка с число 1 говорит нам, что вокруг нее находится только 1 мина.</p>" +
                                "<p>Ячейка с числом 2 говорит нам, что вокруг нее две мины и так далее.</p>" +
                                "<p align=\"center\"></p>" +
                                "<h1 style=\"color:BLUE\" align=\"center\">ПОМЕЧАЕМ МИНЫ</h1>" +
                                "<p>Когда обнаружили мину, пометьте ее флажком.</p>" +
                                "<p>Когда мы определили расположение мин, нажимаем на те ячейке где их не должно быть.</p>" +
                                "<p>Левой кнопкой мыши мы должны открыть все клеточки,</p>" +
                                "<p align=\"center\"></p>" +
                                "<h1 style=\"color:BLUE\" align=\"center\">ОТКРЫВАЕМ ЯЧЕЙКИ</h1>" +
                                "<p>Смотрим на все открывшиеся нам числа и определяем, где могут располагаться мины.</p>" +
                                "<p>Если двойка, значит, вокруг две мины, тройка - три мины и так далее,</p>" +
                                "<p>Пока не пометим все мины и не откроем все свободные клетки.</p>" +
                                "<p align=\"center\"></p>" +
                                "<h1 style=\"color:BLUE\" align=\"center\">СОВЕТЫ</h1>" +
                                "<p>Начинайте играть с самого легкого уровня «Новичок».</p>" +
                                "<p>Будьте внимательны! Один неверный клик и вы проиграли.</p>",
                        "Описание", JOptionPane.PLAIN_MESSAGE);
            }
        });

        JMenuItem helpItem = new JMenuItem("Управление");
        helpItem.setFont(font);
        helpMenu.add(helpItem);
        helpItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "<html><p align=\"left\">• клик правой кнопкой мыши - отметить бомбу   </p>" +
                                                                             "<p align=\"left\">   </p>" +
                                                                             "<p align=\"left\">• клик левой кнопкой мыши -   открыть ячейку   </p>",
                                                                             "Управление",
                                                                              JOptionPane.PLAIN_MESSAGE);
            }
        });



        menuBar.add(sizeMenu);
        menuBar.add(complexityMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        pack();   //изменение размера формы (устанавливает минимально возможный размер)
    }

    private void checkComplexity() {
        if (COMPLEXITY == "medium")
            BOMBS = BOMBS + COEF;
        else if (COMPLEXITY == "hard")
            BOMBS = BOMBS + 3 * COEF;
        else if (COMPLEXITY == "easy")
            BOMBS = BOMBS;
    }

    private void restartByButton()
    {
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initPanel();

        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                label.setText(getMessage ());
                JFrame.setDefaultLookAndFeelDecorated(true);
                initFrame();
                initLabel();
            }
        });
    }

    private void setImages () //установка картинок
    {
        for (Box box : Box.values())    //подгрузка картинок(!не отображение)
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage (String name)
    {
        String filename = "img/" + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon (getClass().getResource(filename));
        return icon.getImage();
    }

}

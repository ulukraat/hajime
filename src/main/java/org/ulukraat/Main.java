package org.ulukraat;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("выберите бойца : " +
                "\n1.Иппо " +
                "\n2.Такамура");
        int playerChoice = scanner.nextInt();
        Characters player, computer;

        if (playerChoice == 1) {
            player = new Ippo();
            computer = new Takamura();
        } else if (playerChoice == 2) {
            player = new Takamura();
            computer = new Ippo();
        } else {
            System.out.println("Неверный выбор!");
            return;
        }

        System.out.println("Игра началась!");
        while (player.getHealth() > 0 && computer.getHealth() > 0) {
            // Ход игрока
            System.out.println("\nТвой ход!");
            System.out.println("1. Атака");
            System.out.println("2. Блок");
            System.out.println("3. Специальное умение");

            int playerAction = scanner.nextInt();

            // Ход компьютера (случайное действие)
            int computerAction = new Random().nextInt(3) + 1;
            System.out.println("Компьютер выбрал: " + getActionName(computerAction));

            // Обработка хода
            processTurn(player, computer, playerAction, computerAction);

            // Вывод текущего здоровья
            System.out.println(player.getName() + " Здоровье: " + player.getHealth());
            System.out.println(computer.getName() + " Здоровье: " + computer.getHealth());

            // Переход к следующему раунду
            player.nextRound();
            computer.nextRound();
        }

        if (player.getHealth() <= 0) {
            System.out.println("Ты проиграл! ");
        } else {
            System.out.println("Ты выиграл!");
        }
    }

    private static String getActionName(int action) {
        switch (action) {
            case 1:
                return "Атака";
            case 2:
                return "Блок";
            case 3:
                return "Специальное умение";
            default:
                return "";
        }
    }

    private static void processTurn(Characters player, Characters computer, int playerAction, int computerAction) {
        // Установка состояния блока
        if (playerAction == 2) {
            player.useDefense();
        }
        if (computerAction == 2) {
            computer.useDefense();
        }

        // Проверка действий игрока и компьютера одновременно
        if (playerAction == 3 && computerAction == 3) {
            player.useSpecialSkill(computer);
            computer.useSpecialSkill(player);
        } else {
            if (playerAction == 1 && !computer.isBlocking()) {
                computer.takeDamage(player.getPhysicalDamage());
            } else if (playerAction == 3) {
                player.useSpecialSkill(computer);
            }

            if (computerAction == 1 && !player.isBlocking()) {
                if (player instanceof Ippo && ((Ippo) player).isDodging()) {
                    System.out.println(player.getName() + " уворачивается от атаки и наносит урон.");
                    computer.takeDamage(computer.getPhysicalDamage());
                } else {
                    player.takeDamage(computer.getPhysicalDamage());
                }
            } else if (computerAction == 3) {
                computer.useSpecialSkill(player);
            }
        }
    }
}

class Characters {
    private String name;
    private int physicalDamage;
    private int health;
    private boolean blocking;
    private int defenseCount;
    private int cooldown;
    private SpecialSkill specialSkill;
    private int specialSkillUses;

    public Characters(String name, int physicalDamage, int health, SpecialSkill specialSkill) {
        this.name = name;
        this.physicalDamage = physicalDamage;
        this.health = health;
        this.blocking = false;
        this.defenseCount = 0;
        this.cooldown = 0;
        this.specialSkill = specialSkill;
        this.specialSkillUses = 3;
    }

    public String getName() {
        return name;
    }

    public int getPhysicalDamage() {
        return physicalDamage;
    }

    public int getHealth() {
        return health;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public void useDefense() {
        if (cooldown > 0) {
            System.out.println(name + " Защита на кулдауне. Осталось раундов: " + cooldown);
            return;
        }

        defenseCount++;
        if (defenseCount >= 2) {
            cooldown = 2;
            defenseCount = 0;
            System.out.println(name + " Защита использована два раза подряд. Кулдаун на 2 раунда.");
        } else {
            System.out.println(name + " Защита использована.");
        }
        setBlocking(true);
    }

    public void useSpecialSkill(Characters opponent) {
        if (specialSkillUses > 0) {
            specialSkill.use(this, opponent);
            specialSkillUses--;
        } else {
            System.out.println(name + " Специальное умение больше не доступно.");
        }
    }

    public void nextRound() {
        if (cooldown > 0) {
            cooldown--;
        }
        System.out.println(name + " Следующий раунд. Кулдаун: " + cooldown);
        setBlocking(false);
    }
}

class Ippo extends Characters {
    private boolean dodging;

    public Ippo() {
        super("Ippo", 400, 1700, new SpecialSkill("Уворот", "Уворот от атаки и возврат урона противнику") {
            @Override
            public void use(Characters user, Characters opponent) {
                if (opponent.isBlocking()) {
                    System.out.println(user.getName() + " использует " + getName() + ", но противник блокирует.");
                } else {
                    System.out.println(user.getName() + " использует " + getName() + " и уворачивается от атаки, нанося урон противнику.");
                    ((Ippo) user).setDodging(true);
                }
            }
        });
        this.dodging = false;
    }

    public boolean isDodging() {
        return dodging;
    }

    public void setDodging(boolean dodging) {
        this.dodging = dodging;
    }

    @Override
    public void nextRound() {
        super.nextRound();
        setDodging(false);
    }
}

class Takamura extends Characters {
    public Takamura() {
        super("Takamura", 600, 2000, new SpecialSkill("Пробивной удар", "Пробивает защиту и наносит 700 урона") {
            @Override
            public void use(Characters user, Characters opponent) {
                if (opponent instanceof Ippo && ((Ippo) opponent).isDodging()) {
                    System.out.println(user.getName() + " использует " + getName() + ", но " + opponent.getName() + " уворачивается и наносит урон.");
                    user.takeDamage(700);
                } else {
                    System.out.println(user.getName() + " использует " + getName() + " и пробивает защиту, нанося 700 урона.");
                    opponent.takeDamage(700);
                }
            }
        });
    }
}

abstract class SpecialSkill {
    private String name;
    private String description;

    public SpecialSkill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void use(Characters user, Characters opponent);
}


# Dusk of a Shattered Kingdom #

This is a traditional roguelike where you are being imprisoned at the bottom of a dungeon and need to escape.



Influences:
Brogue (ui, controls)
Shadow of Mordor (Bosses)




## My tools: ##

+ Groovy 2.4.0
+ SquidLib 1.95.1 ( https://github.com/SquidPony/SquidLib)
+ Gradle for building
+ Java8
+ Intellij


## Download ##
Coming


## Compile ##
To compile from source you will need a JDK and to install groovy and gradle.  I recommend using http://gvmtool.net/ for that.
Once you are set up, it will be something like:

```bash
git clone git://github.com/stewsters/dusk.git

cd dusk

gradle run
```


## Controls ##
I tried to use Brogue as a model for my UI, so controls are similar.

```
Movement: numpad, vi keys, or arrow keys (if you don't like diagonal movement)

Melee: Bump into enemies

a: apply

d: drop

i: view inventory

g: Pick up

e: equip

r: remove

t: Throw

esc: back out of menus

```




## Glyphs ##



### Tiles ###

    _  Altar
    O  Cloud
    .  Corridors
    +/ Door
    <> Staircases
    ß  Statue
    ^  Traps
    ♈  Foliage
    £  Tree
    #  Walls
    ~  shallow Water, Lava
    ≈  Deep water, Lava

### Items ###

    [  Armor, shields, cloaks, boots, girdles, gauntlets and helmets
    *  Gems and rocks
    ]  Tools (keys, writing sets, elemental orbs, and various other things)
    ♀  Necklaces, amulet
    ↑  Melee weapons (all of them)
    }  Missile weapons (bows, crossbows, slings)
    /  Missiles (arrows, quarrels, etc)
    =  Rings
    \  Wands, staffs, rods
    !  Potions, Quaffables
    ?  Scrolls
    %  corpses
    ;  Food  (inc  and herbs)
    $  Gold Coins
    {  Musical Instruments
    "  Books

### Monsters ###
Monsters are a-z A-Z

    @  Player
    a  Armored Hulk
    b
    c  craven - a small creature that will run away if injured
    d
    e  dark elves
    f
    g  goblin
    h
    i  imprisoned Spirit
    j
    k  knight - These are optional level bosses that tend to hang out near stairways
    l
    m  Minotaur - if they see you are range they will try to charge you.
    n
    o  orc -  Fairly standard
    p  phalanx
    q
    r  rat
    s  Statue
    t  Troll
    u
    v  vampire
    w  wolf - They move faster than you
    x
    y
    z

## Licence ##
    MIT

    Feel free to make Lets Play videos and monetize them.

    If you want to change the font, take a look at the assets/settings.props file

```
    font{
        name="Ariel"
        size=14
    }
```
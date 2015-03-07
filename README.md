# Dusk of a Shattered Kingdom #

This is a traditional roguelike

Influences
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
    c
    d  dark elves, Dragon
    e
    f
    g  goblin, Gargoyle
    h
    i  ifrit
    j
    k
    l
    m
    n
    o  orc, Ogre
    p
    q
    r  rat
    s
    t  Troll
    u
    v  vampire
    w  wolf, warg, white worm
    x
    y
    z  zombie

## Licence ##
    Feel free to make Lets Play videos and monetize them.
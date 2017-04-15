Describe story

# Components

Inventory
  items
  max
  
Equipment
  Armor
  Cloak
  Weapon

## Core Stats

  Strength
  Life - Does not regenerate, however you can brew potions to heal
  Stamina - Regenerates a small amount each turn
    resource for melee attacks and dodge.  
    Low stamina also slows attack speed and damage (up to 30%).  
    Light armor uses less stamina and heavy armor decreases it more.
    Stamina Regen slower if you are too damaged or too toxic
    (will likely need to be a larger number to have much variation)

## Derived

Hits
Armor - Protects at the beginning of the fight, tmp hitpoints
    max derived from gear, regenerates after battle (rest?)
    Armor give a defense value, which works as temp hp. It represents a defensive focus and stamina. 
    It is regenerated out of combat/waiting. Critical hits on it will damage items with defence

Poise - resist stunlock.  If something hits you while.  Increased with armor

Move Speed - penalized by weight of armor
Adrenaline - goes up when you hit an enemy, goes down when someone hits you. Combo. Reward for not being timid
Mana - lets you cast spells -> not 100% sure on this

Toxicity - lowers health Regen, let's you use potions.

Nutrition/Hunger - 


Insight?

Weapon

Break down into conditional probabilities


Survival aspects
 Hunger- eating, food
 Light- torches


Smell deposited on ground with an intensity and a time. Overwrite smell on ground if stronger
Los and brightness. Each square has a brightness, and you can see it with a clear los and distance per brightness.
Potions are like estus, can be recharged in town at witches hut. Rare treasure/craft. Cost something to recharge?
Horse runs away if afraid, fear and stamina.
Carrying capacity boosted by horse saddle bags. Things not in saddle bags can be placed into limited slots.


Your adventurer learns a new fact about a species each time he kills one. They are written into a bestiary.  Every new fact gives 1 xp.  Once you learn everything about a monster you can no longer grind them.
If you make it back to town, you can leave a copy of your bestiary for future adventurers. (Maybe they retire and become teachers, avoids regrind)


## Technical

Djikstra maps, calc in parallel?
Creature layer
Shadow layer
Ground layer
Dither field
Render layers separately

OnHit


Summoned monsters should either hunt or follow
Auto dodge-> move away rather than get hit, only active on rests.
Wrestle down monster
Climb on monster
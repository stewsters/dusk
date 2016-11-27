# Weapons


# Weapon Characteristics

Name - the name of the weapon

Strength Requirement - increases with weight and type of weapon.  They can be used below required, but there is a penalty

Moveset - The attack pattern of the weapon, based on type.

Special - hold control then bump attack to use special
SP - Special points.  Recharged +1 when you hit something. Cleared when you drop the weapon or are out of combat for too long
SP_MAX - the maximum special points


Damage - Increased by weapon weight/str.  min(weight,str)

Recovery Time - Time in ticks after attack.  Increased by weapon weight

Weight
  Controls strength and hand req
  Req You need strength to use it effectively
  Strength cap- you can hit harder with a more robust weapon
  weight increases stat req, damage, slower
  
  Light - fast recovery, low damage
  Heavy - take longer to recover, but greater damage

  Large weapons are less likely to hit vs small opponents
  Small weapons should have a maximal strength bonus


## 2 handed
Weapons can be used 1 or 2 handed. 
2h str boost
2 handed gives you a strong bonus, 1 handed let's you use additional techniques, shields, 
2 hands should give a strength boost, and may add additional abilities.
Strength required to wield weapon, 

All weapons are optionally handed. Weapon weight vs str, double hands boosts str. Strength bonus limited by weapon weight.


there should be weapons types 
Each should have its own attack logic and trigger logic
Each weapon has attack patterns.
Weapon type gives base damage and attack pattern
Some weapons cannot be used in close range, the user has to back up first.
Long weapons get a penalty when an obstacle is in their attack arc.

Each has min str stats to use
Each modifies the base damage

Quality makes everything better

Weapon durability should be a function of weight. Loss of durability will increase with the strength of the hits.
High but finite durability forces new weapon usage


Long weapons get a penalty when an obstacle is in their attack arc.

Weapons with abilities charge rather than proc


To hit
Dice
Sides

Some have utility uses, ie axe
Weapons can have critical effects or alt fire

# Weapon examples
#########################

Saber- kill shot, counter bayonet charge
Arming Sword - slash
Longsword - slash, sweep
Greatsword - 360 sweep

Hand Axe - dismember/bleed

Stiletto - Light
Mail breaker- ignores armor

Spear - lunge, pin, prevent advance?
Halberd - sweep

Club - stun
Mace - stun
Maul - Heavy, knockback

Reaper Blade - Scythe should attack in a sweeping arc

#######################

# Moveset
Standard - bump to attack
Lunge - moving directly towards an enemy when there is one space between you will do 3x damage
Heavy - take longer to recover, but greater damage
Long - attack the creature and the one behind it.  Good for holding a hallway
Cleave - attacks ajacent opponents on successful hit
Sweep - attacks both adjacent
Slash - attacks when you move between 2 spaces adjacent to an enemy
Feint Retreat - attack on retreat for lower damage and longer recovery

Fend - Stops attackers from closing into 1 space away from you. has a cooldown on your turn, you can only fend 1 guy
Range - attack units at distance

Weapons should always hit, unless there is an ability to block them
Weapons with special abilities charge rather than proc

# Special
Force - throws an opponent backwards (or all opponents?)
Slaying (specific type) - Does incredible damage vs that type
Stun - Adds time until next turn.  Should be less than the time to recharge the special
Wallrun - walking into the wall will run up and flip 2 spaces backwards.  Allows you to escape getting cornered at the cost of getting hit when you land.
Evasion - If you are hit while weilding an evasion weapon you automatically move out of range if there is space.


# Procedural Generation
Descriptive names.  Each should have some logic that will give hints at the function

## Set
(Hero)'s Greatsword
(Hero)'s plate

Black knight (weapon type)
White knight (weapon type)

(Element) Greatsword

Cursed?
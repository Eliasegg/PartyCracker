# PartyCracker

Meraki Studios Trial Project! throw Party Crackers into the ground and watch them explode and give you rewards.
## Features
- Very easy to setup, plug and play.
- Configurable infiniteParty Cracker types. 
- Both Color Codes and RGB support.
- Per reward settings:
    - Weight or likeliness of getting that reward. 
    - Commands to run after the cracker explodes.
    - Messages to run (or not) after the cracker explodes.
    - Whether to show holograms at the end with the reward you've got.
- Up to 5 animations to choose from.
- Configurable particle list, sound list.
- Glowing items!
- TAB auto complete.
- Reload config with a command.
- Highly-packed lightweight plugin.
- Easily extendable.
- No dependencies. Only optional if you want to enhance your experience!

## Command Usage

- /partycracker give <player> <crackerType> <quantity> - **Gives a player a set amount of Party Crackers of the specified type.**
- /partycracker reload - **Reloads the plugin configuration**

#### Permissions:
- partycracker.*
- partycracker.give
- partycracker.reload
## Configuration File
The default configuration file looks like this:
```yaml
# PartyCracker configuration file! all fields are pretty self explanatory
# but I have left comments for all of the fields in the first example
# as well as additional notes where I felt they fit well into.
# I hope you like the plugin!
crackers:
  # If you'd like to add a new PartyCracker type, just copy and paste
  # this configuration section and give it another name. (See: special cracker type)
  normal:
    item:
      # Material for this specific cracker type.
      material: REDSTONE
      # Name for the cracker type item.
      # Supports both color codes (EX: &e) and RGB for 1.16+ (EX: &#FFFFFF).
      name: '&eParty Cracker: Normal!' 
      # Lore for the item.
      # Supports both color codes (EX: &e) and RGB for 1.16+ (EX: &#FFFFFF).
      lore: 
        - '&eDrop the item on the ground'
        - '&7in order to receive &l&arewards!'
      # Whether you'd like that this item has enchantment glow or not.
      glow: false
    # How many seconds it will take for this cracker to explode
    explosion_time: 3 
    # How many seconds it will take for the placeholder (if enabled) item to disappear.
    #  See the rewards section for more info.
    disappear_after: 5
    # List of particles to use, it will pick a random one.
    particle_list:
      - 'TOTEM'
      - 'END_ROD'
      - 'FLAME'
      - 'NOTE'
    # List of animations to use, it will pick a random one.
    # Available animations: BLACK_SUN, MEGUMIN_EXPLOSION, DIAMOND, FLOWER, RINGS
    animation_list: 
      - 'BLACK_SUN'
      - 'DIAMOND'
      - 'FLOWER'
      - 'RINGS'
      - 'MEGUMIN_EXPLOSION'
    # List of sounds to use when the cracker explodes.
    sound_list: 
      - 'entity.generic.explode'
      - 'entity.player.levelup'
    # You can add more rewards by just copying each section and adding the next number. 
    # Just copy the "1" section and rename it to "3" and so on.
    rewards: 
      '1':
        # Likeliness of this item being your reward when exploding.
        chance: 30
        # Commands to execute after the cracker explodes.
        # You can use ${player} and ${random_int-int} and it will replace accordingly.
        # ${random_20:10} will pick a number between 20 and 10. IMPORTANT: use higher number first.
        # ${player} will be replaced with the player who threw the cracker
        commands: 
          - 'give ${player} iron_ingot ${random_20:10}' 
          - 'say ${player} won :D!' 
        # We can set the chat messages to broadcast to the server if someone wins this reward. 
        # Useful for high tier items! (Supports both color codes and RGB) and also ${player} placeholder.
        chat_messages: 
          - ""
        # This section is in regards to showing a placeholder after the cracker explodes to show what you've got.
        # (Only enable if you have DecentHolograms in your servers plugins folder)
        after_placeholder:
          # Self-explanatory. Whether it is enabled or not.
          enabled: true
          # Which material to show in the hologram.
          material: IRON_INGOT
          # Hologram lines, supports both colors and RGB.
          lines: 
            - "&eYou got an iron ingot!"
            - "&7--------"
      '2':
        chance: 70
        commands:
          - 'give ${player} stone_sword ${random_2:1}'
          - 'effect give EliasEnamorado minecraft:speed'
        chat_messages:
          - "&e${player} &7won a stone sword and a speed boost!"
        after_placeholder:
          enabled: false
          material: STONE_SWORD
          lines:
            - "&eNot enabled :[!"
            - "&7--------"
  special:
    item:
      material: FIREWORK_ROCKET
      name: '&eParty Cracker: &lSpecial!'
      lore:
        - '&eDrop the item on the ground'
        - '&7in order to receive &l&agod tier rewards!'
      glow: true
    explosion_time: 3
    disappear_after: 5
    particle_list:
      - 'TOTEM'
      - 'END_ROD'
      - 'FLAME'
      - 'NOTE'
    animation_list:
      - 'FLOWER'
      - 'RINGS'
      - 'MEGUMIN_EXPLOSION'
    sound_list:
      - 'entity.generic.explode'
      - 'entity.player.levelup'
    rewards:
      '1':
        chance: 30
        commands:
          - 'give ${player} diamond_sword 1'
        chat_messages:
          - "&e${player} found a &bDIAMOND SWORD!"
        after_placeholder:
          enabled: true
          material: DIAMOND_SWORD
          lines:
            - "&bOlimpo Sword!"
            - "&7--------"
      '2':
        chance: 70
        commands:
          - 'give ${player} coal ${random_4:2}'
          - 'say Better luck next time :['
        chat_messages:
          - ""
        after_placeholder:
          enabled: true
          material: COAL
          lines:
            - "&0Bad luck!"
            - "&7--------"
```

## How it works?

All of the magic is taken care of in the **PartyCrackerScheduler** class. This is where the rewards, animations, sounds and particles
are decided. We are only creating static objects for each one of the cracker types and retrieving them using a non-blocking singleton-based approach.

Due to the nature of the plugin, some workarounds had to be done in order to make it work as well as it could. I also added some features that weren't asked in the doc in order to make it more easy to manage and extend. Example:
- Added multiple cracker types. 
- Added the whole placeholder (or winnings showcase) system (which can also be disabled).
- Added 5 animations that you can pick from a list.
- RGB Support.
- Configurable chat messages after anyone has won (per-reward and can be disabled as well!).
- Placeholders for player and the ability to pick between a random number. 
- People throwing all their Party Crackers at once. I've made it so you can only throw one at a time.

## Extending Party Crackers and Rewards
This plugin was made in heavy considerations for extendability. All you'd need to do is create a new configuration section for every reward or even cracker types. 
It is recommended that you use a YAML tool to check whether the syntax of your config file is correctly formatted.

## Extra info and dependencies.

- Tested against Java 17, Minecraft 1.18.2 though it should work from 1.13 - 1.18.2 as NameSpacedKeys which are crucial for the plugin were added in 1.13.
- No required dependencies, however, in order to display the winnings showcase placeholders, you need to install the [Decent Holograms](https://www.spigotmc.org/resources/decent-holograms-1-8-1-18-2-papi-support-no-dependencies.96927/) plugin.
- Little to no external libraries were used besides a borrowed class from [XSeries](https://github.com/CryptoMorin/XSeries) [XParticles](https://github.com/CryptoMorin/XSeries/blob/master/src/main/java/com/cryptomorin/xseries/particles/XParticle.java) in order to create cool looking particle animations.

I hope this plugin catches your eye, a good effort (and a can of coke) went into it. ¡Adiós!
Description
==================
Energy batteries, but multi block!
This mod is inspired by [Colossal Chests](https://www.curseforge.com/minecraft/mc-mods/colossal-chests "Clossal Chests") mod.
This mod's textures are very bad, so feel free to make a PR on github!
This mod requires the [Cyclops Core](https://www.curseforge.com/minecraft/mc-mods/cyclops-core "Cyclops Core") library!      

How to use
==================
Basicly, this mod allows you to build energy batteries of any size that can store ForgeEnergy (RF).   
(you can set the maximum size in the config file)
The structure has to be a cuboid, but don't have to be cubic. You have to build the wall with a kind of block, you also have to fill the internal with another kind of blocks (valid blocks below), but note that these two types have to match a battery type, or it won't form.     
(Two battery structures can't close cling with each other)   
Every battery has to have one and only one BatteryCore (there's only one type of core, it works with any battery type), and it must be on the wall of the structure (just replace a wall block with a battery core), when your are done, right click the battery core with empty hand.   
This will start the structure checking, because there's only one type of core block, it will check the structure with all battery types, so there will be some error messages, but if the last message is success, you can just ignore other error messages (will probably improve this in the future)   
![Info Message](https://raw.githubusercontent.com/shBLOCK/ColossalBattery/master/images/chat_message.png "Info Message")   
If anything is right, you should see a battery like this:
![Basic Battery](https://raw.githubusercontent.com/shBLOCK/ColossalBattery/master/images/basic_battery.png "Basic Battery")   
You can also build a cuboid battery of any size:
![Long Battery](https://raw.githubusercontent.com/shBLOCK/ColossalBattery/master/images/long_battery.png "Long Battery")   
If you break any block in the structure, the structure will turn to normal blocks.   
You can access the energy through the battery core block (for example, you can connect a energy pipe to the core), but the core can't active input/output to other blocks. So you can use the BatteryInterface block, it can be in a structure, it have 3 modes, you can shift-right-click to switch between these modes, it will change the color of the node on the block. Brown: unactive mode, Blue: auto input, Orange: auto output.
![Interface Modes](https://raw.githubusercontent.com/shBLOCK/ColossalBattery/master/images/interface_mode.png "Interface Modes")   
You can see the amount of energy in the battery by the energy bar on the battery or right click any blocks in the structure to get the exact number.   
The storage space of the battery and the max transfer rate is depending on the amount of valid blocks (core and interface don't count) and the battery type (currently the transfer rate is per-interface, but will probably change this later).

Battery Types
==================
![Battery Types](https://raw.githubusercontent.com/shBLOCK/ColossalBattery/master/images/battery_types.png "Battery Types")   
The ultimate battery type that provides MASSIVE energy stonage and transfer rate, it's like the Draconic Evolution mod's Tier-8 Energy Core, but because the energy system of forge, if you use other mods energy reader to read the energy amount, the max amount is MAX_INT.
# Command Blocker
[![Latest Version](https://img.shields.io/badge/dynamic/json?color=ed37aa&label=Latest%20Version&query=name&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F5280%2Fversions%2Flatest)](https://www.spigotmc.org/resources/5280/)
![Minecraft Version Support](https://img.shields.io/badge/Minecraft%20Versions-1.4.7--1.16.2-9450cc)
[![Downloads](https://img.shields.io/badge/dynamic/json?color=2230f2&label=Downloads&query=downloads&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F5280)](https://www.spigotmc.org/resources/5280/)


 This plugin allows server operators to block commands from normal players. This provides safety and orignality to
  your server. 
 
 ## Installation
 Just drop the plugin from [here](https://www.spigotmc.org/resources/5280/) into your server
  plugins folder.

This plugin works on Bukkit and Spigot servers including forks (PaperMC) and BungeeCord servers including forks
 (Waterfall).

 ## Usage
`/cb add` - Allows you to add a command to be blocked for normal players

`/cb reload` - Reloads the configuration files

`/cb remove` - Allows you to remove a command that is blocked for normal players

`/cb edit` - Allows you to edit a command that is blocked for normal players

`/cb editop` - Allows you to edit a command that is blocked for operators. You need to have the permission cb.editop

`/cb addop` - Allows you to add a command that is blocked for operators. You need to have the permission cb.addop

`/cb removeop` - Allows you to remove a command that is blocked for operators. You need to have the permission
cb.removeop

## Command Blocker Legacy
In order to properly install this plugin on Spigot servers that run lower than 1.8.8, you must also install the
 module called Command Blocker Legacy, available [here](https://www.spigotmc.org/resources/command-blocker-legacy.82948/). 
It includes libraries which are missing from those versions of the game, such as [Gson](https://github.com/google/gson). 

## Contributing
All pull requests are welcome. If you found a bug or would like a new feature added, contact me through the [issues
 tab](https://github.com/TreyRuffy/CommandBlocker/issues).
 
## License
[GPLv3](https://choosealicense.com/licenses/gpl-3.0/)
 
 
# Command Blocker

[![Latest Version](https://img.shields.io/badge/dynamic/json?color=ed37aa&label=Latest%20Version&query=name&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F5280%2Fversions%2Flatest)](https://www.spigotmc.org/resources/5280/)
![Minecraft Version Support](https://img.shields.io/badge/Minecraft%20Versions-1.4.7--1.16.5+-9450cc)
[![Downloads](https://img.shields.io/badge/dynamic/json?color=2230f2&label=Downloads&query=downloads&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F5280)](https://www.spigotmc.org/resources/5280/)
[![Twitter: TreyRuffy](https://img.shields.io/twitter/follow/TreyRuffy.svg?style=social)](https://twitter.com/TreyRuffy)


<blockquote style="text-align: center;">This plugin allows server operators to block commands from normal players. This provides safety and originality to your server. 
</blockquote>

<h3 align="center"><a href="https://github.com/cominixo/BetterF3/">Check out the Fabric Mod</a></h3>

<h3>
<a href="https://www.curseforge.com/minecraft/mc-mods/betterf3/" target="_blank">🏠 CurseForge Homepage</a>
</h3>

## Installation

```sh
Just download and drop the plugin into your server plugins folder.

This plugin works on Bukkit and Spigot servers including forks (PaperMC) and BungeeCord servers including forks (Waterfall).
```

## Features
- Block any command
- Block commands from operators (example: /stop)
- Have commands run when a player executes a blocked command
- Block tab completion with [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)
- Hide the plugin list from players
- Hide the server version from players
- Create your own messages to be sent when a player executes a blocked command. You can even use hex color codes in 
  1.16+.
- Block commands in certain worlds
- Completely open source with GPLv3 license

## Command Blocker Legacy
In order to properly install this plugin on Spigot servers that run lower than 1.8.8, you must also install the
module called Command Blocker Legacy, available [here](https://www.spigotmc.org/resources/command-blocker-legacy.82948/).
It includes libraries which are missing from those versions of the game, such as [Gson](https://github.com/google/gson).

## Screenshots and Videos


## Usage
`/cb add` - Allows you to add a command to be blocked for normal players

`/cb reload` - Reloads the configuration files

`/cb remove` - Allows you to remove a command that is blocked for normal players

`/cb edit` - Allows you to edit a command that is blocked for normal players

`/cb editop` - Allows you to edit a command that is blocked for operators. You need to have the permission cb.editop.

`/cb addop` - Allows you to add a command that is blocked for operators. You need to have the permission cb.addop.

`/cb removeop` - Allows you to remove a command that is blocked for operators. You need to have the permission
cb.removeop.

## Authors

👤 **TreyRuffy**<br/>

* Twitter: [@TreyRuffy](https://twitter.com/TreyRuffy/)
* GitHub: [@TreyRuffy](https://github.com/TreyRuffy/)
* CurseForge: [@TreyRuffy](https://www.curseforge.com/members/treyruffy/)
* SpigotMC: [@TreyRuffy](https://www.spigotmc.org/members/treyruffy.31262/)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br/>Feel free to check [issues page](https://github.com/TreyRuffy/CommandBlocker/issues/).

## Show your support

Give a ⭐️ if you like this project!

## 📝 License

Copyright © 2021 [TreyRuffy](https://github.com/TreyRuffy/). <br/>
This project is [GPLv3](https://github.com/TreyRuffy/CommandBlocker/blob/master/COPYING) licensed.

***
_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator/)_
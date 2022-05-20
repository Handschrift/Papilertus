# General information about the Economy plugin

The Economy Plugin's goal is to provide a way to create a fully customizable idle game for discord. In the 'default'
theme you have seeds (collectables) from which you grow plants (coins). With the plants you can buy butterflies (
upgrades) to get even more seeds for your activities.

You can get seeds by doing different activities (voice, messages, bumping (has to be enabled in the config) ).

# Basic commands

All commands are implemented as slash-commands, you have to put a slash ('/') in front of the command name. If you have
proper permissions, the commands should be suggested once you typed a slash.

| Name                             | Description                                                               |
|----------------------------------|---------------------------------------------------------------------------|
| daily                            | Gives you your daily collectable                                          |
| Help                             | Shows this help page                                                      |
| plant (changeable in the config) | Inserts your collectables into your inventory                             |
| work                             | Asks you a question to get some collectables                              |
| shop                             | opens the shop                                                            |
| profile                          | shows your profile                                                        |
| leaderboard                      | shows the leaderboard (sorted by weekly gains)                            |
| collect                          | collects 'ripe' collectables                                              |
| inventory                        | Shows your inventory                                                      |
| forecast                         | Shows the forecast (collectable -> currency conversion for the next days) |
| give                             | Gives another user some coins                                             |

# Config

Remember, if you use the 'prehosted' instance of papilertus, then you won't be able to edit the config file. To be able
to create and edit a config file you have to host/run papilertus yourself! (See more information in the main papilertus
page)
[Alternatively you can ask for a custom version I can host for you]

The config is a basic json file (will be a yaml-File in the future) called 'Economy.json'. It will be in the
config-directory.

A basic config can look like this:

```json
{
  "currencyName": "Plants",
  "currencyIcon": ":deciduous_tree:",
  "collectableName": "Seeds",
  "collectableIcon": ":seedling:",
  "coinOnMessageSent": true,
  "coinMessageCooldown": 20,
  "coinOnVoiceActivity": true,
  "baseCoinOnMessageAmount": 2.0,
  "baseCoinOnVoiceActivityAmount": 0.2,
  "enableWorkMinigame": true,
  "workCooldown": 60,
  "baseWorkGain": 6.0,
  "convertCommandName": "plant",
  "baseCollectablesOnBumpGain": 110.0,
  "decimals": 0,
  "baseDailyGain": 50.0,
  "upgrades": [
    {
      "name": "Monarch Butterfly",
      "description": "Upgrades seed gain by voice",
      "incrementType": "VOICE",
      "icon": ":butterfly:",
      "incrementScale": 1.1,
      "basePrice": 54.0
    },
    {
      "name": "Periander Metalmark",
      "description": "Upgrades seed gain by message",
      "incrementType": "MESSAGE",
      "icon": ":butterfly:",
      "incrementScale": 1.1,
      "basePrice": 18.2
    },
    {
      "name": "Mountain Apollo",
      "description": "Upgrades seed gain by work",
      "incrementType": "WORK",
      "icon": ":butterfly:",
      "incrementScale": 1.1,
      "basePrice": 51.3
    },
    {
      "name": "Great Purple Hairstreak",
      "description": "Upgrades seed gain by bump",
      "incrementType": "BUMP",
      "icon": ":butterfly:",
      "incrementScale": 1.8,
      "basePrice": 100.4
    },
    {
      "name": "Southern Dogface",
      "description": "Upgrades seed gain by treasure",
      "incrementType": "TREASURE",
      "icon": ":butterfly:",
      "incrementScale": 1.5,
      "basePrice": 30.2
    },
    {
      "name": "Essex Skipper",
      "description": "Upgrades seed gain per daily",
      "incrementType": "DAILY",
      "icon": ":butterfly:",
      "incrementScale": 1.1,
      "basePrice": 35.0
    }
  ],
  "autoCollect": false,
  "timeToGrowDuration": 1,
  "timeToGrowUnit": "DAYS",
  "timeToDieDuration": 3,
  "timeDieUnit": "DAYS",
  "forecastNames": [
    ":fire: Dry",
    ":sunny: Sunny",
    ":cloud_rain: Heavy rain"
  ]
}
```

Here you can edit the coin/collectable gains, different upgrades or the name of the collectables / coins (and much
more!).

Make sure to restart your bot after you made changes to the config.

# Further information

The plugin is open source thus you can feel free to contribute to the source code. If you have questions or find bugs
please open an issue here on github.
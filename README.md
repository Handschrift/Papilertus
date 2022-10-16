![](papilertus.png#center "Papilertus")

# About this project

Papilertus is a (high configurable) general purpose bot providing different abilities for basic server management,
moderation and entertainment.
It also provides a plugin interface giving the ability to extend the bot with custom features. <br>
Currently the bot is in a very early state so feel free to open any issues if there are some.

# Requirements

- Java 17

<!-- 
- MongoDB 5.0.4
--->

# Installation

## Gradle

### Clone from GitHub:

`git clone https://github.com/Handschrift/Papilertus.git`

### Build the jar with all dependencies:

`./gradlew shadowJar`

This should create an executable Papilertus-1.0-all.jar

### Run the .jar with:

`java -jar Papilertus-1.0-all.jar`

After the first start it should create a configuration file where you can enter your bot token.
If you don't have a token, create a bot at the [Discord developer portal](https://discord.com/developers).

<!-- 
## Docker

### Prerequisites

Make sure that you have the latest version of docker and docker-compose installed.

### Clone from GitHub:

`git clone https://github.com/Handschrift/Papilertus.git`

### Build the docker container

`docker build -t papilertus:latest .`

### Editing the config

First run the jar-file, if no config is present yet, it should throw an error and create a configuration file in a
config directory.
The configuration files of any plugin and of Papilertus will be stored there.
You will find a 'Papilertus.yaml' file in there, it should look like this:

```yaml
token: "YOUR_TOKEN"
pluginDir: "plugins/"
databaseUrl: "mongodb://localhost/"
databaseUsername: ""
databasePassword: ""
cacheFlags:
  - "EMOTE"
  - "ACTIVITY"
  - "CLIENT_STATUS"
  - "ONLINE_STATUS"
feedbackRecipientId: "ID_OF_USER"
databaseName: "Papilertus"
possibleNotificationMessages:
  - "This is an open source bot!"
  - "you can host it yourself"
  - "you can send feedback with feedback"
  - "You can add plugins to the bot"
  - "You can turn off this message in the config"
probabilityForNotifications: 1
```

You just have to provide the bot token. You can leave the other fields if you don't use a mongodb.

### Run from docker-compose file

`docker-compose -f docker-compose.yml up`

### Editing the config

If you want to edit the config just terminate the docker containers with `docker-compose -f docker-compose.yml down`
edit the config you created and restart the docker containers.
--->

## Configuration

| Key                          | Default value          | Description                                         |
|------------------------------|------------------------|-----------------------------------------------------|
| token                        | empty                  | The bot token if you plan to host the bot yourself  |
| plugindir                    | "plugins/"             | Path to your Plugins folder                         |
| databaseUrl                  | "mongodb://localhost/" | URL to your mongodb                                 |
| databaseUsername             | "admin"                | username of your mongodb                            |
| databasePassword             | "admin"                | password of your mongodb                            |
| cacheFlags                   | list (see above)       | list of disabled caches                             |
| feedbackRecipientId          | empty                  | user id of the user who should receive the feedback |
| databaseName                 | "Papilertus"           | mongodb database name                               |
| possibleNotificationMessages | list (see above)       | list of possible notifications                      |
| probabilityForNotifications  | 1 (equals 1%)          | probability for a notification to be sent           |

# Core Commands

These are commands build into Papilertus

| Command  | Description                                        |
|----------|----------------------------------------------------|
| feedback | Sends feedback to the user specified in the config |

# Plugins

## How to install plugins

On the first start, the bot should have created a folder called "plugins". To install Plugins just download the plugin
or build it yourself and put the .jar into the "plugins" folder.

You have to restart the bot after you have installed a plugin!

### Attention!

- Even with the ability to write custom plugins, we are not responsible for any damage done with plugins of third
  parties

### Creating Plugins

#### Clone from GitHub:

`git clone https://github.com/Handschrift/Papilertus.git`

#### Build the library version of Papilertus:

`./gradlew shadowJar`

A jar should be generated in build/libs which you can use as a dependency in a gradle project.

#### plugin.json

Every plugin needs a plugin.json which you can create in the resources directory of your project

The file should contain the following:

```json
{
  "name": "NAME_OF_THE_PLUGIN",
  "author": "NAME_OF_THE_AUTHOR",
  "mainClass": "MAIN_CLASS_PATH"
}
```

#### Basic setup

Create a Class which implements the Plugin interface and the overrides the corresponding methods:

```kotlin
class MyPlugin : Plugin {
    override fun getCommands(): List<Command> {

    }

    override fun getContextMenuEntries(): List<ContextMenuEntry> {

    }

    override fun getListeners(): List<EventListener> {

    }

    override fun onLoad(data: PluginData) {
        println("Hello World")
    }

    override fun onUnload() {

    }
}
```

#### Creating commands

First create a class extending the Command class provided by Papilertus:

```kotlin
class MyCommand : Command("name", "description") {
    override fun execute(event: SlashCommandInteractionEvent) {
        event.reply("Test!").queue()
    }
}
```

Then in your main class you have to add the command to the command list:

```kotlin
override fun getCommands(): List<Command> {
    return listOf(
        MyCommand()
    )
}
```

# Supporting this project

You can support this project with:

- contributing to the code
- adding the bot to your server / host your own instance
- testing
<!-- 
- donations
--- >
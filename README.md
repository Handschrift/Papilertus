![](papilertus.png "Papilertus")
# About this project
The Papilertus is a (high configurable) general purpose bot providing different abilities for basic server management, moderation and entertainment.
It also provides a plugin interface giving the ability to extend the bot with custom features. [See How to create plugins](#how-to-create-plugins) <br>
Currently the bot is in a very early state so feel free to open any issues if there are some.
# Requirements
- Java 11
- MongoDB 5.0.4 (soon)
# Installation
## Gradle
### Clone from GitHub:
`git clone https://github.com/Handschrift/OpenPackagedBot.git`
### Build the jar with all dependencies:
`./gradlew shadowJar`

This should create an executable OpenPackagedBot-1.0-all.jar
### Run the .jar with:
`java -jar OpenPackagedBot-1.0-all.jar`

After the first start it should create a configuration file where you can enter your bot token.
If you don't have a token, create a bot at the [Discord developer portal](https://discord.com/developers).
## Docker
- Coming Soon
## Configuration
| Key          | Default value     | Description |
|--------------|-----------|------------|
| token        | empty     | The bot token if you plan to host the bot yourself           |
| plugindir    | "plugins/"     | Path to your Plugins folder           |
| databaseUrl        | "mongodb://localhost/"     | URL to your mongodb (currently not used)           |
| databaseUsername        | "admin"     | username of your mongodb (currently not used)           |
| databasePassword        | "admin"     | password of your mongodb (currently not used)           |
- more configurations are coming soon
# Plugins
## How to install plugins
On the first start, the bot should have created a folder called "plugins". To install Plugins just download the plugin or build it yourself and put the .jar into the "plugins" folder.

You have to restart the bot after you have installed a plugin!
## How to create plugins
### General structure
- TBD
### Creating a new command
- TBD
### Creating a configuration file
- TBD
### Connecting to the mongodb
- TBD
### Attention!
- Even with the ability to write custom plugins, we are not responsible for any damage done with plugins of third parties
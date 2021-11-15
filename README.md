# About this project
The "OpenPackageBot" is a (high configurable) general purpose bot providing different abilities for basic server management, moderation and entertainment.
It also provides a plugin interface giving the ability to extend the bot with custom features. [See How to create plugins](#How to create plugins) <br>
Currently the bot is in a very early state so feel free to open any issures if there are some.
# Requirements
- Java 11
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
- more configurations are coming soon
# How to create plugins
- TBA
- Even with the ability to write custom plugins, we are not responsible for any damage done with plugins of third parties
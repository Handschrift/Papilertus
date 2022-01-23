![](papilertus.png#center "Papilertus")
# About this project
Papilertus is a (high configurable) general purpose bot providing different abilities for basic server management, moderation and entertainment.
It also provides a plugin interface giving the ability to extend the bot with custom features. <br>
Currently the bot is in a very early state so feel free to open any issues if there are some.
# Requirements
- Java 11
- MongoDB 5.0.4
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
### Prerequisites
Make sure that you have the latest version of docker and docker-compose installed.
### Clone from GitHub:
`git clone https://github.com/Handschrift/OpenPackagedBot.git`
### Build the docker container
`docker build -t papilertus:latest .`
### Creating config
In your clone directory create another directory called config.
Then you have to manually create a "config.json". It should look like this:
```
{
  "token": "YOUR TOKEN!",
  "pluginDir": "plugins/",
  "databaseUrl": "mongodb://localhost/",
  "databaseUsername": "admin",
  "databasePassword": "admin"
}
```
You just have to provide the bot token. You can leave the other fields if you don't use a mongodb.
### Run from docker-compose file
`docker-compose -f docker-compose.yml up`
### Editing the config
If you want to edit the config just terminate the docker containers with `docker-compose -f docker-compose.yml down` edit the config you created and restart the docker containers.
## Configuration
| Key              | Default value          | Description                                        |
|------------------|------------------------|----------------------------------------------------|
| token            | empty                  | The bot token if you plan to host the bot yourself |
| plugindir        | "plugins/"             | Path to your Plugins folder                        |
| databaseUrl      | "mongodb://localhost/" | URL to your mongodb                                |
| databaseUsername | "admin"                | username of your mongodb                           |
| databasePassword | "admin"                | password of your mongodb                           |
- more configurations are coming soon
# Plugins
## How to install plugins
On the first start, the bot should have created a folder called "plugins". To install Plugins just download the plugin or build it yourself and put the .jar into the "plugins" folder.

You have to restart the bot after you have installed a plugin!

### Attention!
- Even with the ability to write custom plugins, we are not responsible for any damage done with plugins of third parties
# Supporting this project
You can support this project with:
- contributing tho the code
- adding the bot to your server / host your own instance
- testing
- donations
![](papilertus.png#center "Papilertus")
# About this project
Papilertus is a (high configurable) general purpose bot providing different abilities for basic server management, moderation and entertainment.
It also provides a plugin interface giving the ability to extend the bot with custom features. [See How to create plugins](#how-to-create-plugins) <br>
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
## How to create plugins
### General structure
- First make sure that you have a working build (a .jar file) of the official Papilertus bot.
- We assume that you are using gradle as a build system, but you may use any other build system
- Now create a gradle project and add a `plugin.json`  to your resource directory. (Make sure that it is inside the jar file on build)
- Now fill the `plugin.jar` with content. You have to provide the name, author, and mainClass attributes
```json
{
  "name": "NAME_OF_YOUR_PLUGIN",
  "author": "YOUR_NAME",
  "mainClass": "path.to.your.main.class"
}
```
- Add your build jarfile as a local dependency to your build.gradle
- Now create your Mainclass and let it implement the Plugin interface and the needed methods
```java
public class TestPlugin implements Plugin {

    @Override
    public void onLoad(PluginData pluginData) {
    }

    @Override
    public List<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
       
    }

    @Override
    public void onUnload() {

    }
}
```
- Now let's take a look at the onLoad-Method and add a statement to print out "Hello World"
```java
  @Override
    public void onLoad(PluginData pluginData) {
        System.out.println("Hello World");
    }
```
- Now build your Plugin and put it into the plugin directory of the Papilertus bot
- That's it, you have created your first plugin!
### Creating a new command
- TBD
### Creating a configuration file
- TBD
### Connecting to the mongodb
- TBD
### Attention!
- Even with the ability to write custom plugins, we are not responsible for any damage done with plugins of third parties
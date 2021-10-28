package com.openfunbot.init;

import com.openfunbot.config.Config;

public class OpenFunBot {
    public static void main(String[] args) {
        Config config = Config.getConfig();
        System.out.println(config.getToken());
    }
}
